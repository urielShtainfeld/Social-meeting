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

import model.Item;

/**
 * Created by ushtinfeld on 17/03/2018.
 */

public class ItemsList extends ArrayAdapter<Item> {
    private Activity context;
    private List<Item> attendanceList;

    public ItemsList(Activity context,List<Item> attendanceList){
        super(context,R.layout.item_list,attendanceList);
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.item_list,null,true);
        TextView titleTxt = (TextView) listViewItem.findViewById(R.id.itemNameTxtView);
        TextView descriptionTxt = (TextView) listViewItem.findViewById(R.id.QuantityTxtView);
        Item attendance = attendanceList.get(position);
        if (attendance.getName() != null) {
            titleTxt.setText(attendance.getName());
            descriptionTxt.setText(Integer.toString(attendance.getQuantity()));
        }
        return listViewItem;
    }
}
