package ua.azbest.csstatservice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ua.azbest.csstatservice.activity.AddPictureActivity;
import ua.azbest.csstatservice.dao.PictureDaoImplementation;
import ua.azbest.csstatservice.model.Picture;
import ua.azbest.csstatservice.view.CustomAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private static final String ACTIVE_PICTURE = "Active Picture";

    RecyclerView recyclerView;
    FloatingActionButton addPictureButton;
    ImageView imageView;
    TextView textView;

    PictureDaoImplementation myDB;
    List<Picture> gallery;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleView);
        imageView = findViewById(R.id.imageViewEmptyData);
        textView = findViewById(R.id.textViewNoData);

        /*activatePictureDetail = findViewById(R.id.pictureDetail);
        activatePictureDetail.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, PictureDetailActivity.class);
            startActivity(intent);
        });*/

        addPictureButton = findViewById(R.id.addPicture);
        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPictureActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        myDB = new PictureDaoImplementation(MainActivity.this);
        gallery = new ArrayList<>();

        storeDataInArray();
        customAdapter = new CustomAdapter(MainActivity.this, this,  gallery);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    public void storeDataInArray() {
        Cursor cursor = myDB.readAllData();
        Log.d(TAG, String.valueOf(cursor.getCount()));
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                gallery.add(new Picture(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)),
                        Picture.fromFormattedString(cursor.getString(3)),
                        Picture.fromFormattedString(cursor.getString(4))
                ));
            }
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAll) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete  All Data?");
        builder.setMessage("Are you sure you want to delete ALL data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                PictureDaoImplementation dao = new PictureDaoImplementation(MainActivity.this);
                dao.deleteAllData();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
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