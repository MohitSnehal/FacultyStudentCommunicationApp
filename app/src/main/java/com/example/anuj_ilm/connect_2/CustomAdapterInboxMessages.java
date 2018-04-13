package com.example.anuj_ilm.connect_2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Anuj-ILM on 1/17/2018.
 */

public class CustomAdapterInboxMessages extends ArrayAdapter<FacultyMessage>
{
    public CustomAdapterInboxMessages(@NonNull Context context, FacultyMessage[] messages) {

        super(context, R.layout.custom_inbox_row ,messages);

    }

    @NonNull


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext()) ;
        View customView = inflater.inflate(R.layout.custom_inbox_row , parent , false) ;

        FacultyMessage obj = getItem(position) ;
        TextView inboxFromValue = (TextView)customView.findViewById(R.id.inboxFromValue) ;
        TextView inboxSubjectValue = (TextView)customView.findViewById(R.id.inboxSubjectValue) ;
        TextView inboxTimestampTextView = (TextView)customView.findViewById(R.id.inboxTimestampTextView) ;
        inboxFromValue.setText(obj.getSender());
        inboxSubjectValue.setText(obj.getSubject());
        String tmpTimestamp[] = obj.getTimestamp().split(" ") ;
        String timestamp = tmpTimestamp[2] + " " + tmpTimestamp[1] + " " + tmpTimestamp[5] + " , " + tmpTimestamp[3] ;
        inboxTimestampTextView.setText(timestamp);

        return customView;
    }
}

