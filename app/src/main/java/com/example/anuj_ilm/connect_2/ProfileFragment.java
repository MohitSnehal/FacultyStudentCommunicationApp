package com.example.anuj_ilm.connect_2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final int GALLERY_INTENT = 2 ;



    public void uploadProfilePicture(View view)
    {
        Intent in = new Intent(Intent.ACTION_PICK) ;
        in.setType("image/*") ;
        startActivityForResult(in , GALLERY_INTENT);
    }

    public ProfileFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //((UserDetails)getActivity()).setTitle("Profile");


        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

}
