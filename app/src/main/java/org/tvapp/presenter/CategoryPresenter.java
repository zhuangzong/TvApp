package org.tvapp.presenter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;

import org.tvapp.R;
import org.tvapp.databinding.ItemCategoryBinding;


public class CategoryPresenter extends Presenter {

    private String selectedCategory = "";

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = 270;
        layoutParams.height = 100;
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        TextView textView = viewHolder.view.findViewById(R.id.item_text);
        textView.setText((String) item);
        if (selectedCategory.equals(item)) {
            textView.setTextColor(ContextCompat.getColor(viewHolder.view.getContext(), R.color.bg_grey));
        } else {
            textView.setTextColor(ContextCompat.getColor(viewHolder.view.getContext(), R.color.white));
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }


}
