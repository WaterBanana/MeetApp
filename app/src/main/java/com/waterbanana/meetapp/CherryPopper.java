package com.waterbanana.meetapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class CherryPopper extends ActionBarActivity {
    private ViewPager viewPager;
    private Toolbar toolbar;
    private String TAG = "CherryPopper.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cherry_popper);

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        viewPager = (ViewPager) findViewById( R.id.cherrypopper_viewpager );
        viewPager.setAdapter(new CPAdapter(getSupportFragmentManager()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cherry_popper, menu);
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

    class CPAdapter extends FragmentPagerAdapter{
        private int CNT = 2;

        public CPAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch( position ){
                case 0:
                    return CP1Home.newInstance(viewPager);
                default:
                    return CP2SMS.newInstance();
            }
        }

        @Override
        public int getCount() {
            return CNT;
        }
    }
}
