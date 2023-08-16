package org.tvapp.ui.detail;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.tvapp.base.BaseActivity;
import org.tvapp.databinding.ActivityDetailBinding;
import org.tvapp.model.DataModel;

public class DetailActivity extends BaseActivity{

    private ActivityDetailBinding binding;
    @Override
    protected View provideContentViewId() {
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        DataModel.Detail data = (DataModel.Detail) intent.getSerializableExtra("data");
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        detailFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.detailFragmentContainer.getId(),detailFragment)
                .commit();
    }
}
