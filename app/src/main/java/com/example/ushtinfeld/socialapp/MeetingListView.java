package com.example.ushtinfeld.socialapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import controller.FirebaseDba;
import model.Meeting;

public class MeetingListView extends AppCompatActivity {

    ListView meetingListView;
    List<Meeting> meetingList;
    FloatingActionButton newMeeting;
    final static String MEETTITLE = "MeetingTitle";
    final static String MEETID = "MeetingID";
    final static String EDITABLE = "Editable";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting__list__view);
        meetingListView = (ListView) findViewById(R.id.listViewMeeting);
        meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meeting meeting = meetingList.get(position);
                Intent intent = new Intent(getApplicationContext(),MeetingCard.class);
                intent.putExtra(EDITABLE,false);
                intent.putExtra(MEETID,meeting.getId());
                startActivity(intent);
            }
        });
        newMeeting = (FloatingActionButton) findViewById(R.id.fab);
        newMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MeetingCard.class);
                intent.putExtra(EDITABLE,true);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        meetingList = FirebaseDba.getInstance().GetMeetings();
        MeetingList adapter = new MeetingList(MeetingListView.this,meetingList);
        adapter.notifyDataSetChanged();
        meetingListView.setAdapter(adapter);
    }
}
