package com.waterbanana.meetapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.waterbanana.common.MAViewPager;
import com.waterbanana.common.SlidingTabLayout;

//Comment
public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private SharedPreferences sp;
    private final String PREFS_NAME = "VirginityCheck";
    private final String PREFS_ISVIRGIN = "isVirgin";
    //private SlidingTabLayout mSTL;
    //GAA 05JUL2015 - Testing GitHub Version Control
    //hello

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);

        sp = getSharedPreferences( PREFS_NAME, 0 );
        boolean isVirgin = sp.getBoolean( PREFS_ISVIRGIN, true );
        if( isVirgin ){
            //sp.edit().putBoolean(PREFS_ISVIRGIN, false).apply();
            Intent intent = new Intent( this, CherryPopper.class );
            finish();
            startActivity( intent );
        }

        SlidingTabLayout mSTL = (SlidingTabLayout) findViewById( R.id.main_sliding_tabs_layout );

        MAViewPager viewPager = (MAViewPager) findViewById( R.id.pager );
        viewPager.setAdapter(new TabsViewPager(getSupportFragmentManager()));
        viewPager.setPagingEnabled(false);
        //mSTL.setDistributeEvenly(true);
        mSTL.setViewPager( viewPager );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == R.id.action_settings ){

        }
        else if( id == R.id.social ){
            Intent intent = new Intent( this, TmpGroupsPage.class );
            startActivity( intent );
        }
        else if( id == R.id.resetapp ){
            sp.edit().putBoolean( PREFS_ISVIRGIN, true ).apply();
            Intent intent = new Intent( this, MainActivity.class );
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            finish();
            startActivity( intent );
        }

        return super.onOptionsItemSelected( item );
    }

    class TabsViewPager extends FragmentStatePagerAdapter{
        private int CNT = 6;

        private String TAG = "TabsViewPager";
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
            else if( position == 4){
                return "Group 5";
            }
            else{
                return "Test";
            }
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "Tab Position: " + position );
            if( position != CNT - 1 )
                return new CalendarFrag();
            else
                return new TestFragment();
        }

        @Override
        public int getCount() {
            return CNT;
        }
    }
}
