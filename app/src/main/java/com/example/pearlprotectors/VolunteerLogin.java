package com.example.pearlprotectors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class VolunteerLogin extends AppCompatActivity {

    Button reg;
    EditText editText1;
    EditText editText2;
    Button log;
    FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_login);

        editText1=(EditText)findViewById(R.id.Lemail);
        editText2=(EditText)findViewById(R.id.Lpass);
        log=(Button)findViewById(R.id.login);
        reg=(Button)findViewById(R.id.regi);

        firebaseAuth=FirebaseAuth.getInstance();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VolunteerLogin.this, VolunteerRegister.class);
                startActivity(intent);
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emaill = editText1.getText().toString();
                final String passwordd = editText2.getText().toString();

                if (emaill.isEmpty()) {
                    editText1.setError("Correct email required");
                } if (passwordd.isEmpty()) {
                    editText2.setError("Correct Password required");
                }else{


                    firebaseAuth.signInWithEmailAndPassword(emaill, passwordd)
                            .addOnCompleteListener(VolunteerLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(VolunteerLogin.this, "Successfully sign in", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(VolunteerLogin.this, VolunteerHome.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(VolunteerLogin.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }

}