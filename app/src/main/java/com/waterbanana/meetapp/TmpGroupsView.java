package com.waterbanana.meetapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class TmpGroupsView extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private LocalDb localDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp_groups_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        localDb = new LocalDb(this);
        listView = (ListView) findViewById(R.id.listView_ViewSelfGroups);
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
}
