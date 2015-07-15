package com.waterbanana.meetapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
        viewPager.setAdapter(new TabsViewPager(getSupportFragmentManager()));
        //mSTL.setDistributeEvenly(true);
        mSTL.setViewPager( viewPager );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == R.id.action_settings ){

        }
        else if( id == R.id.social ){
            Intent intent = new Intent( this, SocialScreen.class );
            startActivity( intent );
        }

        return super.onOptionsItemSelected( item );
    }

    class TabsViewPager extends FragmentStatePagerAdapter{

        public TabsViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if( position == 0 )
                return "Group 1";
            else if(position == 1){
                return "Group 2";
            }
            else if(position == 2) {
                return "Group 3";
            }
            else if(position == 3){
                return "Group 4";
            }
            else{
                return "Test";
            }
        }

        @Override
        public Fragment getItem(int position) {
            if( position == 0 ){
                return new Calendar();
            }
            else if (position == 1){
                return new Calendar();
            }
            else if (position == 2){
                return new Calendar();
            }
            else if (position == 3){
                return new Calendar();
            }
            else{
                return new TestFragment();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
