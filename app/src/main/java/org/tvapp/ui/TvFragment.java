package org.tvapp.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.VerticalGridPresenter;
import androidx.viewbinding.ViewBinding;

import com.google.gson.Gson;

import org.tvapp.base.BaseFragment;
import org.tvapp.databinding.FragmentMovieBinding;
import org.tvapp.databinding.FragmentTvBinding;
import org.tvapp.db.DatabaseHelper;
import org.tvapp.db.bean.BaseResult;
import org.tvapp.db.bean.ListResult;
import org.tvapp.db.bean.ParamStruct;
import org.tvapp.db.bean.TagVideo;
import org.tvapp.db.bean.VideoJoin;
import org.tvapp.db.callback.OnGetRecommendListCallback;
import org.tvapp.presenter.CategoryPresenter;
import org.tvapp.presenter.CustomVerticalGridPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.utils.LogUtils;
import org.tvapp.widget.FiltersWidget;

import java.util.List;
import java.util.stream.Collectors;


public class TvFragment extends BaseFragment implements OnGetRecommendListCallback {

    private FragmentTvBinding binding;

    private final ArrayObjectAdapter adapter = new ArrayObjectAdapter(new ImageCardPresenter(true));
    private final CustomVerticalGridPresenter verticalGridPresenter = new CustomVerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
    private ArrayObjectAdapter categoryAdapter;
    private final CategoryPresenter categoryPresenter = new CategoryPresenter();
    private ListResult result;
    private int selectedGenre = 0;
    private int selectedRegion = 0;
    private int selectedYear = 0;
    private int selectedRating = 0;
    private int selectedAudio = 0;

    @Override
    protected ViewBinding provideContentViewId(LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentTvBinding.inflate(inflater, container, false);
        return binding;
    }

    @Override
    public void initView() {
        super.initView();
        initCategory();
        initMovies();
    }

    @Override
    public void initData() {
        super.initData();
        ParamStruct paramStruct = new ParamStruct();
        paramStruct.setPage(1);
        paramStruct.setPageSize(100);
        paramStruct.setCategoryId(3);
        new DatabaseHelper
                .Builder(requireContext())
                .setOnGetRecommendListCallback(this).build()
                .callGetRecommendList(new Gson().toJson(paramStruct));
    }

    private void initCategory() {
        CustomVerticalGridPresenter verticalGridPresenter = new CustomVerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_LARGE, false);
        verticalGridPresenter.setNumberOfColumns(1);
        VerticalGridPresenter.ViewHolder mGridViewHolder = verticalGridPresenter.onCreateViewHolder(binding.frameCategory);

        categoryAdapter = new ArrayObjectAdapter(categoryPresenter);
        binding.frameCategory.addView(mGridViewHolder.view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        verticalGridPresenter.onBindViewHolder(mGridViewHolder, categoryAdapter);

        verticalGridPresenter.setOnItemViewClickedListener((itemViewHolder, item, rowViewHolder, row) -> {
            categoryPresenter.setSelectedCategory((String) item);
            categoryAdapter.notifyItemRangeChanged(0, categoryAdapter.size());
            setGridData(result.getList().stream()
                    .filter(result -> result.getTagTitle().equals(item))
                    .collect(Collectors.toList()).get(0).getVideoList());
        });
    }

    private void initMovies() {
        verticalGridPresenter.setNumberOfColumns(6);
        VerticalGridPresenter.ViewHolder mGridViewHolder = verticalGridPresenter.onCreateViewHolder(binding.frameContainer);
        binding.frameContainer.addView(mGridViewHolder.view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        verticalGridPresenter.onBindViewHolder(mGridViewHolder, adapter);
    }

    private void setGridData(List<VideoJoin> results) {
        adapter.clear();
        adapter.addAll(0, results);
        adapter.notifyItemRangeChanged(0, adapter.size());
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


    @Override
    public void onGetRecommendListComplete(String message) {
        requireActivity().runOnUiThread(() -> {
            Gson gson = new Gson();
            BaseResult baseResult = gson.fromJson(message, BaseResult.class);
            if (baseResult.getCode().equals("0000")) {
                result = gson.fromJson(gson.toJson(baseResult.getData()), ListResult.class);
                if (result.getList() != null && result.getList().size() > 0) {
                    List<String> tags = result.getList().stream().map(TagVideo::getTagTitle).collect(Collectors.toList());
                    categoryPresenter.setSelectedCategory(tags.get(0));
                    categoryAdapter.addAll(0, tags);
                    categoryAdapter.notifyItemRangeChanged(0, categoryAdapter.size());
                    setGridData(result.getList().get(0).getVideoList());
                }
            } else {
                LogUtils.e("onGetRecommendListComplete: " + baseResult.getMsg());
                Toast.makeText(requireContext(), baseResult.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
