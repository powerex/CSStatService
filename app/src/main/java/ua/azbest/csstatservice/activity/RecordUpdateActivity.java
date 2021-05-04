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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Calendar;

import ua.azbest.csstatservice.R;
import ua.azbest.csstatservice.dao.RecordDaoImplementation;
import ua.azbest.csstatservice.model.Picture;
import ua.azbest.csstatservice.model.Record;

public class RecordUpdateActivity extends AppCompatActivity {

    private final String TAG = "Update Record Activity";

    private int recordId;
    private Record record;

    private TextView pictureTitleEdit;
    private EditText crossStitchCountEdit;
    private EditText chosenDateEdit;
    private Button buttonApplyRecordEdit;
    private Button buttonCancelEditing;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_record);
        pictureTitleEdit = findViewById(R.id.textViewPictureTitleInAddRecordEdit);
        crossStitchCountEdit = findViewById(R.id.editTextDayCrossStitchesEdit);
        chosenDateEdit = findViewById(R.id.editTextChosenDateEdit);
        buttonApplyRecordEdit = findViewById(R.id.buttonApplyRecordEdit);
        buttonCancelEditing = findViewById(R.id.buttonCancelEditing);

        getAndSetIntentData();

        buttonApplyRecordEdit.setOnClickListener((v)->{
            RecordDaoImplementation dao = new RecordDaoImplementation(RecordUpdateActivity.this);
            record.setCrosses(Integer.parseInt(String.valueOf(crossStitchCountEdit.getText())));
            record.setDate(Picture.fromFormattedString(String.valueOf(chosenDateEdit.getText())));
            dao.updateData(String.valueOf(record.getId()), record);
            finish();
        });

        buttonCancelEditing.setOnClickListener((v)->{
            finish();
        });

        chosenDateEdit.setOnClickListener((v) -> {
            Calendar cal = Calendar.getInstance();
            int year, month, day;
            String sDate = String.valueOf(chosenDateEdit.getText());
            if (sDate == null || sDate.equals("")) {
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            } else {
                LocalDate date = Picture.fromFormattedString(String.valueOf(chosenDateEdit.getText()));
                year = date.getYear();
                month = date.getMonthValue();
                day = date.getDayOfMonth();
            }

            DatePickerDialog dialog = new DatePickerDialog(
                    RecordUpdateActivity.this,
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
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + RecordUpdateActivity.this.chosenDateEdit);
                RecordUpdateActivity.this.chosenDateEdit.setText(chosenDate);
            }
        };
    }

    private void getAndSetIntentData() {
        final String intentRecordData = "recordData";
        final String intentPictureTitle = "pictureName";
        if (getIntent().hasExtra(intentRecordData)) {
            record = (Record)getIntent().getSerializableExtra(intentRecordData);
            crossStitchCountEdit.setText(String.valueOf(record.getCrosses()));
            chosenDateEdit.setText(record.getStringDate());
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
        if (getIntent().hasExtra(intentPictureTitle)) {
            pictureTitleEdit.setText(getIntent().getStringExtra(intentPictureTitle));
        }
    }
}
