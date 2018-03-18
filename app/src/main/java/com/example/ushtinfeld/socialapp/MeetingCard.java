package com.example.ushtinfeld.socialapp;

import android.content.Context;
import android.content.Intent;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants;
import com.w9jds.FloatingActionMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import controller.FirebaseDba;
import model.Meeting;
import model.MyItem;

public class MeetingCard extends AppCompatActivity {
    //todo: add date and time of meeting
    //todo: add for add attend And item
    Meeting meeting;
    EditText titleTxt,descriptionTxt, locationTxt;
    Button saveMeetingBtn,addDateBtn;
    FloatingActionMenu fabMeetCard;
    FloatingActionButton fabAddAttend,fabAddItem,fabAddMyItems;
    boolean editable;
    List<MyItem> myItemList;
    ListView myItemsListView;
    Date DateAndTime;
    TextView dateTxt;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_card);
        final Intent intent = getIntent();
        myItemsListView = (ListView)findViewById(R.id.listViewItem);
        titleTxt = (EditText) findViewById(R.id.TitleTxt);
        descriptionTxt = (EditText) findViewById(R.id.descriptionTxt);
        locationTxt = (EditText) findViewById(R.id.locationTxt);
        saveMeetingBtn = (Button) findViewById(R.id.saveMeetingBtn);
        fabMeetCard = (FloatingActionMenu) findViewById(R.id.fabMeetCard);
        fabAddAttend = (FloatingActionButton) findViewById(R.id.addAttend);
        fabAddItem = (FloatingActionButton) findViewById(R.id.addItem);
        fabAddMyItems = (FloatingActionButton) findViewById(R.id.fabTakeItem);
        dateTxt = (TextView) findViewById(R.id.DateAndTime);
        addDateBtn = (Button) findViewById(R.id.InsertDate);
        addDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SingleDateAndTimePickerDialog.Builder(context)
                        .mustBeOnFuture()
                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                            @Override
                            public void onDisplayed(SingleDateAndTimePicker picker) {
                                //retrieve the SingleDateAndTimePicker
                            }
                        })

                        .title("Simple")
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
                                DateAndTime = date;
                                dateTxt.setText(DateAndTime.toString());
                            }
                        }).display();
            }
        });
        saveMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeeting();
                saveMeetingBtn.setVisibility(View.INVISIBLE);
                fabMeetCard.setVisibility(View.VISIBLE);
            }
        });



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
        fabAddMyItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendIntent = new Intent(getApplicationContext(),MyItemListView.class);
                attendIntent.putExtra(MeetingListView.MEETTITLE,meeting.getTitle());
                attendIntent.putExtra(MeetingListView.MEETID,meeting.getId());
                startActivity(attendIntent);
            }
        });
        String meetID = intent.getStringExtra(MeetingListView.MEETID);
        editable = intent.getBooleanExtra(MeetingListView.EDITABLE,true);
        setEditable(editable,meetID);
    }
    private void addMeeting(){
        String title = titleTxt.getText().toString();
        if (!TextUtils.isEmpty(title)){
            String desc = descriptionTxt.getText().toString();
            String loc = locationTxt.getText().toString();
            double longtitude = 5.5;
            double latitude = 5.5;
            int noOfAttendence = 0;
            this.meeting = FirebaseDba.getInstance().insertMeeting(title,desc,loc,latitude,longtitude,DateAndTime);
            Toast.makeText(this,"Meeting added",Toast.LENGTH_LONG).show();
            setEditable(editable,this.meeting.getId());
        }else {
            Toast.makeText(this,"You must enter meeting title",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (meeting != null) {
            myItemList = FirebaseDba.getInstance().getMyItems(meeting.getId());
            MyItemList adapter = new MyItemList(MeetingCard.this, myItemList);
            if (myItemList != null) {
                myItemsListView.setAdapter(adapter);
            }
        }
    }
    public void setEditable(boolean editable,String meetID){
        if (editable){
            fabMeetCard.setVisibility(View.INVISIBLE);

        }else{
            meeting = FirebaseDba.getInstance().getMeetByID(meetID);
            saveMeetingBtn.setVisibility(View.INVISIBLE);
            titleTxt.setFocusable(false);
            descriptionTxt.setFocusable(false);
            locationTxt.setFocusable(false);
            titleTxt.setText(meeting.getTitle());
            descriptionTxt.setText(meeting.getDescription());
            locationTxt.setText(meeting.getLocationName());
            dateTxt.setText(meeting.getDate().toString());
            fabMeetCard.setVisibility(View.VISIBLE);
            addDateBtn.setVisibility(View.INVISIBLE);
        }
    }
}
