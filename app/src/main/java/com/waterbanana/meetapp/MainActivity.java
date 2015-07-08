package com.waterbanana.meetapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.waterbanana.common.SlidingTabLayout;

//Comment
public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;
    //private SlidingTabLayout mSTL;
    //GAA 05JUL2015 - Testing GitHub Version Control
    //hello

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
            else if(position == 1){
                return "All Groups";
            }
            else if(position == 2) {
                return "Test";
            }
            else if(position == 3){
                return "Calendar";
            }
            else{
                return "Social";
            }
        }

        @Override
        public Fragment getItem(int position) {
            if( position == 0 ){
                return new ViewUsers();
            }
            else if (position == 1){
                return new ViewGroups();
            }
            else if (position == 2){
                return new TestFragment();
            }
            else if (position == 3){
                return new Calendar();
            }
            else{
                return new SocialScreen();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
