package org.tvapp.ui.detail;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.tvapp.base.BaseActivity;
import org.tvapp.base.Constants;
import org.tvapp.databinding.ActivityDetailBinding;
import org.tvapp.model.DataModel;

public class DetailActivity extends BaseActivity{

    private ActivityDetailBinding binding;

    public static void launch(Context activity, int videoId) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_ID, videoId);
        activity.startActivity(intent);
    }


    @Override
    protected View provideContentViewId() {
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.EXTRA_ID, getIntent().getIntExtra(Constants.EXTRA_ID, 0));
        detailFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.detailFragmentContainer.getId(),detailFragment)
                .commit();
    }
}
