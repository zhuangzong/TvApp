package org.tvapp.presenter;

import android.content.Context;

import androidx.leanback.widget.ShadowOverlayHelper;
import androidx.leanback.widget.VerticalGridPresenter;
import androidx.leanback.widget.VerticalGridView;

public class CustomVerticalGridPresenter extends VerticalGridPresenter {

    public CustomVerticalGridPresenter(int focusZoomFactor, boolean useFocusDimmer) {
        super(focusZoomFactor, useFocusDimmer);
    }


    @Override
    public boolean isUsingDefaultShadow() {
        return false;
    }

    @Override
    protected ShadowOverlayHelper.Options createShadowOverlayOptions() {
        ShadowOverlayHelper.Options options = new ShadowOverlayHelper.Options();
        options.dynamicShadowZ(0, 0);
        return options;
    }

    @Override
    public boolean isUsingZOrder(Context context) {
        return false;
    }

    @Override
    protected void initializeGridViewHolder(ViewHolder vh) {
        super.initializeGridViewHolder(vh);
        VerticalGridView gridView = vh.getGridView();
        int top = 20;
        int bottom = gridView.getPaddingBottom();
        int right = gridView.getPaddingRight();
        int left = 50;
        gridView.setPadding(left, top, right, bottom);
    }
}
