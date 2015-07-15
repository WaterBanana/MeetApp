package com.waterbanana.meetapp;

/**
 * Created by Master N on 7/6/2015.
 */

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Demonstration purposes only.
 */
public class SocialScreen extends ActionBarActivity implements View.OnClickListener{
    //private ListView allGroupsListView;
    private Button btnNew, btnDelete;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_social);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar( toolbar );
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_create_entry, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == android.R.id.home ){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate( R.layout.fragment_view_social, container, false );
//        allGroupsListView = (ListView) view.findViewById( R.id.listview_all_groups );
//        btnNew = (Button) view.findViewById( R.id.buttonNew );
//        btnDelete = (Button) view.findViewById( R.id.buttonRemove );
//        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById( R.id.all_groups_list_refresh );
//
//        btnNew.setOnClickListener(this);
//        btnDelete.setOnClickListener(this);
//
//        new LoadAllGroups().execute();
//
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new LoadAllGroups().execute();
//            }
//        });
//
//        return view;
//    }

    @Override
    public void onClick(View v) {
//        Intent intent;
//
//        switch( v.getId() ){
//            case R.id.buttonAddGroup:
//                intent = new Intent( getActivity(), CreateGroup.class );
//                startActivity( intent );
//                break;
//            case R.id.buttonRemoveGroup:
//                intent = new Intent( getActivity(), RemoveGroup.class );
//                startActivity( intent );
//                break;
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        new LoadAllGroups().execute();
    }

//    class LoadAllGroups extends AsyncTask<String, String, String>{
//        HashMap<String, String> userList;
//        ArrayList<HashMap<String, String>> allGroupsList;
//
//        @Override
//        protected String doInBackground(String... params) {
//            DbHandler db = new DbHandler();
//            User[] entries = db.getAllGroups();
//            allGroupsList = new ArrayList<>();
//
//            for( int i = 0; i < entries.length; i++ ){
//                for( int j = 0; j < entries[i].getGroups().size(); j++ ){
//                    userList = new HashMap<>();
//                    userList.put( "userid", entries[i].getID() );
//                    userList.put( "groupid", Integer.toString( entries[i].getGroups().get( j ) ) );
//                    allGroupsList.add( userList );
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ListAdapter adapter = new SimpleAdapter( getActivity(), allGroupsList, R.layout.list_groups_view,
//                            new String[] { "userid", "groupid" },
//                            new int[] { R.id.groups_list_userid, R.id.groups_list_groupid } );
//                    allGroupsListView.setAdapter( adapter );
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
//            });
//        }
//    }
}