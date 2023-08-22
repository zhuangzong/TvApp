package org.tvapp.ui.playback;


import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.PlaybackSeekDataProvider;
import androidx.leanback.widget.PlaybackSeekUi;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import org.tvapp.adapter.LeanbackIjkPlayerAdapter;
import org.tvapp.base.Constants;
import org.tvapp.db.DatabaseHelper;
import org.tvapp.db.bean.BaseResult;
import org.tvapp.db.bean.ListResult;
import org.tvapp.db.bean.ParamStruct;
import org.tvapp.db.bean.TagVideo;
import org.tvapp.db.bean.VideoDetailInfo;
import org.tvapp.db.callback.OnGetRecommendListCallback;
import org.tvapp.presenter.CustomListRowPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.utils.IjkVideoPlayerGlue;
import org.tvapp.utils.LogUtils;
import org.tvapp.utils.VideoPlayerGlue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlaybackIjkFragment extends VideoSupportFragment implements OnGetRecommendListCallback{
    private static final int UPDATE_DELAY = 16;

    private IjkVideoPlayerGlue mPlayerGlue;
    private LeanbackIjkPlayerAdapter mPlayerAdapter;
    private IjkMediaPlayer mPlayer;
    private Playlist mPlaylist;
    private TrackSelector mTrackSelector;
    private PlaylistActionListener mPlaylistActionListener;
    private final ArrayObjectAdapter mVideoCursorAdapter = new ArrayObjectAdapter(new ImageCardPresenter(false));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VideoDetailInfo mVideo = (VideoDetailInfo) (getArguments() != null ? getArguments().getSerializable(Constants.EXTRA_VIDEO) : null);
        mPlaylist = new Playlist();
        mPlaylist.add(mVideo);
        ParamStruct paramStruct = new ParamStruct();
        paramStruct.setCategoryId(1);
        paramStruct.setPage(1);
        paramStruct.setPageSize(1);
        new DatabaseHelper.Builder(requireContext())
                .setOnGetRecommendListCallback(this)
                .build()
                .callGetRecommendList(new Gson().toJson(paramStruct));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControlsOverlayAutoHideEnabled(true);
        initializePlayer();
        ArrayObjectAdapter mRowsAdapter = initializeRelatedVideosRow();
        setAdapter(mRowsAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mPlayerGlue != null && mPlayerGlue.isPlaying()) {
            mPlayerGlue.pause();
        }
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            mTrackSelector = null;
            mPlayerGlue = null;
            mPlayerAdapter = null;
            mPlaylistActionListener = null;
        }
    }

    private void initializePlayer() {
        mPlayer = new IjkMediaPlayer();
        mPlayerAdapter = new LeanbackIjkPlayerAdapter(this, mPlayer, UPDATE_DELAY);

        mPlaylistActionListener = new PlaylistActionListener(mPlaylist);
        mPlayerGlue = new IjkVideoPlayerGlue(getActivity(), mPlayerAdapter, mPlaylistActionListener);
        mPlayerGlue.setHost(new VideoSupportFragmentGlueHost(this));
        play(mPlaylist.playlist.get(0));

    }

    private ArrayObjectAdapter initializeRelatedVideosRow() {
        /*
         * To add a new row to the mPlayerAdapter and not lose the controls row that is provided by the
         * glue, we need to compose a new row with the controls row and our related videos row.
         *
         * We start by creating a new {@link ClassPresenterSelector}. Then add the controls row from
         * the media player glue, then add the related videos row.
         */
        ClassPresenterSelector presenterSelector = new ClassPresenterSelector();
        presenterSelector.addClassPresenter(
                mPlayerGlue.getControlsRow().getClass(), mPlayerGlue.getPlaybackRowPresenter());
        presenterSelector.addClassPresenter(ListRow.class, new CustomListRowPresenter());

        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(presenterSelector);

        rowsAdapter.add(mPlayerGlue.getControlsRow());
        for (VideoDetailInfo detail : mPlaylist.playlist){
            mVideoCursorAdapter.add(detail);
        }
        HeaderItem header = new HeaderItem("Related Videos");
        ListRow row = new ListRow(header, mVideoCursorAdapter);
        rowsAdapter.add(row);
        return rowsAdapter;
    }

    private void play(VideoDetailInfo video) {
        if(video!=null){
            mPlayerGlue.setTitle(video.getTitle());
            mPlayerGlue.setSubtitle(video.getPlotSummary());
            prepareMediaForPlaying(video.getVideoUrl());
        }else {
            Toast.makeText(requireContext(), "No more videos", Toast.LENGTH_SHORT).show();
        }
    }

    public void skipToNext() {
        mPlayerGlue.next();
    }

    public void skipToPrevious() {
        mPlayerGlue.previous();
    }

    public void rewind() {
        mPlayerGlue.rewind();
    }

    public void fastForward() {
        mPlayerGlue.fastForward();
    }

    private void prepareMediaForPlaying(String mediaSourceUri) {
        try {
            mPlayer.setDataSource(mediaSourceUri);
            mPlayer.prepareAsync();
            hideControlsOverlay(false);
            mPlayerGlue.play();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGetRecommendListComplete(String message) {
        BaseResult baseResult = new Gson().fromJson(message, BaseResult.class);
        if (baseResult.getCode().equals("0000")) {
            TagVideo details = new Gson().fromJson(new Gson().toJson(baseResult.getData()), ListResult.class).getList().get(0);
            mVideoCursorAdapter.addAll(0, details.getVideoList());
        }
    }

    class PlaylistActionListener implements IjkVideoPlayerGlue.OnActionClickedListener {

        private final Playlist mPlaylist;

        PlaylistActionListener(Playlist playlist) {
            this.mPlaylist = playlist;
        }

        @Override
        public void onPrevious() {
            play(mPlaylist.previous());
        }

        @Override
        public void onNext() {
            play(mPlaylist.next());
        }
    }

    static class Playlist{
        private final List<VideoDetailInfo> playlist;
        private int currentPosition;

        public Playlist() {
            playlist = new ArrayList<>();
            currentPosition = 0;
        }

        /**
         * Clears the videos from the playlist.
         */
        public void clear() {
            playlist.clear();
        }

        /**
         * Adds a video to the end of the playlist.
         *
         * @param video to be added to the playlist.
         */
        public void add(VideoDetailInfo video) {
            playlist.add(video);
        }

        /**
         * Sets current position in the playlist.
         *
         */
        public void setCurrentPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        /**
         * Returns the size of the playlist.
         *
         * @return The size of the playlist.
         */
        public int size() {
            return playlist.size();
        }

        /**
         * Moves to the next video in the playlist. If already at the end of the playlist, null will
         * be returned and the position will not change.
         *
         * @return The next video in the playlist.
         */
        public VideoDetailInfo next() {
            if ((currentPosition + 1) < size()) {
                currentPosition++;
                return playlist.get(currentPosition);
            }
            return null;
        }

        /**
         * Moves to the previous video in the playlist. If the playlist is already at the beginning,
         * null will be returned and the position will not change.
         *
         * @return The previous video in the playlist.
         */
        public VideoDetailInfo previous() {
            if (currentPosition - 1 >= 0) {
                currentPosition--;
                return playlist.get(currentPosition);
            }
            return null;
        }
    }


}
