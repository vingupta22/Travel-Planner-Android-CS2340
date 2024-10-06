package com.example.CS2340FAC_Team41.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CS2340FAC_Team41.R;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Register button - creates new account and goes to HomeActivity
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Registration logic here

                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Optional: Close SignupActivity
            }
        });

        // Login button - goes back to LoginActivity
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}