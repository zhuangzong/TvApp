package org.tvapp.ui.detail;

import android.os.Bundle;
import android.util.Log;
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
import org.tvapp.presenter.DetailPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.utils.Common;

import java.util.List;


public class DetailFragment extends RowsSupportFragment {
    private final CustomListRowPresenter rowPresenter = new CustomListRowPresenter();
    private final DetailPresenter detailPresenter = new DetailPresenter();
    private final ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object item) {
                if (item instanceof ListRow) {
                    return rowPresenter;
                } else if (item instanceof DataModel.Detail) {
                    return detailPresenter;
                }
                return null;
            }
        });

        Bundle bundle = getArguments();
        DataModel.Detail data = bundle != null ? (DataModel.Detail) bundle.getSerializable("data") : null;
        arrayObjectAdapter.add(data);

        DataModel dataList = Common.getData(requireActivity());
        // set data to rows
        ArrayObjectAdapter adapterRows = new ArrayObjectAdapter(new ImageCardPresenter(false));
        adapterRows.addAll(0, dataList.getResult().get(0).getDetails());
        HeaderItem headerItem = new HeaderItem("Similar");
        ListRow listRow = new ListRow(headerItem, adapterRows);
        arrayObjectAdapter.add(listRow);
        // set adapter
        setAdapter(arrayObjectAdapter);
    }

}
