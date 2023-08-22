package org.tvapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;

import org.tvapp.R;
import org.tvapp.databinding.ItemVideoCardBinding;
import org.tvapp.db.bean.TagVideo;
import org.tvapp.db.bean.VideoJoin;
import org.tvapp.model.DataModel;
import org.tvapp.ui.detail.DetailActivity;

import java.io.Serializable;

/**
 * the presenter for the banner item
 */
public class ImageCardPresenter extends Presenter {
    private Context context;
    private ItemVideoCardBinding binding;
    private final boolean axisVertical;

    public ImageCardPresenter(boolean axisVertical) {
        this.axisVertical = axisVertical;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        context = parent.getContext();
        binding = ItemVideoCardBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        if(item instanceof VideoJoin){
            VideoJoin content = (VideoJoin) item;
            ImageCardView imageCardView = binding.videoCard;
            imageCardView.setCardType(ImageCardView.CARD_TYPE_FLAG_IMAGE_ONLY);
            imageCardView.setInfoVisibility(ImageCardView.CARD_REGION_VISIBLE_ACTIVATED);
            imageCardView.setMainImageScaleType(ImageView.ScaleType.CENTER_CROP);
            Resources res = imageCardView.getResources();
            int width;
            int height;
            if(axisVertical){
                width = res.getDimensionPixelSize(R.dimen.card_ver_width);
                height = res.getDimensionPixelSize(R.dimen.card_ver_height);
            }else{
                width = res.getDimensionPixelSize(R.dimen.card_hor_width);
                height = res.getDimensionPixelSize(R.dimen.card_hor_height);
            }
            imageCardView.setMainImageDimensions(width, height);
            binding.tvCardTitle.setWidth(width);
            String url =  content.getVideoPic().replace("http://", "https://");
            Glide.with(context).load(url).into(imageCardView.getMainImageView());
            binding.tvCardTitle.setText(content.getTitle());
        }

        viewHolder.view.setOnClickListener(v -> {
            if (context instanceof DetailActivity) {
                ((DetailActivity) context).finish();
            }
            assert item instanceof VideoJoin;
            DetailActivity.launch(context, ((VideoJoin) item).getVideoId());
        });
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
