package com.waterbanana.meetapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Scraffy on 8/19/2015.
 */

public class GroupTestAvailability extends RelativeLayout {

    private Path drawPath;
    private Paint drawPaint, erasePaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private Resources r;
    private int startTime, endTime, width, lineWidth, seekBarStart, seekBarEnd;
    private float startY, endY;
    private int groupidToLoad = 0000;

    ArrayList<Ribbon> ribbons;
    DbHandler db = new DbHandler();
    User[] users;
    double tallyPercentage;
    int currentColor;
    private int paintColor = 0xff00ff00;
    Canvas leftCanvas;

    //Height is divided into integers.
    private int timeIntervals = 97;
    private int[] groupCoordinates = new int[timeIntervals];

    private AvailabilityTickMarker marker;
    private View markerView;

    private int myWidth;
    private int myHeight;
    private int barResolution;

    private String TAG = "GroupTestAvailability";

    private GridView timesView;
    private String date;

//    public GroupTestAvailability (
//            Context context, int viewWidth, int lineWidth,
//            int startTime, int endTime, float startY, float endY) {
//
//        this( context, null, 0, lineWidth );
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.startY = startY;
//        this.endY = endY;
//        Log.d( TAG, "Start: " + startTime + " End: " + endTime );
//        Log.d( TAG, "Start Y: " + startY + " End Y: " + endY );
//        this.lineWidth = lineWidth;
//        width = viewWidth;
//
//    }

//    public GroupTestAvailability (Context context, AttributeSet attrs) {
//
//        this(context, attrs, 0, 0);
//
//    }
    public GroupTestAvailability(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing(context);
    }

    public void setTimesView(GridView timesView){
        this.timesView = timesView;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setGroupidToLoad(int groupid){
        groupidToLoad = groupid;
    }

    private void setupDrawing(Context context){
        //get drawing area setup for interaction
        // comment
        users = null;

        r = getResources();

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);

        erasePaint = new Paint();
//        erasePaint.setARGB(0,0,0,0);//transparent
        erasePaint.setColor(Color.TRANSPARENT);

        //initial path properties
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(r.getDimension(R.dimen.availability_line_draw_width));
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setStrokeJoin(Paint.Join.MITER);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);

        erasePaint.setAntiAlias(true);
        erasePaint.setStrokeWidth(r.getDimension(R.dimen.availability_line_draw_width));
        erasePaint.setStyle(Paint.Style.STROKE);
        erasePaint.setStrokeJoin(Paint.Join.ROUND);
        erasePaint.setStrokeCap(Paint.Cap.ROUND);

        //instantiate canvas
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        //initialize groupCoordinates to dummy values
        for(int i=0; i<groupCoordinates.length; i++){
            groupCoordinates[i] = 0;
        }

        refreshUserList();

    }

    public void refreshUserList(){
        new LoadAllUsers().execute();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        this.myWidth = w;
        this.myHeight = h;
        this.barResolution = h/timeIntervals;
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //draw view
//        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
//        canvas.drawPath(drawPath, drawPaint);
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
//        leftCanvas = canvas;
//        new LoadAllUsers().execute();

//        canvas.drawLine(
//                100,
//                0,
//                100,
//                4000,
//                drawPaint
//        );
        if( users != null && timesView != null)
        populateGroupAvailability(canvas);

    }


    private void populateGroupAvailability(Canvas newCanvas){
        int start;
        int end;

        //look through each ribbon and increment the corresponding time values by 1 each time
        //it is seen as available
        Log.d("MainActivity", "Number of distinct users: " + users.length);
        for( int i = 0; i < users.length; i++ ){

            ribbons = users[i].getRibbons();
            for( int j = 0; j < ribbons.size(); j++ ){
                if(ribbons.get(j).getDate().equals(date)) {
                    start = ribbons.get(j).getStart();
                    end = ribbons.get(j).getEnd();
                    //ended here GAA 13 AUG 2015
                    //having issues with managing the times
                    for (int k = start; k <= end; k++) {
                        groupCoordinates[k]++;

                    }
                }
            }
        }
        for(int i=0; i<groupCoordinates.length; i++) {
            if (groupCoordinates[i] > 0) {
//                drawCanvas.drawLine(
//                        r.getDimensionPixelOffset(R.dimen.availability_line_draw_position), // Ctrl+B to navigate to definition
//                        (float)(0 + i * barResolution),
//                        r.getDimensionPixelOffset(R.dimen.availability_line_draw_position),
//                        (float)(barResolution + i * barResolution),
//                        drawPaint
//                );
                tallyPercentage = (groupCoordinates[i]*1.0)/users.length;

                if(tallyPercentage<.33){
                    currentColor = Color.RED;
                }else if(tallyPercentage<.66){
                    currentColor = Color.rgb(255,165,0);//Orange
                }else if(tallyPercentage<1.00){
                    currentColor = Color.YELLOW;
                }else if(tallyPercentage==1.00){
                    currentColor = Color.GREEN;
                }


                drawPaint.setStyle(Paint.Style.FILL);
                drawPaint.setColor(currentColor);
                drawPaint.setStrokeWidth(r.getDimension(R.dimen.availability_line_draw_width));
//                drawCanvas.drawRect(
//                        0, //left
//                        (float) (barResolution + i * barResolution), //top
//                        75, //right
//                        (float) (0 + i * barResolution), //bottom
//                        drawPaint
//                );
                //public void drawLine (float startX, float startY, float stopX, float stopY, Paint paint)
//                newCanvas.drawLine(
//                        0,
//                        (float) (0 + i * barResolution),
//                        100,
//                        (float) (barResolution + i * barResolution),
//                        drawPaint
//                );
                newCanvas.drawLine(
                        100,
                        (float) timesView.getChildAt(i).getTop(),
                        100,
                        (float) timesView.getChildAt(i).getBottom(),
                        drawPaint

                );

                /*
                drawPaint.setStyle(Paint.Style.STROKE);
                drawPaint.setColor(Color.BLACK);
                drawPaint.setStrokeWidth(1);
                drawCanvas.drawRect(
                        0, //left
                        (float)(barResolution+i*barResolution), //top
                        75, //right
                        (float)(0+i*barResolution), //bottom
                        drawPaint
                );
                */

            }
        }
        //invalidate();//should call the onDraw method
//        this.draw(drawCanvas);

    }

    class LoadAllUsers extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            ArrayList<Ribbon> ribbons;
            DbHandler db = new DbHandler();


            users = db.getUsersByGroupId(groupidToLoad);

            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            postInvalidate();//should call onDraw
        }
    }

//    public int getStartTime() {
//        return startTime;
//    }
//
//    public int getEndTime() {
//        return endTime;
//    }
//
//    public float getStartY() {
//        return startY;
//    }
//
//    public float getEndY() {
//        return endY;
//    }

}


