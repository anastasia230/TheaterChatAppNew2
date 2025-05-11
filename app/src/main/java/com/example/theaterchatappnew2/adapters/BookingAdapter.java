package com.example.theaterchatappnew2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.theaterchatappnew2.R;
import com.example.theaterchatappnew2.models.Booking;
import java.util.List;


public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    public interface OnBookingClickListener {
        void onBookingClick(Booking booking);
    }

    private List<Booking> bookings;
    private OnBookingClickListener listener;

    public BookingAdapter(List<Booking> bookings, OnBookingClickListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.title.setText(booking.getTitle());
        holder.code.setText(booking.getCode());
        holder.date.setText(booking.getDate());
        holder.tickets.setText(booking.getTickets() + " TICKETS");

        holder.itemView.setOnClickListener(v -> listener.onBookingClick(booking));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }
        //TODO: Allages ston adapter gia swsth diaxeirish , enswmatwsh bookings sthn lista, dhmioyrgia booking_detail_activity
    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView title, code, date, tickets;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookingTitle);
            code = itemView.findViewById(R.id.bookingCode);
            date = itemView.findViewById(R.id.bookingDate);
            tickets = itemView.findViewById(R.id.bookingTickets);
        }
    }
}