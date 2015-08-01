package com.waterbanana.meetapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CP2SMS extends Fragment {
    private Button btnBegin;
    private String TAG = "CP2SMS.java";

    public CP2SMS() {}

    public static CP2SMS newInstance(){
        CP2SMS f = new CP2SMS();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cp2_sm, container, false);

        btnBegin = (Button) view.findViewById( R.id.btnCP2SendSMS);
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CPSMSHandler.class);
                //Log.d(TAG, "CPCSMSHandler started.");
                startActivity(i);
            }
        });

        return view;
    }

}
