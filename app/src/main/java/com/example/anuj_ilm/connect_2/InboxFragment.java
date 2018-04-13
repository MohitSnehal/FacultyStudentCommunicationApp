package com.example.anuj_ilm.connect_2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    static ListView inboxListView ;


    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //((UserDetails)getActivity()).setTitle("Inbox");
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        inboxListView = (ListView) getView().findViewById(R.id.inboxListView);
        FirebaseDatabase database = FirebaseDatabase.getInstance() ;
        DatabaseReference inboxMessageRef = database.getReference("messages/" + UserDetails.branch + "/" + UserDetails.year + "/" + UserDetails.section) ;
        inboxMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<FacultyMessage> array = new ArrayList<>();
                for(DataSnapshot mySnapshot: dataSnapshot.getChildren()) {
                    //User tmpUser;
                    FacultyMessage value = mySnapshot.getValue(FacultyMessage.class);
                    array.add(value) ;

                }
                if (array.size() != 0)
                {
                    FacultyMessage arr[] = new FacultyMessage[array.size()] ;
                    int i = array.size() - 1  ;
                    for (FacultyMessage tmp : array)
                    {
                        arr[i] = tmp ;
                        i-- ;
                    }
                    ListAdapter listAdapter = new CustomAdapterInboxMessages(getContext() , arr);
                    inboxListView.setAdapter(listAdapter);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("recieve", "Failed to read value.", error.toException());
            }
        });


        inboxListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                                .beginTransaction() ;
                        Bundle args = new Bundle();
                        ListAdapter listAdapter = inboxListView.getAdapter();
                        FacultyMessage selectedMessaqe = (FacultyMessage)listAdapter.getItem(position);

                        args.putString("sender",selectedMessaqe.getSender());
                        args.putString("body",selectedMessaqe.getBody());
                        args.putString("attachmentUrls" , selectedMessaqe.getAttachmentUrls());
                        args.putString("subject" , selectedMessaqe.getSubject());
                        args.putString("timestamp" , selectedMessaqe.getTimestamp());
                        InboxMessageFragment obj = new InboxMessageFragment();
                        Log.d("rece" , args.getString("sender")) ;
                        obj.setArguments(args);
                        fragmentTransaction.replace(R.id.flMain , obj) ;
                        fragmentTransaction.addToBackStack("bcd");
                        fragmentTransaction.commit() ;
                    }
                }
        );
    }
}
