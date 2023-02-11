package com.km.fatorti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Company;
import com.km.fatorti.model.Invoice;
import com.km.fatorti.model.User;

import java.util.Date;


/**
 * @author Aws Ayyash
 */

public class BillDetails extends AppCompatActivity {


    private Button payButton;
    private TextView detailsText;
    private Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);


        Intent intent = getIntent();
        if (intent != null) {
            String strObj = intent.getStringExtra("billObj");
            Gson gson = new Gson();
            bill = gson.fromJson(strObj, Bill.class);
        } else
            // dummy bill, i should get it from the intent!
            bill = new Bill(1, new Date(122, 7, 1), null, Company.ELECTRICITY, 50, false,
                    new User("admin", "admin", "admin@exp.com", "admin", "123456789"));

        setTitle("Bill '#" + bill.getId() + "' Details");

        setUpViews();

        putDetailsData(bill);
        if (bill.getPaid())
            payButton.setVisibility(View.GONE);
        else
            payButton.setVisibility(View.VISIBLE);

       /* payButton.setOnClickListener(view -> {

            // go to payment page, using intents,
        });*/

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // i should pay it here, then show him the invoice.
                //int billId = bill.getId();
                //Invoice invoice = new Invoice(bill.hashCode(),billId); // I am using the bill hashcode as an id for the invoice!!!

                Intent intentPay  = new Intent(BillDetails.this, PayActivity.class);

                Gson gson = new Gson();
                String billObjectStringJson = gson.toJson(bill);
                intentPay.putExtra("billObj", billObjectStringJson);
                startActivity(intentPay);
                finish();
            }
        });
    }


    private void putDetailsData(Bill bill) {

        String detailsTxt = bill.formatDetails();//formatDetails(bill);
        detailsText.setText(detailsTxt);
    }

    private void setUpViews() {
        payButton = findViewById(R.id.payButton);
        detailsText = findViewById(R.id.textViewDetails);
    }

    public String formatDetails(@NonNull Bill bill) {

        String details = "Bill serial# = " + bill.getId() + ",\nDate Of Issue = " + Bill.dateFormat.format(bill.getDateOfIssue());
        String paidStr = "";

        if (bill.getPaid() && bill.getDateOfPayment() != null) // it should be: isPaid(); !!!
            paidStr = ",\nDate Of Payment = " + Bill.dateFormat.format(bill.getDateOfPayment());
        else
            paidStr = ",\nDate Of Payment = NOT PAID!";

        details += paidStr;

        details += ",\nCompany = " + bill.getCompany().toString() + ",\nValue = " + bill.getValue();

        return details;
    }
}