package com.agroscan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.agroscan.utils.SharedPrefsHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnCreateUser, btnAlreadyHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnAlreadyHaveAccount = findViewById(R.id.btnAlreadyHaveAccount);

        btnCreateUser.setOnClickListener(v -> handleRegister());
        btnAlreadyHaveAccount.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, getString(R.string.error_passwords_mismatch), Toast.LENGTH_SHORT).show();
            return;
        }

        // Use email prefix as username
        String username = email.contains("@") ? email.split("@")[0] : email;

        if (SharedPrefsHelper.userExists(this, username)) {
            Toast.makeText(this, "El usuario ya existe. Usa otro correo.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPrefsHelper.saveUser(this, username, password, fullName, email);
        Toast.makeText(this, getString(R.string.success_registered), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
