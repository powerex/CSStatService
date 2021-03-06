package ua.azbest.csstatservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ua.azbest.csstatservice.MainActivity;
import ua.azbest.csstatservice.R;
import ua.azbest.csstatservice.dao.PictureDaoImplementation;
import ua.azbest.csstatservice.model.Picture;
import ua.azbest.csstatservice.model.Settings;

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
            startActivityForResult(intent, 1);
        });

        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
//            if (picture != null)
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
            startActivityForResult(intent, 1);
        }
        if (item.getItemId() == R.id.viewPictureRecord) {
            Intent intent = new Intent(PictureDetailActivity.this, RecordsListActivity.class);
            intent.putExtra("pictureId", picture.getId());
            intent.putExtra("pictureTitle", picture.getTitle());
//            startActivity(intent);
            startActivityForResult(intent, picture.getId());
        }
        if (item.getItemId() == R.id.viewPictureList) {
            Intent intent = new Intent(this, MainActivity.class);
            Settings.setActivePicture(this, 0);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAndSetIntentData() {
        if (getIntent().hasExtra("pictureId")) {
            PictureDaoImplementation dao = new PictureDaoImplementation(this);

            picture = dao.getPictureById(getIntent().getIntExtra("pictureId", 0));

            textViewPictureTitle.setText(picture.getTitle());
            textViewCrossStitchLeft.setText(String.valueOf(picture.getCrossStitchCount()));
        } else {
//            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0) {
        /*    PictureDaoImplementation dao = new PictureDaoImplementation(this);
            picture = dao.getPictureById(requestCode);
            textViewPictureTitle.setText(picture.getTitle());
            textViewCrossStitchLeft.setText(String.valueOf(picture.getCrossStitchCount()));
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                if (picture != null)
                    ab.setTitle(picture.getTitle());
            }*/
          recreate();
        }
    }

}
