package com.km.fatorti.interfaces.impl;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.km.fatorti.interfaces.BillService;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Company;
import com.km.fatorti.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * used to fetch bill records, initially used to filter dummy bill records by their paid status
 *
 * @author Karim Halayqa
 */
public class BillServiceImplementation implements BillService {

    FirebaseFirestore db;
    final String collectionName = "Bill";
    public List<Bill> bills = new ArrayList<>();

    /**
     * adds dummy data to the dummyBill list
     */
    public BillServiceImplementation() {
        db = FirebaseFirestore.getInstance();
        findAll();
    }

    @Override
    public void findAll() {

        db.collection("Bill")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            bills.clear();
                            bills.addAll(documentSnapshots.toObjects(Bill.class));

                            Log.d(TAG, "onSuccess: " + bills);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: FAILED");
                    }
                });
        Log.d(TAG, "bill list size is: " + bills.size());
//        Thread.sleep(10000);
    }

    @Override
    public void save(Bill bill) {
        db.collection(collectionName).add(bill).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();

                db.collection(collectionName).document(id).update("documentId", id);
                bill.setDocumentId(id);
            }
        });
    }
    @Override
    public void updateAndSetPaidWithDate(Bill bill){
        db.collection(collectionName).document(bill.getDocumentId())
                .update("paid",true,
                        "dateOfPayment",new Date());
    }

    @Override
    public void saveAll(List<Bill> bills) {
        for (Bill bill : bills) {
            //db.collection(collectionName).add(bill);

            // Aws
            db.collection(collectionName).add(bill).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String id = documentReference.getId();

                    db.collection(collectionName).document(id).update("documentId", id);
                    bill.setDocumentId(id);
                }
            });
        }
    }

    /**
     * filters dummy bills by their paid status
     *
     * @param paid
     * @return
     */
    @Override
    public List<Bill> findBillsByPaid(Boolean paid) throws InterruptedException {
        List<Bill> result = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getPaid().equals(paid)) {
                result.add(bill);
            }
        }
        return result;
    }

    @Override
    public List<Bill> findBillsByPaidAndCompany(Boolean paid, List<Company> company) throws InterruptedException {
        if (company.isEmpty()) {
            return findBillsByPaid(paid);
        }
        List<Bill> result = new ArrayList<>();

        for (Bill bill : bills) {
            if (bill.getPaid().equals(paid) && company.contains(bill.getCompany())) {
                result.add(bill);
            }
        }
        return result;
    }

    @Override
    public Bill getBillByID(int billId) {


        findAll();
        for (Bill bill : bills) {
            if (bill.getId() == billId)
                return bill;
        }
        return null;
    }
}
