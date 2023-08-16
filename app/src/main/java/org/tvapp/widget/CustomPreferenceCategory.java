package org.tvapp.widget;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import org.tvapp.R;

public class CustomPreferenceCategory extends PreferenceCategory {
    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.selector_menu));
    }

}
