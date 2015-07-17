package com.waterbanana.meetapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalendarFrag extends Fragment{
    private int CNT = 9;

    public CalendarFrag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_calendar2, container, false );

        ViewPager viewPager = (ViewPager) view.findViewById( R.id.fragment_cal2_viewpager );
        viewPager.setAdapter( new CalendarMonthsAdapter( getChildFragmentManager() ) );
        viewPager.setCurrentItem( CNT/2 );

        return view;
    }

    class CalendarMonthsAdapter extends FragmentStatePagerAdapter {

        private String TAG = "CalendarMonthsAdapter";

        public CalendarMonthsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "Getting position: " + position);
            Bundle monthBundle = new Bundle();
            int offset = position - (CNT / 2);
            monthBundle.putInt("offset", offset);
            Log.d( TAG, "Offset: " + offset );
            Fragment calendar = Fragment.instantiate( getActivity(), Calendar.class.getName(), monthBundle );

            return calendar;
        }

        @Override
        public int getCount() {
            return CNT;
        }
    }
}
