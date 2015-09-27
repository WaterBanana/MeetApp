package com.waterbanana.meetapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * Created by Eddie on 9/26/2015.
 */
public class EnterGroupIdDialog extends DialogFragment{

    public interface GroupIdListener{
        void setPositiveButton(DialogFragment dialog);
        void setNegativeButton(DialogFragment dialog);
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_cpfailed, null);
        builder.setView( view );

        Resources r = getResources();
        builder.setPositiveButton(r.getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.setPositiveButton(EnterGroupIdDialog.this);
            }
        })
                .setNegativeButton(r.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.setNegativeButton(EnterGroupIdDialog.this);
                    }
                });

        return builder.create();
    }
}
