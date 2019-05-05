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

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private TextInputLayout inputPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = findViewById(R.id.textInputLayout_email);
        inputPassword = findViewById(R.id.textInputLayout_password);
        inputPassword2 = findViewById(R.id.textInputLayout_password2);

        TextView loginLink = findViewById(R.id.textView_login);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        Button registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegister()){
                    finish();
                }
            }
        });

    }

    private boolean validateRegister() {
        if (!validateEmail() | !validatePassword() | !validatePassword2()) {
            return false;
        }
        // TODO: Registrarse en servidor
        return true;
    }

    private boolean validateEmail() {
        String email = getEmail();
        // Regex extraido de: https://www.owasp.org/index.php/OWASP_Validation_Regex_Repository
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}";
        if (email.isEmpty()) {
            inputEmail.setError(getString(R.string.error_empty));
            return false;
        } else if (!email.matches(emailRegex)) {
            inputEmail.setError(getString(R.string.error_email_format));
            return false;
        } else if (false) {
            // TODO: Validar si existe?
            inputEmail.setError(getString(R.string.error_email_taken));
            return false;
        }

        inputEmail.setError("");
        return true;
    }

    private boolean validatePassword() {
        String password = getPassword();
        // Regex extraido de: https://www.owasp.org/index.php/OWASP_Validation_Regex_Repository
        String passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}";
        if (password.isEmpty()) {
            inputPassword.setError(getString(R.string.error_empty));
            return false;
        } else if (password.length() < 8 || !password.matches(passwordRegex)) {
            inputPassword.setError(getString(R.string.error_password_format));
            return false;
        }
        inputPassword.setError("");
        return true;
    }

    private boolean validatePassword2() {
        String password2 = getPassword2();
        if (password2.isEmpty()) {
            inputPassword2.setError(getString(R.string.error_empty));
            return false;
        } else if (!password2.equals(getPassword())) {
            inputPassword2.setError(getString(R.string.error_password2_different));
            return false;
        }
        inputPassword2.setError("");
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

    private String getPassword2() {
        //Es bueno utilizar trim() ya que los correctores pueden introducir un espacio indeseado al final de los inputs.
        return Objects.requireNonNull(inputPassword2.getEditText()).getText().toString().trim();
    }
}
