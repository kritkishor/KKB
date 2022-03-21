package com.assignment.androidphoto91.app.controllers;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.androidphoto91.R;
import com.assignment.androidphoto91.components.Photo;

import java.util.ArrayList;

public class RecycleViewPhotoAdapter extends RecyclerView.Adapter<RecycleViewPhotoAdapter.PhotoViewHolder> {

    private int currentItemSelected;
    private boolean isNewClick = true;
    
    private ArrayList<Photo> photoList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    
    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView photoName;
        ImageView thumbnail;
        OnItemClickListener onItemClickListener;

        public PhotoViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            photoName = itemView.findViewById(R.id.textView_photoName);
            thumbnail = itemView.findViewById(R.id.imageView_thumbnail);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public RecycleViewPhotoAdapter(ArrayList<Photo> photoList, Context context, OnItemClickListener onItemClickListener) {
        this.photoList = photoList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_photo_entry, parent, false);
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(view, onItemClickListener);
        return photoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.photoName.setText(photoList.get(position).getName());
        //Uri uri = Uri.parse(photoList.get(position).getPath());
        //holder.thumbnail.setImageURI(uri);
        holder.thumbnail.setImageBitmap(photoList.get(position).getBitMap());
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


}