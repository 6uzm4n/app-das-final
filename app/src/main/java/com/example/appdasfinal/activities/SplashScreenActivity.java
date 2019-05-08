package com.example.appdasfinal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.appdasfinal.R;
import com.example.appdasfinal.httpRequests.HTTPRequestSender;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Se comprueba si el usuario ya ha iniciado sesión y se le redirige a la actividad correspondiente después de unos segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
                String token = session.getString("session", null);
                Intent i;
                if (token == null) {
                    i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                } else {
                    HTTPRequestSender.getInstance().setServerToken(token);
                    i = new Intent(SplashScreenActivity.this, ListActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
