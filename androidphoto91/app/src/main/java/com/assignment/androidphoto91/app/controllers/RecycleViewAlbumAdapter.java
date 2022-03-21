package com.assignment.androidphoto91.app.controllers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.androidphoto91.R;
import com.assignment.androidphoto91.app.MainActivity;
import com.assignment.androidphoto91.components.Album;
import com.assignment.androidphoto91.components.Photo;

import java.util.ArrayList;

public class RecycleViewAlbumAdapter extends RecyclerView.Adapter<RecycleViewAlbumAdapter.AlbumViewHolder> {

    private ArrayList<Album> albumList;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView albumName;
        OnItemClickListener onItemClickListener;

        public AlbumViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            albumName = itemView.findViewById(R.id.textView_albumName);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public RecycleViewAlbumAdapter(ArrayList<Album> albumList, Context context, OnItemClickListener onItemClickListener) {
        this.albumList = albumList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_album_entry, parent, false);
        AlbumViewHolder albumViewHolder = new AlbumViewHolder(view, onItemClickListener);
        return albumViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.albumName.setText(albumList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
