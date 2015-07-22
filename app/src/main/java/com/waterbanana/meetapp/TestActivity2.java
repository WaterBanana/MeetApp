package com.waterbanana.meetapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;


public class TestActivity2 extends ActionBarActivity {
    private DrawingView drawView;
    private ToggleButton toggle1;
    private ToggleButton toggle2;
    private ToggleButton toggle3;
    private ToggleButton toggle4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawView = (DrawingView)findViewById(R.id.drawing);
//        toggle1 = (ToggleButton)findViewById(R.id.toggleButton);
//        new TestActivity2Frag();
        setContentView(R.layout.activity_test_activity2);

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_test_activity2, container, false);
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
