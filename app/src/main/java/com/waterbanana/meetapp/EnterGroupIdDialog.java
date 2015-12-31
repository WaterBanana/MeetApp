package com.waterbanana.meetapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used on availability screen to choose which group to view availabilities for.
 *
 * Created by Eddie on 9/26/2015.
 */
public class EnterGroupIdDialog extends DialogFragment{
    private RelativeLayout layout;
    private ProgressBar progressBar;
    private ListView listView;
    private LocalDb db;
    private View view;
    private Context context;

    public interface GroupIdListener{
        void getSelectedId(DialogFragment dialog, String selectedGroupId);
//        void setPositiveButton(DialogFragment dialog);
//        void setNegativeButton(DialogFragment dialog);
    }

    GroupIdListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mListener = (GroupIdListener) activity;
        } catch( ClassCastException e ){
            throw new ClassCastException(activity.toString()
                    + " must implement GroupIdListener" );
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        view = getActivity().getLayoutInflater().inflate(R.layout.popup_window_entergroupid, null);
        builder.setView( view );

        listView = (ListView) view.findViewById(R.id.popup_window_groupid_listview);
        progressBar = (ProgressBar) view.findViewById(R.id.popup_window_entergroupid_progressdialog);
        context = view.getContext();
        db = new LocalDb(context);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mSelectedGroupId = parent.getItemAtPosition(position).toString();
                mListener.getSelectedId(EnterGroupIdDialog.this, mSelectedGroupId);
            }
        });

        new LoadSelfGroups().execute();

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }

    class LoadSelfGroups extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            // HACKY FIXES: NEEDS REFINEMENT
            Log.d("EnterGroupIdDialog", "LOADING GROUPS");
            DbHandler dbHandler = new DbHandler();
            String localId = db.getLocalId();
            String encLocalId = db.encryptId(localId);
            int[] groupIds = dbHandler.getGroupsByUserId(encLocalId);
            ArrayList<HashMap<String, String>> groupIdsList = new ArrayList<>();
            for( int groupid : groupIds ){
                HashMap<String, String> map = new HashMap<>();
                map.put("groupid", Integer.toString(groupid));
                groupIdsList.add(map);
            }

            final ListAdapter listAdapter = new SimpleAdapter(context, groupIdsList, R.layout.listview_contents_simple_single_entry,
                    new String[]{"groupid"}, new int[]{R.id.tv_entry});

            if(getActivity() == null)
                return null;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(listAdapter);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.setVisibility(View.GONE);
        }
    }
}
