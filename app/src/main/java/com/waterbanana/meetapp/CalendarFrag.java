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

    private int groupIdToLoad = 0;

    public CalendarFrag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_calendar2, container, false );

        ViewPager viewPager = (ViewPager) view.findViewById( R.id.fragment_cal2_viewpager );
        viewPager.setAdapter( new CalendarMonthsAdapter( getChildFragmentManager() ) );
        viewPager.setCurrentItem( CNT/2 );

        if( getArguments() != null ){
            groupIdToLoad = getArguments().getInt("groupid");
        }

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
            monthBundle.putInt("groupid", groupIdToLoad);
            Log.d( TAG, "Offset: " + offset );
            return Fragment.instantiate( getActivity(), Calendar.class.getName(), monthBundle );
        }

        @Override
        public int getCount() {
            return CNT;
        }
    }
}
