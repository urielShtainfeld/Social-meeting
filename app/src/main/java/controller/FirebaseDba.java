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
import java.util.stream.Collectors;

import model.Attendance;
import model.Item;
import model.Meeting;
import model.MyItem;
import model.SearchItem;

/**
 * Created by ushtinfeld on 15/03/2018.
 */
public class FirebaseDba {
    private static FirebaseDba instance = null;
    private static DatabaseReference DBMeeting;
    private static List<Meeting> meetingList;
    private static List<SearchItem> searchItems;
    //private static DatabaseReference DBAttend;
    private static DatabaseReference DBSearchItem;
    //private static DatabaseReference DBMyItem;
    private static FirebaseAuth mAuth;
    private final static String DEFAULTNAME = "He-Who-Must-Not-Be-Named";

    protected FirebaseDba() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        this.DBMeeting = FirebaseDatabase.getInstance().getReference("Meeting");
        this.DBSearchItem = FirebaseDatabase.getInstance().getReference("SearchItem");
        this.DBMeeting.keepSynced(true);
        this.DBSearchItem.keepSynced(true);
        this.meetingList = new ArrayList<>();
        this.meetingList = getMeetings();
        this.mAuth = FirebaseAuth.getInstance();

    }
    public static FirebaseDba getInstance() {
        if(instance == null) {
            instance = new FirebaseDba();
        }
        return instance;
    }
    public void addSearchItem(String name){
        SearchItem searchItem = new SearchItem(name);
        getDBSearchItem().child(name).setValue(searchItem);
    }


    public static DatabaseReference getDBSearchItem() {
        return DBSearchItem;
    }
    public Meeting insertMeeting(String title, String desc, String loc, double latitude, double longtitude
            ,Date date){

        String id = getDBMeeting().push().getKey();
        Meeting meeting = new Meeting(id,title,desc,loc,latitude,longtitude, date);

        final FirebaseUser user = mAuth.getCurrentUser();
        if (user.getDisplayName()!= null && user.getDisplayName().length()>0 ){
            meeting.getAttendances().add(new Attendance(user.getDisplayName(),user.getEmail()));
        }
        else
        {
            meeting.getAttendances().add(new Attendance(DEFAULTNAME,user.getEmail()));
        }
        getDBMeeting().child(id).setValue(meeting);
        return meeting;
    }
    public void updateMeeting(Meeting meeting){
        getDBMeeting().child(meeting.getId()).setValue(meeting);
    }
    public void deleteMeeting(Meeting meeting){
        getDBMeeting().child(meeting.getId()).setValue(null);
    }
    public List<Meeting> getMeetings(){

        getDBMeeting().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                meetingList.clear();
                for (DataSnapshot meetingSnapshot:dataSnapshot.getChildren() ) {
                    Meeting meeting = meetingSnapshot.getValue(Meeting.class);
                    if (checkIfAttend(meeting)){
                        meetingList.add(meeting);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return meetingList;
    }
    public boolean checkIfAttend(Meeting meeting){
        List<Attendance> meetAttendance = meeting.getAttendances();
        final FirebaseUser user = mAuth.getCurrentUser();
        //  APK 24 return (meetAttendance.stream()
        //        .filter(item -> item.getEmail().equals(user.getEmail()))
        //        .collect(Collectors.toList()).size);

        // for first use before add meetings
        if (meetAttendance.size() > 0) {
            for (Attendance attendance : meetAttendance) {
                if (attendance.getEmail().equals(user.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }
    public List<Meeting> getMeetingList() {
        return meetingList;
    }

    public Meeting getMeetByID(String meetId){
        for (Meeting meeting:getMeetingList()) {
            if (meeting.getId().equals(meetId)){
                return  meeting;
            }
        }
        return null;
    }
    public DatabaseReference getDBMeeting() {
        return DBMeeting;
    }


   /* public void insertAttend(String meetID,String name,String phone){
        DBAttend = FirebaseDatabase.getInstance().getReference("Attendance").child(meetID);
        String id = DBAttend.push().getKey();
        Attendance attendance = new Attendance(id,name,phone);
        DBAttend.child(id).setValue(attendance);
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
        if (attendancesList.size()>0){
            getMyItems(meetID);
        }
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
        Item attendance = new Item(id,name,qty,qty);
        DBItem.child(id).setValue(attendance);
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

    public List<MyItem> getMyItems(String meetID) {
        final FirebaseUser user = mAuth.getCurrentUser();
        Attendance attendance = getAttendanceFromMeeting(meetID,user.getEmail());
        if (attendance != null) {
            DBMyItem = FirebaseDatabase.getInstance().getReference("MyItem").child(attendance.getId());
            DBMyItem.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    myItemsList.clear();
                    for (DataSnapshot meetingSnapshot : dataSnapshot.getChildren()) {
                        MyItem myItem = meetingSnapshot.getValue(MyItem.class);
                        myItemsList.add(myItem);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        if (myItemsList.size()>0){

        }
        return myItemsList;
    }
    public void saveMyItems(String meetID,Item item){
        final FirebaseUser user = mAuth.getCurrentUser();
        Attendance attendance = getAttendanceFromMeeting(meetID,user.getEmail());
        if (attendance != null) {
            DBMyItem = FirebaseDatabase.getInstance().getReference("MyItem").child(attendance.getId());
            String id = DBMyItem.push().getKey();
            MyItem myItem = new MyItem(id, item.getName(), item.getSelectedQty());
            DBMyItem.child(id).setValue(myItem);
        }
    }
    public void saveItemChanges(String meetId,Item item){
        FirebaseDatabase.getInstance().getReference("Item").child(meetId).child(item.getId()).setValue(item);
    }
    public void clearCache(){
        this.attendancesList.clear();
        this.itemsList.clear();
        this.myItemsList.clear();

    }
    */
}

