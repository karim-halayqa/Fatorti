package com.km.fatorti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.km.fatorti.interfaces.UserService;
import com.km.fatorti.interfaces.impl.UserServiceDA;
import com.km.fatorti.model.User;


/**
 * @author Aws Ayyash
 */

public class RegisterActivity extends AppCompatActivity {

    public final static String DATAKEYJSON = "localDBObject";
    public final static String DATAKEYFName = "FNAME";
    public final static String DATAKEYLName = "LNAME";
    public final static String DATAKEYUName = "UNAME";
    public final static String DATAKEYEmail = "EMAIL";

    private Button registerButton;
    private EditText editTextUName;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextEmail;

    private TextView resultTextRegistered;
    private EditText editTextLastName;

    private Button goToLoginPage;

    private boolean added = false;
    private UserService userService;
    private static int resultCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        setUpViews();
        userService = getStoredSharedPrefUserService();
        loadUIDataSharedPref();


        registerButton.setOnClickListener(view -> {

            String msgRegistered = emptyInputMsg();
            added = addUser();
            if (added) {
                msgRegistered = successfulRegisterMsg();

                // saving the db object -UserServiceDA-

                saveToPersistentStorageSharedPreference();
            }


            resultTextRegistered.setText(msgRegistered);

            resultTextRegistered.setVisibility(View.VISIBLE);


        });

        goToLoginPage.setOnClickListener(v -> {

            // go to login page, using intent result
            Intent resultIntent = new Intent();

            if (added)
                resultCode = 1;

            setResult(resultCode, resultIntent);
            saveUIDataSharedPref();
            finish();

        });

    }

    private String successfulRegisterMsg() {
        return "Registered successfully. Go to login page!";
    }

    private String emptyInputMsg() {
        return "Please fill in the input!";
    }

    private void saveToPersistentStorageSharedPreference() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String dbObjectStringJson = gson.toJson(userService);
        editor.putString(DATAKEYJSON, dbObjectStringJson);
        editor.commit(); // we can user apply(), to handle it in the background, instead of "commit" which writes to the persistent storage immediately

    }

    private void setUpViews() {

        registerButton = findViewById(R.id.buttonRegister);
        editTextUName = findViewById(R.id.editTextUNameRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextEmail = findViewById(R.id.editTextEmail);

        editTextLastName = findViewById(R.id.editTextLastName);
        resultTextRegistered = findViewById(R.id.resultTextRegister);
        goToLoginPage = findViewById(R.id.goToLoginPageButton);
    }

    private boolean addUser() {

        if (!emptyInput()) {
            User user = new User(editTextFirstName.getText().toString().trim(),
                    editTextLastName.getText().toString().trim(),
                    editTextEmail.getText().toString().trim(),
                    editTextUName.getText().toString().trim(),
                    editTextPassword.getText().toString()
            );

            //userService ; // it is better to always create new instance, (like we are connecting to a DB each time)

            userService.addUser(user);
            return true;
        }
        return false;
    }

    private boolean emptyInput() {
        boolean fName = editTextFirstName.getText().toString().equals("");
        boolean lName = editTextLastName.getText().toString().equals("");
        boolean email = editTextEmail.getText().toString().equals("");
        boolean uName = editTextUName.getText().toString().equals("");
        boolean password = editTextPassword.getText().toString().equals("");

        return fName || lName || email || uName || password;
    }


    // setting up userServiceDA
    public UserService getStoredSharedPrefUserService() {
       /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
        Gson gson = new Gson();

        String strObj = prefs.getString(RegisterActivity.DATAKEYJSON, "");

        if (!strObj.equals("")) {
            userService = gson.fromJson(strObj, UserServiceDA.class);

        } else {
            userService = new UserServiceDA();
        }
        */
        ///------12 feb 2023 load and save to a firebase---

        userService = new UserServiceDA();

        /////-------
        return userService;
    }

    private void saveUIDataSharedPref() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
        SharedPreferences.Editor editor = prefs.edit();

        //Gson gson = new Gson();
        //String dbObjectStringJson = gson.toJson(userService);
        editor.putString(DATAKEYFName, editTextFirstName.getText().toString().trim());
        editor.putString(DATAKEYLName, editTextLastName.getText().toString().trim());
        editor.putString(DATAKEYEmail, editTextEmail.getText().toString().trim());
        editor.putString(DATAKEYUName, editTextUName.getText().toString().trim());

        editor.commit(); // we can user apply(), to handle it in the background, instead of "commit" which writes to the persistent storage immediately

    }

    private void loadUIDataSharedPref() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);

        String strFName = prefs.getString(DATAKEYFName, "");
        String strLName = prefs.getString(DATAKEYLName, "");
        String strUName = prefs.getString(DATAKEYUName, "");
        String strEmail = prefs.getString(DATAKEYEmail, "");

        editTextFirstName.setText(strFName);
        editTextLastName.setText(strLName);
        editTextUName.setText(strUName);
        editTextEmail.setText(strEmail);


    }

}