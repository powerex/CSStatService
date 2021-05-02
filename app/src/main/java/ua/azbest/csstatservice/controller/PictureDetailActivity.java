package ua.azbest.csstatservice.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ua.azbest.csstatservice.R;
import ua.azbest.csstatservice.model.Picture;

public class PictureDetailActivity extends AppCompatActivity {

    private static final String TAG = "Picture Detail Activity";

    private Picture picture;

    FloatingActionButton addRecordButton;
    TextView textViewPictureTitle;
    TextView textViewCrossStitchLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picture_detail);
        addRecordButton = findViewById(R.id.floatingActionButtonRecordAdd);
        textViewPictureTitle = findViewById(R.id.textViewPictureTitle);
        textViewCrossStitchLeft = findViewById(R.id.textViewCrossStitchLeft);

        addRecordButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, AddRecordActivity.class);
            intent.putExtra("pictureDataId", picture.getId());
            intent.putExtra("pictureDataTitle", picture.getTitle());
            startActivity(intent);
        });

        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(picture.getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.picture_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editPicture) {
            Intent intent = new Intent(this, PictureUpdateActivity.class);
            intent.putExtra("pictureData", picture);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.viewPictureRecord) {
            Intent intent = new Intent(PictureDetailActivity.this, RecordsListActivity.class);
            intent.putExtra("pictureId", picture.getId());
            intent.putExtra("pictureTitle", picture.getTitle());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAndSetIntentData() {
        if (getIntent().hasExtra("pictureData")) {
            picture = (Picture)getIntent().getSerializableExtra("pictureData");

            textViewPictureTitle.setText(picture.getTitle());
            textViewCrossStitchLeft.setText(String.valueOf(picture.getCrossStitchCount()));
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

}
