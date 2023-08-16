package org.tvapp.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import org.tvapp.R;

import java.util.List;


public class ProfileFragment extends GuidedStepSupportFragment {
    private static final int ACTION_ID_POSITIVE = 1;
    private static final int ACTION_ID_NEGATIVE = ACTION_ID_POSITIVE + 1;

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        return new GuidanceStylist.Guidance(getString(R.string.dialog_example_title),
                getString(R.string.dialog_example_description),
                "", null);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction action = new GuidedAction.Builder()
                .id(ACTION_ID_POSITIVE)
                .title(getString(R.string.dialog_example_button_positive)).build();
        actions.add(action);
        action = new GuidedAction.Builder(requireContext())
                .id(ACTION_ID_NEGATIVE)
                .title(getString(R.string.dialog_example_button_negative)).build();
        actions.add(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (ACTION_ID_POSITIVE == action.getId()) {
            Toast.makeText(getActivity(), R.string.dialog_example_button_toast_positive_clicked,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.dialog_example_button_toast_negative_clicked,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
