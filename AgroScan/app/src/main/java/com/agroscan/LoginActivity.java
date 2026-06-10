package com.agroscan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.agroscan.utils.SharedPrefsHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Auto-login if session exists
        if (SharedPrefsHelper.getSession(this) != null) {
            String user = SharedPrefsHelper.getSession(this);
            navigateByRole(user);
            return;
        }

        // Seed default admin user on first run
        if (!SharedPrefsHelper.userExists(this, "admin")) {
            SharedPrefsHelper.saveUser(this, "admin", "admin123", "Administrador", "admin@agroscan.com");
            getSharedPreferences("agroscan_users", MODE_PRIVATE).edit()
                    .putBoolean("user_admin_isAdmin", true).apply();
        }

        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> handleLogin());
        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!SharedPrefsHelper.userExists(this, username)) {
            Toast.makeText(this, getString(R.string.error_wrong_credentials), Toast.LENGTH_SHORT).show();
            return;
        }

        if (SharedPrefsHelper.validateUser(this, username, password)) {
            SharedPrefsHelper.saveSession(this, username);
            navigateByRole(username);
        } else {
            Toast.makeText(this, getString(R.string.error_wrong_credentials), Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateByRole(String username) {
        Intent intent = SharedPrefsHelper.isAdmin(this, username)
                ? new Intent(this, AdminHubActivity.class)
                : new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
