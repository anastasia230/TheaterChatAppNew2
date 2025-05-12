package com.example.theaterchatappnew2;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BookingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        setupBottomNav();

        // Temporary mock data
        TextView bookingsText = findViewById(R.id.bookingsTextView);
        bookingsText.setText("🎟 O Theios Vania\n🕒 Sun, 4 June 2023 14:30\n📍 Row 1D, Seat 3D\n🎫 Code: IGK7574\n👤 Name: Giorgos Papadopoulos");
    }

    @Override
    protected String getBottomNavType() {
        return "bookings";
    }
}
