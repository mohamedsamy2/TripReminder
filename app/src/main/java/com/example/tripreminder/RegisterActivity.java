package com.example.tripreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    TextView txtHaveAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtHaveAccount=findViewById(R.id.txtHaveAccount);
        txtHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLoginActiviy();
            }
        });
    }

    private void sendToLoginActiviy() {
        Intent intent= new Intent(com.example.tripreminder.RegisterActivity.this, com.example.tripreminder.LoginActivity.class);
        startActivity(intent);
    }
}