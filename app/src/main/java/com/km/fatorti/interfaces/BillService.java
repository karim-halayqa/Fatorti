package com.km.fatorti.interfaces;

import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Company;

import java.util.List;


/**
 * used to fetch bill records, initially used to filter dummy bill records by their paid status
 * @author Karim Halayqa
 */
public interface BillService {

//    void fetchData(Callback callback);
    void findAll();
    void save(Bill bill);
    void saveAll(List<Bill> bills);
    List<Bill> findBillsByPaid(Boolean paid) throws InterruptedException;
    List<Bill> findBillsByPaidAndCompany(Boolean paid, List<Company> company) throws InterruptedException;
}