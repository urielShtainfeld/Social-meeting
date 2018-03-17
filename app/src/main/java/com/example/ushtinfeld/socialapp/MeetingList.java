package com.example.ushtinfeld.socialapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import model.Meeting;

/**
 * Created by ushtinfeld on 17/03/2018.
 */

public class MeetingList extends ArrayAdapter<Meeting> {
    private Activity context;
    private List<Meeting> meetingList;

    public MeetingList(Activity context,List<Meeting> meetingList){
        super(context,R.layout.meeting_list,meetingList);
        this.context = context;
        this.meetingList = meetingList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.meeting_list,null,true);
        TextView titleTxt = (TextView) listViewItem.findViewById(R.id.titleTxtView);
        TextView descriptionTxt = (TextView) listViewItem.findViewById(R.id.descriptionTxtView);
        Meeting meeting = meetingList.get(position);
        titleTxt.setText(meeting.getTitle());
        descriptionTxt.setText(meeting.getDescription());

        return listViewItem;
    }
}
