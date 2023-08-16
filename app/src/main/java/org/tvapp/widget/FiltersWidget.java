package org.tvapp.widget;


import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.VerticalGridPresenter;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.tvapp.databinding.DialogFiltersBinding;
import org.tvapp.presenter.CategoryPresenter;
import org.tvapp.presenter.CustomVerticalGridPresenter;
import org.tvapp.utils.Common;
import org.tvapp.utils.DisplayUtils;

import java.util.List;
import java.util.Objects;

public class FiltersWidget {

    private DialogFiltersBinding binding;
    private int mSelectedGenre = 0;
    private int mSelectedRegion = 0;
    private int mSelectedYear = 0;
    private int mSelectedRating = 0;
    private int mSelectedAudio = 0;
    private Context context;
    private OnFilterListener onFilterListener;

    private FiltersWidget() {
    }

    private void showFiltersPop() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        binding = DialogFiltersBinding.inflate(LayoutInflater.from(context), null, false);
        bottomSheetDialog.setContentView(binding.getRoot());
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        ViewGroup.LayoutParams layoutParams = binding.getRoot().getLayoutParams();
        layoutParams.height = DisplayUtils.getScreenHeight(context, 60);
        binding.getRoot().setLayoutParams(layoutParams);
        bottomSheetDialog.show();
        setGridView(0);
        setGridView(1);
        setGridView(2);
        setGridView(3);
        setGridView(4);
    }
    private void setGridView(int columnIndex) {
        int selectIndex = 0;
        ViewGroup view = null;
        List<String> lists = null;
        switch (columnIndex) {
            case 0:
                view = binding.filtersFrameGenre;
                lists = Common.getGenreList();
                selectIndex = this.mSelectedGenre;
                break;
            case 1:
                view = binding.filtersFrameRegion;
                lists = Common.getRegionList();
                selectIndex = this.mSelectedRegion;
                break;
            case 2:
                view = binding.filtersFrameYear;
                lists = Common.getYearList();
                selectIndex = this.mSelectedYear;
                break;
            case 3:
                view = binding.filtersFrameRating;
                lists = Common.getRatingList();
                selectIndex = this.mSelectedRating;
                break;
            case 4:
                view = binding.filtersFrameAudio;
                lists = Common.getAudioList();
                selectIndex = this.mSelectedAudio;
                break;
        }


        CustomVerticalGridPresenter verticalGridPresenter = new CustomVerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
        verticalGridPresenter.setNumberOfColumns(1);
        VerticalGridPresenter.ViewHolder mGridViewHolder = verticalGridPresenter.onCreateViewHolder(view);
        CategoryPresenter categoryPresenter = new CategoryPresenter();
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(categoryPresenter);
        if (view != null) {
            view.addView(mGridViewHolder.view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        }
        categoryPresenter.setSelectedCategory(lists != null ? lists.get(selectIndex) : null);
        arrayObjectAdapter.addAll(0, lists);
        verticalGridPresenter.onBindViewHolder(mGridViewHolder, arrayObjectAdapter);

        List<String> finalLists = lists;
        verticalGridPresenter.setOnItemViewClickedListener((itemViewHolder, item, rowViewHolder, row) -> {
            categoryPresenter.setSelectedCategory((String) item);
            arrayObjectAdapter.notifyItemRangeChanged(0, arrayObjectAdapter.size());
            int selectedIndex = finalLists.indexOf(item);
            itemViewHolder.view.setOnKeyListener((v, keyCode, event) -> {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                    onFilterListener.onFilter(mSelectedGenre, mSelectedRegion, mSelectedYear, mSelectedRating, mSelectedAudio);
                }
                return false;
            });
            switch (columnIndex) {
                case 0:
                    this.mSelectedGenre = selectedIndex;
                    break;
                case 1:
                    this.mSelectedRegion = selectedIndex;
                    break;
                case 2:
                    this.mSelectedYear = selectedIndex;
                    break;
                case 3:
                    this.mSelectedRating = selectedIndex;
                    break;
                case 4:
                    this.mSelectedAudio = selectedIndex;
                    break;
            }
        });
    }

    public interface OnFilterListener {
        void onFilter(int genre, int region, int year, int rating, int audio);
    }

    public static class Builder {
        private int selectedGenre = 0;
        private int selectedRegion = 0;
        private int selectedYear = 0;
        private int selectedRating = 0;
        private int selectedAudio = 0;
        private final Context context;
        private OnFilterListener onFilterListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setSelectedGenre(int selectedGenre) {
            this.selectedGenre = selectedGenre;
            return this;
        }

        public Builder setSelectedRegion(int selectedRegion) {
            this.selectedRegion = selectedRegion;
            return this;
        }

        public Builder setSelectedYear(int selectedYear) {
            this.selectedYear = selectedYear;
            return this;
        }

        public Builder setSelectedRating(int selectedRating) {
            this.selectedRating = selectedRating;
            return this;
        }

        public Builder setSelectedAudio(int selectedAudio) {
            this.selectedAudio = selectedAudio;
            return this;
        }

        public Builder setOnFilterListener(OnFilterListener onFilterListener) {
            this.onFilterListener = onFilterListener;
            return this;
        }

        public FiltersWidget build() {
            FiltersWidget filtersWidget = new FiltersWidget();
            filtersWidget.mSelectedGenre = selectedGenre;
            filtersWidget.mSelectedRegion = selectedRegion;
            filtersWidget.mSelectedYear = selectedYear;
            filtersWidget.mSelectedRating = selectedRating;
            filtersWidget.mSelectedAudio = selectedAudio;
            filtersWidget.context = context;
            filtersWidget.onFilterListener = onFilterListener;
            filtersWidget.showFiltersPop();
            return filtersWidget;
        }
    }
}
