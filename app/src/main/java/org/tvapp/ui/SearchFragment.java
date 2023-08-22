package org.tvapp.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.SearchSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;

import com.google.gson.Gson;

import org.tvapp.db.DatabaseHelper;
import org.tvapp.db.bean.BaseResult;
import org.tvapp.db.bean.ListResult;
import org.tvapp.db.bean.ParamStruct;
import org.tvapp.db.bean.SearchResult;
import org.tvapp.db.callback.OnVideoSearchCallback;
import org.tvapp.model.DataModel;
import org.tvapp.presenter.CustomListRowPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.utils.Common;
import org.tvapp.utils.LogUtils;

public class SearchFragment extends SearchSupportFragment implements SearchSupportFragment.SearchResultProvider, OnVideoSearchCallback {

    ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRowsAdapter = new ArrayObjectAdapter(new CustomListRowPresenter());
        setSearchResultProvider(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query) && !query.equals("nil")) {
            query = "%" + query + "%";
        }
        ParamStruct paramStruct = new ParamStruct();
        paramStruct.setTitle(query);
        new DatabaseHelper.Builder(requireActivity())
                .setOnVideoSearchCallback(this)
                        .build()
                                .callVideoSearch(new Gson().toJson(paramStruct));

        return true;
    }

    @Override
    public void onVideoSearchComplete(String message) {
        LogUtils.d( message);
        BaseResult baseResult = new Gson().fromJson(message, BaseResult.class);
        if (baseResult.getCode().equals("0000")) {

            SearchResult searchResult = new Gson().fromJson(new Gson().toJson(baseResult.getData()), SearchResult.class);
            mRowsAdapter.clear();
            ArrayObjectAdapter adapterRows = new ArrayObjectAdapter(new ImageCardPresenter(false));
            adapterRows.addAll(0, searchResult.getMovies());
            HeaderItem headerItem = new HeaderItem("Search Result");
            ListRow listRow = new ListRow(headerItem, adapterRows);
            mRowsAdapter.add(listRow);
        }
    }
}
