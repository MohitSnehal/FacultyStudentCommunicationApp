package com.example.anuj_ilm.connect_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OutboxFragment extends Fragment {

    static ListView outboxListview ;
    private FirebaseDatabase database;

    public OutboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((TeacherHomeActivity)getActivity()).setTitle("Outbox");
        return inflater.inflate(R.layout.fragment_outbox, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        outboxListview = (ListView) getView().findViewById(R.id.outboxListView);
        database = FirebaseDatabase.getInstance() ;
        String username = TeacherHomeActivity.username;
        DatabaseReference outboxMessageRef = database.getReference("facultyOutbox/" + username) ;
        Log.d("tag user" , username) ;
        outboxMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<FacultyOutboxMessage> array = new ArrayList<>();
                for(DataSnapshot mySnapshot: dataSnapshot.getChildren()) {
                    //User tmpUser;
                    FacultyOutboxMessage value = mySnapshot.getValue(FacultyOutboxMessage.class);
                    array.add(value) ;

                }
                if (array.size() != 0)
                {
                    System.out.println("dog");
                    FacultyOutboxMessage arr[] = new FacultyOutboxMessage[array.size()] ;
                    int i = array.size() - 1  ;
                    for (FacultyOutboxMessage tmp : array)
                    {
                        arr[i] = tmp ;
                        i-- ;
                    }
                    ListAdapter listAdapter = new CustomAdapterOutboxMessages(getContext(), arr);
                    outboxListview.setAdapter(listAdapter);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("recieve", "Failed to read value.", error.toException());
            }
        });


        outboxListview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction() ;
                        Bundle args = new Bundle();
                        CustomAdapterOutboxMessages listAdapter = (CustomAdapterOutboxMessages) outboxListview.getAdapter();
                        FacultyOutboxMessage selectedMessaqe = (FacultyOutboxMessage)listAdapter.getItem(position);
                        Log.d("rece onresume" , selectedMessaqe.getRecepients()) ;
                        args.putString("recepients",selectedMessaqe.getRecepients());
                        args.putString("body",selectedMessaqe.getBody());
                        args.putString("attachmentUrls" , selectedMessaqe.getAttachmentUrls());
                        args.putString("subject" , selectedMessaqe.getSubject());
                        args.putString("timestamp" , selectedMessaqe.getTimestamp());
                        OutboxMessageFragment obj = new OutboxMessageFragment();
                        obj.setArguments(args);
                        fragmentTransaction.replace(R.id.tflMain , obj) ;
                        fragmentTransaction.addToBackStack("bcd");
                        fragmentTransaction.commit() ;
                    }
                }
        );
    }
}
