package com.example.ushtinfeld.socialapp;

import android.content.Intent;
import com.w9jds.FloatingActionMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import controller.FirebaseDba;
import model.Meeting;

public class MeetingCard extends AppCompatActivity {
    //todo: add date and time of meeting
    //todo: add for add attend And item
    Meeting meeting;
    EditText titleTxt,descriptionTxt, locationTxt;
    Button saveMeetingBtn;
    FloatingActionMenu fabMeetCard;
    FloatingActionButton fabAddAttend,fabAddItem;
    boolean editable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_card);
        final Intent intent = getIntent();
        titleTxt = (EditText) findViewById(R.id.TitleTxt);
        descriptionTxt = (EditText) findViewById(R.id.descriptionTxt);
        locationTxt = (EditText) findViewById(R.id.locationTxt);
        saveMeetingBtn = (Button) findViewById(R.id.saveMeetingBtn);
        editable = intent.getBooleanExtra(MeetingListView.EDITABLE,true);
        fabMeetCard = (FloatingActionMenu) findViewById(R.id.fabMeetCard);
        fabAddAttend = (FloatingActionButton) findViewById(R.id.addAttend);
        fabAddItem = (FloatingActionButton) findViewById(R.id.addItem);

        saveMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeeting();
                saveMeetingBtn.setVisibility(View.INVISIBLE);
                fabMeetCard.setVisibility(View.VISIBLE);
            }
        });

        if (editable){
            fabMeetCard.setVisibility(View.INVISIBLE);
        }else{
            meeting = FirebaseDba.getInstance().getMeetByID(intent.getStringExtra(MeetingListView.MEETID));
            saveMeetingBtn.setVisibility(View.INVISIBLE);
            titleTxt.setEnabled(false);
            descriptionTxt.setEnabled(false);
            locationTxt.setEnabled(false);
            titleTxt.setText(meeting.getTitle());
            descriptionTxt.setText(meeting.getDescription());
            locationTxt.setText(meeting.getLocationName());
            fabMeetCard.setVisibility(View.VISIBLE);
        }

        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemIntent = new Intent(getApplicationContext(),AddItem.class);
                itemIntent.putExtra(MeetingListView.MEETTITLE,meeting.getTitle());
                itemIntent.putExtra(MeetingListView.MEETID,meeting.getId());
                startActivity(itemIntent);
            }
        });

        fabAddAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendIntent = new Intent(getApplicationContext(),AddAttendance.class);
                attendIntent.putExtra(MeetingListView.MEETTITLE,meeting.getTitle());
                attendIntent.putExtra(MeetingListView.MEETID,meeting.getId());
                startActivity(attendIntent);
            }
        });

    }
    private void addMeeting(){
        String title = titleTxt.getText().toString();
        if (!TextUtils.isEmpty(title)){
            String desc = descriptionTxt.getText().toString();
            String loc = locationTxt.getText().toString();
            double longtitude = 5.5;
            double latitude = 5.5;
            int noOfAttendence = 0;
             this.meeting = FirebaseDba.getInstance().insertMeeting(title,desc,loc,latitude,longtitude);
            Toast.makeText(this,"Meeting added",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"You must enter meeting title",Toast.LENGTH_LONG).show();
        }
    }
}
