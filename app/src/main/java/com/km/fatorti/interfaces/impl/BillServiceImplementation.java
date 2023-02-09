package com.km.fatorti.interfaces.impl;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
 * @author Karim Halayqa
 */
public class BillServiceImplementation implements BillService {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String collectionName = "Bill";

    /**
     * adds dummy data to the dummyBill list
     */
    public BillServiceImplementation() {

    }

    @Override
    public List<Bill> findAll() {
        List<Bill> billsList = new ArrayList<>();
        db.collection(collectionName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Bill bill = documentSnapshot.toObject(Bill.class);
                            billsList.add(bill);
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("getAllBills", "Error getting documents: ", e);
                    }
                });
        return billsList;
    }

    @Override
    public void save(Bill bill) {
        db.collection(collectionName).add(bill);
    }

    @Override
    public void saveAll(List<Bill> bills) {
        for(Bill bill: bills) {
            db.collection(collectionName).add(bill);
        }
    }

    /**
     * filters dummy bills by their paid status
     * @param paid
     * @return
     */
    @Override
    public List<Bill> findBillsByPaid(Boolean paid) {
        List<Bill> bills = findAll();
        List<Bill> result = new ArrayList<>();
        for(Bill bill : bills){
            if(bill.getPaid().equals(paid)){
                result.add(bill);
            }
        }
        return result;
    }

    @Override
    public List<Bill> findBillsByPaidAndCompany(Boolean paid, List<Company> company) {
        if(company.isEmpty()) {
            return findBillsByPaid(paid);
        }
        List<Bill> bills = findAll();
        List<Bill> result = new ArrayList<>();

        for(Bill bill : bills){
            if(bill.getPaid().equals(paid) && company.contains(bill.getCompany())){
                result.add(bill);
            }
        }
        return result;
    }
}
