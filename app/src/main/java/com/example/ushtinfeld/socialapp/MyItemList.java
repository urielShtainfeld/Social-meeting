package com.example.ushtinfeld.socialapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import model.MyItem;

/**
 * Created by ushtinfeld on 18/03/2018.
 */

public class MyItemList extends ArrayAdapter<MyItem> {
    private Activity context;
    private List<MyItem> myItems;

    public MyItemList(Activity context, List<MyItem> attendanceList) {
        super(context, R.layout.my_item_list, attendanceList);
        this.context = context;
        this.myItems = attendanceList;
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.my_item_list,null,true);
        TextView titleTxt = (TextView) listViewItem.findViewById(R.id.myItemNameTxtView);
        TextView descriptionTxt = (TextView) listViewItem.findViewById(R.id.myQuantityTxtView);
        MyItem myItem = myItems.get(position);
        if (myItem.getName() != null) {
            titleTxt.setText(myItem.getName());
            descriptionTxt.setText(Integer.toString(myItem.getQuantity()));
        }
        return listViewItem;
    }
}
