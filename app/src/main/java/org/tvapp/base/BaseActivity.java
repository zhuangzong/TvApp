package org.tvapp.base;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());
        initView();
        initData();
        initListener();
    }

    public void initView() {
    }

    public void initData() {
    }

    public void initListener() {
    }

    protected abstract View provideContentViewId();
}
