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