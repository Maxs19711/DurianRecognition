package com.example.cardview;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButton {

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;

    Animation fade_in;
    ProgressButton(Context ct, View view)
    {
        cardView = view.findViewById(R.id.cardView);
        layout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textView);
    }

    void buttonActivated()
    {
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Recognizing...");
    }
    void buttonFinished()
    {
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.purple_200));
        progressBar.setVisibility(View.GONE);
        textView.setText("Done");
    }
}
