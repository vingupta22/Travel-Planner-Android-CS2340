package com.example.CS2340FAC_Team41.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CS2340FAC_Team41.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signupEmail;
    private EditText signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.edit_username);
        signupPassword = findViewById(R.id.edit_password);
        signupButton = findViewById(R.id.btn_register);
        // Register button - creates new account and goes to HomeActivity
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernN = signupEmail.getText().toString().trim();
                usernN += "@gmail.com";
                String pass = signupPassword.getText().toString().trim();
                if (usernN.isEmpty()) {
                    signupEmail.setError("Email can't be empty");
                }
                if (pass.isEmpty()) {
                    signupEmail.setError("Password can't be empty");
                } else {
                    String finalUsernN = usernN;
                    auth.createUserWithEmailAndPassword(usernN, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this,
                                                "Successful sign in", Toast.LENGTH_SHORT).show();
                                        //Sprint 2 changes:
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                        model.User user = new model.User(finalUsernN);
                                        mDatabase.child("users").child(finalUsernN).setValue(user);
                                        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        //end Sprint2 changes
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Signup failed"
                                                + task.getException(), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignupActivity.this,
                                                SignupActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}