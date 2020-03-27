package com.fhh.bxgu.component.exercise.question;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fhh.bxgu.R;

class QuestionViewHolder extends RecyclerView.ViewHolder {
    private TextView[] choices;
    private TextView title;
    private int answer;

    QuestionViewHolder(@NonNull View itemView) {
        super(itemView);
        choices = new TextView[4];
        choices[0] = itemView.findViewById(R.id.choice_a);
        choices[1] = itemView.findViewById(R.id.choice_b);
        choices[2] = itemView.findViewById(R.id.choice_c);
        choices[3] = itemView.findViewById(R.id.choice_d);
        title = itemView.findViewById(R.id.exercise_title);
    }
    @SuppressLint("DefaultLocale")
    void bind(Question question,int pos) {
        choices[0].setText(question.getA());
        choices[1].setText(question.getB());
        choices[2].setText(question.getC());
        choices[3].setText(question.getD());
        answer = question.getAnswer();
        title.setText(String.format("%d.%s",pos+1,question.getTitle()));
        for(TextView i:choices) {
            i.setOnClickListener(checkAnswer);
        }
    }
    private View.OnClickListener checkAnswer = v -> {
        for(TextView i:choices) {
            if(v==i) {
                i.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.choice_error, 0, 0, 0);
            }
        }
        choices[answer].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.choice_right, 0, 0, 0);
    };
}
