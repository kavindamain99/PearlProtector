package com.example.pearlprotectors;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pearlprotectors.Models.SponsorInquiryDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VolunteerAddInquoryDetails extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    Button button;
    String id,image,title,venue,date;
    DatabaseReference ref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_add_inquory_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            id = bundle.getString("id");
            title = bundle.getString("title");
            image = bundle.getString("image");
            venue = bundle.getString("venue");
            date = bundle.getString("date");
        }

        editText1 = (EditText)findViewById(R.id.subject);
        editText2 = (EditText)findViewById(R.id.email);
        editText3 = (EditText)findViewById(R.id.inquiry);
        button = (Button)findViewById(R.id.submit);

        ref = FirebaseDatabase.getInstance().getReference("VolunteerInquiryDetails");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String subject = editText1.getText().toString();
                final String email = editText2.getText().toString();
                final String inquiry = editText3.getText().toString();

                if (subject.isEmpty()) {
                    editText1.setError("Subject is required");
                }else if (email.isEmpty()) {
                    editText2.setError("Email is required");
                }else if (inquiry.isEmpty()) {
                    editText3.setError("Inquiry is required");
                }else {
                    // Getting image upload ID.
                    String key = ref.push().getKey();

                    System.out.println();

                    @SuppressWarnings("VisibleForTests")
                    SponsorInquiryDetails projects = new SponsorInquiryDetails(key,image,title,venue,date,subject,email,inquiry);

                    // Adding image upload id s child element into databaseReference.
                    ref.child(key).setValue(projects);

                    // Showing toast message after done uploading.
                    Toast.makeText(getApplicationContext(), "Data Uploaded Successfully ", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}