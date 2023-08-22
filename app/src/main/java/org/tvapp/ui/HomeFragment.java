package org.tvapp.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.tvapp.db.DatabaseHelper;
import org.tvapp.db.bean.BaseResult;
import org.tvapp.db.bean.ListResult;
import org.tvapp.db.bean.ParamStruct;
import org.tvapp.db.bean.TagVideo;
import org.tvapp.db.callback.OnGetRecommendListCallback;
import org.tvapp.presenter.BannerPresenter;
import org.tvapp.presenter.CustomListRowPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.utils.LogUtils;

import java.util.List;


public class HomeFragment extends RowsSupportFragment implements OnGetRecommendListCallback {

    private final ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter();
    private int page = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        setPresenter();
        setAdapter(arrayObjectAdapter);
        setSelectedPosition(0, true);
        getVerticalGridView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (getSelectedPosition() == arrayObjectAdapter.size() - 2) {
                    page++;
                    initData();
                }
            }
        });
    }

    private void setPresenter() {
        BannerPresenter bannerPresenter = new BannerPresenter();
        CustomListRowPresenter rowPresenter = new CustomListRowPresenter();
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
    }


    private void initData() {
        ParamStruct paramStruct = new ParamStruct();
        paramStruct.setPage(page);
        paramStruct.setPageSize(5);
        paramStruct.setCategoryId(1);
        new DatabaseHelper
                .Builder(requireContext())
                .setOnGetRecommendListCallback(this).build()
                .callGetRecommendList(new Gson().toJson(paramStruct));
    }

    @Override
    public void onGetRecommendListComplete(String message) {
        requireActivity().runOnUiThread(() -> {
            Gson gson = new Gson();
            BaseResult baseResult = gson.fromJson(message, BaseResult.class);
            if (baseResult.getCode().equals("0000")) {
                ListResult listResult = gson.fromJson(gson.toJson(baseResult.getData()), ListResult.class);
                if (page == 1 && listResult.getBanner() != null && listResult.getBanner().size() > 0) {
                    arrayObjectAdapter.add(0, listResult.getBanner());
                }
                if (listResult.getList() != null && listResult.getList().size() > 0) {
                    for (TagVideo tagVideo : listResult.getList()) {
                        ArrayObjectAdapter adapterRows = new ArrayObjectAdapter(new ImageCardPresenter(false));
                        adapterRows.addAll(0, tagVideo.getVideoList());
                        HeaderItem headerItem = new HeaderItem(tagVideo.getTagTitle());
                        ListRow listRow = new ListRow(headerItem, adapterRows);
                        arrayObjectAdapter.add(listRow);
                    }
                }
            } else {
                LogUtils.d("onGetRecommendListComplete: " + baseResult.getMsg());
                Toast.makeText(requireContext(), baseResult.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
