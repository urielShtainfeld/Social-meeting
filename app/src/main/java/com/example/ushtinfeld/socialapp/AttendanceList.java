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

import model.Attendance;
import model.Meeting;

/**
 * Created by ushtinfeld on 17/03/2018.
 */

public class AttendanceList extends ArrayAdapter<Attendance> {
    private Activity context;
    private List<Attendance> attendanceList;

    public AttendanceList(Activity context,List<Attendance> attendanceList){
        super(context,R.layout.attendance_list,attendanceList);
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.attendance_list,null,true);
        TextView titleTxt = (TextView) listViewItem.findViewById(R.id.AttendNameTxt);
        TextView descriptionTxt = (TextView) listViewItem.findViewById(R.id.AttendEmailTxt);
        Attendance attendance = attendanceList.get(position);
        if (attendance != null) {
            titleTxt.setText(attendance.getName());
            descriptionTxt.setText(attendance.getEmail());
        }
        return listViewItem;
    }
}
