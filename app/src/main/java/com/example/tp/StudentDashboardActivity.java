package com.example.tp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tp.ui.auth.LoginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentDashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawerLayout);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        NavigationView navigationView = findViewById(R.id.navigationView);

        if (toolbar != null && drawerLayout != null) {
            toolbar.setNavigationOnClickListener(v ->
                    drawerLayout.openDrawer(GravityCompat.START)
            );
        }

        if (navigationView != null) {
            View header = navigationView.getHeaderView(0);
            if (header != null) {
                ImageView profileImage = header.findViewById(R.id.profileImage);
                TextView studentName = header.findViewById(R.id.studentName);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (studentName != null && user != null) {
                    String name = user.getDisplayName() != null ? user.getDisplayName() : user.getEmail();
                    studentName.setText(name != null ? name : "Student Name");
                }
            }

            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_logout) logout();
                drawerLayout.closeDrawers();
                return true;
            });
        }
    }

    private void logout() {
        mAuth.signOut();
        getSharedPreferences("tp_prefs", MODE_PRIVATE)
                .edit()
                .remove("ROLE")
                .apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("fromLogout", true);
        startActivity(intent);
        finish();
    }
}
