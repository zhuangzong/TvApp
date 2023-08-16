package org.tvapp.ui;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.system.Settings;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.VerticalGridPresenter;
import androidx.leanback.widget.VerticalGridView;
import androidx.viewbinding.ViewBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.tvapp.R;
import org.tvapp.base.BaseFragment;
import org.tvapp.databinding.DialogFiltersBinding;
import org.tvapp.databinding.FragmentMovieBinding;
import org.tvapp.model.DataModel;
import org.tvapp.presenter.CustomVerticalGridPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.presenter.CategoryPresenter;
import org.tvapp.utils.Common;
import org.tvapp.utils.DisplayUtils;
import org.tvapp.widget.FiltersWidget;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class MovieFragment extends BaseFragment {

    private FragmentMovieBinding binding;
    private DataModel dataModel;
    private final ArrayObjectAdapter adapter = new ArrayObjectAdapter(new ImageCardPresenter(true));
    private final CustomVerticalGridPresenter verticalGridPresenter = new CustomVerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
    private VerticalGridPresenter.ViewHolder mGridViewHolder;

    private int selectedGenre = 0;
    private int selectedRegion = 0;
    private int selectedYear = 0;
    private int selectedRating = 0;
    private int selectedAudio = 0;

    @Override
    protected ViewBinding provideContentViewId(LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentMovieBinding.inflate(inflater, container, false);
        return binding;
    }

    @Override
    public void initView() {
        super.initView();
        dataModel = Common.getData(requireContext());
        initCategory();
        initMovies();
    }

    private void initCategory() {
        List<String> titles = dataModel.getResult().stream().map(DataModel.Result::getTitle).collect(Collectors.toList());
        CustomVerticalGridPresenter verticalGridPresenter = new CustomVerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
        verticalGridPresenter.setNumberOfColumns(1);
        VerticalGridPresenter.ViewHolder mGridViewHolder = verticalGridPresenter.onCreateViewHolder(binding.frameCategory);
        CategoryPresenter categoryPresenter = new CategoryPresenter();
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(categoryPresenter);

        binding.frameCategory.addView(mGridViewHolder.view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        verticalGridPresenter.onBindViewHolder(mGridViewHolder, arrayObjectAdapter);
        categoryPresenter.setSelectedCategory(titles.get(0));
        arrayObjectAdapter.addAll(0, titles);
        verticalGridPresenter.setOnItemViewClickedListener((itemViewHolder, item, rowViewHolder, row) -> {
            categoryPresenter.setSelectedCategory((String) item);
            arrayObjectAdapter.notifyItemRangeChanged(0, arrayObjectAdapter.size());
            setGridData(dataModel.getResult().stream()
                    .filter(result -> result.getTitle().equals(item))
                    .collect(Collectors.toList()).get(0).getDetails());
        });
    }

    private void initMovies() {
        verticalGridPresenter.setNumberOfColumns(5);
        mGridViewHolder = verticalGridPresenter.onCreateViewHolder(binding.frameContainer);
        binding.frameContainer.addView(mGridViewHolder.view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        setGridData(dataModel.getResult().get(0).getDetails());
    }

    private void setGridData(List<DataModel.Detail> results) {
        adapter.clear();
        adapter.addAll(0, results);
        verticalGridPresenter.onBindViewHolder(mGridViewHolder, adapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        binding.tvFilter.setOnClickListener(v -> {
            new FiltersWidget.Builder(requireContext())
                    .setSelectedGenre(selectedGenre)
                    .setSelectedRegion(selectedRegion)
                    .setSelectedYear(selectedYear)
                    .setSelectedRating(selectedRating)
                    .setSelectedAudio(selectedAudio)
                    .setOnFilterListener((genre, region, year, rating, audio) -> {
                        this.selectedGenre = genre;
                        this.selectedRegion = region;
                        this.selectedYear = year;
                        this.selectedRating = rating;
                        this.selectedAudio = audio;
                    })
                    .build();
        });
    }



}
