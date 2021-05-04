package ua.azbest.csstatservice.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Calendar;

import ua.azbest.csstatservice.R;
import ua.azbest.csstatservice.dao.PictureDaoImplementation;
import ua.azbest.csstatservice.model.Picture;

public class AddPictureActivity extends AppCompatActivity {

    private static final String TAG = "AddPicture Main Activity";

    TextView title;
    TextView crossStitchCount;
    TextView startDate;
    TextView wishDate;
    Button addPictureButton;
    DatePickerDialog.OnDateSetListener startDateSetListener;
    DatePickerDialog.OnDateSetListener wishDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);

        title = findViewById(R.id.editTextPictureTitle);
        crossStitchCount = findViewById(R.id.editTextCrossStitchCount);
        startDate = findViewById(R.id.editTextStartDate);
        wishDate = findViewById(R.id.editTextWishDate);
        addPictureButton = findViewById(R.id.buttonAddPicture);

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
                    AddPictureActivity.this,
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
                    AddPictureActivity.this,
                    android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                    wishDateSetListener,
                    year, month, day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

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

        addPictureButton.setOnClickListener((v) -> {
            PictureDaoImplementation myDB = new PictureDaoImplementation(AddPictureActivity.this);
            Picture picture = new Picture(
                    title.getText().toString().trim(),
                    Integer.valueOf(crossStitchCount.getText().toString().trim()),
                    Picture.fromFormattedString(startDate.getText().toString().trim()),
                    Picture.fromFormattedString(wishDate.getText().toString().trim())
            );
            myDB.addPicture(picture);
            finish();
        });

    }


}