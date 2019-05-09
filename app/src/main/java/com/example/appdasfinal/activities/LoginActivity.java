package com.example.appdasfinal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.appdasfinal.R;
import com.example.appdasfinal.httpRequests.ServerRequestHandler;
import com.example.appdasfinal.httpRequests.ServerRequestHandlerListener;
import com.example.appdasfinal.utils.ErrorNotifier;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements ServerRequestHandlerListener, Loader {

    TextInputLayout inputEmail;
    TextInputLayout inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.textInputLayout_email);
        inputPassword = findViewById(R.id.textInputLayout_password);

        TextView registerLink = findViewById(R.id.textView_register);
        registerLink.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(v -> {
            if (validateLogin()) {
                showProgress(true);
                ServerRequestHandler.login(getEmail(), getPassword(), LoginActivity.this);
            }
        });
    }

    @Override
    public void onLoginSuccess(String token) {
//        showProgress(false);
        inputEmail.setError("");
        if (token != null) {
            SharedPreferences preferences = getSharedPreferences("session", MODE_PRIVATE);
            preferences.edit().putString("session", token).apply();
            Intent i = new Intent(LoginActivity.this, ListActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onLoginFailure(String message) {
        showProgress(false);
        inputEmail.setError(getString(R.string.error_login));
    }

    private boolean validateLogin() {
        return !(!validateEmail() | !validatePassword());
    }

    private boolean validateEmail() {
        String username = getEmail();
        if (username.isEmpty()) {
            inputEmail.setError(getString(R.string.error_empty));
            return false;
        }
        inputEmail.setError("");
        return true;
    }

    private boolean validatePassword() {
        String password = getPassword();
        if (password.isEmpty()) {
            inputPassword.setError(getString(R.string.error_empty));
            return false;
        }
        inputPassword.setError("");
        return true;
    }

    private String getEmail() {
        //Es bueno utilizar trim() ya que los correctores pueden introducir un espacio indeseado al final de los inputs.
        return Objects.requireNonNull(inputEmail.getEditText()).getText().toString().trim();

    }

    private String getPassword() {
        //Es bueno utilizar trim() ya que los correctores pueden introducir un espacio indeseado al final de los inputs.
        return Objects.requireNonNull(inputPassword.getEditText()).getText().toString().trim();
    }

    @Override
    public void onNoConnection() {
        showProgress(false);
        ErrorNotifier.notifyInternetConnection(getWindow().getDecorView().getRootView());
    }

    @Override
    public View getContentView() {
        return findViewById(R.id.scrollView_login);
    }

    @Override
    public View getProgressBar() {
        return findViewById(R.id.progressBar_login);
    }
}
