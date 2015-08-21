package com.waterbanana.meetapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waterbanana.common.RangeSeekBar;

public class TestActivity extends AppCompatActivity {
    private GridView verticalTimes;
    private RelativeLayout midlayout;
    private String[] times = new String[97];

    private String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // create RangeSeekBar as Integer range between 20 and 75
        RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 96, this);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                Log.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
            }
        });
        midlayout = (RelativeLayout) findViewById(R.id.availability_layout_mid);
        //layout.addView(seekBar);

        setupTimesArray();

        verticalTimes = new GridView(this);
        verticalTimes.setNumColumns(1);
        verticalTimes.setAdapter(new TimesViewAdapter(this));

        midlayout.addView(verticalTimes);

//        verticalTimes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                midlayout.setMinimumHeight(verticalTimes.getHeight());
//                Log.d( TAG, "Minimum height: " + verticalTimes.getHeight() );
//
//                Log.d(TAG, "Child count: " + Integer.toString(verticalTimes.getChildCount()));
//                View lastTime = verticalTimes.getChildAt(verticalTimes.getChildCount() - 1);
//                midlayout.setMinimumHeight(lastTime.getBottom());
//                Log.d(TAG, "Bottom: " + Integer.toString(lastTime.getBottom()));
//                midlayout.invalidate();
//                midlayout.removeAllViews();
//                midlayout.addView(verticalTimes);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//                    verticalTimes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                else
//                    verticalTimes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            }
//        });

        RelativeLayout rightLayout = (RelativeLayout) findViewById(R.id.availability_layout_right);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
//        if(Build.VERSION.SDK_INT >= 17){
//            params.setMarginStart(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin));
//        }
//        else{
//            params.setMargins(
//                    getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin) * 10, 0, 0, 0
//            );
//        }
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightLayout.addView(seekBar, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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

            return view;
        }
    }
}
