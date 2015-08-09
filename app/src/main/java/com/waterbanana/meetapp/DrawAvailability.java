package com.waterbanana.meetapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.ToggleButton;


public class DrawAvailability extends ActionBarActivity {
    private DrawingView drawView;
    private Switch toggle1;
    private ToggleButton toggle2;
    private ToggleButton toggle3;
    private ToggleButton toggle4;
    private ScrollView scrollView;
    private RelativeLayout mRelativeLayout;
    private View view;
    private Handler handler = new Handler();
    private AvailabilityTickMarker marker;
    private String TAG = "DrawAvailability.java";
    private GestureDetector mGestureDetector;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_availability);
        drawView = (DrawingView) findViewById(R.id.drawing);
        scrollView = (ScrollView) findViewById(R.id.scrollview_availability);
//        toggle1 = (ToggleButton)findViewById(R.id.toggleButton);
//        new TestActivity2Frag();
        drawView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

        toggle1 = (Switch) findViewById(R.id.drawEraseSwitch);
        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    drawView.setErase(true);
                } else {
                    drawView.setErase(false);
                }
            }
        });

        marker = new AvailabilityTickMarker(this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_availability_act);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout llleft = (LinearLayout) findViewById(R.id.ll_leftside);
        lp.addRule(RelativeLayout.RIGHT_OF, llleft.getId());
        mRelativeLayout.addView(marker, lp);


//        marker = new AvailabilityTickMarker(this);
//        mGestureDetector = new GestureDetector(this, marker);
//
//        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_availability_act);
//        view = getLayoutInflater().inflate( R.layout.availability_marker, null );
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //Log.d(TAG, "Marker pressed." );
//                return mGestureDetector.onTouchEvent(event);
//            }
//        });
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//        mRelativeLayout.addView( view, lp );
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_draw_availability, container, false);
//        toggle1 = (ToggleButton)view.findViewById(R.id.toggleButton);
//        toggle2 = (ToggleButton)view.findViewById(R.id.toggleButton2);
//        toggle3 = (ToggleButton)view.findViewById(R.id.toggleButton3);
//        toggle4 = (ToggleButton)view.findViewById(R.id.toggleButton4);
//
//        return view;
//
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDragged(String action) {
        if( action.equals("SWIPE_UP") ){
            Log.d( TAG, "Swipe up" );
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    lp.addRule(RelativeLayout.ALIGN_TOP);
                    mRelativeLayout.updateViewLayout(view, lp);
                }
            });
        }
        else{
            Log.d(TAG, "Swipe down");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    lp.addRule(RelativeLayout.ALIGN_BOTTOM);
                    mRelativeLayout.updateViewLayout(view, lp);
                }
            });
        }
    }

//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //detect user touch
//        float touchX = event.getX();
////        float touchY = event.getY();
//        float touchY = event.getY();
//        //drawing down, move, and up
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //this should be our create ribbon event
//                toggle1.setChecked(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //this should be our adjust ribbon event
//                //if already there, erase
//                //else draw line
////                toggle2.setChecked(true);
//                break;
//            case MotionEvent.ACTION_UP:
//                //this should be our finalize ribbon event
//                toggle1 = (ToggleButton)findViewById(R.id.toggleButton);
//                toggle1.setChecked(false);
//                break;
//            default:
//                return false;
//        }
//
//
//        return true;
//    }
}
