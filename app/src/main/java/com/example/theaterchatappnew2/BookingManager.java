package com.example.theaterchatappnew2;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class BookingManager {

    private static final List<BookingInfo> bookings = new ArrayList<>();
    private static final String PREFS_NAME = "BookingPrefs";
    private static final String KEY_BOOKINGS = "bookings";
    private static SharedPreferences prefs;
    private static String currentUser;
    private static final Gson gson = new Gson();

    // ➕ Αρχικοποίηση με context και username
    public static void init(Context context, String username) {
        currentUser = username;
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadBookings();
    }

    public static void addBooking(BookingInfo booking) {
        bookings.add(booking);
        saveBookings();
    }

    public static boolean removeBookingByCode(String code) {
        for (BookingInfo booking : new ArrayList<>(bookings)) {
            if (booking.code.equalsIgnoreCase(code)) {
                bookings.remove(booking);
                ShowManager.releaseSeats(
                        booking.title.toLowerCase(),
                        booking.datetime,
                        booking.seatCount
                );
                saveBookings();
                return true;
            }
        }
        return false;
    }

    public static boolean removeBookingByTitle(String title) {
        return bookings.removeIf(b -> b.title.equalsIgnoreCase(title));
    }

    // ➕ Επιστρέφει μόνο τις κρατήσεις του συγκεκριμένου χρήστη
    public static List<BookingInfo> getBookings() {
        List<BookingInfo> result = new ArrayList<>();
        for (BookingInfo b : bookings) {
            if (b.name.equalsIgnoreCase(currentUser)) {
                result.add(b);
            }
        }
        return result;
    }

    public static void clearBookings() {
        bookings.clear();
        saveBookings();
    }

    private static void saveBookings() {
        if (prefs == null) return;
        String json = gson.toJson(bookings);
        prefs.edit().putString(KEY_BOOKINGS, json).apply();
    }
    public static void clearAllBookings() {
        bookings.clear();
        if (prefs != null) {
            prefs.edit().remove(KEY_BOOKINGS).apply();
        }
    }

    private static void loadBookings() {
        if (prefs == null) return;
        String json = prefs.getString(KEY_BOOKINGS, null);
        if (json != null) {
            bookings.clear();
            bookings.addAll(gson.fromJson(json, new TypeToken<List<BookingInfo>>() {}.getType()));
        }
    }
}
