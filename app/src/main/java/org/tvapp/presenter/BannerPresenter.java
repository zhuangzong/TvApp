package org.tvapp.presenter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.RowPresenter;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import org.tvapp.adapter.GenreAdapter;
import org.tvapp.databinding.BannerInfoBinding;
import org.tvapp.db.bean.BannerVideoResult;
import org.tvapp.db.bean.VideoTagsInfo;
import org.tvapp.utils.Common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * the presenter of banner
 */
public class BannerPresenter extends RowPresenter {

    private BannerInfoBinding binding;
    private Context context;

    @Override
    public boolean isUsingDefaultSelectEffect() {
        return false;
    }

    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        context = parent.getContext();
        binding = BannerInfoBinding.inflate(LayoutInflater.from(context), parent, false);
        setHeaderPresenter(null); // remove the default header
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindRowViewHolder(ViewHolder viewHolder, Object item) {
        if(item instanceof List){
            @SuppressWarnings("unchecked")
            List<BannerVideoResult> details = (List<BannerVideoResult>) item;
            BannerVideoResult model = details.get(0) ;
            setDataToView(model);
            setHorizontalGridView(details);
        }
    }

    /**
     * set data to view
     * @param details List<DataModel.Detail>
     */
    private void setHorizontalGridView(List<BannerVideoResult> details) {
        HorizontalGridView horizontalGridView = binding.horizontalGridView;
        BannerItemPresenter bannerItemPresenter = new BannerItemPresenter();
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(bannerItemPresenter);
        ItemBridgeAdapter adapter = new ItemBridgeAdapter(arrayObjectAdapter);
        FocusHighlightHelper.setupHeaderItemFocusHighlight(adapter);
        horizontalGridView.setAdapter(adapter);
        arrayObjectAdapter.addAll(0,details);

        adapter.setAdapterListener( new ItemBridgeAdapter.AdapterListener() {
            @Override
            public void onBind(ItemBridgeAdapter.ViewHolder vh) {
                super.onBind(vh);
                vh.itemView.setOnKeyListener((v, keyCode, event) -> {
                    if(event.getAction() == KeyEvent.ACTION_DOWN
                            && vh.getLayoutPosition() == 0
                            && keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                        vh.itemView.clearFocus();
                        return true;
                    }
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        setDataToView(details.get(vh.getLayoutPosition()));
                    }
                    return false;
                });
            }
        });

    }

    /**
     * set data to view
     * @param model data
     */
    private void setDataToView(BannerVideoResult model) {
        binding.title.setText(model.getTitle());
        binding.subtitle.setText(model.getCreatedAt().split(" ")[0]);
        binding.description.setText(model.getDescription());
        Glide.with(context)
                .load(model.getVideoPic().replace("http://", "https://"))
                .into(binding.imgBanner);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.genres.setFocusable(false);
        binding.genres.setFocusableInTouchMode(false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.genres.setLayoutManager(layoutManager);
        List<String> genres = Arrays.asList(model.getTags());
        GenreAdapter adapter = new GenreAdapter(genres,context);
        binding.genres.setAdapter(adapter);
    }
}
