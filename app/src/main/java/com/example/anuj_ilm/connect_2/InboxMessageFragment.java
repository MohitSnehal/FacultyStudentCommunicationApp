package com.example.anuj_ilm.connect_2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxMessageFragment extends Fragment {

    private String timestamp,subject,attachmentUrls,body,sender , tmpurls[];
    private TextView inboxMessageFromValue,inboxSubjectValue,inboxAttachmentsTextView,inboxMessageValue,inboxTimestampTextview;
    private Button openAttachmentButton ;
    private Spinner attachmentSpinner ;
    private ArrayAdapter<String> attachmentSpinnerAdapter ;
    public static String selectedAttachmentUrl ;



    public InboxMessageFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //((UserDetails)getActivity()).setTitle("Inbox");
        return inflater.inflate(R.layout.fragment_inbox_message, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle fromOutboxFragment = getArguments();

        inboxMessageFromValue = (TextView) getView().findViewById(R.id.inboxMessageFromValue);
        inboxSubjectValue = (TextView) getView().findViewById(R.id.inboxSubjectValue);
        //inboxAttachmentsTextView = (TextView) getView().findViewById(R.id.inboxAttachmentsTextView);
        inboxMessageValue = (TextView) getView().findViewById(R.id.inboxMessageValue);
        inboxTimestampTextview = (TextView) getView().findViewById(R.id.inboxTimestampTextview);

        openAttachmentButton = (Button)getView().findViewById(R.id.openAttachmentButton);
        attachmentSpinner = (Spinner) getView().findViewById(R.id.attachmentSpinner);


        sender = fromOutboxFragment.getString("sender");
        body = fromOutboxFragment.getString("body");
        attachmentUrls = fromOutboxFragment.getString("attachmentUrls");
        subject = fromOutboxFragment.getString("subject");
        timestamp = fromOutboxFragment.getString("timestamp");

        inboxMessageFromValue.setText(sender);
        inboxSubjectValue.setText(subject);
        //inboxAttachmentsTextView.setText(attachmentUrls);
        inboxMessageValue.setText(body);
        String tmpTimestamp[] =timestamp.split(" ") ;
        timestamp = "Received at : " + tmpTimestamp[2] + " " + tmpTimestamp[1] + "  " + tmpTimestamp[5] + " , " + tmpTimestamp[3] ;
        inboxTimestampTextview.setText(timestamp);

        tmpurls = attachmentUrls.split(",") ;
        if(!tmpurls[0].equals(""))
        {
            String spinnerTitles[] = new String[tmpurls.length];
            for (int i = 0 ; i<tmpurls.length ; i++)
            {
                spinnerTitles[i] = "Attachment " + Integer.toString(i+1) ;
            }
            openAttachmentButton.setVisibility(View.VISIBLE);
            attachmentSpinnerAdapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, spinnerTitles);
            attachmentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            attachmentSpinner.setAdapter(attachmentSpinnerAdapter);

            attachmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    selectedAttachmentUrl = tmpurls[position] ;
                }

                public void onNothingSelected(AdapterView<?> parentView)
                {
                    //do nothing
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();



    }
}
