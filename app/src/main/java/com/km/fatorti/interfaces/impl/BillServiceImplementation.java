package com.km.fatorti.interfaces.impl;

import com.km.fatorti.interfaces.BillService;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * used to fetch bill records, initially used to filter dummy bill records by their paid status
 * @author Karim Halayqa
 */
public class BillServiceImplementation implements BillService {
    private List<Bill> dummyBills;

    /**
     * adds dummy data to the dummyBill list
     */
    public BillServiceImplementation() {
        dummyBills = new ArrayList<>();
        // Add paid bills
        dummyBills.add(new Bill(new Date(2022, 7, 1), Company.ELECTRICITY, 100.00, true));
        dummyBills.add(new Bill(new Date(2022, 7, 15), Company.WATER, 250.00, true));
        dummyBills.add(new Bill(new Date(2022, 8, 1), Company.GAZ, 75.00, true));
        dummyBills.add(new Bill(new Date(2022, 8, 15), Company.ELECTRICITY, 125.00, true));
        dummyBills.add(new Bill(new Date(2022, 9, 1), Company.WATER, 50.00, true));
        dummyBills.add(new Bill(new Date(2022, 9, 15), Company.GAZ, 200.00, true));
        dummyBills.add(new Bill(new Date(2022, 10, 1), Company.ELECTRICITY, 75.00, true));
        dummyBills.add(new Bill(new Date(2022, 10, 15), Company.WATER, 150.00, true));
        dummyBills.add(new Bill(new Date(2022, 11, 1), Company.GAZ, 100.00, true));
        dummyBills.add(new Bill(new Date(2022, 11, 15), Company.ELECTRICITY, 250.00, true));

        // Add unpaid bills
        dummyBills.add(new Bill(new Date(2022, 7, 1), Company.WATER, 100.00, false));
        dummyBills.add(new Bill(new Date(2022, 8, 15), Company.GAZ, 250.00, false));
        dummyBills.add(new Bill(new Date(2022, 9, 1), Company.ELECTRICITY, 75.00, false));
        dummyBills.add(new Bill(new Date(2022, 10, 15), Company.WATER, 125.00, false));
    }

    /**
     * filters dummy bills by their paid status
     * @param paid
     * @return
     */
    @Override
    public List<Bill> findBillsByPaid(Boolean paid) {
        List<Bill> result = new ArrayList<>();

        for(Bill bill : dummyBills){
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
        List<Bill> result = new ArrayList<>();

        for(Bill bill : dummyBills){
            if(bill.getPaid().equals(paid) && company.contains(bill.getCompany())){
                result.add(bill);
            }
        }
        return result;
    }
}
