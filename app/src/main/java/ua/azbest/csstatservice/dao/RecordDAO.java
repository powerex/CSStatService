package ua.azbest.csstatservice.dao;

import android.database.Cursor;

import org.jetbrains.annotations.NotNull;

import ua.azbest.csstatservice.model.Picture;
import ua.azbest.csstatservice.model.Record;

public interface RecordDAO {
    void addRecord(@NotNull Record record);
    Cursor readAllData();
    Cursor readDataByPictureId(int pictureID);
    void updateData(String row_id, Record record);
    void deleteOneRecord(String rowId);
    void deleteAllData();
}
