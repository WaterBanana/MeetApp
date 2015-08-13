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

    private int drawOrErase = 1;

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

    private void populateGroupAvailability(){
        int start;
        int end;

        //look through each ribbon and increment the corresponding time values by 1 each time
        //it is seen as available
        for( int i = 0; i < users.length; i++ ){
            Log.d("MainActivity", "Number of distinct users: " + users.length);
            ribbons = users[i].getRibbons();
            for( int j = 0; j < ribbons.size(); j++ ){
                start = ribbons.get(j).getStart();
                end = ribbons.get(j).getEnd();
                for(int k=start; k<=end; k=k+15){
                    groupCoordinates[k%15]++;

                }
            }
        }
        invalidate();
        this.draw(drawCanvas);

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