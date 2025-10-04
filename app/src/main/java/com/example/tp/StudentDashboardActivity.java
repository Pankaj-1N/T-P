package com.example.tp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import com.example.tp.ui.auth.LoginActivity;

public class StudentDashboardActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private MaterialCardView cardNotices, cardQA, cardPlacements,
            cardProfile, cardSettings, cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_placeholder);
        setTitle("Student Dashboard");

        // Toolbar
        topAppBar = findViewById(R.id.topAppBar);
        if (topAppBar != null) {
            topAppBar.setTitle("Student Dashboard");
        }

        // Cards (use placeholders for now)
        cardNotices    = findViewById(R.id.cardNotices);
        cardQA         = findViewById(R.id.cardQA);
        cardPlacements = findViewById(R.id.cardPlacements);
        cardProfile    = findViewById(R.id.cardProfile);
        cardSettings   = findViewById(R.id.cardSettings);
        cardLogout     = findViewById(R.id.cardLogout);

        // Card click listeners (wrap with try-catch for safety)
        if (cardNotices != null) cardNotices.setOnClickListener(v -> startSafe(NoticesActivity.class));
        if (cardQA != null) cardQA.setOnClickListener(v -> startSafe(QAManagementActivity.class));
        if (cardPlacements != null) cardPlacements.setOnClickListener(v -> startSafe(PlacementRecordsActivity.class));
        if (cardProfile != null) cardProfile.setOnClickListener(v -> startSafe(ProfileActivity.class));
        if (cardSettings != null) cardSettings.setOnClickListener(v -> startSafe(SettingsActivity.class));
        if (cardLogout != null) cardLogout.setOnClickListener(this::logout);
    }

    private void startSafe(Class<?> cls) {
        try {
            startActivity(new Intent(this, cls));
        } catch (Throwable t) {
            Toast.makeText(this, "Screen missing: " + cls.getSimpleName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void logout(View v) {
        try {
            FirebaseAuth.getInstance().signOut();
        } catch (Throwable ignored) {}
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finishAffinity();
    }
}
