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
 * Created by Eddie on 7/31/2015.
 */
public class EnterPhoneNumberDialog extends DialogFragment {

    public interface EnterPhoneNumberDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    EnterPhoneNumberDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mListener = (EnterPhoneNumberDialogListener) activity;
        } catch( ClassCastException e ){
            throw new ClassCastException(activity.toString()
                    + " must implement EnterPhoneNumberDialogListener" );
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
                mListener.onDialogPositiveClick(EnterPhoneNumberDialog.this);
            }
        })
        .setNegativeButton(r.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick(EnterPhoneNumberDialog.this);
            }
        });

        return builder.create();
    }
}
