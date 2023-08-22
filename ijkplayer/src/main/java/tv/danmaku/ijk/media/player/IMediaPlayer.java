/*
 * Copyright (C) 2013-2014 Bilibili
 * Copyright (C) 2013-2014 Zhang Rui <bbcallen@gmail.com>
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

package tv.danmaku.ijk.media.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public interface IMediaPlayer {
    /*
     * Do not change these values without updating their counterparts in native
     */
    int MEDIA_INFO_UNKNOWN = 1;
    int MEDIA_INFO_STARTED_AS_NEXT = 2;
    int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
    int MEDIA_INFO_BUFFERING_START = 701;
    int MEDIA_INFO_BUFFERING_END = 702;
    int MEDIA_INFO_NETWORK_BANDWIDTH = 703;
    int MEDIA_INFO_BAD_INTERLEAVING = 800;
    int MEDIA_INFO_NOT_SEEKABLE = 801;
    int MEDIA_INFO_METADATA_UPDATE = 802;
    int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
    int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;

    int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;
    int MEDIA_INFO_AUDIO_RENDERING_START = 10002;
    int MEDIA_INFO_AUDIO_DECODED_START = 10003;
    int MEDIA_INFO_VIDEO_DECODED_START = 10004;
    int MEDIA_INFO_OPEN_INPUT = 10005;
    int MEDIA_INFO_FIND_STREAM_INFO = 10006;
    int MEDIA_INFO_COMPONENT_OPEN = 10007;
    int MEDIA_INFO_VIDEO_SEEK_RENDERING_START = 10008;
    int MEDIA_INFO_AUDIO_SEEK_RENDERING_START = 10009;
    int MEDIA_INFO_MEDIA_ACCURATE_SEEK_COMPLETE = 10100;

    int MEDIA_ERROR_UNKNOWN = 1;
    int MEDIA_ERROR_SERVER_DIED = 100;
    int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
    int MEDIA_ERROR_IO = -1004;
    int MEDIA_ERROR_MALFORMED = -1007;
    int MEDIA_ERROR_UNSUPPORTED = -1010;
    int MEDIA_ERROR_TIMED_OUT = -110;

    void setDisplay(SurfaceHolder sh);

    void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void prepareAsync() throws IllegalStateException;

    void start() throws IllegalStateException;

    void stop() throws IllegalStateException;

    void pause() throws IllegalStateException;

    void setScreenOnWhilePlaying(boolean screenOn);

    int getVideoWidth();

    int getVideoHeight();

    boolean isPlaying();

    void seekTo(long msec) throws IllegalStateException;

    boolean getCurrentFrame(Bitmap bitmap);

    long getCurrentPosition();

    long getDuration();

    void release();

    void reset();

    void setVolume(float leftVolume, float rightVolume);

    void setSpeed(float speed);

    float getSpeed();

    int getAudioSessionId();

    int getSelectedTrack(int type);

    void selectTrack(int track);

    void deselectTrack(int track);

    void setOption(int category, String name, String value);

    void setOption(int category, String name, long value);

    @SuppressWarnings("EmptyMethod")
    @Deprecated
    void setLogEnabled(boolean enable);

    @Deprecated
    boolean isPlayable();

    IMediaPlayer setListener(Listener listener);

    /*--------------------
     * Listeners
     */
    interface Listener {
        void onPrepared(IMediaPlayer mp);
        void onCompletion(IMediaPlayer mp);
        void onSeekComplete(IMediaPlayer mp);
        void onInfo(IMediaPlayer mp, int what, int extra);
        boolean onError(IMediaPlayer mp, int what, int extra);
        default void onTimedText(IMediaPlayer mp, IjkTimedText text) {}
        default void onBufferingUpdate(IMediaPlayer mp, int percent) {}
        default void onBufferingUpdate(IMediaPlayer mp, long position) {}
        default void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {}
    }

    /*--------------------
     * Optional
     */
    void setAudioStreamType(int streamtype);

    @Deprecated
    void setKeepInBackground(boolean keepInBackground);

    int getVideoSarNum();

    int getVideoSarDen();

    @Deprecated
    void setWakeMode(Context context, int mode);

    void setLooping(boolean looping);

    boolean isLooping();

    /*--------------------
     * AndroidMediaPlayer: JELLY_BEAN
     */
    List<ITrackInfo> getTrackInfo();

    /*--------------------
     * AndroidMediaPlayer: ICE_CREAM_SANDWICH:
     */
    void setSurface(Surface surface);

    /*--------------------
     * AndroidMediaPlayer: M:
     */
    void setDataSource(IMediaDataSource mediaDataSource);
}
