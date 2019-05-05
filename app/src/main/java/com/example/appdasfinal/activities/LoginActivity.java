package com.example.appdasfinal.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appdasfinal.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout inputUsername;
    TextInputLayout inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = findViewById(R.id.textInputLayout_email);
        inputPassword = findViewById(R.id.textInputLayout_password);

        TextView registerLink = findViewById(R.id.textView_register);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        final Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLogin()){
                    Intent i = new Intent(LoginActivity.this, RequestActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private boolean validateLogin() {
        if (!validateUsername() | !validatePassword()) {
            return false;
        }
        // TODO: Validaciones
        return true;
    }

    private boolean validateUsername() {
        String username = getUsername();
        if (username.isEmpty()) {
            inputUsername.setError(getString(R.string.error_empty));
            return false;
        }
        inputUsername.setError("");
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

    private String getUsername() {
        //Es bueno utilizar trim() ya que los correctores pueden introducir un espacio indeseado al final de los inputs.
        return Objects.requireNonNull(inputUsername.getEditText()).getText().toString().trim();

    }

    private String getPassword() {
        //Es bueno utilizar trim() ya que los correctores pueden introducir un espacio indeseado al final de los inputs.
        return Objects.requireNonNull(inputPassword.getEditText()).getText().toString().trim();
    }
}
