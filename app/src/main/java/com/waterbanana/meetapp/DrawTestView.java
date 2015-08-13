package com.waterbanana.meetapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private int startTime, endTime, width, lineWidth, seekBarStart, seekBarEnd;
    private float startY, endY;

//    private int timeIntervals = 96;
//    private int[] touchCoordinates = new int[timeIntervals];
    private String TAG = "DrawTestView.java";

    public DrawTestView(
            Context context, int viewWidth, int lineWidth,
            int startTime, int endTime, float startY, float endY,
            int seekBarStart, int seekBarEnd
    ) {
        this( context, null, 0, lineWidth );
        this.startTime = startTime;
        this.endTime = endTime;
        this.startY = startY;
        this.endY = endY;
        this.seekBarStart = seekBarStart;
        this.seekBarEnd = seekBarEnd;
        Log.d( TAG, "Start: " + startTime + " End: " + endTime );
        this.lineWidth = lineWidth;
        width = viewWidth;

//        for( int i = startTime; i < endTime; i++ ){
//            touchCoordinates[i] = 1;
//        }
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

        erasePaint = new Paint();
//        erasePaint.setARGB(0,0,0,0);//transparent
        erasePaint.setColor(Color.TRANSPARENT);

        //initial path properties
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(width);
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

        //initialize touchCoordinates as untouched (-1)
//        for(int i=0; i<touchCoordinates.length; i++){
//            touchCoordinates[i] = 0;
//        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 200;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension( width, height );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCanvas.drawLine(
                0.5f * (width - lineWidth),
                startY,
                0.5f * (width - lineWidth),
                endY,
                drawPaint
        );

//        for(int i=0; i<touchCoordinates.length; i++) {
//            if (touchCoordinates[i] > 0) {
//                drawCanvas.drawPoint(touchX, i, drawPaint);
//                //drawLine(float startY, float endY, float stopX, float stopY, Paint paint)
//                drawCanvas.drawLine(
//                        0,
//                        (i * 100 - 100),
//                        0,
//                        (20 + i * 100 - 100),
//                        drawPaint
//                );
//            }
//        }
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
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

    public int getSeekBarStart() {
        return seekBarStart;
    }

    public int getSeekBarEnd() {
        return seekBarEnd;
    }
}
