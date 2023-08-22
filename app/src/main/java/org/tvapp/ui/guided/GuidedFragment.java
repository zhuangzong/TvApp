package org.tvapp.ui.guided;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import org.tvapp.R;
import org.tvapp.base.Constants;

import java.util.List;

public class GuidedFragment extends GuidedStepSupportFragment {
    private String title;
    private String description;
    private String breadcrumb;
    private int icon;
    private String[] actionList;
    private boolean noBackground= false;

    private OnGuidedActionClickedListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnGuidedActionClickedListener) {
            listener = (OnGuidedActionClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGuidedActionClickedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        title = bundle != null ? bundle.getString(Constants.EXTRA_TITLE) : null;
        description = bundle != null ? bundle.getString(Constants.EXTRA_DESCRIPTION) : null;
        breadcrumb = bundle != null ? bundle.getString(Constants.EXTRA_BREADCRUMB) : null;
        icon = bundle != null ? bundle.getInt(Constants.EXTRA_ICON) : 0;
        noBackground = bundle != null && bundle.getBoolean(Constants.EXTRA_NO_BACKGROUND);
    }

    @Override
    public View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return noBackground ? null : super.onCreateBackgroundView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        return new GuidanceStylist.Guidance(noBackground ? "" : title,
                noBackground ? "" : description,
                noBackground ? "" : breadcrumb,
                !noBackground && icon > 0 ? AppCompatResources.getDrawable(requireContext(), icon) : null);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        actionList = bundle != null ? bundle.getStringArray(Constants.EXTRA_ACTIONS) : null;
        if (actionList != null) {
            for (String action : actionList) {
                GuidedAction guidedAction = new GuidedAction.Builder(requireContext())
                        .id(actions.size() + 1)
                        .title(action).build();
                actions.add(guidedAction);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.post(() -> {
            getActionItemView(0).setActivated(true);
            getActionItemView(0).requestFocus();
            for (int i = 0; i < actionList.length; i++) {
                View itemView = getActionItemView(i);
                itemView.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.selector_menu));
                itemView.setPadding(30, 30, 30, 30);
            }
        });
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        listener.onGuidedActionClicked(action);
        requireActivity().finish();
    }

}
