package com.waterbanana.meetapp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Master N on 7/24/2015.
 */
public class DetailsFragment extends ListFragment {
    int groupId;
    int[] fId;
    ArrayList<Integer> groupMembers;
    int currGroup;
    int mCurCheckPosition = 0;
    public static int currIndex;



    //public static ListView groupMem;
    ArrayList<String> userIds;
    User[] users;

    public static DetailsFragment newInstance(long index){


        DetailsFragment f = new DetailsFragment();

        Bundle args = new Bundle();
        args.putLong("index", index);

        //currIndex = index;
        f.setArguments(args);

        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Gets ID of button that was clicked
        if(getArguments()!=null) {
            groupId = (int) getArguments().getLong("index");
        }
        //groupMem = new ListView(getActivity());

        userIds = new ArrayList<>();

        new getMems().execute();

        /*ArrayAdapter<String> connectArrayToListView = new
                ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_activated_1,
                userIds);

        groupMem.setAdapter(connectArrayToListView);*/

        if(savedInstanceState !=null){
            mCurCheckPosition = savedInstanceState.getInt("curChoice",0);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("curChoice", mCurCheckPosition);
    }
    class getMems extends AsyncTask<String, String, String>{

        private DbHandler g = new DbHandler();
        @Override
        protected String doInBackground(String... strings) {
            Log.d("DetailsFragment", "GID" + groupId);
            users = g.getUsersByGroupId(groupId);
            for(int i = 0; i<users.length; i++){
                userIds.add(users[i].getID());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> connectArrayToListView = new
                                ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1,
                                userIds);
                        setListAdapter(connectArrayToListView);
                    }
                });
            }
        }
    }
}
//Create async to get members of group clicked
//get arguments, returns bundle, in bundle get id of button that was clicked
//then find users based on id
//instead of findviewbyid from social frag, do listview = new listview(getActivity())
