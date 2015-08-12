package com.waterbanana.meetapp;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by Eddie on 8/8/2015.
 */
public class TimesView extends GridView{
    private GridView verticalTimes;
    private String[] times = new String[96];
    private Context _context;

    private String TAG = "TimesView";

    public TimesView(Context context) {
        this(context, null, 0);
    }

    public TimesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _context = context;

        verticalTimes = new GridView(context);
        setupTimesArray();
        setupGridView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    private void setupGridView(){
        Log.d( TAG, "Setting up GridView" );

        verticalTimes.setNumColumns(1);

    }

    private void setupTimesArray(){
        Log.d( TAG, "Setting up times" );
        times[0] = "12:00am";
        times[1] = "12:15";
        times[2] = "12:30";
        times[3] = "12:45";
        times[4] = "1:00";
        int tmpTime = 100;
        int counter = 0;
        int i = 5;
        while( times[i] != null ){
            if( counter++ % 4 == 0 ){
                tmpTime += 140;
            }
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
            Log.d( TAG, "Inserted into time array " + i + ": " + times[i] );
            i++;
        }
    }

    class TimesViewAdapter extends BaseAdapter{
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
