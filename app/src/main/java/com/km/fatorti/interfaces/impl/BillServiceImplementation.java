package com.km.fatorti.interfaces.impl;

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
    private List<Bill> dummyBills;

    /**
     * adds dummy data to the dummyBill list
     */
    public BillServiceImplementation() {
        dummyBills = new ArrayList<>();
        // Add paid bills


        dummyBills.add(new Bill(1,new Date(122, 7, 1), new Date(), Company.ELECTRICITY, 100.00, true,
                new User("admin","admin")));
        dummyBills.add(new Bill(2, new Date(122, 7, 15), new Date(),
                Company.WATER, 250.00, true,new User("admin","admin")));

        dummyBills.add(new Bill(3, new Date(122, 8, 1),new Date(), Company.GAZ, 75.00, true,
        new User("admin","admin")));
        dummyBills.add(new Bill(4,new Date(122, 8, 15),new Date(), Company.ELECTRICITY, 125.00, true,
                new User("admin","admin")));
        dummyBills.add(new Bill(5,new Date(122, 9, 1),new Date(), Company.WATER, 50.00, true,
                new User("admin","admin")));
        dummyBills.add(new Bill(6,new Date(122, 9, 15),new Date(122,9,17), Company.GAZ, 200.00, true,
        new User("admin","admin")));
        dummyBills.add(new Bill(7,new Date(122, 10, 1),new Date(), Company.ELECTRICITY, 75.00, true
        ,new User("admin","admin")));
        dummyBills.add(new Bill(8,new Date(122, 10, 15),new Date(122,10,19), Company.WATER, 150.00,
                true, new User("admin","admin")));
        dummyBills.add(new Bill(9,new Date(122, 11, 1),new Date(), Company.GAZ, 100.00, true,
                new User("admin","admin")));
        dummyBills.add(new Bill(10,new Date(122, 11, 15),new Date(122,11,20),
                Company.ELECTRICITY, 250.00, true, new User("admin","admin")));

        // Add unpaid bills
        dummyBills.add(new Bill(11,new Date(122, 7, 1),null, Company.WATER, 100.00, false,
                new User("admin","admin")));
        dummyBills.add(new Bill(12,new Date(122, 8, 15), null,Company.GAZ, 250.00, false
        ,new User("admin","admin")));
        dummyBills.add(new Bill(13,new Date(122, 9, 1),null, Company.ELECTRICITY, 75.00, false,
                new User("admin","admin")));
        dummyBills.add(new Bill(14,new Date(122, 10, 15),null, Company.WATER, 125.00, false
        ,new User("admin","admin")));
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
