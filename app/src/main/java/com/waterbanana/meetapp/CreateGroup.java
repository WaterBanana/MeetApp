package com.waterbanana.meetapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Demonstration purposes only.
 */
public class CreateGroup extends ActionBarActivity {
    private EditText etGroupsAddUserid, etGroupsAddGroupid;
    private Button btnAddExe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        etGroupsAddUserid = (EditText) findViewById( R.id.editTextGroupsUserid );
        etGroupsAddGroupid = (EditText) findViewById( R.id.editTextGroupsGroupid );

        btnAddExe = (Button) findViewById( R.id.btnCreateGroupExe );
        btnAddExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewGroup().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
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

    class NewGroup extends AsyncTask<String, String, String>{
        private DbHandler db;
        private int success = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new DbHandler();
        }

        @Override
        protected String doInBackground(String... params) {
            success = db.insertToGroupsTable(etGroupsAddUserid.getText().toString(),
                    Integer.parseInt(etGroupsAddGroupid.getText().toString()));

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if( success == -1 || success == 0 )
                Toast.makeText( getBaseContext(), "Error adding to group.", Toast.LENGTH_LONG ).show();
            else
                Toast.makeText( getBaseContext(), "Successfully added to group.", Toast.LENGTH_LONG ).show();
            finish();
        }
    }
}
