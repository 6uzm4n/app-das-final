package com.example.appdasfinal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.example.appdasfinal.R;
import com.example.appdasfinal.httpRequests.ServerRequestHandler;
import com.example.appdasfinal.httpRequests.ServerRequestHandlerListener;

public class SplashScreenActivity extends AppCompatActivity implements ServerRequestHandlerListener {

    private static final int SPLASH_DURATION = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Se comprueba si el usuario ya ha iniciado sesión y se le redirige a la actividad correspondiente después de unos segundos
        new Handler().postDelayed(() -> {
            SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
            String authString = session.getString("auth", null);
            if (authString == null) {
                goToLogin();
            } else {
                ServerRequestHandler.login(authString, this);
            }
        }, SPLASH_DURATION);
    }

    private void goToLogin() {
        // auto login failed
        // remove saved info to prevent future errors
        SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
        session.edit().remove("auth").apply();

        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void goToList() {
        Intent i = new Intent(SplashScreenActivity.this, ListActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onLoginSuccess(String token) {
        if (token != null) {
            goToList();
        } else {
            goToLogin();
        }
    }

    @Override
    public void onLoginFailure(String message) {
        goToLogin();
    }

    @Override
    public void onNoConnection() {
        goToLogin();
    }
}
