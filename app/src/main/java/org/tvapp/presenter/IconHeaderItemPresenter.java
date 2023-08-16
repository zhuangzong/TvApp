/*
 * Copyright (c) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tvapp.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowHeaderPresenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tvapp.R;
import org.tvapp.databinding.IconHeaderItemBinding;


public class IconHeaderItemPresenter extends RowHeaderPresenter {

    private IconHeaderItemBinding binding;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {

       binding = IconHeaderItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        HeaderItem headerItem = ((ListRow) item).getHeaderItem();
        View rootView = viewHolder.view;
        rootView.setFocusable(true);
        binding.headerLabel.setText(headerItem.getName());
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    @Override
    protected void onSelectLevelChanged(ViewHolder holder) {

    }
}
