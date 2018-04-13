package com.example.anuj_ilm.connect_2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyDetailFragment extends Fragment {


    public FacultyDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //((TeacherHomeActivity)getActivity()).setTitle("Faculty Details");
        return inflater.inflate(R.layout.fragment_faculty_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getting the array name from bundle send by FacultyFragment
        String stringResourceName = getArguments().getString("array name");
        int resID = this.getResources().getIdentifier(stringResourceName , "array", "com.example.anuj_ilm.connect_2");
        ArrayAdapter<CharSequence> facultyAdapter = ArrayAdapter.createFromResource(this.getContext() ,
                resID , android.R.layout.simple_spinner_item);


        //populating the details from string array into fields of the faculty details
        TextView nameTextView = (TextView) getView().findViewById(R.id.facultyDetailsNameField);
        TextView designationTextView = (TextView) getView().findViewById(R.id.facultyDetailsDesignationField);
        TextView emailTextView = (TextView) getView().findViewById(R.id.facultyDetailsEmailField);
        TextView contactTextView = (TextView) getView().findViewById(R.id.facultyDetailsContactField);
        TextView locationTextView = (TextView) getView().findViewById(R.id.facultyDetailsLocationField);
        ImageView imageView = (ImageView) getView().findViewById(R.id.facultyDetailsImageView);

        nameTextView.setText(facultyAdapter.getItem(0).toString());
        designationTextView.setText(facultyAdapter.getItem(1).toString());
        emailTextView.setText(facultyAdapter.getItem(2).toString());
        contactTextView.setText(facultyAdapter.getItem(3).toString());
        locationTextView.setText(facultyAdapter.getItem(4).toString());

        String imageResourceName = facultyAdapter.getItem(5).toString();
        resID = getResources().getIdentifier(imageResourceName , "drawable", "com.example.anuj_ilm.connect_2");
        imageView.setImageResource(resID);

    }


}
