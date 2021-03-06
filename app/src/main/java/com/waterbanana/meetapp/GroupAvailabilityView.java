package com.waterbanana.meetapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class GroupAvailabilityView extends RelativeLayout
{
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint, erasePaint;
    //initial color
    private int paintColor = 0xff00ff00;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    private Resources r;
    private Handler handler = new Handler();
    private String TAG = "DrawingView";

    //Height is divided into integers.
    private int timeIntervals = 96;
    private int[] groupCoordinates = new int[timeIntervals];

    private AvailabilityTickMarker marker;
    private View markerView;

    private int myWidth;
    private int myHeight;
    private int barResolution;

//    private DbHandler db;
//    private User[] users;

    public GroupAvailabilityView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing(context);
    }

//    userList.put( "start", Integer.toString(ribbons.get(j).getStart()) );
//    userList.put( "end", Integer.toString( ribbons.get( j ).getEnd() ) );

    ArrayList<Ribbon> ribbons;
    DbHandler db = new DbHandler();
    User[] users;
    double tallyPercentage;
    int currentColor;


    private void populateGroupAvailability(){
        int start;
        int end;

        //look through each ribbon and increment the corresponding time values by 1 each time
        //it is seen as available
        Log.d("MainActivity", "Number of distinct users: " + users.length);
        for( int i = 0; i < users.length; i++ ){

            ribbons = users[i].getRibbons();
            for( int j = 0; j < ribbons.size(); j++ ){
                start = ribbons.get(j).getStart();
                end = ribbons.get(j).getEnd();
                //ended here GAA 13 AUG 2015
                //having issues with managing the times
                for(int k=start; k<=end; k++){
                    groupCoordinates[k]++;

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
                drawCanvas.drawRect(
                        0, //left
                        (float) (barResolution + i * barResolution), //top
                        75, //right
                        (float) (0 + i * barResolution), //bottom
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
        invalidate();//should call the onDraw method
//        this.draw(drawCanvas);

    }

    private void setupDrawing(Context context){
        //get drawing area setup for interaction
        // comment

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
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    class LoadAllUsers extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            ArrayList<Ribbon> ribbons;
            DbHandler db = new DbHandler();


            users = db.getAllUsers();

            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            populateGroupAvailability();
        }
    }
}