package com.example.theaterchatappnew2;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ShowManager {

    private static final int MAX_SEATS_PER_SHOW = 5;

    private static final String PREFS_NAME = "ShowPrefs";
    private static final String KEY_AVAILABILITY = "seatAvailability";
    private static final String KEY_POINTER = "seatPointer";

    private static final Map<String, Integer> seatAvailability = new HashMap<>();
    private static final Map<String, Integer> seatPointer = new HashMap<>();

    private static SharedPreferences prefs;
    private static Gson gson = new Gson();

    public static void init(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();

        String savedAvailability = prefs.getString(KEY_AVAILABILITY, null);
        String savedPointer = prefs.getString(KEY_POINTER, null);

        if (savedAvailability != null) {
            seatAvailability.putAll(gson.fromJson(savedAvailability, mapType));
        } else {
            seatAvailability.put("hamlet_18:00", MAX_SEATS_PER_SHOW);
            seatAvailability.put("hamlet_21:00", MAX_SEATS_PER_SHOW);
            seatAvailability.put("romeo_18:00", MAX_SEATS_PER_SHOW);
            seatAvailability.put("romeo_21:00", MAX_SEATS_PER_SHOW);
        }

        if (savedPointer != null) {
            seatPointer.putAll(gson.fromJson(savedPointer, mapType));
        } else {
            seatPointer.put("hamlet_18:00", 1);
            seatPointer.put("hamlet_21:00", 1);
            seatPointer.put("romeo_18:00", 1);
            seatPointer.put("romeo_21:00", 1);
        }
    }

    private static void save() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_AVAILABILITY, gson.toJson(seatAvailability));
        editor.putString(KEY_POINTER, gson.toJson(seatPointer));
        editor.apply();
    }

    public static boolean bookSeats(String show, String time, int requestedSeats) {
        String key = show.toLowerCase() + "_" + time;
        int available = seatAvailability.getOrDefault(key, 0);
        if (available >= requestedSeats) {
            seatAvailability.put(key, available - requestedSeats);
            save();
            return true;
        }
        return false;
    }

    public static void releaseSeats(String show, String time, int seatsToRelease) {
        String key = show.toLowerCase() + "_" + time;
        int current = seatAvailability.getOrDefault(key, 0);
        int newCount = Math.min(current + seatsToRelease, MAX_SEATS_PER_SHOW);
        seatAvailability.put(key, newCount);

        // Αν γέμισε πλήρως, επανεκκίνηση pointer
        if (newCount == MAX_SEATS_PER_SHOW) {
            seatPointer.put(key, 1);
        }

        save();
    }

    public static String generateSeats(String show, String time, int count) {
        String key = show.toLowerCase() + "_" + time;
        int start = seatPointer.getOrDefault(key, 1);
        StringBuilder seats = new StringBuilder();
        for (int i = 0; i < count; i++) {
            seats.append("A").append(start + i);
            if (i < count - 1) seats.append(", ");
        }
        seatPointer.put(key, start + count);
        save();
        return seats.toString();
    }

    public static int getAvailableSeats(String show, String time) {
        return seatAvailability.getOrDefault(show.toLowerCase() + "_" + time, 0);
    }
    public static void resetAll() {
        for (String key : seatAvailability.keySet()) {
            seatAvailability.put(key, MAX_SEATS_PER_SHOW);
            seatPointer.put(key, 1);
        }
    }

    public static String suggestAlternative(String show) {
        for (String key : seatAvailability.keySet()) {
            if (key.startsWith(show.toLowerCase())) {
                int available = seatAvailability.get(key);
                if (available > 0) {
                    String[] parts = key.split("_");
                    return parts[1]; // return available time
                }
            }
        }
        return null;
    }
}
