package com.waterbanana.meetapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class CherryPopper extends ActionBarActivity {
    private ViewPager viewPager;
    private Toolbar toolbar;
    private String tNum;
    private BroadcastReceiver receiver;
    private ProgressDialog progressBar;
    private SharedPreferences sp;
    private String TAG = "CherryPopper.java";

    private final String PREFS_NAME = "VirginityCheck";
    private final String PREFS_ISVIRGIN = "isVirgin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cherry_popper);

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tNum = telephonyManager.getLine1Number();
        progressBar = new ProgressDialog( this );

        viewPager = (ViewPager) findViewById( R.id.cherrypopper_viewpager );
        viewPager.setAdapter(new CPAdapter(getSupportFragmentManager()));

        sp = getSharedPreferences( PREFS_NAME, 0 );

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String actionName = intent.getAction();

                Bundle extras = intent.getExtras();
                if( extras == null ){
                    Log.d( TAG, "Extras == null" );
                    return;
                }

                Object[] pdus = (Object[]) extras.get( "pdus" );
                SmsMessage msg = SmsMessage.createFromPdu( (byte[]) pdus[0] );
                String origNum = msg.getOriginatingAddress();
                Log.d(TAG, "(" + actionName + ") Verified number: " + origNum);
                if( origNum.substring(1).equals( tNum ) ) {
                    sp.edit().putBoolean( PREFS_ISVIRGIN, false ).apply();
                    Intent mainIntent = new Intent( getBaseContext(), MainActivity.class );
                    progressBar.dismiss();
                    finish();
                    startActivity( mainIntent );
                }
            }
        };
        Log.d( TAG, "Receiver created." );
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
//        filter.addAction( "msg_sent" );
//        filter.addAction( "msg_delivered" );

        filter.addAction( Telephony.Sms.Intents.SMS_RECEIVED_ACTION );
        registerReceiver(receiver, filter);
        Log.d( TAG, "Receiver registered." );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.d( TAG, "Receiver unregistered." );
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
                    return CP2SMS.newInstance(tNum, progressBar);
            }
        }

        @Override
        public int getCount() {
            return CNT;
        }
    }
}
