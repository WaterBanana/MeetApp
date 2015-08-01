package com.waterbanana.meetapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CP1Home extends Fragment {
    private Button next;
    private static ViewPager viewPager;

    public CP1Home() { }

    public static CP1Home newInstance( ViewPager vp ){
        CP1Home frag = new CP1Home();
        viewPager = vp;

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cphome, container, false);
        final int nextPage = viewPager.getCurrentItem() + 1;

        next = (Button) view.findViewById( R.id.btnCPGoToPg2 );
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem( nextPage );
            }
        });

        return view;
    }


}
