package com.km.fatorti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Company;
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

        // dummy bill, i should get it from the intent!
        bill = new Bill(1, new Date(122, 7, 1),null, Company.ELECTRICITY,50,false,
                new User("admin","admin","admin@exp.com","admin","123456789"));

        setTitle("Bill '#" + bill.getId() + "' Details");

        setUpViews();

        putDetailsData(bill);
        if (bill.getPaid())
            payButton.setVisibility(View.GONE);
        else
            payButton.setVisibility(View.VISIBLE);

        payButton.setOnClickListener(view -> {

            // go to payment page, using intents,
        });
    }

    private void putDetailsData(Bill bill) {

        String detailsTxt = formatDetails(bill);
        detailsText.setText(detailsTxt);
    }

    private void setUpViews() {
        payButton = findViewById(R.id.payButton);
        detailsText = findViewById(R.id.textViewDetails);
    }

    public String formatDetails(Bill bill) {

        String details = "Bill serial# = " + bill.getId() + ",\nDate Of Issue = " + Bill.dateFormat.format(bill.getDateOfIssue());
        String paidStr = "";

        if (bill.getPaid()) // it should be: isPaid(); !!!
            paidStr = ",\nDate Of Payment = " + Bill.dateFormat.format(bill.getDateOfPayment());
        else
            paidStr = ",\nDate Of Payment = NOT PAID!";

        details += paidStr;

        details += ",\nCompany = " + bill.getCompany().toString() + ",\nValue = " + bill.getValue();

        return details;
    }
}