package com.waterbanana.meetapp;
//NOT BEING USED
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Master N on 7/24/2015.
 */
public class TitlesFragment extends ListFragment{

    boolean mDuelPane;
    int mCurCheckPosition = 0;
    public ArrayList<String> groups;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        /*ArrayAdapter<String> connectArrayToListView = new
                ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_activated_1,
                GroupInfo.groups);//used to be NAMES
        setListAdapter(connectArrayToListView);*/

        //View detailsFrame = getActivity().findViewById(R.id.details);

        // mDuelPane = detailsFrame != null && detailsFrame.getVisibility()
        //     == View.VISIBLE;

        if(savedInstanceState !=null){
            mCurCheckPosition = savedInstanceState.getInt("curChoice",0);
        }
        // if(mDuelPane){
        //  getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //  showDetails(mCurCheckPosition);

        // }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("curChoice",mCurCheckPosition);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }


    void showDetails(int index){
        mCurCheckPosition = index;
       // if(mDuelPane){
            getListView().setItemChecked(index, true);

           /* DetailsFragment details = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);*/

         //   if(details == null || details.getShownIndex() != index){
              /*  details = DetailsFragment.newInstance(index);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();*/
     //  }
    }

    class Ngroup extends AsyncTask<String, String, String>{
        private DbHandler g = new DbHandler();
        @Override
        protected String doInBackground(String... strings) {
            //groups = g.getGroupsByUserId();
            return null;
        }
    }

}