package com.waterbanana.meetapp;

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
public class ViewUsers extends Fragment implements View.OnClickListener {
    private ListView listView;
    private Button btnCreate, btnRemove;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_view_users, container, false );

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById( R.id.main_swipe_refresh_list );
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadAllRibbons().execute();
            }
        });

        new LoadAllRibbons().execute();
        listView = (ListView) view.findViewById( R.id.mainListView );

        btnCreate = (Button) view.findViewById( R.id.buttonCreate );
        btnCreate.setOnClickListener(this);
        btnRemove = (Button) view.findViewById( R.id.buttonDelete );
        btnRemove.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadAllRibbons().execute();
    }

    /**
     * Shows all users in DB.
     */



    @Override
    public void onClick(View v) {
        Intent intent;
        switch( v.getId() ){
            case R.id.buttonCreate:
                intent = new Intent( getActivity(), CreateEntry.class );
                startActivity( intent );
                break;
            case R.id.buttonDelete:
                intent = new Intent( getActivity(), RemoveEntry.class );
                startActivity( intent );
                break;
        }
    }

    class LoadAllRibbons extends AsyncTask<String, String, String>{
        HashMap<String, String> userList;
        ArrayList<HashMap<String, String>> allUsersList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            allUsersList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<Ribbon> ribbons;
            DbHandler db = new DbHandler();
            User[] users = db.getAllUsers();


            for( int i = 0; i < users.length; i++ ){
                Log.d( "MainActivity", "Number of distinct users: " + users.length );
                ribbons = users[i].getRibbons();
                for( int j = 0; j < ribbons.size(); j++ ){
                    userList = new HashMap<>();
                    userList.put( "id", Integer.toString( ribbons.get(j).getId() ) );
                    userList.put( "userid", users[i].getID() );
                    userList.put( "date", ribbons.get( j ).getDate() );
                    userList.put( "start", Integer.toString(ribbons.get(j).getStart()) );
                    userList.put( "end", Integer.toString( ribbons.get( j ).getEnd() ) );
                    allUsersList.add(userList);
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
                    ListAdapter adapter = new SimpleAdapter(getActivity(), allUsersList, R.layout.list_view,
                            new String[]{"id", "userid", "date", "start", "end"},
                            new int[]{R.id.ribbonIDTV, R.id.userIDTV, R.id.dateTV, R.id.startTV, R.id.endTV});
                    Log.d("MainActivityAsync", "onPostExecute: Setting new adapter.");
                    listView.setAdapter(adapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}