package ua.azbest.csstatservice.dao;

import org.jetbrains.annotations.NotNull;

import ua.azbest.csstatservice.model.Record;

public interface RecordDAO {
    void addRecord(@NotNull Record record);
}
