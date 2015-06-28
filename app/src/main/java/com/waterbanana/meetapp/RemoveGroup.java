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
public class RemoveGroup extends ActionBarActivity {
    private EditText etRemoveGroupUserid, etRemoveGroupGroupid;
    private Button btnDeleteGroupExe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_group);

        etRemoveGroupUserid = (EditText) findViewById( R.id.editTextRemoveGroupsUserid );
        etRemoveGroupGroupid = (EditText) findViewById( R.id.editTextRemoveGroupsGroupid );

        btnDeleteGroupExe = (Button) findViewById( R.id.btnRemoveGroupExe );
        btnDeleteGroupExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteGroup().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remove_group, menu);
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

    class DeleteGroup extends AsyncTask<String, String, String>{
        private int success = -1;
        private DbHandler db;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new DbHandler();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                success = db.removeFromGroupsTable(etRemoveGroupUserid.getText().toString(),
                        Integer.parseInt(etRemoveGroupGroupid.getText().toString()));
            } catch( DbHandler.DBException e ){
                //success = -1;
                Log.e( "RemoveGroup", e.toString() );
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if( success == -1 ){
                Toast.makeText( getBaseContext(), "Error removing from group.", Toast.LENGTH_LONG ).show();
            }
            else
                Toast.makeText( getBaseContext(), "Successfully removed from group.", Toast.LENGTH_LONG ).show();
            finish();
        }
    }
}
