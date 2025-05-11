package com.example.theaterchatappnew2;

import android.os.Bundle;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.theaterchatappnew2.models.Booking;
import java.util.List;
import java.util.ArrayList;
import com.example.theaterchatappnew2.adapters.BookingAdapter;


public class BookingsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private List<Booking> bookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        bookings = new ArrayList<>();
        //bookings.add(new Booking("Ο Θείος Βάνιας", "IGK7574", "SUN, 04 JUN 2023 14:30", 1));

        recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingAdapter(bookings, this::onBookingClick);
        recyclerView.setAdapter(adapter);

        setupBottomNav();
    }

    private void onBookingClick(Booking booking) {
        Intent intent = new Intent(this, BookingDetailsActivity.class);
        intent.putExtra("booking_title", booking.getTitle());
        intent.putExtra("booking_code", booking.getCode());
        intent.putExtra("booking_date", booking.getDate());
        intent.putExtra("booking_tickets", booking.getTickets());
        startActivity(intent);
    }

    @Override
    protected String getBottomNavType() {
        return "bookings";
    }
}

