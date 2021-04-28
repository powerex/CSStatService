package ua.azbest.csstatservice.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;

import ua.azbest.csstatservice.model.Picture;

public class PictureDaoImplementation extends SQLiteOpenHelper implements PictureDAO {

    private Context context;
    private static final String DATABASE_NAME = "CrossStitchLibrary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "picture";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "picture_title";
    private static final String COLUMN_CROSSES = "all_crosses";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_WISH_DATE = "wish_date";

    public PictureDaoImplementation(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CROSSES + " INTEGER, " +
                COLUMN_START_DATE + " TEXT,  " +
                COLUMN_WISH_DATE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void addPicture(@NotNull Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, picture.getTitle());
        cv.put(COLUMN_CROSSES, picture.getCrossStitchCount());
        cv.put(COLUMN_START_DATE, picture.getStringStartDate());
        cv.put(COLUMN_WISH_DATE, picture.getStringWishDate());
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Cursor readAllData() {
       String query = "SELECT * FROM " + TABLE_NAME;
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = null;
       if (db != null) {
           cursor = db.rawQuery(query, null);
       }
       return cursor;
    }

    @Override
    public void updateData(String row_id, Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, picture.getTitle());
        cv.put(COLUMN_CROSSES, picture.getCrossStitchCount());
        cv.put(COLUMN_START_DATE, picture.getStringStartDate());
        cv.put(COLUMN_WISH_DATE, picture.getStringWishDate());

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

}
