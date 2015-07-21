package com.waterbanana.meetapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.widget.ToggleButton;

public class DrawingView extends View
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

    //Height is divided into integers.
    //When corresponding y-coordinate is pressed, then the group it belongs to out of 100, will be colored
    private int[] touchCoordinates = new int[20];

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        //get drawing area setup for interaction
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);

        erasePaint = new Paint();
//        erasePaint.setARGB(0,0,0,0);//transparent
        erasePaint.setColor(Color.TRANSPARENT);

        //initial path properties
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(100);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        erasePaint.setAntiAlias(true);
        erasePaint.setStrokeWidth(100);
        erasePaint.setStyle(Paint.Style.STROKE);
        erasePaint.setStrokeJoin(Paint.Join.ROUND);
        erasePaint.setStrokeCap(Paint.Cap.ROUND);

        //instantiate canvas
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        //initialize touchCoordinates as untouched (-1)
        for(int i=0; i<touchCoordinates.length; i++){
            touchCoordinates[i] = -1;
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
        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    //listen for user input
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch
//        float touchX = event.getX();
//        float touchY = event.getY();
        float touchX = 75;
        int touchY = (int)event.getY();
        //drawing down, move, and up
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //this should be our create ribbon event
//                if(touchY<100){
//                    touchCoordinates[0] = touchCoordinates[0]*-1;//flag
//                }
//                else if(touchY<200){
//                    touchCoordinates[1] = touchCoordinates[1]*-1;
//                }
//                else if(touchY<300){
//                    touchCoordinates[2] = touchCoordinates[2]*-1;
//                }
//                else if(touchY<400){
//                    touchCoordinates[3] = touchCoordinates[3]*-1;
//                }
//                else if(touchY<500){
//                    touchCoordinates[4] = touchCoordinates[4]*-1;
//                }
//                else if(touchY<600){
//                    touchCoordinates[5] = touchCoordinates[5]*-1;
//                }
//                else if(touchY<700){
//                    touchCoordinates[6] = touchCoordinates[6]*-1;
//                }
//                else if(touchY<800){
//                    touchCoordinates[7] = touchCoordinates[7]*-1;
//                }
//                else if(touchY<900){
//                    touchCoordinates[8] = touchCoordinates[8]*-1;
//                }
//                else if(touchY<1000){
//                    touchCoordinates[9] = touchCoordinates[9]*-1;
//                }
//                else if(touchY<1100){
//                    touchCoordinates[10] = touchCoordinates[10]*-1;
//                }
//                else if(touchY<1200){
//                    touchCoordinates[11] = touchCoordinates[11]*-1;
//                }
//                else if(touchY<1300){
//                    touchCoordinates[12] = touchCoordinates[12]*-1;
//                }
//                else if(touchY<1400){
//                    touchCoordinates[13] = touchCoordinates[13]*-1;
//                }
//                else if(touchY<1500){
//                    touchCoordinates[14] = touchCoordinates[14]*-1;
//                }
//                else if(touchY<1600){
//                    touchCoordinates[15] = touchCoordinates[15]*-1;
//                }
//                else if(touchY<1700){
//                    touchCoordinates[16] = touchCoordinates[16]*-1;
//                }
//                else if(touchY<1800){
//                    touchCoordinates[17] = touchCoordinates[17]*-1;
//                }
//                else if(touchY<1900){
//                    touchCoordinates[18] = touchCoordinates[18]*-1;
//                }
//                else if(touchY<2000){
//                    touchCoordinates[19] = touchCoordinates[19]*-1;
//
//                }
//                drawPath.moveTo(touchX, startTouchY);
                break;
            case MotionEvent.ACTION_MOVE:
                //this should be our adjust ribbon event
//                drawPath.lineTo(touchX, event.getY());
//                touchCoordinates[touchY] = touchCoordinates[touchY]+1;//flag as touched
                if(touchY<100){
                    touchCoordinates[0] = 1;//flag
                }
                else if(touchY<200){
                    touchCoordinates[1] = 1;
                }
                else if(touchY<300){
                    touchCoordinates[2] = 1;
                }
                else if(touchY<400){
                    touchCoordinates[3] = 1;
                }
                else if(touchY<500){
                    touchCoordinates[4] = 1;
                }
                else if(touchY<600){
                    touchCoordinates[5] = 1;
                }
                else if(touchY<700){
                    touchCoordinates[6] = 1;
                }
                else if(touchY<800){
                    touchCoordinates[7] = 1;
                }
                else if(touchY<900){
                    touchCoordinates[8] = 1;
                }
                else if(touchY<1000){
                    touchCoordinates[9] = 1;
                }
                else if(touchY<1100){
                    touchCoordinates[10] = 1;
                }
                else if(touchY<1200){
                    touchCoordinates[11] = 1;
                }
                else if(touchY<1300){
                    touchCoordinates[12] = 1;
                }
                else if(touchY<1400){
                    touchCoordinates[13] = 1;
                }
                else if(touchY<1500){
                    touchCoordinates[14] = 1;
                }
                else if(touchY<1600){
                    touchCoordinates[15] = 1;
                }
                else if(touchY<1700){
                    touchCoordinates[16] = 1;
                }
                else if(touchY<1800){
                    touchCoordinates[17] = 1;
                }
                else if(touchY<1900){
                    touchCoordinates[18] = 1;
                }
                else if(touchY<2000){
                    touchCoordinates[19] = 1;
                }
//                drawPath.moveTo(touchX, startTouchY);
                break;
            case MotionEvent.ACTION_UP:
                //this should be our finalize ribbon event
//                drawCanvas.drawPath(drawPath, drawPaint);
//                drawPath.reset();
//                drawCanvas.drawColor(Color.TRANSPARENT);
//                for(int i=0; i<touchCoordinates.length; i++){
//                    if(touchCoordinates[i]>0){
//                        touchCoordinates[i]=touchCoordinates[i]*-1;
//                    }
//                }
                if(touchY<100){
                    touchCoordinates[0] = touchCoordinates[0]*-1;//flag
                }
                else if(touchY<200){
                    touchCoordinates[1] = touchCoordinates[1]*-1;
                }
                else if(touchY<300){
                    touchCoordinates[2] = touchCoordinates[2]*-1;
                }
                else if(touchY<400){
                    touchCoordinates[3] = touchCoordinates[3]*-1;
                }
                else if(touchY<500){
                    touchCoordinates[4] = touchCoordinates[4]*-1;
                }
                else if(touchY<600){
                    touchCoordinates[5] = touchCoordinates[5]*-1;
                }
                else if(touchY<700){
                    touchCoordinates[6] = touchCoordinates[6]*-1;
                }
                else if(touchY<800){
                    touchCoordinates[7] = touchCoordinates[7]*-1;
                }
                else if(touchY<900){
                    touchCoordinates[8] = touchCoordinates[8]*-1;
                }
                else if(touchY<1000){
                    touchCoordinates[9] = touchCoordinates[9]*-1;
                }
                else if(touchY<1100){
                    touchCoordinates[10] = touchCoordinates[10]*-1;
                }
                else if(touchY<1200){
                    touchCoordinates[11] = touchCoordinates[11]*-1;
                }
                else if(touchY<1300){
                    touchCoordinates[12] = touchCoordinates[12]*-1;
                }
                else if(touchY<1400){
                    touchCoordinates[13] = touchCoordinates[13]*-1;
                }
                else if(touchY<1500){
                    touchCoordinates[14] = touchCoordinates[14]*-1;
                }
                else if(touchY<1600){
                    touchCoordinates[15] = touchCoordinates[15]*-1;
                }
                else if(touchY<1700){
                    touchCoordinates[16] = touchCoordinates[16]*-1;
                }
                else if(touchY<1800){
                    touchCoordinates[17] = touchCoordinates[17]*-1;
                }
                else if(touchY<1900){
                    touchCoordinates[18] = touchCoordinates[18]*-1;
                }
                else if(touchY<2000){
                    touchCoordinates[19] = touchCoordinates[19]*-1;

                }

                break;
            default:
                return false;
        }


        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for(int i=0; i<touchCoordinates.length; i++){
            if(touchCoordinates[i]>0){
//                drawCanvas.drawPoint(touchX, i, drawPaint);
                drawCanvas.drawLine(75, (0+i*100), 75, (100+i*100), drawPaint);
            }
            else if (touchCoordinates[i]<=0){
                //drawCanvas.drawLine(75, (0+i*100), 75, (100+i*100), erasePaint);

            }
        }

        invalidate();//causes onDraw to execute
        return true;
    }
}
