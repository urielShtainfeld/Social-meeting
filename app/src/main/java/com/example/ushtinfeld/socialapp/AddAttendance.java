package com.example.ushtinfeld.socialapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;

import java.util.ArrayList;
import java.util.List;

import controller.FirebaseDba;
import model.Attendance;

public class AddAttendance extends AppCompatActivity {

    private TextView meetingTitleTxt;
    private EditText enterNameTxt;
    private EditText enterEmailTxt;
    private Button saveAttendanceBtn;
    List<Attendance> attendanceList;
    ListView attendanceListView;
    private static final int REQUEST_INVITE = 0;

    private String id;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);

        meetingTitleTxt = (TextView) findViewById(R.id.meetingAttendTxt);
        enterNameTxt = (EditText) findViewById(R.id.NameTxt);
        enterEmailTxt = (EditText) findViewById(R.id.emailTxt);
        saveAttendanceBtn = (Button) findViewById(R.id.saveAttendBtn);
        attendanceListView = (ListView)findViewById(R.id.listViewAttendance);

        Intent intent = getIntent();

        id = intent.getStringExtra(MeetingListView.MEETID);
        title = intent.getStringExtra(MeetingListView.MEETTITLE);
        meetingTitleTxt.setText(title);
        saveAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAttendance();
            }
        });

    }

    private void saveAttendance(){
        String name = enterNameTxt.getText().toString();
        String email = enterEmailTxt.getText().toString();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) ){
            FirebaseDba.getInstance().insertAttend(this.id,name,email);
            Toast.makeText(this,"Attendance added",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"You must enter attendance name and phone",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        attendanceList = FirebaseDba.getInstance().GetAttendances(id);
        AttendanceList adapter = new AttendanceList(AddAttendance.this,attendanceList);
        if (attendanceList != null) {
            attendanceListView.setAdapter(adapter);
        }
    }
}
