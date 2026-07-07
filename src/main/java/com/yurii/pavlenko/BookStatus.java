package com.yurii.pavlenko;

public class BookStatus {
    private String title;
    private boolean available;

    public BookStatus(String message, boolean ok) {
        this.title = message;
        this.available = ok;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return available;
    }
}
