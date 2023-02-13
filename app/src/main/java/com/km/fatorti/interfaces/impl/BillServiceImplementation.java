package com.km.fatorti.interfaces.impl;

import static android.content.ContentValues.TAG;

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

    FirebaseFirestore db;
    final String collectionName = "Bill";
    public static List<Bill> bills = new ArrayList<>();

    /**
     * adds dummy data to the dummyBill list
     */
    public BillServiceImplementation() {
        db = FirebaseFirestore.getInstance();
//        ArrayList<Bill> bills = new ArrayList<>();
//        Random rand = new Random();
//        Calendar calendar = Calendar.getInstance();
//
//        User user = new User();
//        // Generate 25 bills with dummy data
//        for (int i = 1; i <= 25; i++) {
//            int companyNumber = rand.nextInt(3) + 1; // random number between 1 and 3
//            Company company;
//            if (companyNumber == 1) {
//                company = Company.WATER;
//            } else if (companyNumber == 2) {
//                company = Company.GAZ;
//            } else {
//                company = Company.ELECTRICITY;
//            }
//
//            double value = Math.floor(rand.nextDouble() * 1000); // random value between 0 and 100
//
//            calendar.set(Calendar.YEAR, rand.nextInt(2021 - 2020 + 1) + 2020); // random year between 2020 and 2021
//            calendar.set(Calendar.MONTH, rand.nextInt(12)); // random month
//            calendar.set(Calendar.DAY_OF_MONTH, rand.nextInt(31) + 1); // random day of month
//            Date dateOfIssue = calendar.getTime();
//
//            calendar.set(Calendar.YEAR, rand.nextInt(2022 - 2021 + 1) + 2021); // random year between 2021 and 2022
//            calendar.set(Calendar.MONTH, rand.nextInt(12)); // random month
//            calendar.set(Calendar.DAY_OF_MONTH, rand.nextInt(31) + 1); // random day of month
//            Date dateOfPayment = calendar.getTime();
//
//            Boolean paid;
//            if (i <= 10) {
//                paid = false;
//            } else {
//                paid = true;
//            }
//
//            Bill bill = new Bill(i, dateOfIssue, dateOfPayment, company, value, paid, user);
//            bills.add(bill);
//        }
//        for(Bill bill:bills) {
//            db.collection(collectionName)
//                    .add(bill);
//        }

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
