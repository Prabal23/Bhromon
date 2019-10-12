package com.rent.car.rentacar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Pranto on 30/4/2018.
 */

public class PackageDetails extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_details);

        //Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        //TextView taka = (TextView)findViewById(R.id.taka);

        final String id = getIntent().getStringExtra("id");
        final String name = getIntent().getStringExtra("name");
        final String price = getIntent().getStringExtra("price");

        TextView packagename = (TextView)findViewById(R.id.name);
        packagename.setText(name);

        TextView packageprice = (TextView)findViewById(R.id.price);
        packageprice.setText(price);

        LinearLayout rent = (LinearLayout) findViewById(R.id.info);
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SylhetOutside.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });
    }
}
