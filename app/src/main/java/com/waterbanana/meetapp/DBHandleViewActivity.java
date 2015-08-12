package com.waterbanana.meetapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.waterbanana.common.MAViewPager;
import com.waterbanana.common.SlidingTabLayout;

public class DBHandleViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SlidingTabLayout mSTL;
    private MAViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSTL = (SlidingTabLayout) findViewById(R.id.main_sliding_tabs_layout);
        viewPager = (MAViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new DBHandleViewAdapter(getSupportFragmentManager()));
        mSTL.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dbhandle_view, menu);
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

    class DBHandleViewAdapter extends FragmentPagerAdapter{

        public DBHandleViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new ViewUsers();
                case 1:
                    return new ViewGroups();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
