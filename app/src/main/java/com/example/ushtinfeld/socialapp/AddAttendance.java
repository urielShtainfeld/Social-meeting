package com.example.ushtinfeld.socialapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import controller.FirebaseDba;

public class AddAttendance extends AppCompatActivity {

    private TextView meetingTitleTxt;
    private EditText enterNameTxt;
    private EditText enterEmailTxt;
    private Button saveAttendanceBtn;
    private ListView attendanceList;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);

        meetingTitleTxt = (TextView) findViewById(R.id.titleTxtView);
        enterNameTxt = (EditText) findViewById(R.id.NameTxt);
        enterEmailTxt = (EditText) findViewById(R.id.emailTxt);
        saveAttendanceBtn = (Button) findViewById(R.id.saveAttendBtn);
        attendanceList = (ListView)findViewById(R.id.listViewAttendance);

        Intent intent = getIntent();

        id = intent.getStringExtra(MeetingListView.MEETID);
        String title = intent.getStringExtra(MeetingListView.MEETTITLE);
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
}
