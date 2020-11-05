package com.example.vegetablessell.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.vegetablessell.R;
import com.example.vegetablessell.utils.SessionManagement;
import com.example.vegetablessell.utils.Utilities;

public class SplashActivity extends AppCompatActivity {

    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new SessionManagement(this);

        if(Utilities.isNetworkAvailable(this)) {

            if(null == session.getSession() || "" == session.getSession()) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            Toast.makeText(this, "connected to network", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not Connected to Network", Toast.LENGTH_SHORT).show();
        }

    }
}