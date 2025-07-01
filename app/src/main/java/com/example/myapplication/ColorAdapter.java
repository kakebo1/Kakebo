package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
    private final List<Integer> colorList;
    private final OnColorClickListener listener;

    public interface OnColorClickListener {
        void onColorSelected(int color);
    }

    public ColorAdapter(int[] colors, OnColorClickListener listener) {
        this.colorList = new ArrayList<>();
        for (int color : colors) {
            this.colorList.add(color);
        }
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        int color = colorList.get(position);
        holder.colorView.setBackgroundColor(color);
        holder.itemView.setOnClickListener(v -> listener.onColorSelected(color));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public static class ColorViewHolder extends RecyclerView.ViewHolder {
        ImageView colorView;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.colorPreview);
        }
    }
}
