package org.tvapp.presenter;


import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.leanback.widget.RowPresenter;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import org.tvapp.R;
import org.tvapp.adapter.GenreAdapter;
import org.tvapp.databinding.DetailInfoBinding;
import org.tvapp.db.bean.VideoDetailInfo;
import org.tvapp.db.bean.VideoTagsInfo;
import org.tvapp.ui.RatingDialogFragment;
import org.tvapp.ui.guided.GuidedActivity;
import org.tvapp.ui.playback.PlaybackActivity;
import org.tvapp.ui.playback.PlaybackIjkActivity;
import org.tvapp.utils.Common;
import org.tvapp.utils.LogUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;


public class DetailPresenter extends RowPresenter {

    private DetailInfoBinding binding;
    private Context context;
    private boolean isFavorite;

    @Override
    public boolean isUsingDefaultSelectEffect() {
        return false;
    }


    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        context = parent.getContext();
        binding = DetailInfoBinding.inflate(LayoutInflater.from(context), parent, false);
        setHeaderPresenter(null);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    protected void onBindRowViewHolder(ViewHolder vh, Object item) {
        if (item instanceof VideoDetailInfo) {
            VideoDetailInfo model = (VideoDetailInfo) item;
            setDataToView(model);
        }
    }

    /**
     * set data to view
     *
     * @param model data
     */
    private void setDataToView(VideoDetailInfo model) {
        binding.title.setText(model.getTitle());
        binding.subtitle.setText(model.getCreatedAt().split(" ")[0]);
        binding.country.setText(model.getCountry());
        binding.rating.setText("Rating: 7.8");
        binding.description.setText(model.getPlotSummary());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.genres.setFocusable(false);
        binding.genres.setFocusableInTouchMode(false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.genres.setLayoutManager(layoutManager);
        List<String> genres = model.getTags().stream().map(VideoTagsInfo::getTagTitle).collect(Collectors.toList());
        GenreAdapter adapter = new GenreAdapter(genres,context);
        binding.genres.setAdapter(adapter);
        Glide.with(context)
                .load(model.getVideoPic())
                .into(binding.imgBanner);
        model.setVideoUrl("https://pass-1305950406.cos.ap-guangzhou.myqcloud.com/video/wy_14_1080p.mp4");
        binding.btPlay.setOnClickListener(v -> {
            PlaybackActivity.launch(context, model);
        });
        binding.btPlayIjk.setOnClickListener(v -> {
            PlaybackIjkActivity.launch(context, model);
        });
        isFavorite = Common.isFavorite(context, model.getId());
        if (isFavorite) {
            binding.ivFavorite.setImageResource(R.drawable.ic_favorited);
        } else {
            binding.ivFavorite.setImageResource(R.drawable.ic_favorite);
        }

        binding.btFavorite.setOnClickListener(v -> {
            isFavorite = Common.isFavorite(context, model.getId());
            if (isFavorite) {
                Common.removeFavorite(context, model.getId());
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite);
            } else {
                Common.addFavorite(context, model.getId());
                binding.ivFavorite.setImageResource(R.drawable.ic_favorited);
            }
        });

        binding.btChangeQuality.setOnClickListener(v -> {
            String[] urls  = new String[]{
                    "sh_p.mp4",
                    "001-Luis Fonsi - Despacito ft. Daddy Yankee.mp4",
                    "124mb.mp4",
                    "wy_14_1080p.mp4",
                    "gaoxiao-1_360p.mp4"
            };
            GuidedActivity.launch((FragmentActivity) context, true, urls, action -> {
                String url = "https://pass-1305950406.cos.ap-guangzhou.myqcloud.com/video/"+action.getTitle();
                model.setVideoUrl(url);
            });
        });

        binding.btRating.setOnClickListener(v -> {
            RatingDialogFragment newFragment = RatingDialogFragment.newInstance();
            newFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "dialog");
        });

        binding.btDownload.setOnClickListener(v -> {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(model.getVideoUrl()));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle(model.getTitle());
            request.setDescription(model.getPlotSummary());
            String downloadPath = Environment.getExternalStorageDirectory().getPath() + "/Download/" + model.getTitle() + ".mp4";
            File file = new File(downloadPath);
            Uri uri = Uri.fromFile(file);
            request.setDestinationUri(uri);
            downloadManager.enqueue(request);

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterByStatus(DownloadManager.STATUS_RUNNING);
            Cursor cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                // 获取下载任务的状态和进度
                int status = cursor.getInt(Math.max(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS), 0));
                int downloadedBytes = cursor.getInt(Math.max(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR),0));
                int totalBytes = cursor.getInt(Math.max(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES),0));
                // 根据状态和进度执行相应的操作
                switch (status) {
                    case DownloadManager.STATUS_RUNNING:
                        // 下载正在进行中，可以根据下载进度进行相应的处理
                        float progress = (float) downloadedBytes / totalBytes * 100;
                        // 执行更新UI或其他操作
                        LogUtils.d("下载进度：" + progress);
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        // 下载已完成，可以执行相应的操作
                        // 获取下载文件的本地URI
                        String localUri = cursor.getString(Math.max(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI),0));
                        // 执行其他操作，例如播放下载的文件
                        LogUtils.d("下载完成，本地URI：" + localUri);
                        break;
                    case DownloadManager.STATUS_FAILED:
                        // 获取失败原因
                        int reason = cursor.getInt(Math.max(cursor.getColumnIndex(DownloadManager.COLUMN_REASON),0));
                        LogUtils.d("下载失败，失败原因：" + reason);
                        break;
                }
            }

            if (cursor != null) {
                cursor.close();
            }
        });
    }

}


