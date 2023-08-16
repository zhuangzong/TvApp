package org.tvapp.ui;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import org.tvapp.model.DataModel;
import org.tvapp.presenter.BannerPresenter;
import org.tvapp.presenter.CustomListRowPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.utils.Common;

import java.util.List;


public class HomeFragment extends RowsSupportFragment {

    private final CustomListRowPresenter rowPresenter = new CustomListRowPresenter();
    private final BannerPresenter bannerPresenter = new BannerPresenter();
    private final ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set the presenter of row
        setPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object item) {
                if (item instanceof ListRow) {
                    return rowPresenter;
                } else if (item instanceof List) {
                    return bannerPresenter;
                }
                return null;
            }
        });
        // get data from json file
        DataModel dataList = Common.getData(requireActivity());
        // set data to banner
        arrayObjectAdapter.add(dataList.getResult().get(0).getDetails().subList(0,5));
        // set data to rows
        for (DataModel.Result data : dataList.getResult()) {
            ArrayObjectAdapter adapterRows = new ArrayObjectAdapter(new ImageCardPresenter(false));
            adapterRows.addAll(0, data.getDetails());
            HeaderItem headerItem = new HeaderItem(data.getTitle());
            ListRow listRow = new ListRow(headerItem, adapterRows);
            arrayObjectAdapter.add(listRow);
        }
        // set adapter
        setAdapter(arrayObjectAdapter);
    }

}
