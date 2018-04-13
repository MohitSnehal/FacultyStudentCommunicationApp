package com.example.anuj_ilm.connect_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTableFragment extends Fragment {


    public TimeTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //((UserDetails)getActivity()).setTitle("time table");
        ///showTimeTable();
        //((TeacherHomeActivity)getActivity()).setTitle("Time table");
        return inflater.inflate(R.layout.fragment_time_table, container, false);
    }


}
