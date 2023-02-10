package com.km.fatorti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.VisaPayment;

import java.util.Date;

/**
 * used to get the payment details and, check the payment, then load the invoice;
 *
 * @author Aws Ayyash
 */
public class PayActivity extends AppCompatActivity {

    private Bill bill;

    private Button pay;
    private EditText visaNumberEditText;
    private EditText threeNumberEditText;
    private EditText holderNameEditText;
    private TextView resultCheckPay;
    private TextView headingTextViewPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        if (intent != null) {
            String strObj = intent.getStringExtra("billObj");
            Gson gson = new Gson();
            bill = gson.fromJson(strObj, Bill.class);

            headingTextViewPay.setText(headingTextViewPay.getText() + " Bill#" + bill.getId());

        }

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPay();
            }
        });


    }

    private void setUpViews() {

        pay = findViewById(R.id.buttonPayActivity);
        visaNumberEditText = findViewById(R.id.editTextVisaNumber);
        threeNumberEditText = findViewById(R.id.editTextThreeNumber);
        holderNameEditText = findViewById(R.id.editTextFullName);
        resultCheckPay = findViewById(R.id.textViewCheckPay);
        headingTextViewPay = findViewById(R.id.headingTextViewPay);
    }

    private void checkPay() {

        VisaPayment visaPayment = new VisaPayment(
                visaNumberEditText.getText().toString().trim(),
                Integer.parseInt(threeNumberEditText.getText().toString().trim()),
                holderNameEditText.getText().toString().trim()
        );

        if (visaPayment.isValidVisa()) {
            if (bill.getPaid()) {

                resultCheckPay.setText(R.string.alreadyPaidBill);
                return;
            }
            bill.setPaid(true);
            bill.setDateOfPayment(new Date());

            Intent intentInvoice = new Intent(PayActivity.this, InvoiceActivity.class);
            //intentInvoice.putExtra("billId", bill.getId());

            Gson gson = new Gson();
            String billObjectStringJson = gson.toJson(bill);
            intentInvoice.putExtra("billObj", billObjectStringJson);
           // intentInvoice.putExtra("invoiceId",bill.hashCode());
            startActivity(intentInvoice);


        } else {
            resultCheckPay.setText(R.string.errorVisa);
        }

    }
}