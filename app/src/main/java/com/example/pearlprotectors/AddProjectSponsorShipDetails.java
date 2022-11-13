package com.example.pearlprotectors;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pearlprotectors.Models.Projects;
import com.example.pearlprotectors.Models.SponsorshipDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProjectSponsorShipDetails extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    Button button;
    String id,image,title,venue,date;
    DatabaseReference ref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_sponsor_ship_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            id = bundle.getString("id");
            title = bundle.getString("title");
            image = bundle.getString("image");
            venue = bundle.getString("venue");
            date = bundle.getString("date");
        }

        editText1 = (EditText)findViewById(R.id.name);
        editText2 = (EditText)findViewById(R.id.sponsertype);
        editText3 = (EditText)findViewById(R.id.email);
        editText4 = (EditText)findViewById(R.id.number);
        button = (Button)findViewById(R.id.submit);

        ref = FirebaseDatabase.getInstance().getReference("SponsorshipsDetails");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = editText1.getText().toString();
                final String type = editText2.getText().toString();
                final String email = editText3.getText().toString();
                final String contact = editText4.getText().toString();

                if (name.isEmpty()) {
                    editText1.setError("Name is required");
                }else if (type.isEmpty()) {
                    editText2.setError("Sponsor type is required");
                }else if (email.isEmpty()) {
                    editText3.setError("Email is required");
                }else if (contact.isEmpty()) {
                    editText4.setError("Contact number is required");
                }else {
                    // Getting image upload ID.
                    String key = ref.push().getKey();

                    System.out.println();

                    @SuppressWarnings("VisibleForTests")
                    SponsorshipDetails projects = new SponsorshipDetails(key,image,title,venue,date,name,type,email,contact);

                    // Adding image upload id s child element into databaseReference.
                    ref.child(key).setValue(projects);

                    // Showing toast message after done uploading.
                    Toast.makeText(getApplicationContext(), "Data Uploaded Successfully ", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}