package com.km.fatorti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.km.fatorti.interfaces.BillService;
import com.km.fatorti.interfaces.impl.BillServiceImplementation;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class PayBillsActivity extends AppCompatActivity {
    private static List<Bill> bills;
    private static RecyclerView recyclerView;
    private static BillServiceImplementation billService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bills);
        recyclerView = findViewById(R.id.bills_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Get user, to specify findings
        Intent intent = getIntent();
        String userJson = intent.getStringExtra("user");
        Gson gson = new Gson();
        User user = gson.fromJson(userJson,User.class);
        billService = new BillServiceImplementation(user);
        loadBills(billService);

    }

    private static void loadBills(BillServiceImplementation billService) {
        try {
            Boolean paid = false;
            // Request from firebase
            bills = billService.findBillsByPaid(paid);

            //sort the list by dates in a descending order
            bills = bills.stream().sorted((b1, b2) -> b2.getDateOfIssue().compareTo(b1.getDateOfIssue())).collect(Collectors.toList());
            BillAdapter adapter = new BillAdapter(recyclerView.getContext(), bills);
            recyclerView.setAdapter(adapter);
        } catch (InterruptedException e) {

        }
    }


}