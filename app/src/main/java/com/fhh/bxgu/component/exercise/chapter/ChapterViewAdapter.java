package com.fhh.bxgu.component.exercise.chapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fhh.bxgu.R;
import com.fhh.bxgu.fragment.ExerciseFragment;

public class ChapterViewAdapter extends RecyclerView.Adapter<ChapterViewHolder> {
    private Context context;
    public ChapterViewAdapter(Context context, ChapterStorage.Callbacks fragment) {
        ChapterStorage.load(fragment);
        this.context = context;
    }
    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.single_exercise_chapter_bar,parent,false);
        return new ChapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, final int position) {
        Chapter chapter = ChapterStorage.chapters.get(position);
        holder.bind(position,chapter.getTitle(),context.getString(R.string.str_exec_count,chapter.getCount()));
        holder.setOnClickListener(v -> ((ExerciseFragment.Callbacks)context).onChapterSelected(chapter));
    }

    @Override
    public int getItemCount() {
        return ChapterStorage.chapters.size();
    }
}
