package controller;

import android.text.format.Time;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Attendance;
import model.Meeting;

/**
 * Created by ushtinfeld on 15/03/2018.
 */
public class FirebaseDba {
    //todo: add date and time of meeting
    private static FirebaseDba instance = null;
    DatabaseReference DBMeeting;
    List<Meeting> meetingList;
    DatabaseReference DBAttend;

    protected FirebaseDba() {
        this.DBMeeting = FirebaseDatabase.getInstance().getReference("Meeting");
        this.meetingList = new ArrayList<>();

    }
    public static FirebaseDba getInstance() {
        if(instance == null) {
            instance = new FirebaseDba();
        }
        return instance;
    }

    public void insertMeeting(String title, String desc, String loc, double latitude, double longtitude){

        String id = getDBMeeting().push().getKey();
        Meeting meeting = new Meeting(id,title,desc,loc,latitude,longtitude, new Date(2017,12,12), new Time());
        getDBMeeting().child(id).setValue(meeting);
    }
    public List<Meeting> GetMeetings(){

        getDBMeeting().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                meetingList.clear();
                for (DataSnapshot meetingSnapshot:dataSnapshot.getChildren() ) {
                    Meeting meeting = meetingSnapshot.getValue(Meeting.class);
                    meetingList.add(meeting);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return meetingList;
    }

    public void insertAttend(String meetID,String name,String phone){
        DBAttend = FirebaseDatabase.getInstance().getReference("Attendance").child(meetID);
        String id = DBAttend.push().getKey();
        Attendance attendance = new Attendance(id,name,phone);
        DBAttend.child(id).setValue(attendance);
    }

    public DatabaseReference getDBMeeting() {
        return DBMeeting;
    }

    public List<Meeting> getMeetingList() {
        return meetingList;
    }
}

