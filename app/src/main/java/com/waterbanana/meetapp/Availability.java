//Now need to create an actual table - GAA 22 AUG 2015
//GAA 23AUG2015
//Crashing on lines 122, 142, 365. Has something to do with the new table I'm trying to create(maybe the format?)
package com.waterbanana.meetapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.waterbanana.common.RangeSeekBar;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

/**
 * NOTES:
 * - LocalDb edits will automatically update online database as well.
 *
 * **Please make sure to reset your application via options if you haven't updated since: 28DEC2015**
 *
 */
public class Availability extends AppCompatActivity
    implements EnterGroupIdDialog.GroupIdListener{
    private GridView verticalTimes;
    private RelativeLayout screenlayout, midlayout, rightlayout, leftlayout;
    private String[] times = new String[97];
    private int layoutHeight;
    private RelativeLayout.LayoutParams paramsRLLeft;
    private boolean isInEditMode = false;
    private RangeSeekBar<Integer> seekBar;
    private View viewListener;
    private Button btnDeleteRibbon;
    private String date;
    private ArrayList<NameValuePair> selfRibbons;
    private String TAG = "Availability.java";
    private LocalDbHandler helper;
    private LocalDb localDb;
    private int selectedRID;
    private String month, day, year;
    private GroupTestAvailability gRibbon;
    private ArrayList<Integer> listOfRibbons;
    private boolean saveOnline = false;
    private ScrollView scrollView;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        date = bundle.getString("date", "1999-01-01");
        month = date.split("-")[1];
        day = date.split("-")[2];
        year = date.split("-")[0];
        Log.d("Availability.java", "Month: " + month + " Day: " + day + " Year: " + year);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        screenlayout = (RelativeLayout) findViewById(R.id.relativeLayout_activity_availability);
        midlayout = (RelativeLayout) findViewById(R.id.availability_layout_mid);
        rightlayout = (RelativeLayout) findViewById(R.id.availability_layout_right);
        leftlayout = (RelativeLayout) findViewById(R.id.availability_layout_left);
        scrollView = (ScrollView) findViewById(R.id.availability_scrollview);

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

        pd = new ProgressDialog(this);
        midlayout.addView(verticalTimes);
        gRibbon = (GroupTestAvailability)leftlayout;

        localDb = new LocalDb(this);
        //selfRibbons = localDb.getRibbonsByUserId(localDb.getLocalId(), date);

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

                rightlayout.setLayoutParams(new LinearLayout.LayoutParams(
                        verticalTimes.getWidth(), layoutHeight
                ));

                leftlayout.setLayoutParams(new LinearLayout.LayoutParams(
                        verticalTimes.getWidth(), layoutHeight
                ));
                gRibbon.setTimesView(verticalTimes);
                gRibbon.setDate(date);

                new LoadSelfRibbons().execute();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    verticalTimes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    verticalTimes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        btnDeleteRibbon = new Button(this);
        btnDeleteRibbon.setText(getResources().getText(R.string.delete));
        btnDeleteRibbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightlayout.removeView(seekBar);
                screenlayout.removeView(v);
                int seekBarStart = seekBar.getSelectedMinValue();
                int seekBarEnd = seekBar.getSelectedMaxValue();
                View minView = verticalTimes.getChildAt(seekBarStart);
                View maxView = verticalTimes.getChildAt(seekBarEnd);

                minView.setBackgroundColor(Color.TRANSPARENT);
                maxView.setBackgroundColor(Color.TRANSPARENT);

                Log.d( TAG, "Deleting ribbon: " + selectedRID );
                localDb.removeSelfRibbon(selectedRID);

                isInEditMode = false;
            }
        });

//        //CRASHING GAA 23AUG2015;
//        helper = new LocalDbHandler(this);
//        SQLiteDatabase sqlDB = helper.getReadableDatabase();
//        Cursor cursor = sqlDB.query(LocalDbContract.TABLE,
//                new String[]{
//                        "`"+month+"ID`",
//                        "`"+month+"DATE`",
//                        "`"+month+"RIBBONSTART`",
//                        "`"+month+"RIBBONEND`"},
//                "Day=?", new String[]{day}, null, null, null);




//        public static final String COLUMN_JAN_ID = "01ID";
//        public static final String COLUMN_JAN_DATE = "01DATE";
//        public static final String COLUMN_JAN_RIBBONSTART="01RIBBONSTART";
//        public static final String COLUMN_JAN_RIBBONEND="01RIBBONEND";

//        View minView = verticalTimes.getChildAt(minValue);
//        View maxView = verticalTimes.getChildAt(maxValue);

        //populate drawTestViewArray from the android db

//        //CRASHING - GAA 8:23 PM 23AUG2015:
//        //The table isn't being created properly
//        if (cursor.moveToFirst()) {
//            while(!cursor.isAfterLast()) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG
//                int min = Integer.parseInt(cursor.getString(cursor.getColumnIndex(month+"RIBBONSTART")));
//                int max = Integer.parseInt(cursor.getString(cursor.getColumnIndex(month+"RIBBONEND")));
//                View minView = verticalTimes.getChildAt(min);
//                View maxView = verticalTimes.getChildAt(max);
//
//                DrawTestView newRibbon = new DrawTestView(
//                        this,
//                        min,
//                        max,
//                        minView.getTop() + getResources().getDimensionPixelOffset(R.dimen.dp10),
//                        maxView.getBottom() - getResources().getDimensionPixelOffset(R.dimen.dp10));
//
//                drawTestViewArray.add(newRibbon);
//                cursor.moveToNext();
//            }
//
//        }
//        cursor.close();

        //Show the ribbons that are already there. They should be DrawTestView objects
//        if(!drawTestViewArray.isEmpty()) {
//            for (int i = 0; i < drawTestViewArray.size(); i++) {
//                paintRibbon(null,
//                        drawTestViewArray.get(i).getViewWidth(),//is there a default we can use or a method to grab this? Doing so with make our resulting array smaller
//                        drawTestViewArray.get(i).getLineWidth(),//same as above
//                        drawTestViewArray.get(i).getMinValue(),
//                        drawTestViewArray.get(i).getMaxValue());
//            }
//        }

//        int height, int width, int seekBarStart, int seekBarEnd
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(localDb == null){
            localDb = new LocalDb(this);
        }
    }

    @Override
    public void getSelectedId(DialogFragment dialog, String selectedGroupId){
        gRibbon.setGroupidToLoad(Integer.parseInt(selectedGroupId.substring(9, 13)));
        gRibbon.refreshUserList();

        dialog.dismiss();
    }

//    @Override
//    public void setPositiveButton(DialogFragment dialog) {
//        EditText etGroupId = (EditText) dialog.getDialog().findViewById(R.id.editTextCPFailed);
//        String groupIdStr = etGroupId.getText().toString();
//        gRibbon.setGroupidToLoad(Integer.parseInt(groupIdStr));
//        gRibbon.invalidate();
//
//        dialog.dismiss();
//    }
//
//    @Override
//    public void setNegativeButton(DialogFragment dialog) {
//        dialog.dismiss();
//    }

    // SEEKBAR
    private void putRibbon(int min, int max){
        if(isInEditMode) {
            Log.d( TAG, "Is in edit mode" );
            paintRibbon(viewListener, -1,
                    (int)seekBar.getLineHeight(),
                    seekBar.getWidth(),
                    seekBar.getSelectedMinValue(),
                    seekBar.getSelectedMaxValue());
            screenlayout.removeView(btnDeleteRibbon);
            isInEditMode = false;
        }

        // This block will adjust new ribbons to fit only on the viewed times for ease of selecting.
        boolean prevWasVisible = false;
        for( int i = 0; i < verticalTimes.getChildCount(); i++ ){
            Rect bounds = new Rect();
            verticalTimes.getChildAt(i).getHitRect(bounds);

            Rect scrollBounds = new Rect();
            scrollView.getDrawingRect(scrollBounds);
            if(Rect.intersects(bounds, scrollBounds)){
                if(!prevWasVisible){
                    min = i;
                    prevWasVisible = true;
                }
                max = i;
            }
        }
//        if( selectedRID == -1 ) {
//            min = verticalTimes.getFirstVisiblePosition();
//            max = verticalTimes.getLastVisiblePosition();
//            Log.d( TAG, "First visible view: " + min );
//            Log.d( TAG, "Last visible view: " + max );
//        }

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
                paintRibbon(v, selectedRID, // check if editting ribbon
                        (int) seekBar.getLineHeight(),
                        seekBar.getWidth(),
                        seekBar.getSelectedMinValue(),
                        seekBar.getSelectedMaxValue());
                return true;
            }
        });

        paramsRLLeft = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        paramsRLLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        Log.d(TAG, "Before adding view");
        rightlayout.addView(seekBar, paramsRLLeft);
        Log.d(TAG, "After adding view");
        viewListener = seekBar;
        isInEditMode = true;
        Log.d(TAG, "EDIT MODE: " + isInEditMode);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        screenlayout.addView(btnDeleteRibbon, params);
    }

    // ACCEPTED RIBBON
    private boolean paintRibbon(View view, int ribbonId, int lineWidth, int viewWidth, int minValue, int maxValue){

        Log.d( TAG, "Child count: " + verticalTimes.getChildCount() +
                "minVal: " + minValue + "maxVal: " + maxValue );
        View minView = verticalTimes.getChildAt(minValue);
        View maxView = verticalTimes.getChildAt(maxValue);

        if(view != null) {
            minView.setBackgroundColor(Color.TRANSPARENT);
            maxView.setBackgroundColor(Color.TRANSPARENT);
            rightlayout.removeView(view);    // Removes seekBar
            screenlayout.removeView(btnDeleteRibbon);
        }

        int newRibbonId = (int) localDb.saveSelfRibbon(ribbonId, date, lineWidth, viewWidth, minValue, maxValue, saveOnline);
        if( newRibbonId == -1 )
            newRibbonId = ribbonId;
        Log.d( TAG, "New ribbon id: " + newRibbonId );

        final DrawTestView ribbon = new DrawTestView(
                this, newRibbonId, viewWidth, lineWidth, minValue, maxValue,
                minView.getTop() + getResources().getDimensionPixelOffset(R.dimen.dp10),
                maxView.getBottom() - getResources().getDimensionPixelOffset(R.dimen.dp10)

        );

        ribbon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedRID = ribbon.getRID();
                Log.d( TAG, "Selected ribbon id: " + selectedRID );
                rightlayout.removeView(v);
                saveOnline = true;
                putRibbon(ribbon.getStartTime(), ribbon.getEndTime());

                return true;
            }
        });
//        drawTestViewArray.add(ribbon);
        //add the ribbon into the android db instead


        paramsRLLeft = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramsRLLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsRLLeft.setMargins(0, (int) ribbon.getStartY(), 0, 0);
//        ribbon.setTop((int) ribbon.getStartY());
//        ribbon.setBottom((int) ribbon.getEndY());
        rightlayout.addView(ribbon, paramsRLLeft);

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
        Log.d(TAG, "EDIT MODE: " + isInEditMode);

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
                selectedRID = -1;
                saveOnline = true;
                putRibbon(0, 96);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        localDb.close();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //go and update the online db with the android db
//        //CRASHING GAA 23AUG2015
//        storeRibbonsInLocalDb();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        new LoadSelfRibbons().execute();
//    }

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

    /*public void storeRibbonsInLocalDb(){
        int ribbonBegin;
        int ribbonEnd;
        String[] ribbonDate;
        int ribbonID;
        int grabbedID;
        User user;
        String month;
        SQLiteDatabase sqlDB;
        DbHandler sqlOnlineDB;
        ContentValues newIDValues;
        ContentValues newDateValues;
        ContentValues newRibbonStartValues;
        ContentValues newRibbonEndValues;

        sqlDB = helper.getReadableDatabase();
        sqlOnlineDB = new DbHandler();

        for (int i = 0; i < drawTestViewArray.size(); i++) {
            ribbonID = -1;
            ribbonDate = date.split("-");
            month=ribbonDate[1];
            ribbonBegin = drawTestViewArray.get(i).getMinValue();
            ribbonEnd = drawTestViewArray.get(i).getMaxValue();

            //create ribbon on online db and return online id
            Log.d("Create Online Ribbon", ribbonDate[i] + ":" + ribbonBegin + "-" + ribbonEnd);
            Intent intent = new Intent(this, CreateEntryFromLocal.class);
            intent.putExtra("ribbonID", ribbonID);
            intent.putExtra("date", date);
            intent.putExtra("ribbonBegin", ribbonBegin);
            intent.putExtra("ribbonEnd", ribbonEnd);
            startActivity(intent);


            //how do we get back the online id?
            user = sqlOnlineDB.getAllRibbonsByUserId("0");
            grabbedID = user.getRibbonIDwithDateStartEnd(date, ribbonBegin, ribbonEnd);
            ribbonID = grabbedID;


            //using the online id, create the ribbon in the local database
            newIDValues = new ContentValues();
//            newValues.put("YOUR_COLUMN", "newValue");
            newIDValues.put(month+"ID", ribbonID);
            sqlDB.update(LocalDbContract.TABLE, newIDValues, "Day="+ribbonDate[2], null);

            newDateValues = new ContentValues();
//            newValues.put("YOUR_COLUMN", "newValue");
            newDateValues.put(month+"DATE", date);
            sqlDB.update(LocalDbContract.TABLE, newDateValues, "Day="+ribbonDate[2], null);

            newRibbonStartValues = new ContentValues();
//            newValues.put("YOUR_COLUMN", "newValue");
            newRibbonStartValues.put(month+"RIBBONSTART", ribbonBegin);
            sqlDB.update(LocalDbContract.TABLE, newRibbonStartValues, "Day="+ribbonDate[2], null);

            newRibbonEndValues = new ContentValues();
//            newValues.put("YOUR_COLUMN", "newValue");
            newRibbonEndValues.put(month+"RIBBONEND", ribbonEnd);
            sqlDB.update(LocalDbContract.TABLE, newRibbonEndValues, "Day="+ribbonDate[2], null);

        }
    }*/


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

    class LoadSelfRibbons extends AsyncTask<String, String, String> {
        int RID = -1, lineWidth = 0, viewWidth = 0, minVal = 0, maxVal = 0;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setIndeterminate(true);
            pd.show();

            DialogFragment dialog = new EnterGroupIdDialog();
            dialog.show(getSupportFragmentManager(), "EnterGroupIdDialog");
        }

        @Override
        protected String doInBackground(String... params) {
            selfRibbons = localDb.getSelfRibbons(date);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if( !selfRibbons.isEmpty() ){
                Log.d( TAG, "selfRibbons is not empty; size: " + selfRibbons.size() );
                // Show saved ribbons

                for( int i = 0; i < selfRibbons.size(); i++ ){
                    switch(i%6){
                        case 0:
                            RID = Integer.parseInt( selfRibbons.get(i).getValue() );
                            break;
                        case 1:
                            lineWidth = Integer.parseInt( selfRibbons.get(i).getValue() );
                            break;
                        case 2:
                            viewWidth = Integer.parseInt( selfRibbons.get(i).getValue() );
                            break;
                        case 3:
                            minVal = Integer.parseInt( selfRibbons.get(i).getValue() );
                            break;
                        case 4:
                            maxVal = Integer.parseInt( selfRibbons.get(i).getValue() );
                            break;
                        default:    // First entry: .get(0)
                            Log.d( TAG, "Painting ribbon." );
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    paintRibbon(null, RID, lineWidth, viewWidth, minVal, maxVal);
                                }
                            });
                            //RID = Integer.parseInt( selfRibbons.get(0).getValue() );
                            break;
                    }
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        paintRibbon(null, RID, lineWidth, viewWidth, minVal, maxVal );
//                    }
//                });
            }

            pd.cancel();
        }
    }
}