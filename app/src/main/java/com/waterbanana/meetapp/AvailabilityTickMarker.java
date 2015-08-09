package com.waterbanana.meetapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Eddie on 8/1/2015.
 */
public class AvailabilityTickMarker extends ImageView {
    /**
     * Marker will move in both x and y directions.
     */
    public static final int MODE_ENABLE_ALL = 0;
    /**
     * Marker will only move in x direction.
     */
    public static final int MODE_ONLY_X = 1;
    /**
     * Marker will only move in y direction.
     */
    public static final int MODE_ONLY_Y = 2;

    private Drawable mIcon;
    public float mPosX;
    public float mPosY;
    private float mLastTouchX;
    private float mLastTouchY;
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;

    private boolean onlyX = true;
    private boolean onlyY = true;

    private boolean DEBUG = false;
    private boolean DEBUGT = false;
    private String TAG = "Availability Tick Marker";

    public AvailabilityTickMarker(Context context) {
        this(context, null, 0);
    }

    public AvailabilityTickMarker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvailabilityTickMarker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIcon = context.getResources().getDrawable(R.drawable.availability_marker);
        if (DEBUG) Log.d( TAG, "mIcon intrinsic width: " + mIcon.getIntrinsicWidth() );
        if (DEBUG) Log.d( TAG, "mIcon intrinsic height: " + mIcon.getIntrinsicWidth() );
        //mIcon.setBounds( 0, 0, mIcon.getIntrinsicWidth(), mIcon.getIntrinsicHeight() );
        mIcon.setBounds(0, 0, context.getResources().getDimensionPixelSize(R.dimen.av_marker_size),
                context.getResources().getDimensionPixelSize(R.dimen.av_marker_size));
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Resources r = getResources();
//        setMeasuredDimension(
//                r.getDimensionPixelSize(R.dimen.av_marker_size),
//                r.getDimensionPixelSize(R.dimen.av_marker_size)
//        );
//    }

    public void setMoveMode(int mode){
        switch(mode){
            case MODE_ONLY_X:
                onlyX = true;
                onlyY = false;
                break;
            case MODE_ONLY_Y:
                onlyX = false;
                onlyY = true;
                break;
            default:
                onlyX = true;
                onlyY = true;
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(mPosX, mPosY);
        mIcon.draw(canvas);
        canvas.restore();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float x;
        final float y;
        final int pointerIndex;

        switch( action & MotionEvent.ACTION_MASK ){
            case MotionEvent.ACTION_DOWN:
                if (DEBUGT) Log.d(TAG, "ACTION_DOWN");

                mIcon.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                mIcon.invalidateSelf();

                if(onlyX){
                    x = event.getX();
                    mLastTouchX = x;
                }

                if(onlyY){
                    y = event.getY();
                    mLastTouchY = y;
                }

                mActivePointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (DEBUGT) Log.d( TAG, "ACTION_MOVE" );
                pointerIndex = event.findPointerIndex(mActivePointerId);

                if(onlyX){
                    x = event.getX(pointerIndex);
                    final float dx = x - mLastTouchX;
                    mPosX += dx;
                    mLastTouchX = x;
                }

                if(onlyY){
                    y = event.getY(pointerIndex);
                    final float dy = y - mLastTouchY;
                    mPosY += dy;
                    mLastTouchY = y;
                }

                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (DEBUGT) Log.d( TAG, "ACTION_CANCEL" );
                mIcon.clearColorFilter();
                mIcon.invalidateSelf();
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_UP:
                if (DEBUGT) Log.d( TAG, "ACTION_UP" );
                mIcon.clearColorFilter();
                mIcon.invalidateSelf();
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (DEBUGT) Log.d( TAG, "ACTION_POINTER_UP" );
                pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if( pointerId == mActivePointerId ){
                    // Adjust our pointer.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
        }

        return true;
    }
}