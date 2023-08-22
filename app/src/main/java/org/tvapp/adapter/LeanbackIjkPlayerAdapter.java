/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tvapp.adapter;

import static androidx.core.view.KeyEventDispatcher.dispatchKeyEvent;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.media.PlaybackGlueHost;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.media.SurfaceHolderGlueHost;


import com.bumptech.glide.util.LogTime;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.util.Util;

import org.tvapp.utils.LogUtils;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/** Leanback {@code PlayerAdapter} implementation for . */
public final class LeanbackIjkPlayerAdapter extends PlayerAdapter implements Runnable {


  private final VideoSupportFragment fragment;
  private final IjkMediaPlayer player;
  private final Handler handler;
  private final PlayerListener playerListener;
  private final int updatePeriodMs;

  public LeanbackIjkPlayerAdapter(VideoSupportFragment fragment, IjkMediaPlayer player, int updatePeriodMs) {
    this.fragment = fragment;
    this.player = player;
    this.updatePeriodMs = updatePeriodMs;
    handler = Util.createHandlerForCurrentOrMainLooper();
    playerListener = new PlayerListener();
  }

  @Nullable private SurfaceHolderGlueHost surfaceHolderGlueHost;
  private boolean hasSurface;
  private boolean lastNotifiedPreparedState;

  // PlayerAdapter implementation.

  @Override
  public void onAttachedToHost(PlaybackGlueHost host) {
    if (host instanceof SurfaceHolderGlueHost) {
      surfaceHolderGlueHost = ((SurfaceHolderGlueHost) host);
      surfaceHolderGlueHost.setSurfaceHolderCallback(playerListener);
    }
    notifyStateChanged();
    player.setListener(playerListener);
  }

  // dereference of possibly-null reference callback
  @SuppressWarnings("nullness:dereference.of.nullable")
  @Override
  public void onDetachedFromHost() {
    player.setListener(null);
    if (surfaceHolderGlueHost != null) {
      removeSurfaceHolderCallback(surfaceHolderGlueHost);
      surfaceHolderGlueHost = null;
    }
    hasSurface = false;
    Callback callback = getCallback();
    callback.onBufferingStateChanged(this, false);
    callback.onPlayStateChanged(this);
    maybeNotifyPreparedStateChanged(callback);
  }

  @Override
  public void setProgressUpdatingEnabled(boolean enable) {
    handler.removeCallbacks(this);
    if (enable) {
      handler.post(this);
    }
  }

  @Override
  public boolean isPlaying() {
    return player.isPlaying();
  }

  @Override
  public long getDuration() {
    return player.getDuration();
  }

  @Override
  public long getCurrentPosition() {
    return player.getCurrentPosition();
  }

  // dereference of possibly-null reference getCallback()
  @SuppressWarnings("nullness:dereference.of.nullable")
  @Override
  public void play() {
    if (!player.isPlaying()) {
      player.start();
      getCallback().onPlayStateChanged(this);
    }
  }

  // dereference of possibly-null reference getCallback()
  @SuppressWarnings("nullness:dereference.of.nullable")
  @Override
  public void pause() {
    if (player.isPlaying()) {
      player.pause();
      getCallback().onPlayStateChanged(this);
    }
  }

  @Override
  public void seekTo(long positionInMs) {
    player.seekTo(positionInMs);
  }

  @Override
  public long getBufferedPosition() {
    return player.getDuration() * player.getBitRate() / 100;
  }

  @Override
  public boolean isPrepared() {
    return player.getVideoWidth() != 0
            && player.getVideoHeight() != 0
            && (surfaceHolderGlueHost == null || hasSurface);
  }

  // Runnable implementation.

  // dereference of possibly-null reference callback
  @SuppressWarnings("nullness:dereference.of.nullable")
  @Override
  public void run() {
    Callback callback = getCallback();
    callback.onCurrentPositionChanged(this);
    callback.onBufferedPositionChanged(this);
    handler.postDelayed(this, updatePeriodMs);
  }

  // Internal methods.

  /* package */
  // incompatible argument for parameter callback of maybeNotifyPreparedStateChanged.
  @SuppressWarnings("nullness:argument.type.incompatible")
  void setVideoSurface(@Nullable Surface surface) {
    hasSurface = surface != null;
    player.setSurface(surface);
    maybeNotifyPreparedStateChanged(getCallback());
  }

  /* package */
  // incompatible argument for parameter callback of maybeNotifyPreparedStateChanged.
  @SuppressWarnings("nullness:argument.type.incompatible")
  void notifyStateChanged() {
    Callback callback = getCallback();
    maybeNotifyPreparedStateChanged(callback);
    callback.onPlayStateChanged(this);
    callback.onBufferingStateChanged(this, !player.isPlaying());
    if (!player.isPlaying() && player.getCurrentPosition() == player.getDuration()) {
      callback.onPlayCompleted(this);
    }
  }

  private void maybeNotifyPreparedStateChanged(Callback callback) {
    boolean isPrepared = isPrepared();
    if (lastNotifiedPreparedState != isPrepared) {
      lastNotifiedPreparedState = isPrepared;
      callback.onPreparedStateChanged(this);
    }
  }

  @SuppressWarnings("nullness:argument")
  private static void removeSurfaceHolderCallback(SurfaceHolderGlueHost surfaceHolderGlueHost) {
    surfaceHolderGlueHost.setSurfaceHolderCallback(null);
  }

  private final class PlayerListener implements IMediaPlayer.Listener, SurfaceHolder.Callback {

    // OnPreparedListener implementation.
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
      setVideoSurface(holder.getSurface());
    }


    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
      setVideoSurface(null);
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
      notifyStateChanged();
    }

    // OnCompletionListener implementation.

    @Override
    public void onCompletion(IMediaPlayer mp) {
      notifyStateChanged();
    }

    @Override
    public void onSeekComplete(IMediaPlayer mp) {
      LogUtils.d("onSeekComplete");
      notifyStateChanged();
      player.start();
      fragment.hideControlsOverlay(false);
    }

    @Override
    public void onInfo(IMediaPlayer mp, int what, int extra) {
        Callback callback = getCallback();
        switch (what) {
          case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    callback.onBufferingStateChanged(LeanbackIjkPlayerAdapter.this, true);
                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    callback.onBufferingStateChanged(LeanbackIjkPlayerAdapter.this, false);
                    break;
                case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    callback.onPlayStateChanged(LeanbackIjkPlayerAdapter.this);
                    break;
        }
        notifyStateChanged();
    }

    // OnErrorListener implementation.

    // dereference of possibly-null reference callback
    @SuppressWarnings("nullness:dereference.of.nullable")
    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
      Callback callback = getCallback();
      callback.onError(LeanbackIjkPlayerAdapter.this, what, String.valueOf(extra));
      return true;
    }

    @Override
    public void onTimedText(IMediaPlayer mp, IjkTimedText text) {
      Callback callback = getCallback();
      callback.onDurationChanged(LeanbackIjkPlayerAdapter.this);
      callback.onCurrentPositionChanged(LeanbackIjkPlayerAdapter.this);
      callback.onBufferedPositionChanged(LeanbackIjkPlayerAdapter.this);
      notifyStateChanged();
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
      Callback callback = getCallback();
      callback.onDurationChanged(LeanbackIjkPlayerAdapter.this);
      callback.onCurrentPositionChanged(LeanbackIjkPlayerAdapter.this);
      callback.onBufferedPositionChanged(LeanbackIjkPlayerAdapter.this);
      notifyStateChanged();
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, long position) {
      Callback callback = getCallback();
      callback.onDurationChanged(LeanbackIjkPlayerAdapter.this);
      callback.onCurrentPositionChanged(LeanbackIjkPlayerAdapter.this);
      callback.onBufferedPositionChanged(LeanbackIjkPlayerAdapter.this);
      notifyStateChanged();
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
      getCallback().onVideoSizeChanged(LeanbackIjkPlayerAdapter.this, width, height);
    }


  }
}

