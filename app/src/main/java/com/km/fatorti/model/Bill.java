package com.km.fatorti.model;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * used to store bill information, including date, company, its value and if it's paid or not
 * @author Karim Halayqa
 */
public class Bill {
    private Date date;
    private Company company;
    private double value;
    private Boolean paid;

    public Bill(Date date, Company company, double value, Boolean paid) {
        this.date = date;
        this.company = company;
        this.value = value;
        this.paid = paid;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date)+"     "+company.toString()+"     "+value;
    }
}
