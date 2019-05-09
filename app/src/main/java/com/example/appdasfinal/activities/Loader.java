package com.example.appdasfinal.activities;

import android.view.View;

public interface Loader {

    View getContentView();

    View getProgressBar();

    default void showProgress(boolean show) {
        View contentView = getContentView();
        View progressBar = getProgressBar();
        if (contentView != null && progressBar != null) {
            contentView.setVisibility(show ? View.GONE : View.VISIBLE);
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
