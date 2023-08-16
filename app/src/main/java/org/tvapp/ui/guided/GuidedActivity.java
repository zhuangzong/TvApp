package org.tvapp.ui.guided;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.widget.GuidedAction;

import org.tvapp.R;
import org.tvapp.base.Constants;


public class GuidedActivity extends FragmentActivity implements OnGuidedActionClickedListener{

    private static OnGuidedActionClickedListener mlistener;

    public static void launch(FragmentActivity activity,
                              String title, String description,String breadcrumb,
                              int icon,boolean noBackground, String[] actions, OnGuidedActionClickedListener listener) {
        Intent intent = new Intent(activity, GuidedActivity.class);
        intent.putExtra(Constants.EXTRA_TITLE, title);
        intent.putExtra(Constants.EXTRA_DESCRIPTION, description);
        intent.putExtra(Constants.EXTRA_ACTIONS, actions);
        intent.putExtra(Constants.EXTRA_BREADCRUMB, breadcrumb);
        intent.putExtra(Constants.EXTRA_ICON, icon);
        intent.putExtra(Constants.EXTRA_NO_BACKGROUND, noBackground);
        activity.startActivity(intent);
        mlistener = listener;
    }
    public static void launch(FragmentActivity activity, String title, String description, String[] actions, OnGuidedActionClickedListener listener) {
        Intent intent = new Intent(activity, GuidedActivity.class);
        intent.putExtra(Constants.EXTRA_TITLE, title);
        intent.putExtra(Constants.EXTRA_DESCRIPTION, description);
        intent.putExtra(Constants.EXTRA_ACTIONS, actions);
        activity.startActivity(intent);
        mlistener = listener;
    }
    public static void launch(FragmentActivity activity, String title, String description,String breadcrumb,
                              int icon, String[] actions, OnGuidedActionClickedListener listener) {
        Intent intent = new Intent(activity, GuidedActivity.class);
        intent.putExtra(Constants.EXTRA_TITLE, title);
        intent.putExtra(Constants.EXTRA_DESCRIPTION, description);
        intent.putExtra(Constants.EXTRA_ACTIONS, actions);
        intent.putExtra(Constants.EXTRA_BREADCRUMB, breadcrumb);
        intent.putExtra(Constants.EXTRA_ICON, icon);
        activity.startActivity(intent);
        mlistener = listener;
    }
    public static void launch(FragmentActivity activity, boolean noBackground, String[] actions, OnGuidedActionClickedListener listener) {
        Intent intent = new Intent(activity, GuidedActivity.class);
        intent.putExtra(Constants.EXTRA_ACTIONS, actions);
        intent.putExtra(Constants.EXTRA_NO_BACKGROUND, noBackground);
        activity.startActivity(intent);
        mlistener = listener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guided);
        GuidedFragment guidedFragment = new GuidedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_TITLE, getIntent().getStringExtra(Constants.EXTRA_TITLE));
        bundle.putString(Constants.EXTRA_DESCRIPTION, getIntent().getStringExtra(Constants.EXTRA_DESCRIPTION));
        bundle.putString(Constants.EXTRA_BREADCRUMB, getIntent().getStringExtra(Constants.EXTRA_BREADCRUMB));
        bundle.putInt(Constants.EXTRA_ICON, getIntent().getIntExtra(Constants.EXTRA_ICON,0));
        bundle.putStringArray(Constants.EXTRA_ACTIONS, getIntent().getStringArrayExtra(Constants.EXTRA_ACTIONS));
        bundle.putBoolean(Constants.EXTRA_NO_BACKGROUND, getIntent().getBooleanExtra(Constants.EXTRA_NO_BACKGROUND,false));
        guidedFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.guidedFragment, guidedFragment)
                .commit();
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        mlistener.onGuidedActionClicked(action);
    }
}
