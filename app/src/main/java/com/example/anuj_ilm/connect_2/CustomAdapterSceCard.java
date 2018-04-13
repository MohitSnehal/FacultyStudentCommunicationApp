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
 * Created by Anuj-ILM on 1/6/2018.
 */

class CustomAdapterSceCard extends ArrayAdapter<FacultyInfo> {
    public CustomAdapterSceCard(@NonNull Context context, FacultyInfo[] faculties) {

        super(context, R.layout.custom_row ,faculties);

    }

    @NonNull


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext()) ;
        View customView = inflater.inflate(R.layout.custom_row , parent , false) ;

        FacultyInfo obj = getItem(position) ;
        ImageView facultyImageView = (ImageView)customView.findViewById(R.id.facultyImageView) ;
        TextView titleTextView = (TextView)customView.findViewById(R.id.titleTextView) ;
        TextView designationTextView = (TextView)customView.findViewById(R.id.designationTextView) ;
        titleTextView.setText(obj.getName());
        designationTextView.setText(obj.getDesignation());

        String mDrawableName =  obj.getImageName();
        int resID = customView.getResources().getIdentifier(mDrawableName , "drawable", "com.example.anuj_ilm.connect_2");
        facultyImageView.setImageResource(resID);
        Log.d("resID : " , Integer.toString(resID));

        return customView;
    }
}
