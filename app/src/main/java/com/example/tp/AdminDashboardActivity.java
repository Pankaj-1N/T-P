package com.example.tp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import com.example.tp.ui.auth.LoginActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private MaterialCardView cardNotices, cardQA, cardPlacements, cardStudents,
            cardProfile, cardSettings, cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // ---- ROLE GATE: allow only admin ----
        String role = getSharedPreferences("tp_prefs", MODE_PRIVATE).getString("ROLE", null);
        if (role == null || !"admin".equalsIgnoreCase(role)) {
            startActivity(new Intent(this, StudentDashboardActivity.class));
            finish();
            return;
        }

        // Toolbar
        topAppBar = findViewById(R.id.topAppBar);
        if (topAppBar != null) {
            topAppBar.setTitle("Admin Dashboard");
        }

        // Cards
        cardNotices     = findViewById(R.id.cardNotices);
        cardQA          = findViewById(R.id.cardQA);
        cardPlacements  = findViewById(R.id.cardPlacements);
        cardStudents    = findViewById(R.id.cardStudents);
        cardProfile     = findViewById(R.id.cardProfile);
        cardSettings    = findViewById(R.id.cardSettings);
        cardLogout      = findViewById(R.id.cardLogout);

        // Clicks
        if (cardNotices != null)   cardNotices.setOnClickListener(v -> startSafe(ManageNoticesActivity.class));
        if (cardQA != null)        cardQA.setOnClickListener(v -> startSafe(ManageQAActivity.class));
        if (cardPlacements != null)cardPlacements.setOnClickListener(v -> startSafe(ManagePlacementsActivity.class));
        if (cardStudents != null)  cardStudents.setOnClickListener(v -> startSafe(ManageStudentsActivity.class));
        if (cardProfile != null)   cardProfile.setOnClickListener(v -> startSafe(ProfileActivity.class));
        if (cardSettings != null)  cardSettings.setOnClickListener(v -> startSafe(SettingsActivity.class));
        if (cardLogout != null)    cardLogout.setOnClickListener(this::logout);
    }

    private void startSafe(Class<?> cls) {
        try {
            startActivity(new Intent(this, cls));
        } catch (Throwable t) {
            Toast.makeText(this, "Screen missing: " + cls.getSimpleName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void logout(View v) {
        try { FirebaseAuth.getInstance().signOut(); } catch (Throwable ignored) {}

        // Clear saved role so auto-login won't trigger
        SharedPreferences prefs = getSharedPreferences("tp_prefs", MODE_PRIVATE);
        prefs.edit().remove("ROLE").apply();

        // Relaunch LoginActivity as a fresh task and mark it's from logout
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra("fromLogout", true);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
