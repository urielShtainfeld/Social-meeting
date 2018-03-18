package com.example.ushtinfeld.socialapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import controller.FirebaseDba;
import model.Item;

public class MyItemListView extends AppCompatActivity {

    static class ViewHolder {
        CheckBox checkBox;
        TextView nameTxt;
        EditText qtytext;
    }

    public class ItemsListAdapter extends ArrayAdapter<Item> {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            super(c,R.layout.activity_my_item_list_view,l);
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Item getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).isSelected();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.take_item_list, null);
                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                viewHolder.nameTxt = (TextView) rowView.findViewById(R.id.rowItemName);
                viewHolder.qtytext = (EditText) rowView.findViewById(R.id.rowItemQty);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.nameTxt.setText(list.get(position).getName());
            viewHolder.checkBox.setChecked(list.get(position).isSelected());

            String qtyStr = Integer.toString(list.get(position).getRemainingQuantity());
            viewHolder.qtytext.setText(qtyStr);
            viewHolder.checkBox.setTag(position);
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isSelected();
                    list.get(position).setSelected(newState);

                }
            });
            viewHolder.qtytext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0){
                        list.get(position).setSelectedQty(Integer.parseInt(s.toString()));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
    }

    Button btnSaveMyItems;
    List<Item> items;
    ListView listView;
    ItemsListAdapter myItemsListAdapter;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item_list_view);
        listView = (ListView) findViewById(R.id.listViewMyItems);
        btnSaveMyItems = (Button) findViewById(R.id.AddItemsToMe);
        Intent intent = getIntent();
        id = intent.getStringExtra(MeetingListView.MEETID);
        items = FirebaseDba.getInstance().GetItems(id);
        if(items != null) {
            myItemsListAdapter = new ItemsListAdapter(this, items);
        }else {
            items = new ArrayList<>();
            myItemsListAdapter = new ItemsListAdapter(this, items);
        }
        listView.setAdapter(myItemsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        btnSaveMyItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMyItems();
            }
        });
    }
     public void addMyItems(){
         for (Item item:items) {
             if (item.isSelected()){
                 FirebaseDba.getInstance().saveMyItems(id,item);
                 item.setRemainingQuantity(item.getRemainingQuantity()-item.getSelectedQty());
                 item.setSelected(false);
                 FirebaseDba.getInstance().saveItemChanges(id,item);
                 Toast.makeText(this,"Meeting added",Toast.LENGTH_LONG).show();
             }
         }
     }
}

