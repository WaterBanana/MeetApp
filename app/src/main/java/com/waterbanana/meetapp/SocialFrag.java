package com.waterbanana.meetapp;

/**
 * Created by Master N on 7/10/2015.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

//When group tab is clicked, this class displays the xml file fragment_view_social
public class SocialFrag extends Fragment implements View.OnClickListener{

    int[] gId;
    ArrayList<Integer> usersGroups;
    ListView usersG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_view_social, container, false );
        usersG = (ListView)view.findViewById(R.id.titles);
        usersG.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        usersGroups = new ArrayList<>();
        ArrayAdapter<Integer> connectArrayToListView = new
                ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1,
                usersGroups);//used to be NAMES
        usersG.setAdapter(connectArrayToListView);

        Button openActivity = (Button) view.findViewById( R.id.CreateG );
        openActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GroupInfo.createGroup("Kumar");
                Intent intent = new Intent( getActivity(), CreateGroup.class );
                startActivity( intent );
            }
        });

        Button openActivity2 = (Button) view.findViewById( R.id.LeaveG );
        openActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity(), RemoveGroup.class );
                startActivity( intent );
            }
        });

        new getGroups().execute();

        usersG.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //view.setBackgroundColor(Color.LTGRAY);

                usersG.setItemChecked(i, true);

                //usersG.getst
                long id = Long.valueOf((int)adapterView.getAdapter().getItem(i));
                DetailsFragment memOfG = DetailsFragment.newInstance(id);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, memOfG);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
    //ADD THE PARAMS HERE
                RelativeLayout rightFragment = (RelativeLayout) getActivity().findViewById(R.id.details);
                rightFragment.setLayoutParams(new LinearLayout.LayoutParams(190, 490));
                //180, 490
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class getGroups extends AsyncTask<String, String, String>{
        private DbHandler g = new DbHandler();
        @Override
        protected String doInBackground(String... strings) {
            TelephonyManager nameit = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNum = nameit.getLine1Number();

            gId = g.getGroupsByUserId("Kumar");

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for(int i = 0; i<gId.length; i++){
                usersGroups.add(gId[i]);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    groupAdapter usersGs = new groupAdapter(getActivity(), usersGroups);//used to be NAMES
                    usersG.setAdapter(usersGs);
                }
            });
        }
    }

    class groupAdapter extends ArrayAdapter<Integer> {
        public groupAdapter(Context context, ArrayList<Integer> groups) {
            super(context, 0, groups);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int groupId=getItem(position);
            if(convertView==null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_rows, parent, false);
            }
            TextView groupgid = (TextView) convertView.findViewById(R.id.grouprows);
            groupgid.setText(Integer.toString(groupId));
            return convertView;
        }
    }
}