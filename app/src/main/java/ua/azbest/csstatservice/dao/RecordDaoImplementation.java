package ua.azbest.csstatservice.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

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
}
