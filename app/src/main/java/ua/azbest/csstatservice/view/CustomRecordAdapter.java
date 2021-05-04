package ua.azbest.csstatservice.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ua.azbest.csstatservice.R;
import ua.azbest.csstatservice.activity.RecordUpdateActivity;
import ua.azbest.csstatservice.dao.PictureDaoImplementation;
import ua.azbest.csstatservice.dao.RecordDaoImplementation;
import ua.azbest.csstatservice.model.Record;

public class CustomRecordAdapter extends RecyclerView.Adapter<CustomRecordAdapter.RecordHolder> {

    private Context context;
    Activity activity;
    private List<Record> recordList;

    public CustomRecordAdapter(Activity activity, Context context, List<Record> recordList) {
        this.activity = activity;
        this.context = context;
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.record_row, parent, false);
        return new RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
//        holder.textViewRecordId.setText(String.valueOf(recordList.get(position).getId()));
        holder.textViewRecordId.setText(String.valueOf(position+1));
        holder.textViewRecordCrossCount.setText(String.valueOf(recordList.get(position).getCrosses()));
        holder.textViewDate.setText(recordList.get(position).getStringDate());
        holder.imageButtonDelete.setOnClickListener((v)->{
            RecordDaoImplementation myDB = new RecordDaoImplementation(context);
            myDB.deleteOneRecord(String.valueOf(recordList.get(position).getId()));
            activity.recreate();
        });
        holder.imageButtonEdit.setOnClickListener((v) -> {
            Intent intent = new Intent(context,
                    RecordUpdateActivity.class);
            PictureDaoImplementation pictureDao = new PictureDaoImplementation(context);
            intent.putExtra("pictureName",
                    pictureDao.getPictureById(
                            recordList.get(position).getPictureId()).getTitle()
            );
            intent.putExtra("recordData", recordList.get(position));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {

        TextView textViewRecordId, textViewRecordCrossCount, textViewDate;
        ImageButton imageButtonDelete, imageButtonEdit;

        public RecordHolder(@NonNull View itemView) {
            super(itemView);
            textViewRecordId = itemView.findViewById(R.id.textViewRecordNumber);
            textViewRecordCrossCount = itemView.findViewById(R.id.textViewCrossesCount);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            imageButtonDelete = itemView.findViewById(R.id.imageButtonDelete);
            imageButtonEdit = itemView.findViewById(R.id.imageButtonEdit);
        }
    }

}
