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
 * Created by Scraffy on 8/19/2015.
 */

public class GroupTestAvailability extends View {

    private Path drawPath;
    private Paint drawPaint, erasePaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private Resources r;
    private int startTime, endTime, width, lineWidth, seekBarStart, seekBarEnd;
    private float startY, endY;

    private String TAG = "GroupTestAvailability";

    public GroupTestAvailability (
            Context context, int viewWidth, int lineWidth,
            int startTime, int endTime, float startY, float endY) {

        this( context, null, 0, lineWidth );
        this.startTime = startTime;
        this.endTime = endTime;
        this.startY = startY;
        this.endY = endY;
        Log.d( TAG, "Start: " + startTime + " End: " + endTime );
        Log.d( TAG, "Start Y: " + startY + " End Y: " + endY );
        this.lineWidth = lineWidth;
        width = viewWidth;

    }

    public GroupTestAvailability (Context context, AttributeSet attrs) {

        this(context, attrs, 0, 0);

    }

    public GroupTestAvailability (Context context, AttributeSet attrs, int defStyleAttr, int width) {

        super(context, attrs, defStyleAttr);

        r = getResources();

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(r.getColor(R.color.RibbonColorPrimary));

        //initial path properties
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(width);
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setStrokeJoin(Paint.Join.MITER);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);

        //instantiate canvas
        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 200;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        Log.d( TAG, "Height: " + height );
        setMeasuredDimension( width, height );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d( TAG, "onDraw" );

        canvas.drawLine(
                0.5f * (width - lineWidth),
                0,
                0.5f * (width - lineWidth),
                endY - startY,
                drawPaint
        );
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

}
