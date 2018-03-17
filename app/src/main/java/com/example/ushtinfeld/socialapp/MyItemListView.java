package com.example.ushtinfeld.socialapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

import controller.FirebaseDba;
import model.Item;

public class MyItemListView extends AppCompatActivity {

    static class ViewHolder {
        CheckBox checkBox;
        TextView nameTxt;
        EditText qtytext;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
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

            String qtyStr = Integer.toString(list.get(position).getQuantity());
            viewHolder.qtytext.setText(qtyStr);
            viewHolder.checkBox.setTag(position);

            /*
            viewHolder.checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(position).checked = b;

                    Toast.makeText(getApplicationContext(),
                            itemStr + "onCheckedChanged\nchecked: " + b,
                            Toast.LENGTH_LONG).show();
                }
            });
            */

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isSelected();
                    list.get(position).setSelected(newState);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listViewMyItems);
        btnSaveMyItems = (Button) findViewById(R.id.AddItemsToMe);
        Intent intent = getIntent();
        String id = intent.getStringExtra(MeetingListView.MEETID);
        items = FirebaseDba.getInstance().GetItems(id);

        myItemsListAdapter = new ItemsListAdapter(this, items);
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
                for (Item item:items) {
                    if (item.isSelected()){
                        //todo:add logic to save my item
                    }
                }
              /*  for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {

                    }
                }*/

            }
        });
    }

}

