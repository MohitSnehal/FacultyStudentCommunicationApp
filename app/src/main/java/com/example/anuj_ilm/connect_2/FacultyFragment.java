package com.example.anuj_ilm.connect_2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyFragment extends Fragment {

    private String selectedSchool;

    private FragmentManager supportFragmentManager;
    public FacultyFragment() {
        // Required empty public constructor
    }

    public void submitSchool(View view) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMain, new SceCardFragment());
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //((TeacherHomeActivity)getActivity()).setTitle("Faculty Details");
        return inflater.inflate(R.layout.fragment_faculty, container, false);
    }


    @Override
    public void onResume() {
        Spinner schoolSelectorSpinner = (Spinner)getView().findViewById(R.id.schoolSpinner);
        selectedSchool = "School of Computer Engineering";

        ArrayAdapter<CharSequence> schoolsAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.schools, android.R.layout.simple_spinner_item);
        schoolsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSelectorSpinner.setAdapter(schoolsAdapter);

        schoolSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if(position == 0)
                {
                    selectedSchool = "School of Computer Engineering";
                }
                else
                    selectedSchool = "NA";
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });

        super.onResume();
    }

    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }
}
