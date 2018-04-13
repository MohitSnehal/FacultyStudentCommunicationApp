package com.example.anuj_ilm.connect_2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SceCardFragment extends Fragment {


    static ListView listView;
    public SceCardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onResume() {
       listView = (ListView)getView().findViewById(R.id.sceListView) ;
        if(listView == null && UserDetails.listView != null)
        {
            listView =  UserDetails.listView;
        }

        FacultyInfo[] array = new FacultyInfo[90];

        for(int i = 0 ; i < 90 ; i++)
        {
            String stringResourceName = "faculty_sce_" + Integer.toString(i+1);
            int resID = getResources().getIdentifier(stringResourceName , "array", "com.example.anuj_ilm.connect_2");
            ArrayAdapter<CharSequence> facultyAdapter = ArrayAdapter.createFromResource(this.getContext(),
                    resID , android.R.layout.simple_spinner_item);
            String name = facultyAdapter.getItem(0).toString();
            String designation = facultyAdapter.getItem(1).toString();
            String imageName = facultyAdapter.getItem(5).toString();
            array[i] = new FacultyInfo(name,designation,imageName);
            Log.d("imageName : " , imageName);
        }

        ListAdapter listAdapter = new CustomAdapterSceCard(this.getContext(),array);
        listView.setAdapter(listAdapter);


        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction() ;
                        Bundle args = new Bundle();
                        args.putString("array name", "faculty_sce_" + Integer.toString(position+1));
                        FacultyDetailFragment obj = new FacultyDetailFragment();
                        obj.setArguments(args);
                        fragmentTransaction.replace(R.id.flMain , obj) ;
                        fragmentTransaction.addToBackStack("acd");
                        fragmentTransaction.commit() ;
                    }
                }
        );

        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //((UserDetails)getActivity()).setTitle("Faculty details");
        return inflater.inflate(R.layout.fragment_sce_card, container, false);
    }

}
