package com.CountriesAndPeople.ergo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    Button bMainAdd, bChange, bDell, bPeople, bColor;
    EditText dialog_country, dialog_area, dialog_population, editsearch;
    static ListView list;

    public static boolean people = false;
    public static String colorchange = "#000000";
    private static List<Countries> myCountries;
    ArrayAdapter<Countries> adapter;
    DataBase db;
    private static final long DOUBLE_PRESS_INTERVAL = 500; // in millis
    private long lastPressTime;
    boolean mHasDoubleClicked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bMainAdd = (Button) findViewById(R.id.bMainAdd);
        bChange = (Button) findViewById(R.id.bChange);
        bDell = (Button) findViewById(R.id.bDell);
        bPeople = (Button) findViewById(R.id.bPeople);
        bColor = (Button) findViewById(R.id.bColor);
        dialog_country = (EditText) findViewById(R.id.dialog_country);
        dialog_area = (EditText) findViewById(R.id.dialog_area);
        dialog_population = (EditText)findViewById(R.id.dialog_popular);
        editsearch = (EditText) findViewById(R.id.editsearch);
        db = new DataBase(this);
        list = (ListView) findViewById(R.id.listview);
        myCountries = new ArrayList<Countries>();
        adapter = new MyListAdapter();

        list.setAdapter(adapter);

        displayAll();
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setOnItemLongClickListener(this);
        list.setOnItemClickListener(this);
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bMainAdd.setVisibility(View.VISIBLE);
        bDell.setVisibility(View.VISIBLE);
    }

    public void onClickbAdd(View v) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText country_dialog = new EditText(this);
        country_dialog .setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        country_dialog.setHint("Name of Country");

        final EditText area_dialog = new EditText(this);
        area_dialog.setInputType(InputType.TYPE_CLASS_NUMBER);
        area_dialog.setHint("Area in km2");

        final EditText pop_dialog = new EditText(this);
        pop_dialog.setInputType(InputType.TYPE_CLASS_NUMBER);
        pop_dialog.setHint("Population in mln");

        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);
        view.addView(country_dialog);
        view.addView(area_dialog);
        view.addView(pop_dialog);
        alert.setView(view);
        alert.setPositiveButton("Add Country", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                DataBase db;
                db = new DataBase(MyActivity.this);
                try {
                    db.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                db.createEntry(country_dialog.getText().toString(), area_dialog.getText().toString(),
                        pop_dialog.getText().toString());
                displayOne();
                db.close();
            }
        });
        alert.show();

    }

    public void onClickbDell (View v) {
        DataBase db = new DataBase(MyActivity.this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for ( int i = myCountries.size() - 1 ;  i >= 0  ; i-- ) {
            boolean mark = myCountries.get(i).getMark();
            if ( mark ) {
                Countries clicked = myCountries.get(i);
                String country = clicked.getMake();
                adapter.remove(clicked);
                db.deleteRow(country);
                adapter.notifyDataSetChanged();
            }
        }


    }

    public void onClickbChange (View v) {
        for ( int i = myCountries.size() - 1 ;  i >= 0  ; i-- ) {
            Countries clicked = myCountries.get(i);
            boolean mark = clicked.getMark();
            if ( mark ) {
                clicked.mark = false;
                clicked.color = colorchange;
            }
        }
          adapter.notifyDataSetChanged();
    }

    public void onClickbColor (View v) {
        Intent intent = new Intent("android.intent.action.CHANGECOLOR");
        startActivity(intent);
    }

    public void onClickbPeople (View v){
        Intent intent = new Intent(this, People.class);
        startActivity(intent);
        finish();
    }

    public void displayAll() {
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        Cursor c = db.getData();

        if (c.moveToFirst()) {
            do {
                String country = c.getString(1);
                String area = c.getString(2);
                String pop = c.getString(3);

                if ( !people ) {
                    myCountries.add(new Countries(country, area, pop, iconManager(country), false, "#000000"));
                } else {
                         for (int i = 0; i < People.myPeople.size(); i++ ){
                         if (People.myPeople.get(i).mark &&
                                People.myPeople.get(i).getCountry().toLowerCase().startsWith(country.toLowerCase()) ) {
                                    myCountries.add(new Countries(country, area, pop, iconManager(country), false, "#000000"));

                        }
                    }

                }
            } while (c.moveToNext());
        }
        people = false;
        db.close();
    }

    public int iconManager(String icon) {
        String iconLower = icon.replaceAll(" ", "_").toLowerCase();
        iconLower += "_icon";
        return getResources().getIdentifier(iconLower , "drawable", getPackageName());
    }

    private void displayOne () {

        adapter.notifyDataSetChanged();
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor c = db.getData();
        c.moveToLast();

        String country = c.getString(1);
        String area = c.getString(2);
        String pop = c.getString(3);

        myCountries.add(new Countries(country, area, pop, iconManager(country), false, "#000000"));

        db.close();
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Are you sure?");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Countries clickedCountry = myCountries.get(position);
                String country = clickedCountry.getMake();

                adapter.remove(clickedCountry);
                adapter.notifyDataSetChanged();

                DataBase db = new DataBase(MyActivity.this);
                try {
                    db.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                db.deleteRow(country);

                String message = "You deleted " + country;
                Toast.makeText(MyActivity.this, message, Toast.LENGTH_LONG).show();

                db.close();
            }
        });
        alert.show();

        return true;
    }

    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        findDoubleClick(position);
        if (mHasDoubleClicked) {
            //Double click logic

            Countries clickedCountry = myCountries.get(position);
            String country = clickedCountry.getMake();

            if ( !clickedCountry.getMark() ) {
                String message = "You choise: " + country;
                Toast.makeText(MyActivity.this, message, Toast.LENGTH_LONG).show();
                clickedCountry.mark = true;

                adapter.notifyDataSetChanged();
            } else {
                clickedCountry.mark = false;
                adapter.notifyDataSetChanged();
            }
        }
    }

    private boolean findDoubleClick(final int position) {
        long pressTime = System.currentTimeMillis();
        if ( pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
        mHasDoubleClicked = true;
        } else {
        mHasDoubleClicked = false;
        Handler myHandler = new Handler() {
    public void handleMessage(Message m) {
          if (!mHasDoubleClicked) {
               Intent intent = new Intent("android.intent.action.INFO");
               Countries clickedCountry = myCountries.get(position);
               String country = clickedCountry.getMake();
               String area = clickedCountry.getArea();
               String pop = clickedCountry.getPopulation();
               intent.putExtra("country", country);
               intent.putExtra("area", area);
               intent.putExtra("pop", pop);
               startActivity(intent);
            }
        }

        };
        Message m = new Message();
        myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
        }
        lastPressTime = pressTime;

     return mHasDoubleClicked;
    }

    private class MyListAdapter extends ArrayAdapter<Countries> implements Filterable  {

        List<Countries> filteredCountry;
        Filter filter;
        public MyListAdapter() {
            super(MyActivity.this, R.layout.itemview, myCountries);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.itemview, parent, false);
            }

            Countries currentCountry = myCountries.get(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.itemIcon);
            imageView.setImageResource(currentCountry.getIconID());

            TextView makeText = (TextView) itemView.findViewById(R.id.textCountry);
            makeText.setText(currentCountry.getMake());

            TextView areaText = (TextView) itemView.findViewById(R.id.textArea);
            areaText.setText("" + currentCountry.getArea() + "km2");

            TextView popularText = (TextView) itemView.findViewById(R.id.textPopular);
            popularText.setText("pop. " + currentCountry.getPopulation() + "млн");

            itemView.setBackgroundColor(Color.parseColor(currentCountry.getColor()));

            ImageView imageBird = (ImageView) itemView.findViewById(R.id.itemBird);
            if ( !currentCountry.getMark() ) {
                imageBird.setVisibility(View.INVISIBLE);
            } else {
                imageBird.setVisibility(View.VISIBLE);
            }

            return itemView;
        }

        @Override
        public Filter getFilter() {
            final List<Countries> reserved = new ArrayList<Countries>();
            reserved.addAll(myCountries);
            if ( filter == null ){
                filter = new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();
                    filteredCountry = new ArrayList<Countries>();

                      if( constraint != null ) {
                          myCountries.clear();
                          myCountries.addAll(reserved);
                      //    displayAll();
                          for( Countries filterCountry : myCountries ) {
                                if ( filterCountry.getMake().toLowerCase().startsWith(constraint.toString().toLowerCase() )) {
                                    filteredCountry.add(filterCountry);
                            }
                        }
                        filterResults.values = filteredCountry;
                        filterResults.count = filteredCountry.size();
                    } else {
                        synchronized(this)
                        {
                            filterResults.values = myCountries;
                            filterResults.count = myCountries.size();
                        }
                    }

                    return filterResults;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence contraint, FilterResults results) {


                    if ( results != null ) {
                        myCountries.clear();
                        myCountries.addAll((List<Countries>) results.values);
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }

                }
            };
            }
            return filter;
        }
    }
}
