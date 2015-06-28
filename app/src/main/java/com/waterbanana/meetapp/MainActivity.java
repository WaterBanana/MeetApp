package com.waterbanana.meetapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.waterbanana.common.SlidingTabLayout;


public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;
    //private SlidingTabLayout mSTL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);

        SlidingTabLayout mSTL = (SlidingTabLayout) findViewById( R.id.main_sliding_tabs_layout );

        ViewPager viewPager = (ViewPager) findViewById( R.id.pager );
        viewPager.setAdapter( new TabsViewPager( getSupportFragmentManager() ) );
        mSTL.setDistributeEvenly(true);
        mSTL.setViewPager( viewPager );
    }

    class TabsViewPager extends FragmentStatePagerAdapter{

        public TabsViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if( position == 0 )
                return "All Users";
            else
                return "All Groups";
        }

        @Override
        public Fragment getItem(int position) {
            if( position == 0 ){
                return new ViewUsers();
            }
            else{
                return new ViewGroups();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
