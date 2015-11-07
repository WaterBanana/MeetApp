package com.waterbanana.meetapp;

/**
 * Created by gerar_000 on 8/23/2015 from eddie's CreateEntry class .
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Demonstration purposes only.
 */
public class CreateEntryFromLocal extends ActionBarActivity {
    private String date;
    private int ribbonID, ribbonBegin, ribbonEnd, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        Log.d("CreateEntryFromLocal", "onCreate method");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ribbonID = bundle.getInt("ribbonID");
        date = bundle.getString("date");
        ribbonBegin = bundle.getInt("ribbonBegin");
        ribbonEnd = bundle.getInt("ribbonEnd");
        userID = 0;//dummy value

        new CreateUserFromLocal().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_entry, menu);
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

    class CreateUserFromLocal extends AsyncTask<String, String, String> {
        private int success = -1;

        @Override
        protected String doInBackground(String... params) {
            Ribbon ribbon = new Ribbon( -1, date,
                    ribbonBegin,
                    ribbonEnd );

            Log.d("CreateEntry", "Trying to add new timeslot for " + userID);
            User user = new User(userID + "");
            success = user.newRibbon(ribbon);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if( success != -1 )
                Toast.makeText(getBaseContext(), "Entry created.", Toast.LENGTH_LONG).show();
            else
                Toast.makeText( getBaseContext(), "Error creating entry.", Toast.LENGTH_LONG ).show();
            finish();
        }
    }
}

