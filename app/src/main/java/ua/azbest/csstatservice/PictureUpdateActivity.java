package ua.azbest.csstatservice;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Calendar;

import ua.azbest.csstatservice.dao.PictureDaoImplementation;
import ua.azbest.csstatservice.model.Picture;

public class PictureUpdateActivity extends AppCompatActivity {

    private final String TAG = "PictureUpdateActivity";

    TextView title;
    TextView crossStitchCount;
    TextView startDate;
    TextView wishDate;
    Button updatePictureButton;
    Button deletePictureButton;

    Picture picture;

    DatePickerDialog.OnDateSetListener startDateSetListener;
    DatePickerDialog.OnDateSetListener wishDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_update);

        title = findViewById(R.id.editTextPictureTitleUpdate);
        crossStitchCount = findViewById(R.id.editTextCrossStitchCountUpdate);
        startDate = findViewById(R.id.editTextStartDateUpdate);
        wishDate = findViewById(R.id.editTextWishDateUpdate);
        updatePictureButton = findViewById(R.id.buttonPictureUpdate);
        deletePictureButton = findViewById(R.id.buttonPictureDelete);

        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(picture.getTitle());
        }

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = String.format("%02d/%02d/%04d", dayOfMonth, month, year);
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + date);
                startDate.setText(date);
            }
        };

        wishDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = String.format("%02d/%02d/%04d", dayOfMonth, month, year);
                Log.d(TAG, "onDateSet: dd/MM/yyyy: " + date);
                wishDate.setText(date);
            }
        };

        updatePictureButton.setOnClickListener((v) -> {
            PictureDaoImplementation dao = new PictureDaoImplementation(PictureUpdateActivity.this);
            picture.setTitle(String.valueOf(title.getText()));
            picture.setCrossStitchCount(Integer.valueOf(String.valueOf(crossStitchCount.getText())));
            picture.setStartDate(Picture.fromFormattedString(String.valueOf(startDate.getText())));
            picture.setWishDate(Picture.fromFormattedString(String.valueOf(wishDate.getText())));
            dao.updateData(String.valueOf(picture.getId()), picture);
        });

        deletePictureButton.setOnClickListener((v) -> {
            confirmDialog();
        });

        startDate.setOnClickListener((v) -> {
            Calendar cal = Calendar.getInstance();
            int year, month, day;
            String sDate = String.valueOf(startDate.getText());
            if (sDate == null || sDate.equals("")) {
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            } else {
                LocalDate date = Picture.fromFormattedString(String.valueOf(startDate.getText()));
                year = date.getYear();
                month = date.getMonthValue();
                day = date.getDayOfMonth();
            }

            DatePickerDialog dialog = new DatePickerDialog(
                    PictureUpdateActivity.this,
                    android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                    startDateSetListener,
                    year, month, day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        wishDate.setOnClickListener((v) -> {
            Calendar cal = Calendar.getInstance();
            int year, month, day;
            String sDate = String.valueOf(wishDate.getText());
            if (sDate == null || sDate.equals("")) {
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            } else {
                LocalDate date = Picture.fromFormattedString(String.valueOf(wishDate.getText()));
                year = date.getYear();
                month = date.getMonthValue();
                day = date.getDayOfMonth();
            }

            DatePickerDialog dialog = new DatePickerDialog(
                    PictureUpdateActivity.this,
                    android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                    wishDateSetListener,
                    year, month, day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
    }

    private void getAndSetIntentData() {
        if (getIntent().hasExtra("pictureData")) {
            picture = (Picture)getIntent().getSerializableExtra("pictureData");

            title.setText(picture.getTitle());
            crossStitchCount.setText(String.valueOf(picture.getCrossStitchCount()));
            startDate.setText(picture.getStringStartDate());
            wishDate.setText(picture.getStringWishDate());
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + picture.getTitle() + "?");
        builder.setMessage("Are you sure you want to delete " + picture.getTitle() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PictureDaoImplementation dao = new PictureDaoImplementation(PictureUpdateActivity.this);
                dao.deleteOneRecord(String.valueOf(picture.getId()));
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
