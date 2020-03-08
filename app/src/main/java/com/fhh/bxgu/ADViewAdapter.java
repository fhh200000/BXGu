package com.fhh.bxgu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ADViewAdapter extends RecyclerView.Adapter<ADViewHolder> {
    private Context context;
    private boolean isLandscape;
    @NonNull
    @Override
    public ADViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new ImageView(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new ADViewHolder(view);
    }
    ADViewAdapter(Context context,boolean isLandscape) {
        this.context = context;
        this.isLandscape = isLandscape;
    }
    @Override
    public void onBindViewHolder(@NonNull ADViewHolder holder, int position) {
        if(isLandscape) {
            holder.setRes(StaticVariablePlacer.adBannerStorage.getBanner(position).getBitmapPort());
        }
        else
        {
            holder.setRes(StaticVariablePlacer.adBannerStorage.getBanner(position).getBitmap());
        }
    }

    @Override
    public int getItemCount() {
        return StaticVariablePlacer.adBannerStorage.size();
    }
}
