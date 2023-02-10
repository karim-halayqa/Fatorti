package com.km.fatorti.interfaces;


import com.km.fatorti.InvoiceActivity;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Invoice;

import java.util.List;

/**
 * used to store Invoices information
 * @author Aws Ayyash
 */
public interface InvoiceService {

    List<Invoice> getAll();
    void save(Invoice invoice);
    void saveAll(List<Invoice> invoices);
    //Invoice getInvoice(Invoice invoice);


}
