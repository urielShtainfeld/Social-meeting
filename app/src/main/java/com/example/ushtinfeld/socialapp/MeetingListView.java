package com.example.ushtinfeld.socialapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
    MeetingList adapter;
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
        meetingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteMeeting(position);
                return true;
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
    private void deleteMeeting(int position){
        final int deletePosition =position;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        FirebaseDba.getInstance().deleteMeeting(meetingList.get(deletePosition));
                        meetingList.remove(deletePosition);
                        adapter.notifyDataSetChanged();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this meeeting?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        meetingList = FirebaseDba.getInstance().getMeetings();
        adapter = new MeetingList(MeetingListView.this,meetingList);
        adapter.notifyDataSetChanged();
        meetingListView.setAdapter(adapter);
    }
}
