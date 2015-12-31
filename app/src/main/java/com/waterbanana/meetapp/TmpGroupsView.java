package com.waterbanana.meetapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class TmpGroupsView extends AppCompatActivity
    implements ConfirmationDialog.ConfirmationListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private LocalDb localDb;
    private int groupidToDelete;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp_groups_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pd = new ProgressDialog(this);

        localDb = new LocalDb(this);
        listView = (ListView) findViewById(R.id.listView_ViewSelfGroups);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String groupidStr = parent.getItemAtPosition(position).toString().substring(9, 13);
                groupidToDelete = Integer.parseInt(groupidStr);
                ConfirmationDialog confirmationDialog = new ConfirmationDialog();
                confirmationDialog.setText(getResources().getString(R.string.leave_group_ascertainty));
                confirmationDialog.show(getSupportFragmentManager(), "LeaveGroupConfirmation");

                return false;
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.tmp_groups_view_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshGroupsList(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshGroupsList(true);
    }

    private void refreshGroupsList(boolean pullFromNetwork){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setMessage(getResources().getString(R.string.getting_self_groups));
        pd.show();

        int[] groupIds = localDb.getSelfGroups(pullFromNetwork);
        final ArrayList<HashMap<String, String>> groupsList = new ArrayList<>();

        for( int groupid : groupIds ){
            HashMap<String, String> map = new HashMap<>();
            map.put("groupid", Integer.toString(groupid));
            groupsList.add(map);
        }

        Runnable refreshList = new Runnable() {
            @Override
            public void run() {
                ListAdapter listAdapter = new SimpleAdapter( getBaseContext(), groupsList, R.layout.listview_contents_simple_single_entry,
                        new String[]{"groupid"}, new int[]{R.id.tv_entry});
                listView.setAdapter(listAdapter);
                pd.cancel();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        refreshList.run();
    }

    @Override
    public void setPositiveAction(DialogFragment dialog) {
        new DeleteSelfFromGroup().execute();

        dialog.dismiss();
//        pd.setMessage(getResources().getString(R.string.leaving_group));
//        pd.setIndeterminate(true);
//        pd.show();
//
//        LocalDb db = new LocalDb(this);
//        int success = db.leaveGroup(groupidToDelete);
//        if(success != -1){
//            Toast.makeText(this, getResources().getString(R.string.left_group), Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(TmpGroupsView.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
//        }
//        pd.cancel();
//        refreshGroupsList(true);
    }

    @Override
    public void setNegativeAction(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tmp_groups_view, menu);
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

    class DeleteSelfFromGroup extends AsyncTask<String, String, String> {
        int success = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage(getResources().getString(R.string.leaving_group));
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            DbHandler db = new DbHandler();
            String localId = localDb.getLocalId();
            String encLocalId = localDb.encryptId(localId);
            Log.d("TmpGroupsView", "Attempting to leave " + groupidToDelete );

            try{
                success = db.removeFromGroupsTable(encLocalId, groupidToDelete);
            } catch( DbHandler.DBException e ){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(success != -1)
                Toast.makeText(TmpGroupsView.this, getResources().getString(R.string.left_group), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(TmpGroupsView.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();

            pd.cancel();
            refreshGroupsList(true);
        }
    }
}
