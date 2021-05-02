package ua.azbest.csstatservice.dao;

import android.database.Cursor;

import org.jetbrains.annotations.NotNull;

import ua.azbest.csstatservice.model.Picture;

public interface PictureDAO {
    void addPicture(@NotNull Picture picture);
    Cursor readAllData();
    void updateData(String row_id, Picture picture);
    void deleteOneRecord(String rowId);
    void deleteAllData();
    Picture getPictureById(int id);
}
