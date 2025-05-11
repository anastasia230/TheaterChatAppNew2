package com.example.theaterchatappnew2.models;

public class Booking {
    private String title;
    private String code;
    private String date;
    private int tickets;

    public Booking(String title, String code, String date, int tickets) {
        this.title = title;
        this.code = code;
        this.date = date;
        this.tickets = tickets;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public int getTickets() {
        return tickets;
    }
}
