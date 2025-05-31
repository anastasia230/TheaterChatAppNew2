package com.example.theaterchatappnew2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BookingDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_booking_details);

        //TextView title = findViewById(R.id.detailTitle);
        //TextView code = findViewById(R.id.detailCode);
        //TextView date = findViewById(R.id.detailDate);
        //TextView tickets = findViewById(R.id.detailTickets);

        Intent intent = getIntent();
        //title.setText(intent.getStringExtra("booking_title"));
        //code.setText(intent.getStringExtra("booking_code"));
        //date.setText(intent.getStringExtra("booking_date"));
        //tickets.setText(intent.getIntExtra("booking_tickets", 0) + " Tickets");
    }
}