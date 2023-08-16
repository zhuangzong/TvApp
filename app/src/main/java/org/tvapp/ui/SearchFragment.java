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

import org.tvapp.model.DataModel;
import org.tvapp.presenter.CustomListRowPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.utils.Common;

public class SearchFragment extends SearchSupportFragment implements SearchSupportFragment.SearchResultProvider {

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
//        if (!TextUtils.isEmpty(newQuery) && !newQuery.equals("nil")) {
//            newQuery = "%" + newQuery + "%";
//        }
//        DataModel dataList = Common.getData(requireActivity());
//        ArrayObjectAdapter adapterRows = new ArrayObjectAdapter(new ImageCardPresenter(false));
//        adapterRows.addAll(0, dataList.getResult().get(0).getDetails());
//        HeaderItem headerItem = new HeaderItem("Search Result");
//        ListRow listRow = new ListRow(headerItem, adapterRows);
//        mRowsAdapter.add(listRow);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query) && !query.equals("nil")) {
            query = "%" + query + "%";
        }
        mRowsAdapter.clear();
        DataModel dataList = Common.getData(requireActivity());
        ArrayObjectAdapter adapterRows = new ArrayObjectAdapter(new ImageCardPresenter(false));
        adapterRows.addAll(0, dataList.getResult().get(0).getDetails());
        HeaderItem headerItem = new HeaderItem("Search Result");
        ListRow listRow = new ListRow(headerItem, adapterRows);
        mRowsAdapter.add(listRow);
        return true;
    }
}
