package ua.azbest.csstatservice.model;

import java.time.LocalDate;

public class RecordDTO {

    private int id;
    private int pictureId;
    private int crosses;
    private String date;

    public RecordDTO(Record record) {
        this.id = record.getId();
        this.pictureId = record.getPictureId();
        this.crosses = record.getCrosses();
        this.date = record.getStringDate();
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
