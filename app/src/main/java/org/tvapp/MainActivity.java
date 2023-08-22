package org.tvapp;

import androidx.fragment.app.Fragment;
import androidx.leanback.widget.GuidedAction;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;

import org.tvapp.base.BaseActivity;
import org.tvapp.base.Constants;
import org.tvapp.databinding.ActivityMainBinding;
import org.tvapp.db.DatabaseHelper;
import org.tvapp.db.bean.ParamStruct;
import org.tvapp.db.callback.OnInitCallback;
import org.tvapp.ui.GridFragment;
import org.tvapp.ui.TvFragment;
import org.tvapp.ui.guided.GuidedActivity;
import org.tvapp.ui.guided.GuidedFragment;
import org.tvapp.ui.HomeFragment;
import org.tvapp.ui.MovieFragment;
import org.tvapp.ui.ProfileFragment;
import org.tvapp.ui.SearchFragment;
import org.tvapp.ui.guided.OnGuidedActionClickedListener;
import org.tvapp.ui.settings.SettingsActivity;
import org.tvapp.utils.Common;
import org.tvapp.utils.DisplayUtils;
import org.tvapp.utils.LogUtils;

public class MainActivity extends BaseActivity implements View.OnKeyListener, View.OnClickListener, OnInitCallback {

    private ActivityMainBinding binding;
    private boolean SIDE_MENU = false;
    private String selectedMenu = Constants.MENU_HOME;
    View lastSelectedMenu;

    @Override
    protected View provideContentViewId() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        DatabaseHelper databaseHelper = new DatabaseHelper.Builder(this).setOnInitCallback(this).build();
        ParamStruct paramStruct = new ParamStruct();
        paramStruct.setStaticHost("http://192.168.10.45:8000");
        databaseHelper.callInit(new Gson().toJson(paramStruct));
    }

    @Override
    public void initListener() {
        super.initListener();
        binding.btnSearch.setOnKeyListener(this);
        binding.btnHome.setOnKeyListener(this);
        binding.btnMovie.setOnKeyListener(this);
        binding.btnTv.setOnKeyListener(this);
        binding.btnSettings.setOnKeyListener(this);
        binding.btnProfile.setOnKeyListener(this);

        binding.btnSearch.setOnClickListener(this);
        binding.btnHome.setOnClickListener(this);
        binding.btnMovie.setOnClickListener(this);
        binding.btnTv.setOnClickListener(this);
        binding.btnSettings.setOnClickListener(this);
        binding.btnProfile.setOnClickListener(this);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_UP) return false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (lastSelectedMenu == null) return false;
                lastSelectedMenu.setActivated(false);
                lastSelectedMenu = view;
                view.setActivated(true);
                view.requestFocus();
                if (view.getId() == R.id.btn_search) {
                    selectedMenu = Constants.MENU_SEARCH;
                    changeFragment(new SearchFragment());
                } else if (view.getId() == R.id.btn_home) {
                    selectedMenu = Constants.MENU_HOME;
                    changeFragment(new HomeFragment());
                } else if (view.getId() == R.id.btn_movie) {
                    selectedMenu = Constants.MENU_MOVIE;
                    changeFragment(new MovieFragment());
                } else if (view.getId() == R.id.btn_tv) {
                    selectedMenu = Constants.MENU_TV;
                    changeFragment(new TvFragment());
                } else if (view.getId() == R.id.btn_settings) {
                    selectedMenu = Constants.MENU_SETTINGS;
                    startActivity(new Intent(this, SettingsActivity.class));
                } else if (view.getId() == R.id.btn_profile) {
                    selectedMenu = Constants.MENU_PROFILE;
                    changeFragment(new ProfileFragment());
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (!this.SIDE_MENU) {
                    switchToLastSelectedMenu();
                    openMenu();
                    this.SIDE_MENU = true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (this.SIDE_MENU) {
                    closeMenu();
                }
                break;

        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && SIDE_MENU) {
            SIDE_MENU = false;
            closeMenu();
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            GuidedActivity.launch(this,
                    getString(R.string.exit_title),
                    getString(R.string.exit_message),
                    new String[]{getString(R.string.confirm), getString(R.string.cancel)},
                    action -> {
                        if (action.getTitle().equals(getString(R.string.confirm))) {
                            System.exit(0);
                        }
                    }
            );
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Switch to last selected menu when side menu is open
     */
    private void switchToLastSelectedMenu() {
        switch (selectedMenu) {
            case Constants.MENU_SEARCH:
                binding.btnSearch.requestFocus();
                break;
            case Constants.MENU_HOME:
                binding.btnHome.requestFocus();
                break;
            case Constants.MENU_MOVIE:
                binding.btnMovie.requestFocus();
                break;
            case Constants.MENU_TV:
                binding.btnTv.requestFocus();
                break;
            case Constants.MENU_PROFILE:
                binding.btnProfile.requestFocus();
                break;
            case Constants.MENU_SETTINGS:
                binding.btnSettings.requestFocus();
                break;
        }
    }

    /**
     * Change fragment
     *
     * @param fragment fragment
     */
    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        closeMenu();
    }

    /**
     * Open side menu
     */
    private void openMenu() {
        Animation animSlide = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        binding.navigationBar.startAnimation(animSlide);
        binding.navigationBar.requestLayout();
        binding.navigationBar.getLayoutParams().width = Common.dp2px(this, 120);
    }

    /**
     * Close side menu
     */
    private void closeMenu() {
        binding.navigationBar.requestLayout();
        binding.navigationBar.getLayoutParams().width = Common.dp2px(this, 50);
        SIDE_MENU = false;
    }

    @Override
    public void onClick(View view) {
        lastSelectedMenu.setActivated(false);
        lastSelectedMenu = view;
        view.setActivated(true);
        view.requestFocus();

        if (view.getId() == R.id.btn_search) {
            selectedMenu = Constants.MENU_SEARCH;
            changeFragment(new SearchFragment());
        } else if (view.getId() == R.id.btn_home) {
            selectedMenu = Constants.MENU_HOME;
            changeFragment(new HomeFragment());
        } else if (view.getId() == R.id.btn_movie) {
            selectedMenu = Constants.MENU_MOVIE;
            changeFragment(new MovieFragment());
        } else if (view.getId() == R.id.btn_tv) {
            selectedMenu = Constants.MENU_TV;
            changeFragment(new TvFragment());
        } else if (view.getId() == R.id.btn_settings) {
            selectedMenu = Constants.MENU_SETTINGS;
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (view.getId() == R.id.btn_profile) {
            selectedMenu = Constants.MENU_PROFILE;
            changeFragment(new ProfileFragment());
        }
    }

    @Override
    public void onInitComplete(String message) {
        runOnUiThread(() -> {
            lastSelectedMenu = binding.btnHome;
            lastSelectedMenu.setActivated(true);
            lastSelectedMenu.requestFocus();
            changeFragment(new HomeFragment());
        });
    }
}