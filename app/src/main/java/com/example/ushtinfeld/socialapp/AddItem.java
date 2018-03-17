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

import java.util.List;

import controller.FirebaseDba;
import model.Item;

public class AddItem extends AppCompatActivity {

    private TextView meetingTitleTxt;
    private EditText enterNameTxt;
    private EditText enterEmailTxt;
    private Button saveAttendanceBtn;
    List<Item> attendanceList;
    ListView attendanceListView;


    private String id;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        meetingTitleTxt = (TextView) findViewById(R.id.meetingItemTxt);
        enterNameTxt = (EditText) findViewById(R.id.ItemNameTxt);
        enterEmailTxt = (EditText) findViewById(R.id.ItemQtyTxt);
        saveAttendanceBtn = (Button) findViewById(R.id.saveItemBtn);
        attendanceListView = (ListView)findViewById(R.id.listViewItem);

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
            FirebaseDba.getInstance().insertItem(this.id,name,Integer.parseInt(email));
            Toast.makeText(this,"Item added",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"You must enter Item name and Quantity",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        attendanceList = FirebaseDba.getInstance().GetItems(id);
        ItemsList adapter = new ItemsList(AddItem.this,attendanceList);
        attendanceListView.setAdapter(adapter);
    }
}
