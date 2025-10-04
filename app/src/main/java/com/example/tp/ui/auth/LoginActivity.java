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

    // ✅ Hardcoded UID mapping (from your Firebase)
    private static final String ADMIN_UID = "JgwK52yeelSWVMjlslOFbyuiRWB3";
    private static final String STUDENT_UID = "t9h610VF6TaO1IJ93S36yjJgt6i2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Skip auto-login if we came from an explicit logout
        boolean fromLogout = getIntent().getBooleanExtra("fromLogout", false);
        if (!fromLogout) {
            SharedPreferences prefs = getSharedPreferences("tp_prefs", MODE_PRIVATE);
            String savedRole = prefs.getString("ROLE", null);

            if (savedRole != null) {
                if ("admin".equalsIgnoreCase(savedRole)) {
                    startActivity(new Intent(this, com.example.tp.AdminDashboardActivity.class));
                    finish();
                    return;
                } else if ("student".equalsIgnoreCase(savedRole)) {
                    startActivity(new Intent(this, com.example.tp.StudentDashboardActivity.class));
                    finish();
                    return;
                }
            }
        } else {
            // (Optional) ensure Auth is cleared on return from logout
            try { FirebaseAuth.getInstance().signOut(); } catch (Throwable ignored) {}
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

        // ✅ Firebase Authentication only
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            // Check UID directly
                            if (ADMIN_UID.equals(uid)) {
                                saveRole("admin");
                                startActivity(new Intent(this, AdminDashboardActivity.class));
                                Toast.makeText(this, "Admin login successful", Toast.LENGTH_SHORT).show();
                            } else if (STUDENT_UID.equals(uid)) {
                                saveRole("student");
                                startActivity(new Intent(this, StudentDashboardActivity.class));
                                Toast.makeText(this, "Student login successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Unknown UID: Access denied", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }

                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveRole(String role) {
        SharedPreferences prefs = getSharedPreferences("tp_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ROLE", role);
        editor.apply();
    }
}
