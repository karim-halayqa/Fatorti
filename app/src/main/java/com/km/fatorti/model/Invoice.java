package com.km.fatorti.model;

import com.km.fatorti.interfaces.impl.BillServiceImplementation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * used to store Invoices information
 * @author Aws Ayyash
 */
public class Invoice {

    private int id;
    private int billId;
    //private Date dateOfPayment;

    public Invoice(int id, int billId) {
        this.id = id;
        this.billId = billId;
        // Bill.dateFormat.format();


    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    private String documentId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    // public static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


}
