package com.rent.car.rentacar;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class PackageList extends AppCompatActivity{
    TextView vehicle, call, packnum, packnumbd;
    ListView listView;
    GifImageView loader;
    private String JSON_STRING;
    String id, name, price;
    Dialog dialog;
    ImageView calling;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_list);

        packnum = (TextView)findViewById(R.id.packnum);
        packnumbd = (TextView)findViewById(R.id.packnumbd);
        listView = (ListView) findViewById(R.id.listView);
        loader = (GifImageView) findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        getList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right, R.anim.rightout);
    }

    private void getList(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loader.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loader.setVisibility(View.INVISIBLE);
                JSON_STRING = s;
                showCar();
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

    private void showCar(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("priceinfo");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String name = jo.getString("name");
                if(name.contains("%20")){
                    name = name.replace("%20", " ");
                }
                String price = jo.getString("price");
                if(price.contains("%20")){
                    price = price.replace("%20", " ");
                }
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
            packnum.setText(size+" Packages");
            packnumbd.setText(" ("+s+"টি প্যাকেজ)");

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                PackageList.this, list, R.layout.package_list_item,
                new String[]{"id","name"},
                new int[]{R.id.id, R.id.txtName});

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> map =(HashMap)adapterView.getItemAtPosition(i);
                id = map.get("id").toString();
                name = map.get("name").toString();
                price = map.get("price").toString();
                Intent intent = new Intent(getBaseContext(), PackageDetails.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });
    }
}
