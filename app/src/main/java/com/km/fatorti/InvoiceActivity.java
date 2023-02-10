package com.km.fatorti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.km.fatorti.interfaces.InvoiceService;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        setUpViews();

        invoice = catchIntent();
        putDetailsData();
        saveInvoice(invoice);

    }

    private void saveInvoice(Invoice invoice) {

        InvoiceService invoiceService = new InvoiceServiceDA();
        invoiceService.save(invoice);
    }

    private Invoice catchIntent() {
        Intent intent = getIntent();

        String strBillObj = intent.getStringExtra("billObj");
        Gson gson = new Gson();
        bill = gson.fromJson(strBillObj, Bill.class);
        int invoiceIdBillHashcode = bill.hashCode();
        int billId = bill.getId();
        Invoice invoice = new Invoice(invoiceIdBillHashcode, billId); // I am using the bill hashcode as an id for the invoice!!!

        return invoice;
    }

    private void setUpViews() {
        textViewResultInvoice = findViewById(R.id.textViewResultInvoice);
        headingTextViewInvoice = findViewById(R.id.headingTextViewInvoice);
    }

    private void putDetailsData() {

        headingTextViewInvoice.setText(headingTextViewInvoice.getText().toString() + invoice.getId());

        String detailsTxt = bill.formatDetails();//formatDetails(bill);
        textViewResultInvoice.setText(detailsTxt);
    }
}