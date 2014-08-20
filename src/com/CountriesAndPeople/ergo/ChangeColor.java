package com.CountriesAndPeople.ergo;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class ChangeColor extends Activity implements AdapterView.OnItemClickListener{

    ListView list;
    private List<ColorItem> myColor;
    ArrayAdapter<ColorItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changecolor);
        list = (ListView) findViewById(R.id.listColor);

        myColor = new ArrayList<ColorItem>();
        adapter = new MyListAdapter();

        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        addAll();
    }

    private void addAll() {
        myColor.add(new ColorItem("#FF0000"));
        myColor.add(new ColorItem("#000000"));
        myColor.add(new ColorItem("#FFA500"));
        myColor.add(new ColorItem("#008000"));
        myColor.add(new ColorItem("#C0C0C0"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyActivity.colorchange = adapter.getItem(position).getMake();
        finish();
    }


    private class MyListAdapter extends ArrayAdapter<ColorItem> {
        public MyListAdapter() {
            super(ChangeColor.this, R.layout.itemviewcolor, myColor);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.itemviewcolor, parent, false);
            }

            ColorItem currentColor = myColor.get(position);

            TextView makeText = (TextView) itemView.findViewById(R.id.textColor);
            makeText.setText(currentColor.getMake());

            itemView.setBackgroundColor(Color.parseColor(currentColor.getMake()));

            return itemView;
        }


    }
}
