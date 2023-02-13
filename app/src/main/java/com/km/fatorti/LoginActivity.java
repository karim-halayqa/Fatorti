package com.km.fatorti;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.km.fatorti.interfaces.UserService;
import com.km.fatorti.interfaces.impl.BillServiceImplementation;
import com.km.fatorti.interfaces.impl.UserServiceDA;
import com.km.fatorti.model.Bill;
import com.km.fatorti.model.Company;
import com.km.fatorti.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Aws Ayyash
 */
public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button goToRegisterButton;
    private EditText editTextPassword;
    private EditText editTextUName;
    private TextView loginResultText;

    private static final int reqCode = 1;

    // in case of first time, will create new object, else, will check the sharedPref storage saved previously
    private UserService userServiceDA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ////////////////Aws////////

        // --------------get the data to clean it

       /* ArrayList<Bill> bills = new ArrayList<>();
        Random rand = new Random();
        Calendar calendar = Calendar.getInstance();
        UserService userService = new UserServiceDA();

        List<User> users = userService.getAll();

        // Generate 25 bills with dummy data
        for (int i = 1; i <= 25; i++) {
            int companyNumber = rand.nextInt(3) + 1; // random number between 1 and 3
            Company company;
            if (companyNumber == 1) {
                company = Company.WATER;
            } else if (companyNumber == 2) {
                company = Company.GAZ;
            } else {
                company = Company.ELECTRICITY;
            }

            double value = Math.floor(rand.nextDouble() * 1000); // random value between 0 and 100

            calendar.set(Calendar.YEAR, rand.nextInt(2021 - 2020 + 1) + 2020); // random year between 2020 and 2021
            calendar.set(Calendar.MONTH, rand.nextInt(12)); // random month
            calendar.set(Calendar.DAY_OF_MONTH, rand.nextInt(31) + 1); // random day of month
            Date dateOfIssue = calendar.getTime();

            calendar.set(Calendar.YEAR, rand.nextInt(2022 - 2021 + 1) + 2021); // random year between 2021 and 2022
            calendar.set(Calendar.MONTH, rand.nextInt(12)); // random month
            calendar.set(Calendar.DAY_OF_MONTH, rand.nextInt(31) + 1); // random day of month
            Date dateOfPayment = calendar.getTime();

            Boolean paid;
            if (i <= 10) {
                paid = false;
            } else {
                paid = true;
            }
            int random = (int) (Math.random()*3);
            Bill bill = new Bill(i, dateOfIssue, dateOfPayment, company, value, paid, users.get(random));
            bills.add(bill);
        }
        BillServiceImplementation bbb = new BillServiceImplementation();
        bbb.saveAll(bills);
*/

     /*   ArrayList<Bill> bills = new ArrayList<>();
        Random rand = new Random();
        Calendar calendar = Calendar.getInstance();
        //UserService userService = new UserServiceDA();
        User karim = new User("Karim", "Halayqa", "karim@gmail.com","karim.halayqa","123456789","2Lf8hLqLOpKsBdY7oCKA");
        User aws = new User("Aws", "Ayyash", "aws@gmail.com","aws.ayyash","123456789","o2NTPAZCKtv2ZJwrLV8G");
        User zaid = new User("Zaid", "Khamis", "zaid@gmail.com","zaid.khamis","123456789","IfbMYP1cuMhYWLRTS0e3");


        List<User> users = new ArrayList<>();
        users.add(karim);
        users.add(aws);
        users.add(zaid);
        // Generate 25 bills with dummy data
        for (int i = 1; i <= 25; i++) {
            int companyNumber = rand.nextInt(3) + 1; // random number between 1 and 3
            Company company;
            if (companyNumber == 1) {
                company = Company.WATER;
            } else if (companyNumber == 2) {
                company = Company.GAZ;
            } else {
                company = Company.ELECTRICITY;
            }

            double value = Math.floor(rand.nextDouble() * 1000); // random value between 0 and 100

            calendar.set(Calendar.YEAR, rand.nextInt(2021 - 2020 + 1) + 2020); // random year between 2020 and 2021
            calendar.set(Calendar.MONTH, rand.nextInt(12)); // random month
            calendar.set(Calendar.DAY_OF_MONTH, rand.nextInt(31) + 1); // random day of month
            Date dateOfIssue = calendar.getTime();

            calendar.set(Calendar.YEAR, rand.nextInt(2022 - 2021 + 1) + 2021); // random year between 2021 and 2022
            calendar.set(Calendar.MONTH, rand.nextInt(12)); // random month
            calendar.set(Calendar.DAY_OF_MONTH, rand.nextInt(31) + 1); // random day of month
            Date dateOfPayment = calendar.getTime();

            Boolean paid;
            if (i <= 10) {
                paid = false;
            } else {
                paid = true;
            }
            int random = (int) (Math.random()*3);
            Bill bill = new Bill(i, dateOfIssue, dateOfPayment, company, value, paid, users.get(random));
            bills.add(bill);
        }
        BillServiceImplementation bbb = new BillServiceImplementation();
        bbb.saveAll(bills);
*/
        ////////////////////////


        userServiceDA = setUpUserService();
        userServiceDA.getAll();
        setTitle("Login");

        setUpViews();

        loginButton.setOnClickListener(view -> {

            checkUserByUName(editTextUName.getText().toString().trim(), editTextPassword.getText().toString());
        });

        goToRegisterButton.setOnClickListener(view -> {

            // go to the register new user page, using intents

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, reqCode);
        });



    }

    private void setUpViews() {

        loginButton = findViewById(R.id.loginButton);
        goToRegisterButton = findViewById(R.id.goToRegisterButton);
        editTextUName = findViewById(R.id.editTextUName);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginResultText = findViewById(R.id.loginResultText);

        putDummyLoginUser();
    }
    private void putDummyLoginUser(){
        editTextUName.setText("aws.ayyash");
        editTextPassword.setText("123456789");
    }

    private void checkUserByUName(String uName, String password) {
        User user = userServiceDA.findUser(uName);

        String msgResultLogin = successfullLoginMsg();

        if (user == null) {

            msgResultLogin = notRegisteredMsg(uName);

        } else {
            if (!user.getPassword().equals(password)) {
                msgResultLogin = incorrectPasswordMsg();

            } else {

                // go to main page, using intents, valid "login"
                goToMainPage(user);
            }
        }
        loginResultText.setText(msgResultLogin);


    }

    private String successfullLoginMsg() {
        return "Successful login";
    }

    private String incorrectPasswordMsg() {
        return "Password is not correct!";
    }

    private String notRegisteredMsg(String uName) {
        return "username= " + uName + " is not registered";
    }

    private void goToMainPage(User user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Gson gson = new Gson();
        String gsonObjUser =  gson.toJson(user);
        intent.putExtra("gsonObjUser", gsonObjUser);
        startActivity(intent);
    }


    // i come here, after i register a new user, so i need to load the data again
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == resultCode) {

            userServiceDA = setUpUserService();
            userServiceDA.getAll();

        }

    }

    // this is like loading the DB
    public UserService setUpUserService() {

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());  //Activity1.class


        String strObj = prefs.getString(RegisterActivity.DATAKEYJSON, "");

        if (!strObj.equals("")) {
            Gson gson = new Gson();
            userServiceDA = gson.fromJson(strObj, UserServiceDA.class);

        } else {
            userServiceDA = new UserServiceDA();
        }
*/
        // --------12 feb 2023------

        userServiceDA = new UserServiceDA();

        //
        return userServiceDA;
    }
}