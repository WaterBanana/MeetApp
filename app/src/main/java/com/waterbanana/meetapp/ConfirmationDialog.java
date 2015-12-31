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
import android.widget.TextView;

public class ConfirmationDialog extends DialogFragment {
    private String dialogDesc;

    public interface ConfirmationListener{
        void setPositiveAction(DialogFragment dialog);
        void setNegativeAction(DialogFragment dialog);
    }

    ConfirmationListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mListener = (ConfirmationListener) activity;
        } catch( ClassCastException e ){
            throw new ClassCastException(activity.toString()
                    + " must implement ConfirmationListener" );
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        View view = getActivity().getLayoutInflater().inflate(R.layout.popup_window_confirmation_dialog, null);

        TextView descTV = (TextView) view.findViewById(R.id.popup_desc);
        descTV.setText(dialogDesc);

        builder.setView(view);

        Resources r = getResources();
        builder
                .setPositiveButton(r.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.setPositiveAction(ConfirmationDialog.this);
                    }
        })
                .setNegativeButton(r.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.setNegativeAction(ConfirmationDialog.this);
                    }
                });

        return builder.create();
    }

    public void setText(String message){
        dialogDesc = message;
    }
}
