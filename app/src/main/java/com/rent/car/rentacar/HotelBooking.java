package com.rent.car.rentacar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Pranto on 30/4/2018.
 */

public class HotelBooking extends AppCompatActivity {
    TextView vehicle, call, packname, date, packages;
    GridView gridView, gridView1;
    GifImageView loader;
    private String JSON_STRING;
    String name = "", dd = "";
    Dialog dialog;
    ImageView calling;
    EditText names, add, place, email, phone;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_booking);

        /*final String id = getIntent().getStringExtra("id");
        final String name = getIntent().getStringExtra("name");
        final String price = getIntent().getStringExtra("price");*/

        packname = (TextView) findViewById(R.id.packages);
        //packname.setText(name);

        date = (TextView) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(HotelBooking.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                int d = monthOfYear + 1;
                                int day = dayOfMonth;
                                dd = year + "-" + d + "-" + day;
                                date.setText("বুকিং এর তারিখ - " + dd);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        calling = (ImageView) findViewById(R.id.callimg);
        call = (TextView) findViewById(R.id.call);
        calling.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:01980630574"));
                startActivity(callIntent);*/
                dialContactPhone("01980630574");
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                dialContactPhone("01980630574");
                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:01980630574"));
                *//*if (ActivityCompat.checkSelfPermission(SylhetOutside.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    startActivity(callIntent);
                    return;
                }*//*
                startActivity(callIntent);*/
            }
        });


        //call.setPaintFlags(call.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        vehicle = (TextView) findViewById(R.id.vehicle);
        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Roomlist attendance = new Roomlist();
                attendance.showdialog(HotelBooking.this, "");
            }
        });

        //packages = (TextView)findViewById(R.id.packages);
        packname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hotellist attendance = new Hotellist();
                attendance.showdialog(HotelBooking.this, "");
            }
        });

        names = (EditText) findViewById(R.id.name);
        add = (EditText) findViewById(R.id.add);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);

        LinearLayout email = (LinearLayout) findViewById(R.id.info);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right, R.anim.rightout);
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public class Hotellist {

        public void showdialog(AppCompatActivity activity, String title) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.hotel_list);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            gridView = (GridView) dialog.findViewById(R.id.gridView);
            loader = (GifImageView) dialog.findViewById(R.id.loader);
            loader.setVisibility(View.VISIBLE);
            getHotel();

            Button cancel = (Button) dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public class Roomlist {

        public void showdialog(AppCompatActivity activity, String title) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.room_list);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            gridView1 = (GridView) dialog.findViewById(R.id.gridView);
            loader = (GifImageView) dialog.findViewById(R.id.loader);
            loader.setVisibility(View.VISIBLE);
            getRoom();

            Button cancel = (Button) dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void getHotel() {
        class GetJSON extends AsyncTask<Void, Void, String> {

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
                showHotel();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("http://bhromondata.banijyadarpan.com/rentacar/hotellist.php");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showHotel() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("hotellist");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String name = jo.getString("name");
                if (name.contains("%20")) {
                    name = name.replace("%20", " ");
                }
                byte[] b1 = name.getBytes("UTF-8");
                String hotelname = new String(b1, "UTF-8");

                HashMap<String, String> employees = new HashMap<>();
                employees.put("id", id);
                employees.put("name", name);
                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                HotelBooking.this, list, R.layout.hotel_list_item,
                new String[]{"id", "name"},
                new int[]{R.id.id, R.id.txtName});

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap) adapterView.getItemAtPosition(i);
                name = map.get("name").toString();
                packname.setText(name);
                dialog.dismiss();
            }
        });
    }

    private void getRoom() {
        class GetJSON extends AsyncTask<Void, Void, String> {

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
                showRoom();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("http://bhromondata.banijyadarpan.com/rentacar/roomlist.php");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showRoom() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("roomlist");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String name = jo.getString("name");
                if (name.contains("%20")) {
                    name = name.replace("%20", " ");
                }
                byte[] b1 = name.getBytes("UTF-8");
                String roomname = new String(b1, "UTF-8");

                HashMap<String, String> employees = new HashMap<>();
                employees.put("id", id);
                employees.put("name", name);
                list.add(employees);
            }

            int size = list.size();
            String s = String.valueOf(size);
            s = s.replace("0", "০").replace("1", "১").replace("2", "২").replace("3", "৩").replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭").replace("8", "৮").replace("9", "৯");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                HotelBooking.this, list, R.layout.room_list_item,
                new String[]{"id", "name"},
                new int[]{R.id.id, R.id.txtName});

        gridView1.setAdapter(adapter);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap) adapterView.getItemAtPosition(i);
                String id = map.get("id").toString();
                name = map.get("name").toString();
                vehicle.setText(name);
                dialog.dismiss();
            }
        });
    }

    public void sendEmail() {
        String n = names.getText().toString();
        String a = add.getText().toString();
        String p = packname.getText().toString();
        String v = vehicle.getText().toString();
        String d = date.getText().toString();
        String e = email.getText().toString();
        String ph = phone.getText().toString();
        if (n.equals("")) {
            Toast.makeText(this, "Please type your name\nআপনার নাম লিখুন", Toast.LENGTH_SHORT).show();
        } else if (a.equals("")) {
            Toast.makeText(this, "Please type your address\nআপনার ঠিকানা লিখুন", Toast.LENGTH_SHORT).show();
        } else if (p.equals("")) {
            Toast.makeText(this, "Please choose your hotel\nআপনার হোটেল বাছাই করুন", Toast.LENGTH_SHORT).show();
        } else if (v.equals("")) {
            Toast.makeText(this, "Please choose your room type\nআপনার রুম টাইপ বাছাই করুন", Toast.LENGTH_SHORT).show();
        } else if (d.equals("")) {
            Toast.makeText(this, "Please choose your check in date\nআপনার চেক-ইন তারিখ বাছাই করুন", Toast.LENGTH_SHORT).show();
        } else if (ph.equals("")) {
            Toast.makeText(this, "Please type your phone number\nআপনার ফোন নাম্বারটি লিখুন", Toast.LENGTH_SHORT).show();
        } else {
            /*String[] mail = {"mail.bhromon@gmail.com"};
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, mail);
            email.putExtra(Intent.EXTRA_SUBJECT, "");
            email.putExtra(Intent.EXTRA_TEXT, n+"\n"+a+"\n"+p+"\n"+v+"\n"+e+"\n"+ph);
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "মাধ্যম বাছাই করুন..."));*/
            Intent testIntent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=" + "" + "&body=" + n + "\n" + a + "\n" + p + "\n" + v + "\n" + d + "\n" + e + "\n" + ph + "&to=" + "mail.bhromon@gmail.com");
            testIntent.setData(data);
            startActivity(testIntent);
        }
    }
}
