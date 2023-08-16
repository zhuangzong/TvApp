package org.tvapp.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.VerticalGridSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.VerticalGridPresenter;

import org.tvapp.R;
import org.tvapp.model.DataModel;
import org.tvapp.presenter.CustomVerticalGridPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.utils.Common;

import java.util.List;

public class GridFragment extends VerticalGridSupportFragment {

    private final ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new ImageCardPresenter(true));
    private final VerticalGridPresenter verticalGridPresenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, false);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAdapter(arrayObjectAdapter);
        verticalGridPresenter.setNumberOfColumns(6);
        setGridPresenter(verticalGridPresenter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DataModel dataModel = Common.getData(requireContext());
        int index = 0;
        if (getArguments() != null) {
            index = getArguments().getInt("index");
        }
        setTitle(dataModel.getResult().get(index).getTitle());
        List<DataModel.Detail> videos = dataModel.getResult().get(index).getDetails();
        for (DataModel.Detail element : videos) {
            arrayObjectAdapter.add(element);
        }
    }
}
