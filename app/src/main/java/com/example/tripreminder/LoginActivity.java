package com.example.tripreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    TextView txtNewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtNewUser=findViewById(R.id.txtNewUser);
        txtNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRegisterActivity();
            }
        });
    }

    private void sendToRegisterActivity() {
        Intent intent= new Intent(com.example.tripreminder.LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}