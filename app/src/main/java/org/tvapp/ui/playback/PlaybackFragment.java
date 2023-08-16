package org.tvapp.ui.playback;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.util.Util;

import org.tvapp.model.DataModel;
import org.tvapp.presenter.CustomListRowPresenter;
import org.tvapp.presenter.ImageCardPresenter;
import org.tvapp.utils.Common;
import org.tvapp.utils.VideoPlayerGlue;

import java.util.ArrayList;
import java.util.List;

public class PlaybackFragment extends VideoSupportFragment {
    private static final int UPDATE_DELAY = 16;

    private VideoPlayerGlue mPlayerGlue;
    private LeanbackPlayerAdapter mPlayerAdapter;
    private ExoPlayer mPlayer;
    private Playlist mPlaylist;
    private TrackSelector mTrackSelector;
    private PlaylistActionListener mPlaylistActionListener;
    private final ArrayObjectAdapter mVideoCursorAdapter = new ArrayObjectAdapter(new ImageCardPresenter(false));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataModel.Detail mVideo = (DataModel.Detail) (getArguments() != null ? getArguments().getSerializable("data") : null);
        mPlaylist = new Playlist();
        mPlaylist.add(mVideo);
        DataModel dataList = Common.getData(requireActivity());
        for (int i = 0; i < dataList.getResult().get(0).getDetails().size(); i++) {
            mPlaylist.add(dataList.getResult().get(0).getDetails().get(i));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        mTrackSelector = new DefaultTrackSelector(getActivity());
        mPlayer = new ExoPlayer.Builder(requireContext())
                .setTrackSelector(mTrackSelector)
                .build();
        mPlayerAdapter = new LeanbackPlayerAdapter(requireContext(), mPlayer, UPDATE_DELAY);

        mPlaylistActionListener = new PlaylistActionListener(mPlaylist);
        mPlayerGlue = new VideoPlayerGlue(getActivity(), mPlayerAdapter, mPlaylistActionListener);
        mPlayerGlue.setHost(new VideoSupportFragmentGlueHost(this));
        mPlayerGlue.playWhenPrepared();
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
        presenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(presenterSelector);

        rowsAdapter.add(mPlayerGlue.getControlsRow());
        for (DataModel.Detail detail : mPlaylist.playlist){
            mVideoCursorAdapter.add(detail);
        }
        HeaderItem header = new HeaderItem("Related Videos");
        ListRow row = new ListRow(header, mVideoCursorAdapter);
        rowsAdapter.add(row);
        return rowsAdapter;
    }

    private void play(DataModel.Detail video) {
        if(video!=null){
            mPlayerGlue.setTitle(video.getTitle());
            mPlayerGlue.setSubtitle(video.getOverview());
            prepareMediaForPlaying(Uri.parse("https://pass-1305950406.cos.ap-guangzhou.myqcloud.com/video/124mb.mp4"));
            mPlayerGlue.play();
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

    private void prepareMediaForPlaying(Uri mediaSourceUri) {
        MediaSource mediaSource = new DefaultMediaSourceFactory(getActivity())
                .createMediaSource(MediaItem.fromUri(mediaSourceUri));
        mPlayer.setMediaSource(mediaSource);
    }

    class PlaylistActionListener implements VideoPlayerGlue.OnActionClickedListener {

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
        private final List<DataModel.Detail> playlist;
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
        public void add(DataModel.Detail video) {
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
        public DataModel.Detail next() {
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
        public DataModel.Detail previous() {
            if (currentPosition - 1 >= 0) {
                currentPosition--;
                return playlist.get(currentPosition);
            }
            return null;
        }
    }


}
