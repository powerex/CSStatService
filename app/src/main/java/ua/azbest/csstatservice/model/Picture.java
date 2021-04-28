package ua.azbest.csstatservice.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Picture implements Serializable {
    private int id;
    private String title;
    private int crossStitchCount;
    private LocalDate startDate;
    private LocalDate wishDate;

    public Picture(int id, String title, int crossStitchCount, LocalDate startDate, LocalDate wishDate) {
        this.id = id;
        this.title = title;
        this.crossStitchCount = crossStitchCount;
        this.startDate = startDate;
        this.wishDate = wishDate;
    }

    public Picture(String title, int crossStitchCount, LocalDate startDate, LocalDate wishDate) {
        this.id = 0;
        this.title = title;
        this.crossStitchCount = crossStitchCount;
        this.startDate = startDate;
        this.wishDate = wishDate;
    }

    public Picture() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCrossStitchCount() {
        return crossStitchCount;
    }

    public void setCrossStitchCount(int crossStitchCount) {
        this.crossStitchCount = crossStitchCount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getWishDate() {
        return wishDate;
    }

    public void setWishDate(LocalDate wishDate) {
        this.wishDate = wishDate;
    }

    public String getStringStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(startDate);
    }

    public String getStringWishDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(wishDate);
    }

    public static LocalDate fromFormattedString(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(input, formatter);
        return localDate;
    }
}
