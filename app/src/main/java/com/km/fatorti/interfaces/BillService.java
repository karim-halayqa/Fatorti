package com.km.fatorti.interfaces;

import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Company;

import java.util.List;

/**
 * used to fetch bill records, initially used to filter dummy bill records by their paid status
 * @author Karim Halayqa
 */
public interface BillService {

    List<Bill> findAll();
    void save(Bill bill);
    void saveAll(List<Bill> bills);
    List<Bill> findBillsByPaid(Boolean paid);
    List<Bill> findBillsByPaidAndCompany(Boolean paid, List<Company> company);
}