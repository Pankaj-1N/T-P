package com.example.tp.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tp.R;
import com.example.tp.AdminDashboardActivity;
import com.example.tp.StudentDashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    private static final String ADMIN_UID = "JgwK52yeelSWVMjlslOFbyuiRWB3";
    private static final String STUDENT_UID = "t9h610VF6TaO1IJ93S36yjJgt6i2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boolean fromLogout = getIntent().getBooleanExtra("fromLogout", false);
        if (!fromLogout) {
            SharedPreferences prefs = getSharedPreferences("tp_prefs", MODE_PRIVATE);
            String savedRole = prefs.getString("ROLE", null);
            if ("admin".equalsIgnoreCase(savedRole)) {
                Intent i = new Intent(this, AdminDashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                return;
            } else if ("student".equalsIgnoreCase(savedRole)) {
                Intent i = new Intent(this, StudentDashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                return;
            }
        } else {
            try { FirebaseAuth.getInstance().signOut(); } catch (Throwable ignored) {}
            getSharedPreferences("tp_prefs", MODE_PRIVATE).edit().remove("ROLE").apply();
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Login Failed: " +
                                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null) {
                        Toast.makeText(this, "Login Failed: user is null", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String uid = user.getUid();

                    if (ADMIN_UID.equals(uid)) {
                        saveRole("admin");
                        Intent i = new Intent(this, AdminDashboardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else if (STUDENT_UID.equals(uid)) {
                        saveRole("student");
                        Intent i = new Intent(this, StudentDashboardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        getSharedPreferences("tp_prefs", MODE_PRIVATE).edit().remove("ROLE").apply();
                        Toast.makeText(this, "Unknown UID: Access denied", Toast.LENGTH_SHORT).show();
                        try { mAuth.signOut(); } catch (Throwable ignored) {}
                    }
                });
    }

    private void saveRole(String role) {
        SharedPreferences prefs = getSharedPreferences("tp_prefs", MODE_PRIVATE);
        prefs.edit().putString("ROLE", role).apply();
    }
}
