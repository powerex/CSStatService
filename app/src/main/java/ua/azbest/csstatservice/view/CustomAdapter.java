package ua.azbest.csstatservice.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ua.azbest.csstatservice.R;
import ua.azbest.csstatservice.activity.PictureDetailActivity;
import ua.azbest.csstatservice.model.Picture;
import ua.azbest.csstatservice.model.Settings;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private List<Picture> gallery;
    Activity activity;
    private int position;
    private SharedPreferences settings;
    private static final String ACTIVE_PICTURE = "active_picture";

    Animation translate_anim;

    public CustomAdapter(Activity activity, Context context, List<Picture> gallery) {
        this.activity = activity;
        this.context = context;
        this.gallery = gallery;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.picture_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        this.position = position;
        holder.pictureId.setText(String.valueOf(position + 1));
        holder.title.setText(gallery.get(position).getTitle());
        holder.crossStitchCount.setText(String.valueOf(gallery.get(position).getCrossStitchCount()));
        holder.startDate.setText(gallery.get(position).getStringStartDate());
        holder.wishDate.setText(gallery.get(position).getStringWishDate());
        holder.rowLayout.setOnClickListener((v) -> {
            Intent intent = new Intent(context, PictureDetailActivity.class);
            intent.putExtra("pictureId", gallery.get(position).getId());

            int lastId = gallery.get(position).getId();
            Settings.setActivePicture(context, lastId);

            activity.startActivityForResult(intent, 1);
        });

    }

    private void setLastActivePicture() {

    }

    @Override
    public int getItemCount() {
        return gallery.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pictureId, title, crossStitchCount, startDate, wishDate;
        LinearLayout rowLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pictureId = itemView.findViewById(R.id.textViewPictureId);
            title = itemView.findViewById(R.id.textViewTitle);
            crossStitchCount = itemView.findViewById(R.id.textViewCrossStitchCount);
            startDate = itemView.findViewById(R.id.textViewStartDate);
            wishDate = itemView.findViewById(R.id.textViewWishDate);
            rowLayout = itemView.findViewById(R.id.rowLayout);
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            rowLayout.setAnimation(translate_anim);
        }
    }

}
