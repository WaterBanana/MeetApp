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
public class RemoveEntry extends ActionBarActivity {
    private EditText etEntryId;
    private Button btnDelExe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_entry);

        etEntryId = (EditText) findViewById( R.id.editTextDelEntry );
        btnDelExe = (Button) findViewById( R.id.btnDeleteExe );

        btnDelExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteUser().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remove_entry, menu);
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

    class DeleteUser extends AsyncTask<String, String, String>{
        private int success = 0;

        @Override
        protected String doInBackground(String... args) {
            DbHandler db = new DbHandler();
            try{
                success = db.removeTimeslot( Integer.parseInt( etEntryId.getText().toString() ) );
            } catch( DbHandler.DBException e ){
                Log.e( "RemoveEntry", "Error removing entry: " + e.toString() );
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if( success == 1 ) {
                Toast.makeText( getBaseContext(), "Successfully removed user.", Toast.LENGTH_LONG ).show();
            }
            else{
                Toast.makeText( getBaseContext(), "Error removing user.", Toast.LENGTH_LONG ).show();
            }
            finish();
        }
    }
}
