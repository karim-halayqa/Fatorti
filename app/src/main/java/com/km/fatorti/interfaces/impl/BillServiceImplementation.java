package com.km.fatorti.interfaces.impl;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.km.fatorti.ViewBillActivity;
import com.km.fatorti.interfaces.BillService;
import com.km.fatorti.interfaces.UserService;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Company;
import com.km.fatorti.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * used to fetch bill records, initially used to filter dummy bill records by their paid status
 *
 * @author Karim Halayqa
 */
public class BillServiceImplementation implements BillService {

    private FirebaseFirestore db;
    private final String collectionName = "Bill";
    public static List<Bill> bills = new ArrayList<>();
    private User user;

    /**
     * adds dummy data to the dummyBill list
     */
    public BillServiceImplementation(User user) {
        this.user = user;
        db = FirebaseFirestore.getInstance();
        findAll();
    }

    @Override
    public void findAll() {

        db.collection("Bill").whereEqualTo("receiver", user)
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



                          try {
                                ViewBillActivity.fillBillListByPaid(BillServiceImplementation.this, true);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
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
        while (bills.isEmpty())
            continue;
        for (Bill bill : bills) {
            if (bill.getId() == billId)
                return bill;
        }
        return null;
    }

//    /**
//     * filters the displayed Bill list by its paid status
//     *
//     * @param billService
//     * @param paid
//     */
//    public void fillBillListByPaid(ListView billList, BillService billService, Boolean paid) throws InterruptedException {
//        //finds bills by provided paid flag
//        List<Bill> bills = billService.findBillsByPaid(paid);
//        //sort the list by dates in a descending order
//        bills = bills.stream().sorted((b1, b2) -> b2.getDateOfIssue().compareTo(b1.getDateOfIssue()))
//                .collect(Collectors.toList());
//        //create an ArrayAdapter for the list view to use
//        ArrayAdapter<Bill> billAdapterItems = new ArrayAdapter<Bill>(android.R.layout.simple_list_item_1, bills);
//        billList.setAdapter(billAdapterItems);
//    }
}
