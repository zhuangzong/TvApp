package org.tvapp.ui.playback;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.tvapp.base.BaseActivity;
import org.tvapp.base.Constants;
import org.tvapp.databinding.ActivityPlaybackBinding;
import org.tvapp.db.bean.VideoDetailInfo;
import org.tvapp.model.DataModel;

public class PlaybackActivity extends BaseActivity {

    private ActivityPlaybackBinding binding;
    private PlaybackFragment mPlaybackFragment;
    private static final float GAMEPAD_TRIGGER_INTENSITY_ON = 0.5f;
    // Off-condition slightly smaller for button debouncing.
    private static final float GAMEPAD_TRIGGER_INTENSITY_OFF = 0.45f;
    private boolean gamepadTriggerPressed = false;

    public static void launch(Context activity, VideoDetailInfo data) {
        Intent intent = new Intent(activity, PlaybackActivity.class);
        intent.putExtra(Constants.EXTRA_VIDEO, data);
        activity.startActivity(intent);
    }

    @Override
    protected View provideContentViewId() {
        binding = ActivityPlaybackBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        VideoDetailInfo data = (VideoDetailInfo) intent.getSerializableExtra(Constants.EXTRA_VIDEO);
        mPlaybackFragment = new PlaybackFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.EXTRA_VIDEO, data);
        mPlaybackFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.playbackControlsFragment.getId(),mPlaybackFragment)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BUTTON_R1) {
            mPlaybackFragment.skipToNext();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BUTTON_L1) {
            mPlaybackFragment.skipToPrevious();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BUTTON_L2) {
            mPlaybackFragment.rewind();
        } else if (keyCode == KeyEvent.KEYCODE_BUTTON_R2) {
            mPlaybackFragment.fastForward();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        // This method will handle gamepad events.
        if (event.getAxisValue(MotionEvent.AXIS_LTRIGGER) > GAMEPAD_TRIGGER_INTENSITY_ON
                && !gamepadTriggerPressed) {
            mPlaybackFragment.rewind();
            gamepadTriggerPressed = true;
        } else if (event.getAxisValue(MotionEvent.AXIS_RTRIGGER) > GAMEPAD_TRIGGER_INTENSITY_ON
                && !gamepadTriggerPressed) {
            mPlaybackFragment.fastForward();
            gamepadTriggerPressed = true;
        } else if (event.getAxisValue(MotionEvent.AXIS_LTRIGGER) < GAMEPAD_TRIGGER_INTENSITY_OFF
                && event.getAxisValue(MotionEvent.AXIS_RTRIGGER) < GAMEPAD_TRIGGER_INTENSITY_OFF) {
            gamepadTriggerPressed = false;
        }
        return super.onGenericMotionEvent(event);
    }
}
