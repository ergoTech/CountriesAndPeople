package com.CountriesAndPeople.ergo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Info extends Activity {

    ImageView infoImage;
    TextView infoCountry, infoArea, infoPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        infoImage = (ImageView) findViewById(R.id.info_image);
        infoCountry = (TextView) findViewById(R.id.info_text);
        infoArea = (TextView) findViewById(R.id.info_area);
        infoPop = (TextView) findViewById(R.id.info_pop);

        Intent intent = getIntent();

        final String country = intent.getStringExtra("country");
        infoCountry.setText("Country: " + country);

        String area = intent.getStringExtra("area");
        infoArea.setText("Area: " + area + " km2");

        String pop = intent.getStringExtra("pop");
        infoPop.setText("Population: " + pop + " млн");

        infoImage.setImageResource(iconManager(country));

        infoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://ru.wikipedia.org/wiki/"+ country));
                startActivity(intent);
            }
        });
    }

    public int iconManager(String icon) {
        String iconLower = icon.replaceAll(" ", "_").toLowerCase();
        iconLower += "_full";
        int result = getResources().getIdentifier(iconLower , "drawable", getPackageName());

        return result;
    }

}
