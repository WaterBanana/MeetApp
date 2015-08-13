package com.waterbanana.meetapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waterbanana.common.RangeSeekBar;


public class Availability extends AppCompatActivity {
    private GridView verticalTimes;
    private RelativeLayout midlayout, leftlayout;
    private String[] times = new String[97];
    private int lastStartPosition, lastEndPosition;
    private int minimumHeight = 500;
    private int leftlayoutWidth;
    RangeSeekBar<Integer> seekBar;

    private String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // create RangeSeekBar as Integer range between 0 and 96
        seekBar = new RangeSeekBar<>(0, 96, this);
        seekBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return paintRibbon(v);
            }
        });
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                Log.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);

                verticalTimes.getChildAt(lastStartPosition).setBackgroundColor(Color.TRANSPARENT);
                verticalTimes.getChildAt(lastEndPosition).setBackgroundColor(Color.TRANSPARENT);

                verticalTimes.getChildAt(minValue).setBackgroundColor(
                        getResources().getColor(R.color.RibbonColorPrimary)
                );
                verticalTimes.getChildAt(maxValue).setBackgroundColor(
                        getResources().getColor(R.color.RibbonColorPrimary)
                );

                lastStartPosition = minValue;
                lastEndPosition = maxValue;
//                verticalTimes.invalidate();
//                verticalTimes.getChildAt(minValue).setBackgroundColor(Color.TRANSPARENT);
//                verticalTimes.getChildAt(maxValue).setBackgroundColor(Color.TRANSPARENT);
            }
        });
        midlayout = (RelativeLayout) findViewById(R.id.availability_layout_mid);
        //layout.addView(seekBar);

        setupTimesArray();

        verticalTimes = new GridView(this);
        verticalTimes.setNumColumns(1);
        verticalTimes.setAdapter(new TimesViewAdapter(this));
        verticalTimes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch( event.getAction() & MotionEvent.ACTION_MASK ){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });

        midlayout.addView(verticalTimes);

//        Log.d( TAG, "Child count: " + verticalTimes.getChildCount() );

        verticalTimes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                verticalTimes.getChildAt(0).setBackgroundColor(
                        getResources().getColor(R.color.RibbonColorPrimary)
                );
                verticalTimes.getChildAt(96).setBackgroundColor(
                        getResources().getColor(R.color.RibbonColorPrimary)
                );
                lastStartPosition = 0;
                lastEndPosition = 96;

//                midlayout.setMinimumHeight(verticalTimes.getHeight());
//                Log.d( TAG, "Minimum height: " + verticalTimes.getHeight() );
//
//                Log.d(TAG, "Child count: " + Integer.toString(verticalTimes.getChildCount()));
                View lastTime = verticalTimes.getChildAt(verticalTimes.getChildCount() - 1);
                minimumHeight = lastTime.getBottom() + 20;
                Log.d( TAG, "Minimum height should be: " + lastTime.getBottom() );
//                leftlayout.setMinimumHeight(lastTime.getBottom() - 500);
//                leftlayout.invalidate();
//                Log.d(TAG, "Bottom: " + Integer.toString(lastTime.getBottom()));
//                midlayout.invalidate();
//                midlayout.removeAllViews();
//                midlayout.addView(verticalTimes);

                midlayout.setLayoutParams(new LinearLayout.LayoutParams(
                        leftlayoutWidth,
                        minimumHeight
                ));

                leftlayout.setLayoutParams(new LinearLayout.LayoutParams(
                        leftlayoutWidth,
                        minimumHeight
                ));

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                leftlayout.addView(seekBar, params);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    verticalTimes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    verticalTimes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        leftlayout = (RelativeLayout) findViewById(R.id.availability_layout_left);

        leftlayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                leftlayoutWidth = leftlayout.getWidth();
                Log.d(TAG, "Left layout width: " + leftlayoutWidth);
                Log.d(TAG, "Minimum height: " + minimumHeight);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    verticalTimes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    verticalTimes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });


//        if(Build.VERSION.SDK_INT >= 17){
//            params.setMarginStart(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin));
//        }
//        else{
//            params.setMargins(
//                    getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin) * 10, 0, 0, 0
//            );
//        }
    }

    private boolean paintRibbon(View view){
        int h = (int) seekBar.getLineHeight();
        int viewWidth = seekBar.getWidth();
        final int seekBarStart = seekBar.getSelectedMinValue();
        int seekBarEnd = seekBar.getSelectedMaxValue();
        View lastStartView = verticalTimes.getChildAt(lastStartPosition);
        View lastEndView  = verticalTimes.getChildAt(lastEndPosition);
        lastStartView.setBackgroundColor(Color.TRANSPARENT);
        lastEndView.setBackgroundColor(Color.TRANSPARENT);

        leftlayout.removeView(view);

        final DrawTestView ribbon = new DrawTestView(
                this, viewWidth, h, lastStartPosition, lastEndPosition,
                lastStartView.getTop() + getResources().getDimensionPixelOffset(R.dimen.dp10),
                lastEndView.getBottom() - getResources().getDimensionPixelOffset(R.dimen.dp10),
                seekBarStart, seekBarEnd
        );
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ribbon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int seekStart = ribbon.getSeekBarStart();
                int seekEnd = ribbon.getSeekBarEnd();
                leftlayout.removeView(v);
                createSeekBar(seekStart, seekEnd);
                return true;
            }
        });

        leftlayout.addView(ribbon, lp);

        return true;
    }

    private void createSeekBar(int start, int end){
        seekBar = new RangeSeekBar<>(0, 96, this);
        seekBar.setSelectedMinValue(start);
        seekBar.setSelectedMaxValue(end);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        seekBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return paintRibbon(v);
            }
        });

        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                Log.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);

                verticalTimes.getChildAt(lastStartPosition).setBackgroundColor(Color.TRANSPARENT);
                verticalTimes.getChildAt(lastEndPosition).setBackgroundColor(Color.TRANSPARENT);

                verticalTimes.getChildAt(minValue).setBackgroundColor(
                        getResources().getColor(R.color.RibbonColorPrimary)
                );
                verticalTimes.getChildAt(maxValue).setBackgroundColor(
                        getResources().getColor(R.color.RibbonColorPrimary)
                );

                lastStartPosition = minValue;
                lastEndPosition = maxValue;
//                verticalTimes.invalidate();
//                verticalTimes.getChildAt(minValue).setBackgroundColor(Color.TRANSPARENT);
//                verticalTimes.getChildAt(maxValue).setBackgroundColor(Color.TRANSPARENT);
            }
        });

        leftlayout.addView(seekBar, lp);
    }

    private void setupTimesArray(){
        Log.d( TAG, "Setting up times" );
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
