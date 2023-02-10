package com.km.fatorti.model;

/**
 * used to store Invoices information
 * @author Aws Ayyash
 */

public class Payment {

    private String visaNumber;
    private int threeNumbers;
    private String fullName;

    public Payment(String visaNumber, int threeNumbers, String fullName) {
        this.visaNumber = visaNumber;
        this.threeNumbers = threeNumbers;
        this.fullName = fullName;
    }

    private boolean isValidVisa(){

        return visaNumber.matches("d{16}");

    }
    private boolean isValidThreeNums(){
        return String.valueOf(threeNumbers).matches("d{3}");
    }
    private boolean isValidThreeFullName(){
        return fullName.matches("d{3}");
    }
    public String getVisaNumber() {
        return visaNumber;
    }

    public void setVisaNumber(String visaNumber) {
        this.visaNumber = visaNumber;
    }

    public int getThreeNumbers() {
        return threeNumbers;
    }

    public void setThreeNumbers(int threeNumbers) {
        this.threeNumbers = threeNumbers;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
