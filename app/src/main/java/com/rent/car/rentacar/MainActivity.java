package com.rent.car.rentacar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    TextView vehicle, call, vehiclenum, packnum, servicenum;
    ListView listView;
    GifImageView loader;
    private String JSON_STRING;
    String name, price, ad;
    private SliderLayout imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageSlider = (SliderLayout)findViewById(R.id.slider);

        getSlider();
        getList();
        getCars();

        final Animation a = AnimationUtils.loadAnimation(this, R.anim.left);
        final Animation b = AnimationUtils.loadAnimation(this, R.anim.right);
        final ImageView logo = (ImageView)findViewById(R.id.logo);
        TextView wheel1= (TextView)findViewById(R.id.wheel1);
        wheel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SylhetOutside.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left, R.anim.leftout);
                //logo.setAnimation(a);
            }
        });

        TextView wheel2= (TextView)findViewById(R.id.wheel2);
        wheel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), HotelBooking.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right, R.anim.rightout);
                //logo.setAnimation(b);
            }
        });

        packnum = (TextView)findViewById(R.id.packnum);
        vehiclenum = (TextView)findViewById(R.id.carnum);

        ImageView menu = (ImageView)findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.menu_main);
                popup.show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.about:
                // do your code
                Intent intent = new Intent(getBaseContext(), AboutUS.class);
                startActivity(intent);
                return true;
            case R.id.use:
                // do your code
                Intent intent1 = new Intent(getBaseContext(), TermsOfUse.class);
                startActivity(intent1);
                return true;
            case R.id.contact:
                // do your code
                Intent intent2 = new Intent(getBaseContext(), ContactUS.class);
                startActivity(intent2);
                return true;
            default:
                return false;
        }
    }

    private void getList(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showpack();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("http://bhromondata.banijyadarpan.com/rentacar/placeinfo.php");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showpack(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("priceinfo");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String name = jo.getString("name");
                String price = jo.getString("price");
                byte[] b1 = name.getBytes("UTF-8");
                String carname = new String(b1, "UTF-8");
                byte[] b2 = price.getBytes("UTF-8");
                String pricename = new String(b2, "UTF-8");

                HashMap<String,String> employees = new HashMap<>();
                employees.put("id",id);
                employees.put("name",carname);
                employees.put("price",pricename);
                list.add(employees);
            }

            int size = list.size();
            String s = String.valueOf(size);
            s = s.replace("0", "০").replace("1", "১").replace("2", "২").replace("3", "৩").replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭").replace("8", "৮").replace("9", "৯");
            packnum.setText(size+"("+s+")");

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getCars(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showCar();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("http://bhromondata.banijyadarpan.com/rentacar/carlist.php");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showCar(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("carlist");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String name = jo.getString("name");
                byte[] b1 = name.getBytes("UTF-8");
                String carname = new String(b1, "UTF-8");

                HashMap<String,String> employees = new HashMap<>();
                employees.put("id",id);
                employees.put("name",name);
                list.add(employees);
            }

            int size1 = list.size();
            String s1 = String.valueOf(size1);
            s1 = s1.replace("0", "০").replace("1", "১").replace("2", "২").replace("3", "৩").replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭").replace("8", "৮").replace("9", "৯");
            vehiclenum.setText(size1+"("+s1+")");

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getSlider(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loader.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showSlider();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("http://bhromondata.banijyadarpan.com/rentacar/slider.php");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showSlider(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("image");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                //String id = jo.getString("id");
                String title = jo.getString("title");
                if (title.contains("%20")) {
                    title = title.replace("%20", " ");
                }
                String url = jo.getString("url");
                byte[] b1 = new byte[0];
                try {
                    b1 = url.getBytes("UTF-8");
                    ad = new String(b1, "UTF-8");
                    //Toast.makeText(this, ad, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                HashMap<String,String> url_maps = new HashMap<>();
                //url_maps.put("id",id);
                url_maps.put(title,ad);
                //url_maps.put("url",url);
                for(String name : url_maps.keySet()){
                    TextSliderView textSliderView = new TextSliderView(this);
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(url_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("extra",name);

                    imageSlider.addSlider(textSliderView);
                }
                //imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                //imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                //imageSlider.setCustomAnimation(new DescriptionAnimation());
                imageSlider.setDuration(5000);
                imageSlider.addOnPageChangeListener(this);
                imageSlider.getPagerIndicator().setVisibility(View.INVISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        //make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        imageSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.e("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
