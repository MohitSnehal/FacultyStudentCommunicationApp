package com.example.anuj_ilm.connect_2;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OutboxMessageFragment extends Fragment {

    private String timestamp,subject,attachmentUrls,body,recepients;
    private TextView outboxMessageToValue,outboxSubjectValue,outboxAttachmentsTextView,outboxMessageValue,outboxTimestampTextview;

    public OutboxMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((TeacherHomeActivity)getActivity()).setTitle("Outbox");
        return inflater.inflate(R.layout.fragment_outbox_message, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle fromOutboxFragment = getArguments();

        outboxMessageToValue = (TextView) getView().findViewById(R.id.outboxMessageToValue);
        outboxSubjectValue = (TextView) getView().findViewById(R.id.outboxSubjectValue);
        outboxAttachmentsTextView = (TextView) getView().findViewById(R.id.outboxAttachmentsTextView);
        outboxMessageValue = (TextView) getView().findViewById(R.id.outboxMessageValue);
        outboxTimestampTextview = (TextView) getView().findViewById(R.id.outboxTimestampTextview);

        recepients = fromOutboxFragment.getString("recepients");
        body = fromOutboxFragment.getString("body");
        attachmentUrls = fromOutboxFragment.getString("attachmentUrls");
        subject = fromOutboxFragment.getString("subject");
        timestamp = fromOutboxFragment.getString("timestamp");

        outboxMessageToValue.setText(recepients);
        outboxSubjectValue.setText(subject);
        outboxAttachmentsTextView.setText(attachmentUrls);
        outboxMessageValue.setText(body);
        String tmpTimestamp[] =timestamp.split(" ") ;
        timestamp = "Delivered at : " + tmpTimestamp[2] + " " + tmpTimestamp[1] + "  " + tmpTimestamp[5] + " , " + tmpTimestamp[3] ;
        outboxTimestampTextview.setText(timestamp);




    }
}
