package com.km.fatorti.model;

/**
 * used to check visa details and payments
 * @author Aws Ayyash
 */

public class VisaPayment {

    private String visaNumber;
    private int threeNumbers;
    private String fullName;

    public VisaPayment(String visaNumber, int threeNumbers, String fullName) {
        this.visaNumber = visaNumber;
        this.threeNumbers = threeNumbers;
        this.fullName = fullName;

    }

   /* public boolean isValidVisa(){
        return isValidVisaNumber() && isValidThreeNums() && isValidFullName();
    }*/



    public boolean isValidVisaNumber(){

        return visaNumber.matches("\\d{4} \\d{4} \\d{4} \\d{4}");

    }
    public boolean isValidThreeNums(){
        return String.valueOf(threeNumbers).matches("\\d{3}");
    }
    public boolean isValidFullName(){
        return fullName.matches("[A-Za-z ]+");
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
