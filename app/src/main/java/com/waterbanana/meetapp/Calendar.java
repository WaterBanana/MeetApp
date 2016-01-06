package com.waterbanana.meetapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Demonstration purposes only.
 */
public class Calendar extends Fragment implements View.OnClickListener{
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String tag = "MyCalendarActivity";

    private TextView currentMonth;
    private Button selectedDayMonthYearButton;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private java.util.Calendar _calendar;
    private int month, year;
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";
    private final int[] weekdayIds = {R.string.Sun, R.string.Mon, R.string.Tue, R.string.Wed,
            R.string.Thu, R.string.Fri, R.string.Sat};
    private final String[] weekdays = new String[7];
    private final int[] monthIds = {R.string.Jan, R.string.Feb, R.string.Mar, R.string.Apr, R.string.May,
            R.string.Jun, R.string.Jul, R.string.Aug, R.string.Sep, R.string.Oct, R.string.Nov, R.string.Dec};
    private final int[] monthNums = {R.string.JanVal, R.string.FebVal, R.string.MarVal, R.string.AprVal,
            R.string.MayVal, R.string.JunVal, R.string.JulVal, R.string.AugVal, R.string.SepVal, R.string.OctVal,
            R.string.NovVal, R.string.DecVal};
    private final String[] months = new String[12];
    private int screenHeight;
    private ArrayList<ArrayList<Double>> meetingPopulations = new ArrayList<> ();
    private int offset;
    private int groupIdToLoad;

    public Calendar() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_view, container, false);
        init();
        if( getArguments() != null ) {
            offset = getArguments().getInt("offset");
            groupIdToLoad = getArguments().getInt("groupid");
        }
        else{
            offset = 0;
        }

        _calendar = java.util.Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(java.util.Calendar.MONTH) + 1;
        year = _calendar.get(java.util.Calendar.YEAR);
        Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
                + year);

        int displayMm = month + offset;
        if( displayMm < 1 ){
            month = 12 - Math.abs(displayMm);
            year--;
        }
        else if( displayMm > 12 ){
            month = displayMm - 12;
            year++;
        }
        else{
            month = displayMm;
        }

//        selectedDayMonthYearButton = (Button) this
//                .findViewById(R.id.selectedDayMonthYear);
//        selectedDayMonthYearButton.setText("Selected: ");
//
//        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
//        prevMonth.setOnClickListener(this);
//
        Log.d("Calendar", "Month: " + month + ";");
        currentMonth = (TextView) view.findViewById(R.id.currentMonth);
        currentMonth.setText(months[month - 1]);
//        currentMonth.setText(DateFormat.format(dateTemplate,
//                _calendar.getTime()));
//
//        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
//        nextMonth.setOnClickListener(this);

        calendarView = (GridView) view.findViewById(R.id.calendarView);

        // Initialised
        // Set up weekday header
        GridView weekdayHeader = (GridView) view.findViewById(R.id.weekdayHeader);
        weekdayHeader.setFocusable(false);
        weekdayHeader.setAdapter(new HeaderNameAdapter(getActivity()));

        adapter = new GridCellAdapter(getActivity(),
                R.id.calendarCell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

        final View v = view;
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                screenHeight = v.getHeight();
                calendarView.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.calendar_spacing));
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        return view;
    }

    private void init(){
        // Initialize names
        Resources res = getResources();
        for( int i = 0; i < 12; i++ ){
            if( i < 7 )
                weekdays[i] = res.getString(weekdayIds[i]);
            months[i] = res.getString(monthIds[i]);
        }
    }

    /**
     *
     * @param month
     * @param year
     */
    public void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getActivity(),
                R.id.calendarCell, month, year);
        _calendar.set(year, month - 1, _calendar.get(java.util.Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
//        new LoadAllGroups().execute();
    }

//    @Override
//    public void onDestroy() {
//        Log.d(tag, "Destroying View ...");
//        super.onDestroy();
//    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31 };
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
//        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
//                "dd-MMM-yyyy");
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-mm-dd");
        private int divideBy;

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
                    + "Year: " + year);
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(java.util.Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(java.util.Calendar.DAY_OF_WEEK));
//            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
//            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
//            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            initializeMeetingPopulation();
//            recolorDaysDueToMeetingPopulation(6, 15, 0.91);//(5, 4) = (June, 16)
//            recolorDaysDueToMeetingPopulation(6, 10, 0.89);//(5, 4) = (June, 11)
//            recolorDaysDueToMeetingPopulation(6, 8, 0.20);//(5, 4) = (June, 9)
//            recolorDaysDueToMeetingPopulation(6, 18, 0.55);//(5, 4) = (June, 17)
            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private void initializeMeetingPopulation(){//initializes each day of the month with zero
            for(int i=0; i<12; i++){
                meetingPopulations.add(new ArrayList<Double>());
                for(int j=0; j<getNumberOfDaysOfMonth(i); j++){
                    meetingPopulations.get(i).add(0.0);
                }
            }
        }

        private void recolorDaysDueToMeetingPopulation(int month, int day, double percentage){
            //temp values
            meetingPopulations.get(month).set(day,percentage);//replaces the meeting population to 1, thus to RED
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

//        private String getMonthAsNum(int i){return monthNums[i];}

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
            String month = Integer.toString(mm);
            String year = Integer.toString(yy);
            new DisplayMonth().execute(month, year);
        }

/*        private void displayMonth( int mm, int yy ){
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

//            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
//                    + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
//            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
//                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
//                        + prevMonth + " NextMonth: " + nextMonth
//                        + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
//                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
//                        + prevMonth + " NextMonth: " + nextMonth
//                        + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
//                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
//                        + prevMonth + " NextMonth: " + nextMonth
//                        + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(java.util.Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
//                Log.d(tag,
//                        "PREV MONTH:= "
//                                + prevMonth
//                                + " => "
//                                + getMonthAsString(prevMonth)
//                                + " "
//                                + String.valueOf((daysInPrevMonth
//                                - trailingSpaces + DAY_OFFSET)
//                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
//                Log.d(currentMonthName, String.valueOf(i) + " "
//                        + getMonthAsString(currentMonth) + " " + yy);
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }else if(meetingPopulations.get(currentMonth).get(i-1) == 0.0){//Default
                    list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }else if (meetingPopulations.get(currentMonth).get(i-1) <= 0.29) {//GAA 25 JUN 2015
                    //RED
                    list.add(String.valueOf(i) + "-RED" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }else if(meetingPopulations.get(currentMonth).get(i-1) <= 0.59){
                    //ORANGE
                    list.add(String.valueOf(i) + "-ORANGE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }else if(meetingPopulations.get(currentMonth).get(i-1) <= 0.89){
                    //YELLOW
                    list.add(String.valueOf(i) + "-YELLOW" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }else if(meetingPopulations.get(currentMonth).get(i-1) <= 1.00){
                    //GREEN
                    list.add(String.valueOf(i) + "-GREEN" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }else{
                    list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }

            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                //Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }*/

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.calendar_cell_view, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendarCell);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float logicalDensity = metrics.density;
            int dp = (int) Math.ceil(screenHeight / (int) logicalDensity);
            dp -= 60;
            int cellPreHeight = (int) Math.ceil(dp * (int) logicalDensity);
            gridcell.setHeight(cellPreHeight / divideBy);

            //Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = (TextView) row
                            .findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
//            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
//                    + theyear);

            if (day_color[1].equals("GREY")) {//These are the days outside the current month
                gridcell.setTextColor(Color.LTGRAY);

            }
            else if (day_color[1].equals("WHITE")) {//These days are all the days of the month except the current
                gridcell.setTextColor(Color.GRAY);

            }
            else if (day_color[1].equals("BLUE")) {//This is the current day
                gridcell.setTextColor(Color.BLACK);
                gridcell.setBackgroundColor(Color.LTGRAY);
                //gridcell.setBackgroundColor(Color.BLUE);
            }
            else if (day_color[1].equals("RED")){//this is not triggering
                gridcell.setTextColor(Color.WHITE);
                gridcell.setBackgroundColor(Color.RED);
            }
            else if (day_color[1].equals("ORANGE")){//this is not triggering
                gridcell.setTextColor(Color.WHITE);
                gridcell.setBackgroundColor(Color.YELLOW);
            }
            else if (day_color[1].equals("YELLOW")){//this is not triggering
                gridcell.setTextColor(Color.WHITE);
                gridcell.setBackgroundColor(Color.YELLOW);
            }
            else if (day_color[1].equals("GREEN")){//this is not triggering
                gridcell.setTextColor(Color.WHITE);
                gridcell.setBackgroundColor(Color.GREEN);
            }


            return row;
        }

        @Override
        public void onClick(View view) {
//            String date_month_year = (String) view.getTag();
//            Intent intent = new Intent(getActivity(), DrawAvailability.class);
//            intent.putExtra("DATE", date_month_year);
//            //selectedDayMonthYearButton.setText("Selected: " + date_month_year);
//            //Log.e("Selected date", date_month_year);
//            try {
//                Date parsedDate = dateFormatter.parse(date_month_year);
//                //Log.d(tag, "Parsed Date: " + parsedDate.toString());
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            startActivity(intent);

            final String date_month_year = (String) view.getTag();
            String[] stringDateSplit = date_month_year.split("-");
            switch(stringDateSplit[1]){
                case "January":
                    stringDateSplit[1] = "01";
                    break;
                case "February":
                    stringDateSplit[1] = "02";
                    break;
                case "March":
                    stringDateSplit[1] = "03";
                    break;
                case "April":
                    stringDateSplit[1] = "04";
                    break;
                case "May":
                    stringDateSplit[1] = "05";
                    break;
                case "June":
                    stringDateSplit[1] = "06";
                    break;
                case "July":
                    stringDateSplit[1] = "07";
                    break;
                case "August":
                    stringDateSplit[1] = "08";
                    break;
                case "September":
                    stringDateSplit[1] = "09";
                    break;
                case "October":
                    stringDateSplit[1] = "10";
                    break;
                case "November":
                    stringDateSplit[1] = "11";
                    break;
                case "December":
                    stringDateSplit[1] = "12";
                    break;

            }
            /**
             * newDateFormat is yyyy-mm-dd
             */
            final String newDateFormat = stringDateSplit[2] + "-" + stringDateSplit[1] + "-" + stringDateSplit[0];
            Log.d(tag, "newDateFormat = " + newDateFormat);
            Intent intent = new Intent(getActivity(), Availability.class);
            intent.putExtra("date", newDateFormat);
            intent.putExtra("groupid", groupIdToLoad);
            startActivity(intent);

        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }

        class DisplayMonth extends AsyncTask<String, String, String>{

            @Override
            protected String doInBackground(String... params) {
                int mm = Integer.parseInt(params[0]);
                int yy = Integer.parseInt(params[1]);

                Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
                int trailingSpaces = 0;
                int daysInPrevMonth = 0;
                int prevMonth = 0;
                int prevYear = 0;
                int nextMonth = 0;
                int nextYear = 0;

                int currentMonth = mm - 1;
                String currentMonthName = getMonthAsString(currentMonth);
                daysInMonth = getNumberOfDaysOfMonth(currentMonth);

//            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
//                    + daysInMonth + " days.");

                GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
//            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

                if (currentMonth == 11) {
                    prevMonth = currentMonth - 1;
                    daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                    nextMonth = 0;
                    prevYear = yy;
                    nextYear = yy + 1;
//                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
//                        + prevMonth + " NextMonth: " + nextMonth
//                        + " NextYear: " + nextYear);
                } else if (currentMonth == 0) {
                    prevMonth = 11;
                    prevYear = yy - 1;
                    nextYear = yy;
                    daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                    nextMonth = 1;
//                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
//                        + prevMonth + " NextMonth: " + nextMonth
//                        + " NextYear: " + nextYear);
                } else {
                    prevMonth = currentMonth - 1;
                    nextMonth = currentMonth + 1;
                    nextYear = yy;
                    prevYear = yy;
                    daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
//                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
//                        + prevMonth + " NextMonth: " + nextMonth
//                        + " NextYear: " + nextYear);
                }

                // First day (name) of the month
                int currentWeekDay = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
                trailingSpaces = currentWeekDay;

//            Log.d(tag, "Week Day in " + currentMonthName + ":" + currentWeekDay + " is "
//                    + getWeekDayAsString(currentWeekDay));
//            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
//            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

                if (cal.isLeapYear(cal.get(java.util.Calendar.YEAR)))
                    if (mm == 2)
                        ++daysInMonth;
                    else if (mm == 3)
                        ++daysInPrevMonth;



                // Trailing Month days
                for (int i = 0; i < trailingSpaces; i++) {
//                Log.d(tag,
//                        "PREV MONTH:= "
//                                + prevMonth
//                                + " => "
//                                + getMonthAsString(prevMonth)
//                                + " "
//                                + String.valueOf((daysInPrevMonth
//                                - trailingSpaces + DAY_OFFSET)
//                                + i));
                    list.add(String
                            .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                    + i)
                            + "-GREY"
                            + "-"
                            + getMonthAsString(prevMonth)
                            + "-"
                            + prevYear);
                }

                if( currentWeekDay == 0 && daysInMonth == 28 ){
//                    Log.d( tag, "Number of weeks in month: 4" );
                    divideBy = 4;
                }
                else if( (currentWeekDay == 5 && daysInMonth > 30)
                        || (currentWeekDay == 6 && daysInMonth > 29) ) {
//                    Log.d( tag, "Number of weeks in month: 6" );
                    divideBy = 6;
                }
                else{
//                    Log.d( tag, "Number of weeks in month: 5" );
                    divideBy = 5;
                }
//
//                // Current Month Days
                for (int i = 1; i <= daysInMonth; i++) {
                Log.d(currentMonthName, String.valueOf(i) + " "
                        + getMonthAsString(currentMonth) + " " + yy + " " + daysInMonth);

                    if (i == getCurrentDayOfMonth()) {
                        list.add(String.valueOf(i) + "-BLUE" + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
//                    }else if(meetingPopulations.get(currentMonth).get(i-1) == 0.0){//Default
//                        list.add(String.valueOf(i) + "-WHITE" + "-"
//                                + getMonthAsString(currentMonth) + "-" + yy);
//                    }else if (meetingPopulations.get(currentMonth).get(i-1) <= 0.29) {//GAA 25 JUN 2015
//                        //RED
//                        list.add(String.valueOf(i) + "-RED" + "-"
//                                + getMonthAsString(currentMonth) + "-" + yy);
//                    }else if(meetingPopulations.get(currentMonth).get(i-1) <= 0.59){
//                        //ORANGE
//                        list.add(String.valueOf(i) + "-ORANGE" + "-"
//                                + getMonthAsString(currentMonth) + "-" + yy);
//                    }else if(meetingPopulations.get(currentMonth).get(i-1) <= 0.89){
//                        //YELLOW
//                        list.add(String.valueOf(i) + "-YELLOW" + "-"
//                                + getMonthAsString(currentMonth) + "-" + yy);
//                    }else if(meetingPopulations.get(currentMonth).get(i-1) <= 1.00){
//                        list.add(String.valueOf(i) + "-GREEN" + "-"
//                                + getMonthAsString(currentMonth) + "-" + yy);
                        //GREEN
                    }else{
                        list.add(String.valueOf(i) + "-WHITE" + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    }

                }

                // Leading Month days
                for (int i = 0; i < list.size() % 7; i++) {
                    //Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                    list.add(String.valueOf(i + 1) + "-WHITE" + "-"
                            + getMonthAsString(nextMonth) + "-" + nextYear);
                }

                return null;
            }
        }
    }

    class HeaderNameAdapter extends BaseAdapter{
        private Context _context;

        public HeaderNameAdapter( Context context ){
            _context = context;
        }

        @Override
        public int getCount() {  return 7;  }

        @Override
        public Object getItem(int position) {  return position;  }

        @Override
        public long getItemId(int position) {  return position;  }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if( convertView == null ) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.weekday_header, parent, false);
            }

            TextView weekdayName = (TextView) view.findViewById(R.id.weekdayName);
            String dayNameStr = weekdays[position].substring(0, 3);
            weekdayName.setText(dayNameStr);

            return view;
        }
    }
}
