package org.tvapp.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import org.tvapp.base.BaseFragment;
import org.tvapp.databinding.FragmentTvBinding;


public class TvFragment extends BaseFragment {

    private FragmentTvBinding binding;

    @Override
    protected ViewBinding provideContentViewId(LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentTvBinding.inflate(inflater,container,false);
        return binding;
    }
}
