package org.tvapp.ui.settings;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.preference.LeanbackPreferenceFragmentCompat;
import androidx.leanback.preference.LeanbackSettingsFragmentCompat;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import org.tvapp.R;

import java.util.Arrays;


public class SettingsFragment extends LeanbackSettingsFragmentCompat
        implements DialogPreference.TargetFragment {

    private PreferenceFragmentCompat mPreferenceFragment;
    private final static String PREFERENCE_RESOURCE_ID = "preferenceResource";
    private final static String PREFERENCE_ROOT = "root";

    @Override
    public void onPreferenceStartInitialScreen() {
        mPreferenceFragment = buildPreferenceFragment(R.xml.settings, null);
        startPreferenceFragment(mPreferenceFragment);
    }

    private PreferenceFragmentCompat buildPreferenceFragment(int preferenceResId, String root) {
        PreferenceFragmentCompat fragment = new PrefFragment();
        Bundle args = new Bundle();
        args.putInt(PREFERENCE_RESOURCE_ID, preferenceResId);
        args.putString(PREFERENCE_ROOT, root);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public <T extends Preference> T findPreference(@NonNull CharSequence key) {
        return mPreferenceFragment.findPreference(key);
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        return false;
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref) {
        PreferenceFragmentCompat frag = buildPreferenceFragment(R.xml.settings,pref.getKey());
        startPreferenceFragment(frag);

        return true;
    }

    public static class PrefFragment extends LeanbackPreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            String root = getArguments() != null ? getArguments().getString(PREFERENCE_ROOT, null) : null;
            int prefResId = getArguments() != null ? getArguments().getInt(PREFERENCE_RESOURCE_ID) : 0;
            if (root == null) {
                addPreferencesFromResource(prefResId);
            } else {
                setPreferencesFromResource(prefResId, root);
            }
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            final String[] keys = {"prefs_wifi_connect_wps", "prefs_date", "prefs_time",
                    "prefs_date_time_use_timezone", "app_banner_sample_app", "pref_force_stop",
                    "pref_uninstall", "pref_more_info"};
            if (Arrays.asList(keys).contains(preference.getKey())) {
                Toast.makeText(getActivity(), "Implement your own action handler.", Toast.LENGTH_SHORT).show();
                return true;
            }
            return super.onPreferenceTreeClick(preference);
        }
    }
}
