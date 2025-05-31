package com.example.theaterchatappnew2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

public class BookingsActivity extends BaseActivity {

    private TextView bookingsText;
    private TextView userGreeting;

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        setupBottomNav();

        // Συνδέουμε τα στοιχεία της διεπαφής
        userGreeting = findViewById(R.id.userGreeting);
        bookingsText = findViewById(R.id.bookingsTextView);

        // Εμφάνιση ονόματος ή "Welcome!"
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString(KEY_USERNAME, null);

        BookingManager.init(getApplicationContext(), username);

        if (username != null && !username.isEmpty()) {
            userGreeting.setText("Bookings for " + username + ":");
        } else {
            userGreeting.setText("Welcome!");
        }

        displayLocalBookings();
    }

    private void displayLocalBookings() {
        List<BookingInfo> list = BookingManager.getBookings();
        if (list.isEmpty()) {
            bookingsText.setText("You have no bookings.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (BookingInfo booking : list) {
            builder.append("Title: ").append(booking.title).append("\n")
                    .append("Time: ").append(booking.datetime).append("\n")
                    .append("Seat: ").append(booking.seat).append("\n")
                    .append("Code: ").append(booking.code).append("\n\n");
        }

        bookingsText.setText(builder.toString().trim());
    }

    @Override
    protected String getBottomNavType() {
        return "bookings";
    }
}
