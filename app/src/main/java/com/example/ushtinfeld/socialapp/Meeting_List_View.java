package com.example.ushtinfeld.socialapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import controller.FirebaseDba;
import model.Meeting;

public class Meeting_List_View extends AppCompatActivity {

    ListView meetingListView;
    List<Meeting> meetingList;
    final static String MEETTITLE = "MeetingTitle";
    final static String MEETID = "MeetingID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting__list__view);
        meetingListView = (ListView) findViewById(R.id.listViewMeeting);
        meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meeting meeting = meetingList.get(position);

                Intent intent = new Intent(getApplicationContext(),AddAttendance.class);
                intent.putExtra(MEETTITLE,meeting.getTitle());
                intent.putExtra(MEETID,meeting.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        meetingList = FirebaseDba.getInstance().GetMeetings();
        MeetingList adapter = new MeetingList(Meeting_List_View.this,meetingList);
    }
}
