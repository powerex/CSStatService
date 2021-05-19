package ua.azbest.csstatservice.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Record implements Serializable {

    private int id;
    private int pictureId;
    private int crosses;
    private LocalDate date;

    public Record(int id, int pictureId, int crosses, LocalDate date) {
        this.id = id;
        this.pictureId = pictureId;
        this.crosses = crosses;
        this.date = date;
    }

    public Record(int pictureId, int crosses, LocalDate date) {
        this.id = 0;
        this.pictureId = pictureId;
        this.crosses = crosses;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public int getCrosses() {
        return crosses;
    }

    public void setCrosses(int crosses) {
        this.crosses = crosses;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStringDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(date);
    }


}
