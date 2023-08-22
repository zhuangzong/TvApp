package org.tvapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tvapp.databinding.ItemGenreBinding;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private final List<String> genres;
    private Context context;

    public GenreAdapter(List<String> genres, Context context) {
        this.genres = genres;
        this.context = context;
    }

    private ItemGenreBinding binding;

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemGenreBinding.inflate(LayoutInflater.from(context), parent, false);
        binding.getRoot().setFocusable(false);
        binding.getRoot().setFocusableInTouchMode(false);
        return new GenreViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        binding.tvItemGenre.setText(genres.get(position));
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}


