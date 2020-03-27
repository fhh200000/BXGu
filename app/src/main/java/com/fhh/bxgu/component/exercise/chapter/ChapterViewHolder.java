package com.fhh.bxgu.component.exercise.chapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;
import com.fhh.bxgu.R;
import androidx.recyclerview.widget.RecyclerView;


class ChapterViewHolder extends RecyclerView.ViewHolder {
    private TextView execTitle,execCount,execId;
    private View v;
    ChapterViewHolder(View v) {
        super(v);
        this.v = v;
        execId = v.findViewById(R.id.exercise_id);
        execCount = v.findViewById(R.id.exercise_count);
        execTitle = v.findViewById(R.id.exercise_name);
    }
    @SuppressLint("SetTextI18n")
    void bind(int id, String title, String count) {
        execId.setText(Integer.toString(id+1));
        execTitle.setText(title);
        execCount.setText(count);
    }
    void setOnClickListener(View.OnClickListener c) {
        v.setOnClickListener(c);
    }
}
