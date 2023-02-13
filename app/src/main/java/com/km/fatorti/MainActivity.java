package com.km.fatorti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.km.fatorti.interfaces.BillService;
import com.km.fatorti.interfaces.impl.BillServiceImplementation;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button payBills;
    private Button viewBills;
    private Button contactUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);




        payBills = findViewById(R.id.payBills);
        viewBills = findViewById(R.id.viewBills);
        contactUs = findViewById(R.id.contactUs);


        payBills.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this,"Coming Soon!", Toast.LENGTH_SHORT).show();
        });

        viewBills.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ViewBillActivity.class);
            startActivity(intent);
        });

        contactUs.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(intent);
        });
    }
}