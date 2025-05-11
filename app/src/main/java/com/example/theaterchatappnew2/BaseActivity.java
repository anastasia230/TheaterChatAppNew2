package com.example.theaterchatappnew2;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract String getBottomNavType();  // "home" or "bookings"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomNav() {
        TextView navHome = findViewById(R.id.nav_home);
        TextView navBookings = findViewById(R.id.nav_bookings);
        // Highlight the current section
        String type = getBottomNavType();
        if ("home".equals(type)) {
            navHome.setTextColor(Color.parseColor("#726EFF"));
        } else if ("bookings".equals(type)) {
            navBookings.setTextColor(Color.parseColor("#726EFF"));
        }

        navHome.setOnClickListener(v -> {
            if (!(this instanceof HomeActivity)) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }
        });

        navBookings.setOnClickListener(v -> {
            if (!(this instanceof BookingsActivity)) {
                startActivity(new Intent(this, BookingsActivity.class));
                finish();
            }
        });
    }
}

