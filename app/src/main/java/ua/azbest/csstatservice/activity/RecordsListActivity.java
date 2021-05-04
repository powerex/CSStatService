package ua.azbest.csstatservice.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        customRecordAdapter = new CustomRecordAdapter(RecordsListActivity.this, this, recordList);
        recyclerView.setAdapter(customRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(RecordsListActivity.this));

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(pictureTitle);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            recreate();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.record_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllRecords) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Records?");
        builder.setMessage("Are you sure you want to delete ALL data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(RecordsListActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                RecordDaoImplementation dao = new RecordDaoImplementation(RecordsListActivity.this);
                dao.deleteAllByPictureId(pictureId);
                Intent intent = new Intent(RecordsListActivity.this, RecordsListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

}
