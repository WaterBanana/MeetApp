package com.waterbanana.meetapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class CPSMSHandler extends AppCompatActivity
        implements EnterPhoneNumberDialog.EnterPhoneNumberDialogListener{

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private BroadcastReceiver receiver;
    private SharedPreferences sp;
    private String tNum;
    private EditText etPhonNum;
    private Handler handler = new Handler();
    private Runnable runDialog;
    Context context;

    private final String PREFS_NAME = "VirginityCheck";
    private final String PREFS_ISVIRGIN = "isVirgin";
    private String TAG = "CPSMSHandler.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpsmshandler);

        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBarCPHandler);
        progressBar.setIndeterminate(true);

        sp = getSharedPreferences(PREFS_NAME, 0);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tNum = telephonyManager.getLine1Number();
        Log.d( TAG, "Phone number acquired: " + tNum );

        runDialog = new Runnable() {
            @Override
            public void run() {
                DialogFragment dialog = new EnterPhoneNumberDialog();
                dialog.show(getSupportFragmentManager(), "EnterPhoneNumberDialog");
                progressBar.setVisibility(View.INVISIBLE);
            }
        };

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String actionName = intent.getAction();

                Bundle extras = intent.getExtras();
                if( extras == null ){
                    Log.d(TAG, "Extras == null");
                    return;
                }

                Object[] pdus = (Object[]) extras.get( "pdus" );
                SmsMessage msg = SmsMessage.createFromPdu( (byte[]) pdus[0] );
                String origNum = msg.getOriginatingAddress();
                Log.d(TAG, "(" + actionName + ") Verified number: " + origNum);
                if( origNum.substring(1).equals( tNum ) ) {
                    LocalDb localDb = new LocalDb(context);
                    localDb.saveLocalId(tNum);

                    String encNum = localDb.encryptId(tNum);
                    new RegisterUserTask().execute(encNum);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called.");
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver( receiver, filter );
        Log.d( TAG, "Receiver registered." );
        sendTextMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runDialog);
        Log.d( TAG, "Removed handler callbacks." );
        unregisterReceiver(receiver);
        Log.d( TAG, "Receiver unregistered." );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cpsmshandler, menu);
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        etPhonNum = (EditText) dialog.getDialog().findViewById(R.id.editTextCPFailed);
        tNum = etPhonNum.getText().toString();
        sendTextMessage();
        dialog.getDialog().cancel();
        progressBar.setVisibility( View.VISIBLE );
        //DialogTest4Button btn = DialogTest4Button.instantiate(this, DialogTest4Button.class.getName());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    private void sendTextMessage(){
        Log.d(TAG, "Sending text message to: " + tNum);
        String message = "Thank you for joining us on MeetApp!";

        SmsManager.getDefault().sendTextMessage( tNum, null, message, null, null );
        getContentResolver().delete(
                Uri.parse("content://sms/outbox"),
                "address = ? and body = ?",
                new String[]{
                        "+1" + tNum,
                        message
                }
        );
        getContentResolver().delete(
                Uri.parse("content://sms/sent"),
                "address = ? and body = ?",
                new String[]{
                        "+1" + tNum,
                        message
                }
        );

        Log.d( TAG, "Wait 20 seconds before failure" );
        handler.postDelayed(runDialog, (long) 20000);
    }

    class RegisterUserTask extends AsyncTask<String, String, String> {
        DbHandler db = new DbHandler();
        int success = -1;

        @Override
        protected String doInBackground(String... params) {
            Log.d( TAG, "Verifying number: " + params[0] );
            success = db.registerUser(params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if( success == 1 ) {
                Log.d(TAG, "Number verified!");
                sp.edit().putBoolean(PREFS_ISVIRGIN, false).apply();
                Toast.makeText(CPSMSHandler.this, "Phone number verified!", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ((Activity) context).finish();
                startActivity(mainIntent);
            }
            else{
                Toast.makeText(CPSMSHandler.this, "Server connection error.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
