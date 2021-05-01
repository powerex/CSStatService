package ua.azbest.csstatservice.controller;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ua.azbest.csstatservice.R;
import ua.azbest.csstatservice.dao.RecordDaoImplementation;
import ua.azbest.csstatservice.model.Picture;
import ua.azbest.csstatservice.model.Record;
import ua.azbest.csstatservice.view.CustomRecordAdapter;

public class RecordsListActivity extends AppCompatActivity {

    private final String TAG = "Records List Activity";

    RecyclerView recyclerView;
    RecordDaoImplementation myDB;
    List<Record> recordList;
    int pictureId = 0;

    CustomRecordAdapter customRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        recyclerView = findViewById(R.id.recyclerViewRecordList);

        pictureId = getIntent().getIntExtra("pictureId", 0);
        String pictureTitle = getIntent().getStringExtra("pictureTitle");;

        myDB = new RecordDaoImplementation(RecordsListActivity.this);
        recordList = new ArrayList<>();
        storeDataInArray();
        customRecordAdapter = new CustomRecordAdapter(RecordsListActivity.this, recordList);
        recyclerView.setAdapter(customRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(RecordsListActivity.this));

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(pictureTitle);
        }
    }

    public void storeDataInArray() {
        Cursor cursor = null;
        if (pictureId == 0) {
            cursor =myDB.readAllData();
        } else {
            cursor = myDB.readDataByPictureId(pictureId);
        }
        Log.d(TAG, String.valueOf(cursor.getCount()));
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
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
    }

}
