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

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.ToggleButton;

import static com.waterbanana.meetapp.R.id.CanvasSwitchLayout;
import static com.waterbanana.meetapp.R.id.drawEraseSwitch;

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

    private Switch drawEraseButton;

    private int drawOrErase = 1;

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
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setStrokeJoin(Paint.Join.MITER);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);

        erasePaint.setAntiAlias(true);
        erasePaint.setStrokeWidth(100);
        erasePaint.setStyle(Paint.Style.STROKE);
        erasePaint.setStrokeJoin(Paint.Join.ROUND);
        erasePaint.setStrokeCap(Paint.Cap.ROUND);

        //instantiate canvas
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        //initialize touchCoordinates as untouched (-1)
        for(int i=0; i<touchCoordinates.length; i++){
            touchCoordinates[i] = 0;
        }
        //host layout
        //start
        //GAA 25 JUL 2015 - 3:26am
        //I want to:
        //1. Grab the switch drawEraseSwitch from activity_test_activity2.xml for it's erase or draw info
        //
        //Issues:
        //1. OnCreateView is from fragments (tutorials point to using that method to change setContentView to the xml file with the drawEraseSwitch
        //2. OnCreate is from activities
        //3. This file is a view.

        LinearLayout canvasLayout = (LinearLayout)findViewById(R.id.canvas_main);//keeps on being null
//        View view = getLayoutInflater().inflate(R.layout.canvas_main)
//        drawEraseButton = (Switch)canvasLayout.getChildAt(4);
        //end



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
        int upperBound = 0;
        int lowerBound = 0;
        //drawing down, move, and up


        //GAA 25 JUL 2015 - Another null object reference here.
        //start
/*        drawEraseButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    drawOrErase = -1;
                }else{
                    drawOrErase = 1;
                }

            }
        });*/
        //end

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchCoordinates[touchY/100] = drawOrErase;
                break;

            case MotionEvent.ACTION_MOVE:
                touchCoordinates[touchY/100] = drawOrErase;
                break;

            case MotionEvent.ACTION_UP:
                touchCoordinates[touchY/100] = drawOrErase;
                break;

            default:
                return false;
        }


        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for(int i=0; i<touchCoordinates.length; i++){
            if(touchCoordinates[i]>0){
//                drawCanvas.drawPoint(touchX, i, drawPaint);
                drawCanvas.drawLine(125, (0+i*100), 125, (100+i*100), drawPaint);
            }
            else if (touchCoordinates[i]<=0){
                //drawCanvas.drawLine(75, (0+i*100), 75, (100+i*100), erasePaint);

            }
        }

        invalidate();//causes onDraw to execute
        return true;
    }
}
