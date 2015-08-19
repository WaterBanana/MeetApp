package com.waterbanana.meetapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waterbanana.common.RangeSeekBar;

public class Availability extends AppCompatActivity {
    private GridView verticalTimes;
    private RelativeLayout screenlayout, midlayout, leftlayout, rightlayout;
    private String[] times = new String[97];
    private int layoutHeight;
    private RelativeLayout.LayoutParams paramsRLLeft;
    private boolean isInEditMode = false;
    private RangeSeekBar<Integer> seekBar;
    private View viewListener;
    private Button btnDeleteRibbon;

    private String TAG = "Availability.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        screenlayout = (RelativeLayout) findViewById(R.id.relativeLayout_activity_availability);
        midlayout = (RelativeLayout) findViewById(R.id.availability_layout_mid);
        leftlayout = (RelativeLayout) findViewById(R.id.availability_layout_left);
        rightlayout = (RelativeLayout) findViewById(R.id.availability_layout_right);

        setupTimesArray();
        verticalTimes = new GridView(this);
        verticalTimes.setNumColumns(1);
        verticalTimes.setAdapter(new TimesViewAdapter(this));

        // Remove touch events on the times
        verticalTimes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });

        midlayout.addView(verticalTimes);

        verticalTimes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Set layout height accordingly (based on times GridView)
                layoutHeight = verticalTimes.getChildAt(
                        verticalTimes.getChildCount() - 1
                ).getBottom() + getResources().getDimensionPixelOffset(R.dimen.dp10);
                midlayout.setLayoutParams(new LinearLayout.LayoutParams(
                        verticalTimes.getWidth(), layoutHeight
                ));

                leftlayout.setLayoutParams(new LinearLayout.LayoutParams(
                        verticalTimes.getWidth(), layoutHeight
                ));

                rightlayout.setLayoutParams(new LinearLayout.LayoutParams(
                        verticalTimes.getWidth(), layoutHeight
                ));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    verticalTimes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    verticalTimes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    // SEEKBAR
    private void putRibbon(int min, int max){
        if(isInEditMode) {
            Log.d( TAG, "Is in edit mode" );
            paintRibbon(viewListener, seekBar);
            screenlayout.removeView(btnDeleteRibbon);
            isInEditMode = false;
        }

        seekBar = new RangeSeekBar<>(0, 96, this);
        seekBar.setSelectedMinValue(min);
        seekBar.setSelectedMaxValue(max);
        seekBar.setPrevMin(min);
        seekBar.setPrevMax(max);
        verticalTimes.getChildAt(min).setBackgroundColor(getResources().getColor(R.color.RibbonColorPrimary));
        verticalTimes.getChildAt(max).setBackgroundColor(getResources().getColor(R.color.RibbonColorPrimary));

        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);

                verticalTimes.getChildAt(seekBar.getPrevMin()).setBackgroundColor(Color.TRANSPARENT);
                verticalTimes.getChildAt(seekBar.getPrevMax()).setBackgroundColor(Color.TRANSPARENT);

                verticalTimes.getChildAt(minValue).setBackgroundColor(
                        getResources().getColor(R.color.RibbonColorPrimary)
                );
                verticalTimes.getChildAt(maxValue).setBackgroundColor(
                        getResources().getColor(R.color.RibbonColorPrimary)
                );

                seekBar.setPrevMin(minValue);
                seekBar.setPrevMax(maxValue);
            }
        });

        seekBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "SeekBar onLongClick");
                paintRibbon(v, seekBar);
                return true;
            }
        });

        paramsRLLeft = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        paramsRLLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        Log.d(TAG, "Before adding view");
        leftlayout.addView(seekBar, paramsRLLeft);
        Log.d(TAG, "After adding view");
        viewListener = seekBar;
        isInEditMode = true;
        Log.d(TAG, "EDIT MODE: " + isInEditMode);

        btnDeleteRibbon = new Button(this);
        btnDeleteRibbon.setText(getResources().getText(R.string.delete));
        btnDeleteRibbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftlayout.removeView(seekBar);
                screenlayout.removeView(v);
                int seekBarStart = seekBar.getSelectedMinValue();
                int seekBarEnd = seekBar.getSelectedMaxValue();
                View minView = verticalTimes.getChildAt(seekBarStart);
                View maxView = verticalTimes.getChildAt(seekBarEnd);

                minView.setBackgroundColor(Color.TRANSPARENT);
                maxView.setBackgroundColor(Color.TRANSPARENT);
                isInEditMode = false;
            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        screenlayout.addView(btnDeleteRibbon, params);
    }

    // ACCEPTED RIBBON
    private boolean paintRibbon(View view, RangeSeekBar seekBar){
        int h = (int) seekBar.getLineHeight();
        int viewWidth = seekBar.getWidth();
        int seekBarStart = (int) seekBar.getSelectedMinValue();
        int seekBarEnd = (int) seekBar.getSelectedMaxValue();
        View minView = verticalTimes.getChildAt(seekBarStart);
        View maxView = verticalTimes.getChildAt(seekBarEnd);

        minView.setBackgroundColor(Color.TRANSPARENT);
        maxView.setBackgroundColor(Color.TRANSPARENT);

        leftlayout.removeView(view);    // Removes seekBar
        screenlayout.removeView(btnDeleteRibbon);

        final DrawTestView ribbon = new DrawTestView(
                this, viewWidth, h, seekBarStart, seekBarEnd,
                minView.getTop() + getResources().getDimensionPixelOffset(R.dimen.dp10),
                maxView.getBottom() - getResources().getDimensionPixelOffset(R.dimen.dp10)
        );
        ribbon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                leftlayout.removeView(v);
                putRibbon(ribbon.getStartTime(), ribbon.getEndTime());

                return true;
            }
        });

        paramsRLLeft = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramsRLLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsRLLeft.setMargins(0, (int) ribbon.getStartY(), 0, 0);
//        ribbon.setTop((int) ribbon.getStartY());
//        ribbon.setBottom((int) ribbon.getEndY());
        leftlayout.addView(ribbon, paramsRLLeft);

        ribbon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //ribbon.setTop((int) ribbon.getStartY());
                ribbon.setBottom((int) ribbon.getEndY());
                Log.d(TAG, "Top: " + ribbon.getTop() + " Bottom: " + ribbon.getBottom());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    ribbon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    ribbon.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        isInEditMode = false;
        Log.d( TAG, "EDIT MODE: " + isInEditMode );

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_availability, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_ribbon:
                putRibbon(0, 96);
                break;
            default:
                break;
        }

        return true;
    }

    private void setupTimesArray(){
        Log.d(TAG, "Setting up times");
        times[0] = times[96] = "12:00am";
        times[1] = "12:15";
        times[2] = "12:30";
        times[3] = "12:45";
        times[4] = "1:00";
        int tmpTime = 100;
        int counter = 0;
        int i = 5;
        while( i < times.length - 1 ){
            if( (++counter % 4) == 0 )
                tmpTime += 40;
            tmpTime += 15;

            if( tmpTime == 1300 )
                tmpTime -= 1200;

            times[i] = Integer.toString(tmpTime).substring(0,1);

            if( tmpTime > 960 ){
                times[i] += Integer.toString(tmpTime).substring(1,2);
                times[i] += ":";
                times[i] += Integer.toString(tmpTime).substring(2,4);
            }
            else{
                times[i] += ":";
                times[i] += Integer.toString(tmpTime).substring(1,3);
            }

            if( tmpTime == 1200 )
                times[i] += "pm";

            //Log.d( TAG, "Inserted into time array " + i + ": " + times[i] );
            i++; //counter++;
        }
    }

    class TimesViewAdapter extends BaseAdapter {
        Context _context;

        public TimesViewAdapter( Context context ){
            _context = context;
        }

        @Override
        public int getCount() {
            return times.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if( view == null ){
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate( R.layout.weekday_header, null );
            }
            TextView time = (TextView) view.findViewById(R.id.weekdayName);
            time.setText(times[position]);
            time.setHeight(getResources().getDimensionPixelSize(R.dimen.times_height));

            return view;
        }
    }
}