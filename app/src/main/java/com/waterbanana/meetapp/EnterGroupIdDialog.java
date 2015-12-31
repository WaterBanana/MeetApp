package com.waterbanana.meetapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used on availability screen to choose which group to view availabilities for.
 *
 * Created by Eddie on 9/26/2015.
 */
public class EnterGroupIdDialog extends DialogFragment{
    private ListView listView;

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
        View view = getActivity().getLayoutInflater().inflate(R.layout.popup_window_entergroupid, null);
        builder.setView( view );

        listView = (ListView) view.findViewById(R.id.popup_window_groupid_listview);
        LocalDb db = new LocalDb(view.getContext());
        int[] groupIds = db.getSelfGroups(true);
        ArrayList<HashMap<String, String>> groupIdsList = new ArrayList<>();
        for( int groupid : groupIds ){
            HashMap<String, String> map = new HashMap<>();
            map.put("groupid", Integer.toString(groupid));
            groupIdsList.add(map);
        }

        ListAdapter listAdapter = new SimpleAdapter(view.getContext(), groupIdsList, R.layout.listview_contents_simple_single_entry,
                new String[]{"groupid"}, new int[]{R.id.tv_entry});
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mSelectedGroupId = parent.getItemAtPosition(position).toString();
                mListener.getSelectedId(EnterGroupIdDialog.this, mSelectedGroupId);
            }
        });

//        Resources r = getResources();
//        builder.setPositiveButton(r.getString(R.string.submit), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                mListener.setPositiveButton(EnterGroupIdDialog.this);
//            }
//        })
//                .setNegativeButton(r.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mListener.setNegativeButton(EnterGroupIdDialog.this);
//                    }
//                });

        return builder.create();
    }
}
