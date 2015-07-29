package com.waterbanana.meetapp;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CP2SMS extends Fragment {
    private Button btnBegin;
    private static String tNum;
    private BroadcastReceiver receiver;
    private static ProgressDialog progressBar;
    private String TAG = "CP2SMS.java";

    public CP2SMS() {}

    public static CP2SMS newInstance( String num, ProgressDialog pd ){
        CP2SMS f = new CP2SMS();
        tNum = num;
        progressBar = pd;

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cp2_sm, container, false);

        btnBegin = (Button) view.findViewById( R.id.btnCP2SendSMS);
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verifyPhoneNumber();
                openProgressDialog();

                try {
                    Log.d(TAG, "End CP2");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Log.d( TAG, "Unregistering receiver." );
                //getActivity().unregisterReceiver( receiver );
//                Intent intent = new Intent( getActivity(), CPSMSHandler.class );
//                intent.putExtra( "phone", tNum );
//                startActivity( intent );
            }
        });

        return view;
    }

    private void openProgressDialog(){
        //progressBar = new ProgressDialog( getActivity() );
        Log.d(TAG, "Opening Progress Dialog");
        progressBar.setCancelable(false);
        progressBar.setMessage("Please wait while we verify your phone number...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                verifyPhoneNumber();
            }
        }).run();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Log.d( TAG, "Sleeping for 15 seconds." );
//                    Thread.sleep(20000);
//                    Toast.makeText( getActivity(), "Failed to verify phone number. Read logcat.", Toast.LENGTH_LONG ).show();
//                    Log.e( TAG, "If you reached here, I haven't addressed this error yet." );
//                    progressBar.dismiss();
//                } catch( Exception e ){
//                    e.printStackTrace();
//                }
//            }
//        }).run();
    }

    private void verifyPhoneNumber(){

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                receiveText();
//            }
//        }).run();
//        TelephonyManager telephonyManager =
//                (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
//        tNum = telephonyManager.getLine1Number();
        Log.d(TAG, "Phone number obtained: " + tNum);

        String message = "Thank you for joining us on MeetApp!";
//        PendingIntent piSent = PendingIntent.getBroadcast(
//                getActivity(), 0, new Intent("msg_sent"), 0 );
//        PendingIntent piDelivered = PendingIntent.getBroadcast(
//                getActivity(), 0, new Intent("msg_delivered"), 0 );
        SmsManager smsManager = SmsManager.getDefault();

        try {
            Log.d(TAG, "Sending text message to " + tNum);
            smsManager.sendTextMessage( tNum, null, message, null, null );
            Log.d(TAG, "Text sent.");
        }catch( Exception e ){
            Log.e( TAG, "Error with sending message." );
        }
    }

    private boolean receiveText(){
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        Log.d( TAG, "Intent Filter created." );
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();

                if( extras == null ){
                    Log.d( TAG, "Extras == null" );
                    return;
                }

                Log.d( TAG, "Reading text." );
                Object[] pdus = (Object[]) extras.get("pdus");
                SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[0]);
                String origNumber = msg.getOriginatingAddress();
                if( origNumber == tNum ) {
                    Log.d( TAG, "Phone check: We have a match!" );
                    Toast.makeText(getActivity(), "Matches", Toast.LENGTH_LONG).show();
                }
            }


        };
        Log.d( TAG, "Registering receiver." );
        getActivity().registerReceiver( receiver, filter );

        return false;
    }

}
