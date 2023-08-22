package org.tvapp.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import com.google.gson.Gson;

import org.tvapp.base.Constants;
import org.tvapp.db.DatabaseHelper;
import org.tvapp.db.bean.BaseResult;
import org.tvapp.db.bean.ListResult;
import org.tvapp.db.bean.ParamStruct;
import org.tvapp.db.bean.TagVideo;
import org.tvapp.db.bean.VideoDetailInfo;
import org.tvapp.db.callback.OnGetRecommendListCallback;
import org.tvapp.db.callback.OnGetVideoDetailCallback;
import org.tvapp.model.DataModel;
import org.tvapp.presenter.BannerPresenter;
import org.tvapp.presenter.CustomListRowPresenter;
import org.tvapp.presenter.DetailPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.utils.Common;
import org.tvapp.utils.LogUtils;

import java.util.List;


public class DetailFragment extends RowsSupportFragment implements OnGetVideoDetailCallback, OnGetRecommendListCallback {
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
                } else if (item instanceof VideoDetailInfo) {
                    return detailPresenter;
                }
                return null;
            }
        });
        initData();
        setAdapter(arrayObjectAdapter);
    }

    private void initData() {
        ParamStruct paramStruct = new ParamStruct();
        paramStruct.setId(getArguments() != null ? getArguments().getInt(Constants.EXTRA_ID) : 0);
        new DatabaseHelper.Builder(requireContext())
                .setOnGetVideoDetailCallback(this)
                .build()
                .callGetVideoDetail(new Gson().toJson(paramStruct));
    }

    private void initSimilar() {
        ParamStruct paramStruct = new ParamStruct();
        paramStruct.setCategoryId(1);
        paramStruct.setPage(1);
        paramStruct.setPageSize(1);
        new DatabaseHelper.Builder(requireContext())
                .setOnGetRecommendListCallback(this)
                .build()
                .callGetRecommendList(new Gson().toJson(paramStruct));
    }

    @Override
    public void onGetVideoDetailComplete(String message) {
        requireActivity().runOnUiThread(() -> {
            BaseResult baseResult = new Gson().fromJson(message, BaseResult.class);
            if (baseResult.getCode().equals("0000")) {
                VideoDetailInfo videoDetailInfo = new Gson().fromJson(new Gson().toJson(baseResult.getData()), VideoDetailInfo.class);
                arrayObjectAdapter.add(videoDetailInfo);
                initSimilar();
            } else {
                Log.d("DetailFragment", "onGetVideoDetailComplete: " + baseResult.getMsg());
                Toast.makeText(requireContext(), baseResult.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onGetRecommendListComplete(String message) {
        requireActivity().runOnUiThread(() -> {
            BaseResult baseResult = new Gson().fromJson(message, BaseResult.class);
            if (baseResult.getCode().equals("0000")) {
                TagVideo details = new Gson().fromJson(new Gson().toJson(baseResult.getData()), ListResult.class).getList().get(0);
                ArrayObjectAdapter adapterRows = new ArrayObjectAdapter(new ImageCardPresenter(false));
                adapterRows.addAll(0, details.getVideoList());
                HeaderItem headerItem = new HeaderItem("Similar");
                ListRow listRow = new ListRow(headerItem, adapterRows);
                arrayObjectAdapter.add(listRow);
            }
        });
    }
}
