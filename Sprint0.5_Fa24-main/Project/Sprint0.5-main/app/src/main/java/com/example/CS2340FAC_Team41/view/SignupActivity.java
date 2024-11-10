package com.example.CS2340FAC_Team41.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

import model.User;

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
        signupEmail = findViewById(R.id.edit_username); // Consider renaming to edit_email for clarity
        signupPassword = findViewById(R.id.edit_password);
        signupButton = findViewById(R.id.btn_register);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = signupEmail.getText().toString().trim().toLowerCase(); // Normalize to lowercase
                String pass = signupPassword.getText().toString().trim();

                // Validate inputs
                if (emailInput.isEmpty()) {
                    signupEmail.setError("Email can't be empty");
                    signupEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    signupEmail.setError("Please enter a valid email");
                    signupEmail.requestFocus();
                    return;
                }

                if (pass.isEmpty()) {
                    signupPassword.setError("Password can't be empty");
                    signupPassword.requestFocus();
                    return;
                }

                if (pass.length() < 6) {
                    signupPassword.setError("Password must be at least 6 characters");
                    signupPassword.requestFocus();
                    return;
                }

                // Proceed with registration
                auth.createUserWithEmailAndPassword(emailInput, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this,
                                            "Registration successful", Toast.LENGTH_SHORT).show();

                                    // Get current user
                                    String userId = auth.getCurrentUser().getUid();
                                    String email = auth.getCurrentUser().getEmail();

                                    // Create User object
                                    User user = new User(email);

                                    // Store in Realtime Database under 'users/{userId}'
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userId);
                                    mDatabase.setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> dbTask) {
                                                    if (dbTask.isSuccessful()) {
                                                        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(SignupActivity.this,
                                                                "Failed to store user data", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(SignupActivity.this, "Signup failed: "
                                            + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Redirect to LoginActivity
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
