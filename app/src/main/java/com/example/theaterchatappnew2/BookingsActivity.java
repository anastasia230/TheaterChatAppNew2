package com.example.theaterchatappnew2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.theaterchatappnew2.adapters.BookingAdapter;
import com.example.theaterchatappnew2.models.Booking;
import java.util.ArrayList;
import java.util.List;

public class BookingsActivity extends BaseActivity {

    private TextView userGreeting;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        setupBottomNav();

        userGreeting = findViewById(R.id.userGreeting);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString(KEY_USERNAME, null);

        BookingManager.init(getApplicationContext(), username);

        if (username != null && !username.isEmpty()) {
            userGreeting.setText("Bookings for " + username + ":");
        } else {
            userGreeting.setText("Welcome!");
        }

        // Get BookingInfo list and convert to Booking model list
        List<BookingInfo> infoList = BookingManager.getBookings();
        List<Booking> bookings = new ArrayList<>();
        for (BookingInfo info : infoList) {
            bookings.add(new Booking(
                    info.title,
                    info.code,
                    info.datetime,
                    info.seatCount
            ));
        }

        RecyclerView recyclerView = findViewById(R.id.bookingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookingAdapter adapter = new BookingAdapter(bookings, booking -> {
            // Handle booking click if needed
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected String getBottomNavType() {
        return "bookings";
    }
}
