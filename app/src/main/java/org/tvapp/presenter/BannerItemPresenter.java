package org.tvapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowId;

import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;

import org.tvapp.databinding.CardImageBinding;
import org.tvapp.db.bean.BannerVideoResult;
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
        lp.height = DisplayUtils.getScreenHeight(context, 23);
        binding.imageCard.setLayoutParams(lp);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        if(item instanceof BannerVideoResult){
            BannerVideoResult content = (BannerVideoResult) item;
            String url =  content.getVideoPic().replace("http://", "https://");
            Glide.with(viewHolder.view.getContext()).load(url).into(binding.imageCard);
        }
        setOnClickListener(viewHolder, v -> {
            assert item instanceof BannerVideoResult;
            DetailActivity.launch(viewHolder.view.getContext(), ((BannerVideoResult) item).getVideoId());
        });
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
