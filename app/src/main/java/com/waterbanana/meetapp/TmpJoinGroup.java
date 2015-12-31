package com.waterbanana.meetapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TmpJoinGroup extends AppCompatActivity {
    private Context context;
    private ProgressDialog progressDialog;
    private boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp_join_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        progressDialog = new ProgressDialog(this);

        final EditText etGroupId = (EditText) findViewById(R.id.editTextGroupsJoin);
        Button btnJoinGroup = (Button) findViewById(R.id.btnGroupsJoinToServer);
        btnJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupidStr = etGroupId.getText().toString();
                new DatabaseQuery().execute(groupidStr);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tmp_join_group, menu);
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

    class DatabaseQuery extends AsyncTask<String, String, String>{
        private DbHandler db = new DbHandler();
        private LocalDb localDb = new LocalDb(context);
        private int success = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String localId = localDb.getLocalId();
            String encLocalId = localDb.encryptId(localId);

            success = db.insertToGroupsTable(encLocalId, Integer.parseInt( params[0] ));
            if( success == 1 ){
                if(DEBUG) Log.d( "TmpJoinGroup.java", "Inserted to group" );
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            if( success == 1 ) {
                Toast.makeText(context, "Successfully joined group", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
