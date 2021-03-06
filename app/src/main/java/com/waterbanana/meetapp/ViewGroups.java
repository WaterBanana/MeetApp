package com.waterbanana.meetapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demonstration purposes only.
 */
public class ViewGroups extends Fragment implements View.OnClickListener{
    private ListView allGroupsListView;
    private Button btnAdd, btnRemove;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_view_groups, container, false );
        allGroupsListView = (ListView) view.findViewById( R.id.listview_all_groups );
        btnAdd = (Button) view.findViewById( R.id.buttonAddGroup );
        btnRemove = (Button) view.findViewById( R.id.buttonRemoveGroup );
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById( R.id.all_groups_list_refresh );

        btnAdd.setOnClickListener(this);
        btnRemove.setOnClickListener(this);

        new LoadAllGroups().execute();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadAllGroups().execute();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch( v.getId() ){
            case R.id.buttonAddGroup:
                intent = new Intent( getActivity(), CreateGroup.class );
                startActivity( intent );
                break;
            case R.id.buttonRemoveGroup:
                intent = new Intent( getActivity(), RemoveGroup.class );
                startActivity( intent );
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadAllGroups().execute();
    }

    class LoadAllGroups extends AsyncTask<String, String, String>{
        HashMap<String, String> userList;
        ArrayList<HashMap<String, String>> allGroupsList;

        @Override
        protected String doInBackground(String... params) {
            DbHandler db = new DbHandler();
            User[] entries = db.getAllGroups();
            allGroupsList = new ArrayList<>();

            for( int i = 0; i < entries.length; i++ ){
                for( int j = 0; j < entries[i].getGroups().size(); j++ ){
                    userList = new HashMap<>();
                    userList.put( "userid", entries[i].getID() );
                    userList.put( "groupid", Integer.toString( entries[i].getGroups().get( j ) ) );
                    allGroupsList.add( userList );
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if( getActivity() == null )
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter( getActivity(), allGroupsList, R.layout.list_groups_view,
                            new String[] { "userid", "groupid" },
                            new int[] { R.id.groups_list_userid, R.id.groups_list_groupid } );
                    allGroupsListView.setAdapter( adapter );
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}
