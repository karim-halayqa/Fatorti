package com.km.fatorti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.km.fatorti.interfaces.InvoiceService;
import com.km.fatorti.interfaces.impl.BillServiceImplementation;
import com.km.fatorti.interfaces.impl.InvoiceServiceDA;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Invoice;

import org.w3c.dom.Text;

/**
 * used to show and store the invoice after the payment
 *
 * @author Aws Ayyash
 */
public class InvoiceActivity extends AppCompatActivity {

    private Bill bill;
    private TextView textViewResultInvoice;
    private TextView headingTextViewInvoice;
    private Invoice invoice;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        setUpViews();

        // setting up the invoice object (getting it from the intent)
        if (catchIntent()) {
            putDetailsData();


        }else {
            finish();
        }


    }


    private boolean catchIntent() {

        intent = getIntent();
        if (intent != null) {


            String strInvoiceObj = intent.getStringExtra("invoiceObj");
            String billObjectStrJson = intent.getStringExtra("billObj");
            Gson gson = new Gson();
            invoice = gson.fromJson(strInvoiceObj, Invoice.class);

            bill = gson.fromJson(billObjectStrJson,Bill.class);//getBill(invoice.getBillId());
            return true;
        } else {
            return false;
        }


    }

   /* private Bill getBill(int billId) {

        BillServiceImplementation billServiceImplementation = new BillServiceImplementation();
        return billServiceImplementation.getBillByID(billId);
    }*/

    private void setUpViews() {
        textViewResultInvoice = findViewById(R.id.textViewResultInvoice);
        headingTextViewInvoice = findViewById(R.id.headingTextViewInvoice);
    }

    private void putDetailsData() {

        headingTextViewInvoice.setText("Welcome to Invoice#" + invoice.getId());

        String detailsTxt = bill.formatDetails();//formatDetails(bill);
        textViewResultInvoice.setText(detailsTxt);
    }
}