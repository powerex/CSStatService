package ua.azbest.csstatservice.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import ua.azbest.csstatservice.model.Picture;
import ua.azbest.csstatservice.model.Record;

public class RecordDaoImplementation extends SQLiteOpenHelper implements RecordDAO {

    private Context context;
    private static final String DATABASE_NAME = "CrossStitchLibrary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "records";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PICTURE_ID = "picture_id";
    private static final String COLUMN_CROSSES = "crosses";
    private static final String COLUMN_DATE = "date";

    public RecordDaoImplementation(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PICTURE_ID + " INTEGER, " +
                COLUMN_CROSSES + " INTEGER, " +
                COLUMN_DATE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void addRecord(@NotNull Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PICTURE_ID, record.getPictureId());
        cv.put(COLUMN_CROSSES, record.getCrosses());
        cv.put(COLUMN_DATE, record.getStringDate());
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC";;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    @Override
    public Cursor readDataByPictureId(int pictureID) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PICTURE_ID + "=" + pictureID + " ORDER BY " + COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    @Override
    public Cursor readDataByPictureIdIgnoreBig(int pictureID) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PICTURE_ID + "=" + pictureID + " AND " + COLUMN_CROSSES + "<10000" + " ORDER BY " + COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    @Override
    public List<Record> readRecordsByPictureId(int pictureID) {
        List<Record> recordList = new LinkedList<>();
        Cursor cursor = readDataByPictureId(pictureID);
        if (cursor.getCount() != 0) {
            recordList.clear();
            while (cursor.moveToNext()) {
                recordList.add(new Record(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        Picture.fromFormattedString(cursor.getString(3))
                ));
            }
        }
        return recordList;
    }

    @Override
    public void updateData(String row_id, Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CROSSES, record.getCrosses());
        cv.put(COLUMN_DATE, record.getStringDate());

        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Updated failure", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteOneRecord(String rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "id=?", new String[]{rowId});
        if (result == -1) {
            Toast.makeText(context, "Deleting failure", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    @Override
    public void deleteAllByPictureId(int pictureId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_PICTURE_ID + "=" + pictureId);
    }
}
