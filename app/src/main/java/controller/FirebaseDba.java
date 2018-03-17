package controller;

import android.text.format.Time;

import com.example.ushtinfeld.socialapp.AttendanceList;
import com.example.ushtinfeld.socialapp.MeetingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Attendance;
import model.Item;
import model.Meeting;

/**
 * Created by ushtinfeld on 15/03/2018.
 */
public class FirebaseDba {
    //todo: add date and time of meeting
    private static FirebaseDba instance = null;
    private static DatabaseReference DBMeeting;
    private static List<Meeting> meetingList;
    private static DatabaseReference DBAttend;
    private static List<Attendance> attendancesList;
    private static DatabaseReference DBItem;
    private static List<Item> itemsList;
    private static List<Item> myItemsList;
    private static DatabaseReference DBMyItem;
    private static FirebaseAuth mAuth;

    protected FirebaseDba() {
        this.DBMeeting = FirebaseDatabase.getInstance().getReference("Meeting");
        this.meetingList = new ArrayList<>();
        this.attendancesList = new ArrayList<>();
        this.itemsList = new ArrayList<>();
        this.myItemsList = new ArrayList<>();
        this.mAuth = FirebaseAuth.getInstance();

    }
    public static FirebaseDba getInstance() {
        if(instance == null) {
            instance = new FirebaseDba();
        }
        return instance;
    }

    public Meeting insertMeeting(String title, String desc, String loc, double latitude, double longtitude){

        String id = getDBMeeting().push().getKey();
        Meeting meeting = new Meeting(id,title,desc,loc,latitude,longtitude, new Date(2017,12,12), new Time());
        getDBMeeting().child(id).setValue(meeting);
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user.getDisplayName()!= null){
            insertAttend(meeting.getId(),user.getDisplayName(),user.getEmail());
        }else
        {
            insertAttend(meeting.getId(), "He-Who-Must-Not-Be-Named",user.getEmail());
        }
        return meeting;
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

    public List<Attendance> GetAttendances(String meetID) {
        DBAttend = FirebaseDatabase.getInstance().getReference("Attendance").child(meetID);
        DBAttend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                attendancesList.clear();
                for (DataSnapshot meetingSnapshot:dataSnapshot.getChildren() ) {
                    Attendance attendance = meetingSnapshot.getValue(Attendance.class);
                    attendancesList.add(attendance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return attendancesList;
    }

    public List<Item> GetItems(String meetID) {
        DBItem = FirebaseDatabase.getInstance().getReference("Item").child(meetID);
        DBItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsList.clear();
                for (DataSnapshot meetingSnapshot:dataSnapshot.getChildren() ) {
                    Item attendance = meetingSnapshot.getValue(Item.class);
                    itemsList.add(attendance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return itemsList;
    }
    public void insertItem(String meetID,String name,int qty){
        DBItem = FirebaseDatabase.getInstance().getReference("Item").child(meetID);
        String id = DBItem.push().getKey();
        Item attendance = new Item(id,name,qty);
        DBItem.child(id).setValue(attendance);
    }
    public Meeting getMeetByID(String meetId){
        for (Meeting meeting:getMeetingList()) {
             if (meeting.getId().equals(meetId)){
                 return  meeting;
             }
        }
        return null;
    }

    public Attendance getAttendanceFromMeeting(String meetID,String mail){
        for (Attendance attend:getAttendancesList()) {
            if (attend.getEmail().equals(mail)) {
                return attend;
            }
        }
        for (Attendance attend:GetAttendances(meetID)) {
            if (attend.getEmail().equals(mail)) {
                return attend;
            }
        }
        return null;
    }
    public static List<Attendance> getAttendancesList() {
        return attendancesList;
    }

    public static List<Item> getItemsList() {
        return itemsList;
    }


    public List<Item> getMyItems(String meetID) {
        final FirebaseUser user = mAuth.getCurrentUser();
        Attendance attendance = getAttendanceFromMeeting(meetID,user.getEmail());
        DBMyItem = FirebaseDatabase.getInstance().getReference("Item").child(meetID).child(attendance.getId());
        DBMyItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myItemsList.clear();
                for (DataSnapshot meetingSnapshot:dataSnapshot.getChildren() ) {
                    Item attendance = meetingSnapshot.getValue(Item.class);
                    myItemsList.add(attendance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return myItemsList;
    }

}

