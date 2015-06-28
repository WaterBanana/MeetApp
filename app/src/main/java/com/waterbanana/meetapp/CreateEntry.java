package com.waterbanana.meetapp;

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
public class CreateEntry extends ActionBarActivity{
    private EditText etUserID, etDate, etStart, etEnd;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        etUserID = (EditText) findViewById( R.id.editTextUserId );
        etDate = (EditText) findViewById( R.id.editTextDate );
        etStart = (EditText) findViewById( R.id.editTextStart );
        etEnd = (EditText) findViewById( R.id.editTextEnd );
        btnCreate = (Button) findViewById( R.id.btnCreateExe );

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateUser().execute();
            }
        });
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

    class CreateUser extends AsyncTask<String, String, String>{
        private int success = -1;

        @Override
        protected String doInBackground(String... params) {
            Ribbon ribbon = new Ribbon( -1, etDate.getText().toString(),
                    Integer.parseInt( etStart.getText().toString() ),
                    Integer.parseInt( etEnd.getText().toString() ) );

            Log.d( "CreateEntry", "Trying to add new timeslot for " + etUserID.getText().toString() );
            User user = new User( etUserID.getText().toString() );
            success = user.newRibbon(ribbon);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if( success != -1 )
                Toast.makeText( getBaseContext(), "Entry created.", Toast.LENGTH_LONG ).show();
            else
                Toast.makeText( getBaseContext(), "Error creating entry.", Toast.LENGTH_LONG ).show();
            finish();
        }
    }
}
