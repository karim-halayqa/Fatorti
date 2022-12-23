package com.km.fatorti;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.km.fatorti.interfaces.BillService;
import com.km.fatorti.interfaces.impl.BillServiceImplementation;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewBillActivity extends AppCompatActivity {

    private Button paid;
    private Button unpaid;
    private Boolean paidStatus;
    private CheckBox all;
    private CheckBox electricity;
    private CheckBox water;
    private CheckBox gaz;
    private ListView billList;

    private List<Company> companiesSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bills);

        paid = findViewById(R.id.paid);
        unpaid = findViewById(R.id.unpaid);
        paidStatus = true;
        all = findViewById(R.id.all);
        electricity = findViewById(R.id.electricity);
        water = findViewById(R.id.water);
        gaz = findViewById(R.id.gaz);
        companiesSelected = new ArrayList<>();
        billList = findViewById(R.id.billList);


        BillService billService = new BillServiceImplementation();
        paid.setOnClickListener(view -> {
            paidStatus = true;
            fillBillListByPaid(billService, true);
        });

        unpaid.setOnClickListener(view -> {
            paidStatus = false;
            fillBillListByPaid(billService, false);
        });

        paid.performClick();

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    electricity.setChecked(false);
                    water.setChecked(false);
                    gaz.setChecked(false);
                    companiesSelected.clear();
                }
                fillBillListByPaid(billService, paidStatus);
            }
        });

        electricity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                all.setChecked(false);
                if (isChecked) {
                    companiesSelected.add(Company.ELECTRICITY);
                } else {
                    companiesSelected.remove(Company.ELECTRICITY);
                }
                fillBillListByPaidAndCompany(billService, paidStatus, companiesSelected);

            }
        });

        water.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                all.setChecked(false);
                if (isChecked) {
                    companiesSelected.add(Company.WATER);
                } else {
                    companiesSelected.remove(Company.WATER);
                }
                fillBillListByPaidAndCompany(billService, paidStatus, companiesSelected);
            }
        });

        gaz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                all.setChecked(false);
                if (isChecked) {
                    companiesSelected.add(Company.GAZ);
                } else {
                    companiesSelected.remove(Company.GAZ);
                }
                fillBillListByPaidAndCompany(billService, paidStatus, companiesSelected);
            }
        });

    }

    private void fillBillListByPaid(BillService billService, Boolean paid) {
        List<Bill> bills = billService.findBillsByPaid(paid);
        bills = bills.stream().sorted((b1, b2) -> b2.getDate().compareTo(b1.getDate()))
                .collect(Collectors.toList());
        ArrayAdapter<Bill> billAdapterItems = new ArrayAdapter<Bill>(ViewBillActivity.this,
                android.R.layout.simple_list_item_1, bills);
        billList.setAdapter(billAdapterItems);
    }

    private void fillBillListByPaidAndCompany(BillService billService, Boolean paid, List<Company> company) {
        List<Bill> bills = billService.findBillsByPaidAndCompany(paid, company);
        bills = bills.stream().sorted((b1, b2) -> b2.getDate().compareTo(b1.getDate()))
                .collect(Collectors.toList());
        ArrayAdapter<Bill> billAdapterItems = new ArrayAdapter<Bill>(ViewBillActivity.this,
                android.R.layout.simple_list_item_1, bills);
        billList.setAdapter(billAdapterItems);
    }
}
