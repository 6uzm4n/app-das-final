package com.example.appdasfinal.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.appdasfinal.R;

import java.util.Objects;

public class ErrorNotifier {

    public static void notifyEmptyField(View view) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(view), view.getContext().getString(R.string.error_empty_field), Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorError));
            snackbar.show();
        }
    }

    public static void notifyInternetConnection(View view) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(view), view.getContext().getString(R.string.error_internet_connection), Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorError));
            snackbar.show();
        }
    }

    public static void notifyServerError(View view, String message) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(view), message, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorError));
            snackbar.show();
        }
    }
}
