package ua.azbest.csstatservice.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ua.azbest.csstatservice.MainActivity;
import ua.azbest.csstatservice.R;
import ua.azbest.csstatservice.dao.PictureDaoImplementation;
import ua.azbest.csstatservice.model.Picture;

public class LaunchActivity extends AppCompatActivity {

    private static final String TAG = "Launch Activity";
    private static final String ACTIVE_PICTURE = "active_picture";

    SharedPreferences settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(ACTIVE_PICTURE, MODE_PRIVATE);
        int activePicture = settings.getInt(ACTIVE_PICTURE, 0);
//        Resources res = getResources();
//        int activePicture = res.getInteger(R.integer.active_picture);

        if (activePicture == 0) {
            startBaseActivity();
        } else {
            Intent intent = new Intent(LaunchActivity.this, PictureDetailActivity.class);
            PictureDaoImplementation dao = new PictureDaoImplementation(LaunchActivity.this);
            Picture picture = dao.getPictureById(activePicture);
            if (picture != null) {
                intent.putExtra("pictureData", picture);
                startActivity(intent);
            } else
                startBaseActivity();
        }

//        SharedPreferences.Editor prefEditor = settings.edit();
//        prefEditor.putInt(ACTIVE_PICTURE_ID, 1);
//        prefEditor.apply();
//        Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(0)).itemView.performClick();

    }

    private void startBaseActivity() {
        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
