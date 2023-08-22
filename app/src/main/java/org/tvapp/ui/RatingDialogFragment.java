package org.tvapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import org.tvapp.R;
import org.tvapp.databinding.DialogRatingBinding;

public class RatingDialogFragment extends DialogFragment {
    public static RatingDialogFragment newInstance() {
        return new RatingDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = new ContextThemeWrapper(getActivity(), androidx.appcompat.R.style.Theme_AppCompat_DayNight_Dialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogRatingBinding binding = DialogRatingBinding.inflate(requireActivity().getLayoutInflater());
        builder.setView(binding.getRoot());
        binding.ratingBar.requestFocus();
        binding.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            binding.title.setText(String.format("%s: %s", getResources().getString(R.string.rating), rating));
        });
        return builder.create();
    }
}
