package com.waterbanana.meetapp;

/**
 * Created by Master N on 7/10/2015.
 */

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SocialFrag extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_view_social, container, false );
        return view;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
