package com.waterbanana.meetapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Eddie on 8/12/2015.
 */
public class DrawTestView extends View {
    private Path drawPath;
    private Paint drawPaint, erasePaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private Resources r;
    private int startTime, endTime, width, lineWidth, RID;
    private float startY, endY;

//    private int timeIntervals = 96;
//    private int[] touchCoordinates = new int[timeIntervals];
    private String TAG = "DrawTestView.java";

    public DrawTestView(
            Context context, int ribbonId, int viewWidth, int lineWidth,
            int startTime, int endTime, float startY, float endY
    ) {
        this( context, null, 0, lineWidth );
        this.RID = ribbonId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startY = startY;
        this.endY = endY;
        Log.d( TAG, "Start: " + startTime + " End: " + endTime );
        Log.d( TAG, "Start Y: " + startY + " End Y: " + endY );
        this.lineWidth = lineWidth;
        width = viewWidth;

    }

    //constructor that doesn't require a viewWidth and a lineWidth as input
    public DrawTestView(
            Context context,
            int startTime, int endTime, float startY, float endY
    ) {
        this( context, null, 0, 100 );//lineWidth turned to 100 for now
        this.startTime = startTime;
        this.endTime = endTime;
        this.startY = startY;
        this.endY = endY;
        Log.d( TAG, "Start: " + startTime + " End: " + endTime );
        Log.d( TAG, "Start Y: " + startY + " End Y: " + endY );
        this.lineWidth = 100;//lineWidth turned to 100 for now
        width = 100;//viewWidth turned to 100 for now

    }

    public DrawTestView(Context context, AttributeSet attrs) {
        this( context, attrs, 0, 0 );
    }

    public DrawTestView(Context context, AttributeSet attrs, int defStyleAttr, int width) {
        super(context, attrs, defStyleAttr);

        r = getResources();

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(r.getColor(R.color.RibbonColorPrimary));

        //initial path properties
        drawPaint.setAntiAlias(true);
//        drawPaint.setStrokeWidth(width);
        drawPaint.setStrokeWidth(100);
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setStrokeJoin(Paint.Join.MITER);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);

        //instantiate canvas
        canvasPaint = new Paint(Paint.DITHER_FLAG);

//        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Bitmap unscaledcanvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//                //canvasBitmap = Bitmap.(unscaledcanvasBitmap, getWidth(), getHeight(), false);
//                Log.d( TAG, "Width: " + getWidth() + " Height: " + getHeight() );
//
//                drawCanvas = new Canvas(canvasBitmap);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                else
//                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            }
//        });

        //initialize touchCoordinates as untouched (-1)
//        for(int i=0; i<touchCoordinates.length; i++){
//            touchCoordinates[i] = 0;
//        }
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 200;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
//        Log.d( TAG, "Height: " + height );
        setMeasuredDimension( width, height );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.d( TAG, "onDraw" );

//        drawCanvas.drawLine(
//                0.5f * (width - lineWidth),
//                0,
//                0.5f * (width - lineWidth),
//                endY - startY,
//                drawPaint
//        );

//        for(int i=0; i<touchCoordinates.length; i++) {
//            if (touchCoordinates[i] > 0) {
//                drawCanvas.drawPoint(touchX, i, drawPaint);
//                drawCanvas.drawLine(
//                        0,
//                        (i * 100 - 100),
//                        0,
//                        (20 + i * 100 - 100),
//                        drawPaint
//                );
//            }
//        }'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
        canvas.drawLine(
                0.5f * (width - lineWidth),
                0,
                0.5f * (width - lineWidth),
                endY - startY,
                drawPaint
        );
        //canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        //canvas.drawPath(drawPath, drawPaint);
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndY() {
        return endY;
    }

    public int getViewWidth(){return this.width;}
    public int getLineWidth(){return this.lineWidth;}

    public int getMinValue(){return this.startTime;}

    public int getMaxValue(){return this.endTime;}

    public int getRID(){return this.RID;}
}
