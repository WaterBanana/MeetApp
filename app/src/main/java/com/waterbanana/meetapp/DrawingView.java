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

public class DrawingView extends RelativeLayout
{
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint, erasePaint, testPaint;
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
    private int myWidth;
    private int myHeight;
    private int barResolution;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing(context);
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
//        drawPaint.setStrokeJoin(Paint.Join.MITER);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);

//        testPaint.setAntiAlias(true);
//        testPaint.setColor(Color.RED);
//        testPaint.setStyle(Paint.Style.STROKE);
//        testPaint.setStrokeWidth(4.5f);

        erasePaint.setAntiAlias(true);
        erasePaint.setStrokeWidth(r.getDimension(R.dimen.availability_line_draw_width));
        erasePaint.setStyle(Paint.Style.STROKE);
        erasePaint.setStrokeJoin(Paint.Join.ROUND);
        erasePaint.setStrokeCap(Paint.Cap.SQUARE);

        //instantiate canvas
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        //initialize touchCoordinates as untouched (-1)
        for(int i=0; i<touchCoordinates.length; i++){
            touchCoordinates[i] = 0;
        }

//        RelativeLayout.LayoutParams lp = new LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        lp.addRule(RelativeLayout.CENTER_VERTICAL);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        markerView = inflater.inflate(R.layout.availability_marker, null);
//        markerView.setPadding(
//                r.getDimensionPixelOffset(R.dimen.availability_line_marker_position), 0, 0, 0
//        );
//        markerView = new AvailabilityTickMarker(getContext());
//        addView(markerView);

        //host layout
        //start
        //GAA 25 JUL 2015 - 3:26am
        //I want to:
        //1. Grab the switch drawEraseSwitch from activity_draw_availabilityity.xml for it's erase or draw info
        //
        //Issues:
        //1. OnCreateView is from fragments (tutorials point to using that method to change setContentView to the xml file with the drawEraseSwitch
        //2. OnCreate is from activities
        //3. This file is a view.

        //LinearLayout canvasLayout = (FrameLayout)findViewById(R.id.canvas_main);//keeps on being null
//        View view = getLayoutInflater().inflate(R.layout.canvas_main)
//        drawEraseButton = (Switch)canvasLayout.getChildAt(4);
        //end



    }

    public void setErase(boolean opt){
        if( opt == true ){
            drawOrErase = 0;
        }
        else{
            drawOrErase = 1;
        }
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
                    drawOrErase = 0;
                }else{
                    drawOrErase = 1;
                }
            }
        });*/
        //end

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    touchCoordinates[touchY / barResolution] = drawOrErase;
                }
                catch(Exception e){
                    touchCoordinates[timeIntervals-1] = drawOrErase;
                }
                Log.d("Height:", Integer.toString(myHeight));
                Log.d("Y:", Integer.toString(touchY));
                Log.d("Cell:", Integer.toString(touchY/barResolution));
                break;

            case MotionEvent.ACTION_MOVE:
                try {
                    touchCoordinates[touchY / barResolution] = drawOrErase;
                }
                catch(Exception e){
                    touchCoordinates[timeIntervals-1] = drawOrErase;
                }
                break;

            case MotionEvent.ACTION_UP:
                try {
                    touchCoordinates[touchY / barResolution] = drawOrErase;
                }
                catch(Exception e){
                    touchCoordinates[timeIntervals-1] = drawOrErase;
                }
                break;

            default:
                return false;
        }


        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for(int i=0; i<touchCoordinates.length; i++){
            if(touchCoordinates[i]>0){
//                drawCanvas.drawLine(
//                        r.getDimensionPixelOffset(R.dimen.availability_line_draw_position), // Ctrl+B to navigate to definition
//                        (float)(0+i*barResolution),
//                        r.getDimensionPixelOffset(R.dimen.availability_line_draw_position),
//                        (float)(barResolution+i*barResolution),
//                        drawPaint
//                );
                //drawRect (float left, float top, float right, float bottom, Paint paint)
                drawPaint.setStyle(Paint.Style.FILL);
                drawPaint.setColor(Color.GREEN);
                drawPaint.setStrokeWidth(r.getDimension(R.dimen.availability_line_draw_width));
                drawCanvas.drawRect(
                        75, //left
                        (float) (barResolution + i * barResolution), //top
                        200, //right
                        (float) (0 + i * barResolution), //bottom
                        drawPaint
                );
                drawPaint.setStyle(Paint.Style.STROKE);
                drawPaint.setColor(Color.BLACK);
                drawPaint.setStrokeWidth(1);
                drawCanvas.drawRect(
                        75, //left
                        (float)(barResolution+i*barResolution), //top
                        200, //right
                        (float)(0+i*barResolution), //bottom
                        drawPaint
                );

            }
            else if (touchCoordinates[i]<=0){
                //drawCanvas.drawLine(75, (0+i*100), 75, (100+i*100), erasePaint);

            }
        }

        invalidate();//causes onDraw to execute
        return true;
    }
}