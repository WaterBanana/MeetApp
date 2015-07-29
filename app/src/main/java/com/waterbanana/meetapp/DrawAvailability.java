package com.waterbanana.meetapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
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
                if( event.getAction() == MotionEvent.ACTION_DOWN ){
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                else if( event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL ){
                    scrollView.requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

        toggle1 = (Switch) findViewById(R.id.drawEraseSwitch);
        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    drawView.setErase(true);
                }
                else{
                    drawView.setErase(false);
                }
            }
        });
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
