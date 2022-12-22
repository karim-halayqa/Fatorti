package com.km.fatorti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author Zaid Khamis
 */

public class ContactUsActivity extends AppCompatActivity {

    private EditText edtOtherSubject;
    private EditText edtMsg;
    private Spinner spnSubject;
    private Button btnSendFeedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        // element IDs setup
        edtOtherSubject = findViewById(R.id.edtOtherSubject);
        edtOtherSubject.setVisibility(View.GONE);
        edtMsg = findViewById(R.id.edtMsg);
        btnSendFeedback = findViewById(R.id.btnSendFeedback);
        spnSubject = findViewById(R.id.spnSubject);
        // Listeners
        sendListener();
        spinnerListener();


    }

    private void spinnerListener() {
        // spinner listener to handle visibility of editText:(edtOtherSubject) depending on selection of entries
        spnSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Other")) {
                    edtOtherSubject.setVisibility(View.VISIBLE);
                } else {
                    edtOtherSubject.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void sendListener() {
        // Send button listener
        btnSendFeedback.setOnClickListener(v -> {
            if (edtMsg != null) {
                String msg = edtMsg.getText().toString();
                // Send via intent to any activity handling ACTION_SEND
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                // determine subject title
                String subj = spnSubject.getSelectedItem().toString(); // from spinner
                if (subj.equals("Other")) {
                    if (edtOtherSubject != null) {
                        intent.putExtra(Intent.EXTRA_SUBJECT, edtOtherSubject.getText().toString());
                    } else // call toast
                        makeToast("Please enter a subject.");

                } else intent.putExtra(Intent.EXTRA_SUBJECT, subj);
                // complete intent details and initiate sending activity chooser
                /*****  should select only mail apps, but DOES NOT ****/
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bills@app.ps"});
                intent.setType("message/rfc822");

                startActivity(Intent.createChooser(intent, "Choose an email client:"));

            } else // call toast
                makeToast("Please fill out the message field.");
        });
    }

    private void makeToast(String butter) { // pun intended
        Toast.makeText(getApplicationContext(), butter, Toast.LENGTH_SHORT).show();
    }
}