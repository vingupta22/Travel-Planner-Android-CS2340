package com.example.CS2340FAC_Team41.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CS2340FAC_Team41.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.edit_username);
        loginPassword = findViewById(R.id.edit_password);
        loginButton = findViewById(R.id.btn_login);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                email += "@gmail.com";
                String pass = loginPassword.getText().toString().trim();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(LoginActivity.this,
                                                "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this,
                                                HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this,
                                                "Login Failed", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this,
                                                LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    } else {
                        loginPassword.setError("Password can't be empty");
                    }
                } else if (email.isEmpty()) {
                    loginEmail.setError("Email can't be empty");
                } else {
                    loginEmail.setError("Please enter valid email");
                }
            }
        });

        // Create Account button - takes user to SignupActivity
        findViewById(R.id.btn_create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


    }
}