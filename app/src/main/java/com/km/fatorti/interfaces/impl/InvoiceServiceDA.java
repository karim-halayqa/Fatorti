package com.km.fatorti.interfaces.impl;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.km.fatorti.interfaces.InvoiceService;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Invoice;

import java.util.ArrayList;
import java.util.List;


/**
 * used to store Invoices information
 *
 * @author Aws Ayyash
 */
public class InvoiceServiceDA implements InvoiceService {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String collectionName = "Invoice";

    public InvoiceServiceDA() {

    }


    @Override
    public List<Invoice> getAll() {
        List<Invoice> invoiceList = new ArrayList<>();
        db.collection(collectionName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Invoice invoice = documentSnapshot.toObject(Invoice.class);
                            invoiceList.add(invoice);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("getAllInvoices", "Error getting documents: ", e);

                    }
                });
        return invoiceList;
    }

    @Override
    public void save(Invoice invoice) {

        db.collection(collectionName)
                .add(invoice).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();

                        db.collection(collectionName).document(id).update("documentId", id);
                        invoice.setDocumentId(id);

                    }
                });


    }

    @Override
    public void saveAll(List<Invoice> invoices) {

        for (Invoice invoice : invoices) {
            db.collection(collectionName)
                    .add(invoice)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String id = documentReference.getId();

                            db.collection(collectionName).document(id).update("documentId", id);
                            invoice.setDocumentId(id);
                        }
                    });
        }


    }
}
