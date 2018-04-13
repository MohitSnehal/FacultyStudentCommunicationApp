package com.example.anuj_ilm.connect_2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Anuj-ILM on 1/16/2018.
 */

public class CustomAdapterOutboxMessages extends ArrayAdapter<FacultyOutboxMessage>
{
    public CustomAdapterOutboxMessages(@NonNull Context context, FacultyOutboxMessage[] messages) {

        super(context, R.layout.custom_outbox_row ,messages);

    }

    @NonNull


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext()) ;
        View customView = inflater.inflate(R.layout.custom_outbox_row , parent , false) ;

        FacultyOutboxMessage obj = getItem(position) ;
        TextView outboxToValue = (TextView)customView.findViewById(R.id.outboxToValue) ;
        TextView outboxSubjectValue = (TextView)customView.findViewById(R.id.outboxSubjectValue) ;
        TextView timestampTextView = (TextView)customView.findViewById(R.id.timestampTextView) ;
        outboxToValue.setText(obj.getRecepients());
        outboxSubjectValue.setText(obj.getSubject());
        String tmpTimestamp[] = obj.getTimestamp().split(" ") ;
        String timestamp = tmpTimestamp[2] + " " + tmpTimestamp[1] + " " + tmpTimestamp[5] + " , " + tmpTimestamp[3] ;
        timestampTextView.setText(timestamp);

        return customView;
    }
}
