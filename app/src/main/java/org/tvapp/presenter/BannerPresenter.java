package org.tvapp.presenter;


import android.content.Context;
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

import com.bumptech.glide.Glide;

import org.tvapp.databinding.BannerInfoBinding;
import org.tvapp.model.DataModel;

import java.util.List;

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
            List<DataModel.Detail> details = (List<DataModel.Detail>) item;
            DataModel.Detail model = details.get(0) ;
            setDataToView(model);
            setHorizontalGridView(details);
        }
    }

    /**
     * set data to view
     * @param details List<DataModel.Detail>
     */
    private void setHorizontalGridView(List<DataModel.Detail> details) {
        HorizontalGridView horizontalGridView = binding.horizontalGridView;
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new BannerItemPresenter());
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
    private void setDataToView(DataModel.Detail model) {
        binding.title.setText(model.getTitle());
        binding.subtitle.setText(model.getRelease_date());
        binding.description.setText(model.getOverview());
        Glide.with(context)
                .load("https://www.themoviedb.org/t/p/w780" +model.getBackdrop_path())
                .into(binding.imgBanner);

    }
}
