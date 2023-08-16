package org.tvapp.presenter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.leanback.widget.RowPresenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.tvapp.databinding.DetailInfoBinding;
import org.tvapp.databinding.ItemGenreBinding;
import org.tvapp.model.DataModel;
import org.tvapp.ui.playback.PlaybackActivity;
import org.tvapp.utils.Common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DetailPresenter extends RowPresenter {

    private DetailInfoBinding binding;
    private Context context;

    @Override
    public boolean isUsingDefaultSelectEffect() {
        return false;
    }


    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        context = parent.getContext();
        binding = DetailInfoBinding.inflate(LayoutInflater.from(context), parent, false);
        setHeaderPresenter(null);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    protected void onBindRowViewHolder(ViewHolder vh, Object item) {
        if (item instanceof DataModel.Detail) {
            DataModel.Detail model = (DataModel.Detail) item;
            setDataToView(model);
        }
    }

    /**
     * set data to view
     *
     * @param model data
     */
    private void setDataToView(DataModel.Detail model) {
        binding.title.setText(model.getTitle());
        binding.subtitle.setText(String.format("Release date: %s", model.getRelease_date()));
        binding.rating.setText(String.format("Rating: %s", model.getVote_average()));
        binding.description.setText(model.getOverview());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.genres.setFocusable(false);
        binding.genres.setFocusableInTouchMode(false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.genres.setLayoutManager(layoutManager);
        List<String> allGenres = Common.getGenreList();
        allGenres.remove(0);
        List<String> genres = new ArrayList<>();
        for (int i = 0; i < model.getGenre_ids().size(); i++) {
            int random = (int) (Math.random() * allGenres.size());
            if(genres.contains(allGenres.get(random))){
                i--;
                continue;
            }
            genres.add(allGenres.get(random));
        }
        GenreAdapter adapter = new GenreAdapter(genres);
        binding.genres.setAdapter(adapter);
        Glide.with(context)
                .load("https://www.themoviedb.org/t/p/w780" + model.getBackdrop_path())
                .into(binding.imgBanner);

        binding.btPlay.setOnClickListener(v -> {
            if (context instanceof PlaybackActivity) {
                ((PlaybackActivity) context).finish();
            }
            Intent intent = new Intent(context, PlaybackActivity.class);
            intent.putExtra("data", (Serializable) (DataModel.Detail) model);
            context.startActivity(intent);
        });
    }

    class GenreAdapter extends RecyclerView.Adapter<GenreViewHolder> {

        private final List<String> genres;

        public GenreAdapter(List<String> genres) {
            this.genres = genres;
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
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
