package com.waterbanana.meetapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
    private int[] groupIds;
    private Context _context;
    private SlidingTabLayout mSTL;
    //GAA 05JUL2015 - Testing GitHub Version Control
    //hello

    private boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _context = this;
        new GetGroupsList().execute();
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

        mSTL = (SlidingTabLayout) findViewById( R.id.main_sliding_tabs_layout );
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
        private int CNT = groupIds.length;

        private String TAG = "TabsViewPager";
        public TabsViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Integer.toString(groupIds[position]);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "Tab Position: " + position );

            if(DEBUG){
                if( position != CNT - 1 )
                    return new CalendarFrag();
                else
                    return new TestFragment();
            }
            else{
                Bundle groupBundle = new Bundle();
                groupBundle.putInt("groupid", groupIds[position]);
                return Fragment.instantiate( _context, CalendarFrag.class.getName(), groupBundle );
            }
        }

        @Override
        public int getCount() {
            return CNT;
        }
    }

    class GetGroupsList extends AsyncTask<String, String, String>{
        DbHandler db = new DbHandler();
        LocalDb localDb = new LocalDb(_context);

        @Override
        protected String doInBackground(String... params) {

            groupIds = db.getGroupsByUserId(localDb.getEncLocalId());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            MAViewPager viewPager = (MAViewPager) findViewById( R.id.pager );
            viewPager.setAdapter(new TabsViewPager(getSupportFragmentManager()));
            viewPager.setPagingEnabled(false);
            //mSTL.setDistributeEvenly(true);
            mSTL.setViewPager(viewPager);
        }
    }
}
