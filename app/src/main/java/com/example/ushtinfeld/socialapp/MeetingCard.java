package com.example.ushtinfeld.socialapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.w9jds.FloatingActionMenu;

import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import controller.FirebaseDba;
import model.Meeting;
import model.MyItem;

public class MeetingCard extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    Meeting meeting;
    EditText titleTxt,descriptionTxt, locationTxt;
    Button saveMeetingBtn,addDateBtn,addLocationBtn;
    FloatingActionMenu fabMeetCard;
    FloatingActionButton fabAddAttend,fabAddItem,fabAddMyItems;
    boolean editable;
    List<MyItem> myItemList;
    ListView myItemsListView;
    Date DateAndTime;
    TextView dateTxt;
    final Context context = this;
    double longt,latit;
    private GoogleApiClient mClient;
    private Geofence mGeofencing;
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
        addLocationBtn = (Button) findViewById(R.id.AddLocation);
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
        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLocation();
            }
        });
        String meetID = intent.getStringExtra(MeetingListView.MEETID);
        editable = intent.getBooleanExtra(MeetingListView.EDITABLE,true);
        setEditable(editable,meetID);
    }
    private void addLocation(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MeetingCard.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
        try {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent intent = builder.build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(this, "GooglePlayServices Not Available", Toast.LENGTH_LONG).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "GooglePlayServices Not Available", Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            Toast.makeText(this,"PlacePicker Exception", Toast.LENGTH_LONG).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            if (place == null) {
                Toast.makeText(this,"No place selected", Toast.LENGTH_LONG).show();
                return;
            }

            this.latit = place.getLatLng().latitude;
            this.longt = place.getLatLng().longitude;
            locationTxt.setText(place.getName().toString());
            // Get live data information
        }
    }
    private void addMeeting(){
        String title = titleTxt.getText().toString();
        if (!TextUtils.isEmpty(title)){
            String desc = descriptionTxt.getText().toString();
            String loc = locationTxt.getText().toString();
            double longtitude = longt;
            double latitude = latit;
            Date date = this.DateAndTime;
            this.meeting = FirebaseDba.getInstance().insertMeeting(title,desc,loc,latitude,longtitude,date);
            Toast.makeText(this,"Meeting added",Toast.LENGTH_LONG).show();
            Intent intent = getIntent();
            intent.putExtra(MeetingListView.EDITABLE,false);
            intent.putExtra(MeetingListView.MEETID,meeting.getId());
            finish();
            startActivity(intent);
        }else {
            Toast.makeText(this,"You must enter meeting title",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (meeting != null) {
            if (meeting.getMyItems() != null) {
                myItemList = meeting.getMyItems();
            }
            else
            {
                meeting.setMyItems(new ArrayList<MyItem>());
                myItemList = meeting.getMyItems();
            }
            MyItemList adapter = new MyItemList(MeetingCard.this, myItemList);
            myItemsListView.setAdapter(adapter);

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
            if (meeting.getDate() != null) {
                dateTxt.setText(meeting.getDate().toString());
            }
            fabMeetCard.setVisibility(View.VISIBLE);
            addDateBtn.setVisibility(View.INVISIBLE);
            addLocationBtn.setVisibility(View.INVISIBLE);
        }
    }
}
