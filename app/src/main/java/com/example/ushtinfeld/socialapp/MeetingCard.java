package com.example.ushtinfeld.socialapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import controller.FirebaseDba;

public class MeetingCard extends AppCompatActivity {
    //todo: add date and time of meeting

    EditText titleTxt,descriptionTxt, locationTxt;
    Button saveMeetingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_card);

        titleTxt = (EditText) findViewById(R.id.TitleTxt);
        descriptionTxt = (EditText) findViewById(R.id.descriptionTxt);
        locationTxt = (EditText) findViewById(R.id.locationTxt);
        saveMeetingBtn = (Button) findViewById(R.id.saveMeetingBtn);
        saveMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeeting();
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
            FirebaseDba.getInstance().insertMeeting(title,desc,loc,latitude,longtitude);
            Toast.makeText(this,"Meeting added",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"You must enter meeting title",Toast.LENGTH_LONG).show();
        }
    }
}
