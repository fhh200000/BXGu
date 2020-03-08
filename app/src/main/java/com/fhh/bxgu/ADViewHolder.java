package com.fhh.bxgu;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ADViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    ADViewHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView;
    }

    void setRes(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}


