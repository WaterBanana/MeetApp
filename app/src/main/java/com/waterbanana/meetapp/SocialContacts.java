package com.waterbanana.meetapp;

/**
 * Created by Master N on 7/10/2015.
 */
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

public class SocialContacts extends Fragment implements View.OnClickListener{
    //private ListView allGroupsListView;
    private Button btnNew, btnDelete;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_view_contacts, container, false );

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

        return view;
    }

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
