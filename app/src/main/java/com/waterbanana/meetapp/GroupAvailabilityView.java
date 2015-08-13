package com.waterbanana.meetapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

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

    //The touchY coordinates are returned as pixels; (dp*density)
    //To properly line up things, while using scroll view and different phones
    //the returned value should be divided by the density (which in my phone's case is 3)

    //so.... the received touchY needs to be divided by 3, then placed into touchCoordinates[]

    //Height is divided into integers.
    //When corresponding y-coordinate is pressed, then the group it belongs to out of 100, will be colored
    private int timeIntervals = 96;
    private int[] touchCoordinates = new int[timeIntervals];

    private AvailabilityTickMarker marker;
    private View markerView;

    private int drawOrErase = 1;

    private DbHandler db;
    private User[] users;

    public GroupAvailabilityView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing(context);
    }

//    userList.put( "start", Integer.toString(ribbons.get(j).getStart()) );
//    userList.put( "end", Integer.toString( ribbons.get( j ).getEnd() ) );

    private void populateGroupAvailability(){
        db = new DbHandler();
        users = db.getAllUsers();


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

        //initialize touchCoordinates to dummy values
        for(int i=0; i<touchCoordinates.length; i++){
            touchCoordinates[i] = 1;
        }
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
        for(int i=0; i<touchCoordinates.length; i++){
            if(touchCoordinates[i]>0){
                drawCanvas.drawLine(
                        r.getDimensionPixelOffset(R.dimen.availability_line_draw_position), // Ctrl+B to navigate to definition
                        (0+i*100),
                        r.getDimensionPixelOffset(R.dimen.availability_line_draw_position),
                        (100+i*100),
                        drawPaint
                );
            }
        }

        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

}