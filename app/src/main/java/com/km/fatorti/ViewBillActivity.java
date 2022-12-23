package com.km.fatorti;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
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

        billList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill selectedBill = (Bill) parent.getItemAtPosition(position);
                Intent intent = new Intent(ViewBillActivity.this, BillDetails.class);
                intent.putExtra("bill", selectedBill);
                startActivity(intent);
            }
        });

    }

    /**
     * filters the displayed Bill list by its paid status
     *
     * @param billService
     * @param paid
     */
    private void fillBillListByPaid(BillService billService, Boolean paid) {
        //finds bills by provided paid flag
        List<Bill> bills = billService.findBillsByPaid(paid);
        //sort the list by dates in a descending order
        bills = bills.stream().sorted((b1, b2) -> b2.getDateOfIssue().compareTo(b1.getDateOfIssue()))
                .collect(Collectors.toList());
        //create an ArrayAdapter for the list view to use
        ArrayAdapter<Bill> billAdapterItems = new ArrayAdapter<Bill>(ViewBillActivity.this,
                android.R.layout.simple_list_item_1, bills);
        billList.setAdapter(billAdapterItems);
    }

    /**
     * filters the displayed Bill list by its paid status and list of companies provided
     *
     * @param billService
     * @param paid
     * @param company
     */
    private void fillBillListByPaidAndCompany(BillService billService, Boolean paid, List<Company> company) {
        //finds bills by provided paid flag and list of companies
        List<Bill> bills = billService.findBillsByPaidAndCompany(paid, company);
        //sort the list by dates in a descending order
        bills = bills.stream().sorted((b1, b2) -> b2.getDateOfIssue().compareTo(b1.getDateOfIssue()))
                .collect(Collectors.toList());
        //create an ArrayAdapter for the list view to use
        ArrayAdapter<Bill> billAdapterItems = new ArrayAdapter<Bill>(ViewBillActivity.this,
                android.R.layout.simple_list_item_1, bills);
        billList.setAdapter(billAdapterItems);
    }
}