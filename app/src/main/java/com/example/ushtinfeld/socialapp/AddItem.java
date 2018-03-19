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

import java.util.ArrayList;
import java.util.List;

import controller.FirebaseDba;
import model.Item;
import model.Meeting;

public class AddItem extends AppCompatActivity {

    private TextView meetingTitleTxt;
    private EditText enterNameTxt;
    private EditText enterEmailTxt;
    private Button saveAttendanceBtn;
    List<Item> itemList;
    ListView itemListView;
    private Meeting meeting;
    ItemsList adapter;
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
        itemListView = (ListView)findViewById(R.id.listViewItem);

        Intent intent = getIntent();

        id = intent.getStringExtra(MeetingListView.MEETID);
        this.meeting =FirebaseDba.getInstance().getMeetByID(id);
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
        String qty = enterEmailTxt.getText().toString();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(qty) ){
            meeting.getItems().add(new Item(name,Integer.parseInt(qty),Integer.parseInt(qty)));
            //FirebaseDba.getInstance().insertItem(this.id,name,Integer.parseInt(qty));

            adapter.notifyDataSetChanged();
            Toast.makeText(this,"Item added",Toast.LENGTH_LONG).show();
            FirebaseDba.getInstance().updateMeeting(meeting);
        }
        else
        {
            Toast.makeText(this,"You must enter Item name and Quantity",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (this.meeting.getItems() != null) {
            itemList = this.meeting.getItems();
        }else {
            this.meeting.setItems(new ArrayList<Item>());
            itemList = this.meeting.getItems();
        }

        adapter = new ItemsList(AddItem.this,itemList);
        itemListView.setAdapter(adapter);
    }
}
