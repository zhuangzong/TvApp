package org.tvapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;

import org.tvapp.databinding.CardImageBinding;
import org.tvapp.model.DataModel;
import org.tvapp.ui.detail.DetailActivity;
import org.tvapp.utils.DisplayUtils;

/**
 * the presenter for the banner item
 */
public class BannerItemPresenter extends Presenter {
    private CardImageBinding binding;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        binding = CardImageBinding.inflate(LayoutInflater.from(context), parent, false);
        ViewGroup.LayoutParams lp = binding.imageCard.getLayoutParams();
        lp.width = DisplayUtils.getScreenWidth(context,16);
        lp.height = DisplayUtils.getScreenHeight(context, 19);
        binding.imageCard.setLayoutParams(lp);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        if(item instanceof DataModel.Detail){
            DataModel.Detail content = (DataModel.Detail) item;
            String url = "https://www.themoviedb.org/t/p/w500" + content.getPoster_path();
            Glide.with(viewHolder.view.getContext()).load(url).into(binding.imageCard);
        }
        viewHolder.view.setOnClickListener(v -> {
            Intent intent = new Intent(viewHolder.view.getContext(), DetailActivity.class);
            assert item instanceof DataModel.Detail;
            intent.putExtra("data", (DataModel.Detail) item);
            viewHolder.view.getContext().startActivity(intent);
        });
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
