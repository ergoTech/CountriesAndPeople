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

public class People extends Activity implements AdapterView.OnItemClickListener {

    static List<ItemPeople> myPeople;
    ArrayAdapter<ItemPeople> adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people);
        list = (ListView) findViewById(R.id.listPeople);
        myPeople = new ArrayList<ItemPeople>();
        adapter = new MyListAdapter();
        list.setAdapter(adapter);
        displayAll();

        list.setOnItemClickListener(this);
    }

    private void displayAll() {
        myPeople.add(new ItemPeople("egor","canada", false));
        myPeople.add(new ItemPeople("vasya","brazil", false));
        myPeople.add(new ItemPeople("syava","ukraine", false));
        myPeople.add(new ItemPeople("caiman","china", false));
        myPeople.add(new ItemPeople("Lena","russia", false));
        myPeople.add(new ItemPeople("bendera","ukraine", false));
        myPeople.add(new ItemPeople("olol","chile", false));
        myPeople.add(new ItemPeople("shiz","chile", false));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemPeople currentPeople = myPeople.get(position);
        currentPeople.mark = true;
        MyActivity.people = true;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MyActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private class MyListAdapter extends ArrayAdapter<ItemPeople> {

        public MyListAdapter() {
            super(People.this, R.layout.itempeople, myPeople);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.itempeople, parent, false);
            }

            ItemPeople currentPeople = myPeople.get(position);

            TextView name = (TextView) itemView.findViewById(R.id.textName);
            name.setText(currentPeople.getName());

            TextView country = (TextView) itemView.findViewById(R.id.textCountry);
            country.setText(currentPeople.getCountry());

            if (currentPeople.mark) {
                itemView.setBackgroundColor(Color.DKGRAY);
            }

            return itemView;
        }

    }
}
