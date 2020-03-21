package com.fhh.bxgu.component.exercise.chapter;

import android.view.View;
import android.widget.TextView;
import com.fhh.bxgu.R;
import androidx.recyclerview.widget.RecyclerView;


public class ChapterViewHolder extends RecyclerView.ViewHolder {
    private TextView execTitle,execCount;
    ChapterViewHolder(View v) {
        super(v);
        execCount = v.findViewById(R.id.exercise_count);
        execTitle = v.findViewById(R.id.exercise_name);
    }
    void bind(String title,String count) {
        execTitle.setText(title);
        execCount.setText(count);
    }
}
