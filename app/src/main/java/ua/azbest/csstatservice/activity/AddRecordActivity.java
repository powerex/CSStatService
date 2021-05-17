package ua.azbest.csstatservice.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Calendar;

import ua.azbest.csstatservice.R;
import ua.azbest.csstatservice.dao.RecordDaoImplementation;
import ua.azbest.csstatservice.model.Picture;
import ua.azbest.csstatservice.model.Record;

public class AddRecordActivity extends AppCompatActivity {

    private final String TAG = "Add Record Activity";

    private int pictureId;
    private String title;

    private TextView pictureTitle;
    private EditText crossStitchCount;
    private EditText chosenDate;
    private Button buttonAddRecord;
    private Button buttonCancelAdding;
    private DatePickerDialog.OnDateSetListener dateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_record);
        pictureTitle = findViewById(R.id.textViewPictureTitleInAddRecord);
        crossStitchCount = findViewById(R.id.editTextDayCrossStitches);
        crossStitchCount.setText("0");
        crossStitchCount.setSelectAllOnFocus(true);
        chosenDate = findViewById(R.id.editTextChosenDate);
        buttonAddRecord = findViewById(R.id.buttonAddRecord);
        buttonCancelAdding = findViewById(R.id.buttonCancelAdding);


        Calendar calendar = Calendar.getInstance();
        int yy = 2021, mm = 1, dd = 1;
        yy = calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH);
        dd = calendar.get(Calendar.DAY_OF_MONTH);
        chosenDate.setText(String.format("%02d/%02d/%04d", dd, mm, yy));

        pictureId = getIntent().getIntExtra("pictureDataId", 1);
        title = getIntent().getStringExtra("pictureDataTitle");
        pictureTitle.setText(title);

        buttonAddRecord.setOnClickListener((v) -> {

            RecordDaoImplementation myDB = new RecordDaoImplementation(AddRecordActivity.this);
            String stringDate = "01/01/2000";
            if (chosenDate.getText() != null || !chosenDate.getText().equals("")) {
                stringDate = chosenDate.getText().toString().trim();
                if (stringDate.equals(""))
                    stringDate = "01/01/2000";
            }
            Record record = new Record(
                    pictureId,
                    Integer.valueOf(crossStitchCount.getText().toString().trim()),
                    Picture.fromFormattedString(stringDate)
            );
            myDB.addRecord(record);
            finish();
        });

        buttonCancelAdding.setOnClickListener((v) -> {
            finish();
        });

        chosenDate.setOnClickListener((v) -> {
            Calendar cal = Calendar.getInstance();
            int year, month, day;
            String sDate = String.valueOf(chosenDate.getText());
            if (sDate == null || sDate.equals("")) {
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            } else {
                LocalDate localDate = Picture.fromFormattedString(String.valueOf(chosenDate.getText()));
                year = localDate.getYear();
                month = localDate.getMonthValue();
                day = localDate.getDayOfMonth();
            }

            DatePickerDialog dialog = new DatePickerDialog(
                    AddRecordActivity.this,
                    android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                    dateSetListener,
                    year, month, day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String chosenDate = String.format("%02d/%02d/%04d", dayOfMonth, month, year);
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + AddRecordActivity.this.chosenDate);
                AddRecordActivity.this.chosenDate.setText(chosenDate);
            }
        };
    }

}
