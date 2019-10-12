package com.rent.car.rentacar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class ContactUS extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        TextView textView = (TextView)findViewById(R.id.text);
        textView.setText(Html.fromHtml("<font color=#000000><b>যোগাযোগ করুনঃ</b></font><br><br><p>গাড়ীর জন্যে,<br>ফোন করুনঃ ০১৯৮০৬৩০৫৭৪<br>" +
                "অথবা ইমেইল করুনঃ mail.bhromon@gmail.com<br><br>" +
                "যেকোন অসুবিধা বা অভিযোগের জন্যে ইমেইল করুনঃ obhijogbhromon@gmail.com<br><br><br><font color=#000000><b>Contact us:</b></font><br><br>For booking,<br>call : 01980630574<br>" +
                "or email : mail.bhromon@gmail.com<br><br>" +
                "For reporting anything unpleasant, email us at obhijogbhromon@gmail.com</p>"));
    }
}
