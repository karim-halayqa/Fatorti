package com.km.fatorti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.km.fatorti.interfaces.InvoiceService;
import com.km.fatorti.interfaces.impl.BillServiceImplementation;
import com.km.fatorti.interfaces.impl.InvoiceServiceDA;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Invoice;
import com.km.fatorti.model.User;
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
        setContentView(R.layout.activity_pay);
        setUpViews();
        Intent intent = getIntent();
        if (intent != null) {
            String strObj = intent.getStringExtra("billObj");
            Gson gson = new Gson();
            bill = gson.fromJson(strObj, Bill.class);

            headingTextViewPay.setText("Pay Bill#" + bill.getId());

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

        if (isEmptyInputUI()) {


            resultCheckPay.setText("Error: Please fill the input!");
            return;
        }
        VisaPayment visaPayment = new VisaPayment(
                visaNumberEditText.getText().toString().trim(),
                Integer.parseInt(threeNumberEditText.getText().toString().trim()),
                holderNameEditText.getText().toString().trim()
        );


        if (isValidVisaCheck(visaPayment)) {
            if (bill.getPaid()) {

                resultCheckPay.setText(R.string.alreadyPaidBill);
                return;
            }


            updateBillStatus();
            Intent intentInvoice = new Intent(PayActivity.this, InvoiceActivity.class);


            Invoice invoice = new Invoice(bill.hashCode(), bill.getId()); // I am using the bill hashcode as an id for the invoice!!!

            saveInvoice(invoice);

            Gson gson = new Gson();
            String invoiceObjectStringJson = gson.toJson(invoice);
            String billObjectStrJson = gson.toJson(bill);

            intentInvoice.putExtra("invoiceObj", invoiceObjectStringJson);
            intentInvoice.putExtra("billObj",billObjectStrJson);

            resultCheckPay.setText("Paid!");
            startActivity(intentInvoice);
            finish();


        } else {
            resultCheckPay.setText(R.string.errorVisa);
        }

    }

    private void updateBillStatus() {
        bill.setPaid(true);
        bill.setDateOfPayment(new Date());

        Intent intent = getIntent();
        String userJson = intent.getStringExtra("user");
        Gson gson = new Gson();
        User user = gson.fromJson(userJson,User.class);

        BillServiceImplementation billServiceDA = new BillServiceImplementation(user);

        billServiceDA.updateAndSetPaidWithDate(bill);

    }

    private void showErrorRedInput() {
        visaNumberEditText.setTextColor(Color.RED);
        threeNumberEditText.setTextColor(Color.RED);
        holderNameEditText.setTextColor(Color.RED);

    }
    private void showNormalBlackInput() {
        visaNumberEditText.setTextColor(Color.BLACK);
        threeNumberEditText.setTextColor(Color.BLACK);
        holderNameEditText.setTextColor(Color.BLACK);
    }

    private boolean isValidVisaCheck(VisaPayment visaPayment) {



        if (!visaPayment.isValidFullName()){
            holderNameEditText.setTextColor(Color.RED);
            return false;
        }
        if (!visaPayment.isValidThreeNums()){
            threeNumberEditText.setTextColor(Color.RED);
            return false;
        }

        if (!visaPayment.isValidVisaNumber()){
            visaNumberEditText.setTextColor(Color.RED);
            return false;
        }

        showNormalBlackInput();


        return true;
    }
    private boolean isEmptyInputUI(){

        if (holderNameEditText.getText().toString().trim().equals("")||
                visaNumberEditText.getText().toString().trim().equals("")||
                threeNumberEditText.getText().toString().trim().equals("")){
            showNormalBlackInput();
            return true;
        }
        return false;
    }
    private void saveInvoice(Invoice invoice) {

        InvoiceService invoiceService = new InvoiceServiceDA();
        invoiceService.save(invoice);
    }
}