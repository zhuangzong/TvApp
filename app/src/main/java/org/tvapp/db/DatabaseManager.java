package org.tvapp.db;


import static org.tvapp.base.Constants.DB_CHECK_TYPE;
import static org.tvapp.base.Constants.DB_NAME;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.tvapp.db.bean.BannerVideoResult;
import org.tvapp.db.bean.ExportVersionInfo;
import org.tvapp.db.bean.ParamStruct;
import org.tvapp.db.bean.BaseResult;
import org.tvapp.db.bean.TagVideo;
import org.tvapp.db.bean.VideoDetailInfo;
import org.tvapp.db.bean.VideoJoin;
import org.tvapp.db.bean.VideoTagsInfo;
import org.tvapp.db.entity.CmsCategoryTag;
import org.tvapp.db.entity.CmsTags;
import org.tvapp.db.entity.CmsVideo;
import org.tvapp.utils.FileUtils;
import org.tvapp.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {

    private final Context context;
    private AppDatabase db;
    private final Gson gson = new Gson();
    private String staticHost;
    private VersionManager versionManager;
    private static volatile DatabaseManager instance;

    private DatabaseManager(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * singleton
     *
     * @param context context
     * @return DbHelper
     */
    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager(context);
                }
            }
        }
        return instance;
    }


    /**
     * init database
     *
     * @param params {"staticHost":"<a href="http://xxx.com">...</a>"}
     * @return result {"code":"0000","data":true,"msg":"success"} or
     * {"code":"1001","data":null,"msg":"init database failed"}
     */
    public String initDatabase(String params) {
        try {
            ParamStruct _params = gson.fromJson(params, ParamStruct.class);
            staticHost = _params.getStaticHost();
            String videoDbRootPath = context.getFilesDir().getAbsolutePath();
            String videoLockFile = String.format("%s/lock", videoDbRootPath);
            String videoDbFile = String.format("%s/"+DB_NAME, videoDbRootPath);
            File dbFile = new File(videoDbFile);
            File lockFile = new File(videoLockFile);
            boolean created = initDbFile(dbFile, lockFile);
            if (!created) {
                dbFile = FileUtils.assetsToFile(context, DB_NAME, videoDbFile);
            }
            db = AppDatabase.createFromFile(context, dbFile);
//            versionManager = VersionManager.getInstance(context, db);
//            ExportVersionInfo versionFromCos = downloadVersionInfo(DB_CHECK_TYPE);
//            return versionManager.initVersion(versionFromCos);
            Map<String,Object> data = new HashMap<>();
            data.put("msg", "init ok");
            return BaseResult.successResult(data);
        } catch (Exception e) {
            return BaseResult.errorResult("init database failed" + e.getMessage());
        }
    }

    /**
     * get current data version
     * @param dbFile db file
     * @param lockFile lock file
     * @return current data version info or null
     */
    private boolean initDbFile(File dbFile, File lockFile) {
        try {
            if (dbFile.exists() && lockFile.exists()) {
                return true;
            }
            ExportVersionInfo versionFromCos = downloadVersionInfo(DB_CHECK_TYPE);
            if (versionFromCos != null) {
                dbFile = FileUtils.assetsToFile(context, DB_NAME, dbFile.getAbsolutePath());
                assert dbFile != null;
                String backupFile = String.format("%s/backup/%s.db.bin", dbFile.getParent(), versionFromCos.getVersion());
                FileUtils.copyFile(dbFile.getAbsolutePath(), backupFile);
                String lockContent = String.format("%s done", versionFromCos.getVersion());
                FileUtils.writeFile(lockFile.getAbsolutePath(), lockContent);
            }
            return true;
        } catch (Exception e) {
            LogUtils.d("VideoInit json.Unmarshal versionInfoFromCos failed: " + e.getMessage());
        }
        return false;
    }

    private ExportVersionInfo downloadVersionInfo(String checkType){
        CompletableFuture<String> _checkVersionChan = new CompletableFuture<>();
        FileUtils.downLatestVersionInfo(context, checkType)
                .thenAccept(_checkVersionChan::complete)
                .exceptionally(e -> {
                    LogUtils.d("initVersion downLatestVersionInfo failed: " + e.getMessage());
                    _checkVersionChan.completeExceptionally(e);
                    return null;
                });
        String chanResult = FileUtils.readChanWithTimeout(_checkVersionChan);
        ExportVersionInfo versionFromCos = null;
        if (chanResult != null) {
            versionFromCos = new Gson().fromJson(chanResult, ExportVersionInfo.class);
        }
        return versionFromCos;
    }

    /**
     * update data version
     *
     * @return result {"code":"0000","data":true,"msg":"success"}
     */
    public String updateDataVersion() {
        return versionManager.updateDataVersion();
    }

    /**
     * check version update
     *
     * @return result {"code":"0000","data":true,"msg":"success"}
     */
    public String checkVersionUpdate() {
        return versionManager.checkVersionUpdate();
    }

    /**
     * get recommend list
     *
     * @param params {"page":1,"pageSize":10,"categoryId":1}
     * @return result {"code":"0000","data":{"cur_page":1,"last_page":1,"page_size":10,"total":1,"banner":[],"list":[]},"msg":"success"}
     */
    public String getRecommendList(String params) {
        Map<String, Object> data = new HashMap<>();
        try {
            ParamStruct _params = gson.fromJson(params, ParamStruct.class);
            int page = _params.getPage();
            int pageSize = _params.getPageSize();

            List<CmsCategoryTag> categoryTags = db.dao().getCategoryTags(_params.getCategoryId());

            Set<Integer> tagIdsSet = new HashSet<>();
            for (CmsCategoryTag categoryTag : categoryTags) {
                tagIdsSet.add(categoryTag.getTagId());
            }
            List<Integer> tagIds = new ArrayList<>(tagIdsSet);

            int total = tagIds.size();
            int offset = (page - 1) * pageSize;
            int end = Math.min(page * pageSize, total);
            if (offset >= total) {
                tagIds.clear();
            } else {
                tagIds = tagIds.subList(offset, end);
            }

            List<TagVideo> tagsVideoList = new ArrayList<>();
            if (!tagIds.isEmpty()) {
                tagsVideoList = getVideoByTags(tagIds);
            }

            List<BannerVideoResult> bannerList = new ArrayList<>();
            if (page == 1) {
                bannerList = getBannerVideoList(tagIds);
            }

            int lastPage = total / pageSize;
            data.put("cur_page", page);
            data.put("last_page", lastPage);
            data.put("page_size", pageSize);
            data.put("total", total);
            data.put("banner", bannerList);
            data.put("list", tagsVideoList);
            return BaseResult.successResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResult.errorResult("GetRecommendList failed" + e.getMessage());
        }
    }

    /**
     * get video detail
     *
     * @param params {"id":1} id is video id
     * @return result {"code":"0000","data":{"id":1,"title":"title",
     * "video_url":"<a href="http://xxx.com/xxx.mp4">...</a>","video_pic":"<a href="http://xxx.com/xxx.jpg">...</a>",
     * "source_type":1,"similar":"1,2,3","classify_id":1,"category_id":1,"create_by":1,"update_by":1,
     * "created_at":"2021-01-01 00:00:00","updated_at":"2021-01-01 00:00:00",
     * "tags":[{"id":1,"title":"title"}]},"msg":"success"}
     */
    public String getVideoDetail(String params) {
        VideoDetailInfo detail = new VideoDetailInfo();
        try {
            ParamStruct _params = gson.fromJson(params, ParamStruct.class);
            int id = _params.getId();
            CmsVideo video = db.dao().getVideoById(id);
            if (video == null) {
                return BaseResult.errorResult("GetVideoDetail failed: can not find the video");
            }
            List<VideoTagsInfo> tagsInfo = db.dao().getVideoTagsInfo(id);
            if (tagsInfo == null) {
                return BaseResult.errorResult("GetVideoDetail failed: tags is empty");
            }
            detail.setId(video.getId());
            detail.setTitle(video.getTitle());
            detail.setVideoUrl(staticUrl(video.getVideoUrl()));
            detail.setVideoPic(staticUrl(video.getVideoPic()));
            detail.setSourceType(video.getSourceType());
            detail.setSimilar(video.getSimilar());
            detail.setClassifyId(video.getClassifyId());
            detail.setCategoryId(video.getCategoryId());
            detail.setCreateBy(video.getCreateBy());
            detail.setUpdateBy(video.getUpdateBy());
            detail.setCreatedAt(video.getCreatedAt());
            detail.setUpdatedAt(video.getUpdatedAt());
            detail.setTags(tagsInfo);
            detail.setName(video.getTitle());
            detail.setDuration("1:20:15");
            detail.setCountry("USA");
            detail.setReleaseDate("2023-03-03");
            detail.setStars("Jason Statham,Jason Statham");
            detail.setDirector("Guy Ritchie");
            detail.setCertification("R(US)");
            detail.setGenres("Action,Comed");
            detail.setPlotSummary("Shortly after installing Imgbot, you will receive a pull request with all of your images optimized. Just merge the pull request and you’re done! As you work on your project, Imgbot works alongside you to keep your images optimized.");
            return BaseResult.successResult(detail);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResult.errorResult("GetVideoDetail failed:" + e.getMessage());
        }
    }

    /**
     * video search
     *
     * @param params {"title":"title"} title is video title keyword for search
     * @return result {"code":"0000","data":{"movies":[]},"msg":"success"}
     */
    public String videoSearch(String params) {
        Map<String, Object> data = new HashMap<>();
        Gson gson = new Gson();
        try {
            ParamStruct _params = gson.fromJson(params, ParamStruct.class);
            List<CmsVideo> videoList = db.dao().getVideoListByTitle("%" + _params.getTitle() + "%", 10);
            data.put("movies", videoList);
            return BaseResult.successResult(data);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return BaseResult.errorResult("videoSearch failed:" + e.getMessage());
        }
    }

    /**
     * get video list by tag ids
     *
     * @param tagIds tag ids
     * @return video list
     */
    private List<TagVideo> getVideoByTags(List<Integer> tagIds) {
        List<VideoJoin> resVideo;
        Map<Integer, TagVideo> tagsVideoMap = new HashMap<>();

        if (!tagIds.isEmpty()) {
            int limit = 1000;
            resVideo = db.dao().getVideosByTagIds(tagIds, limit);
            Map<Integer, List<VideoJoin>> videoTagMap = new HashMap<>();
            resVideo.forEach(v -> {
                v.setVideoUrl(staticUrl(v.getVideoUrl()));
                v.setVideoPic(staticUrl(v.getVideoPic()));
                videoTagMap.computeIfAbsent(v.getTagId(), k -> new ArrayList<>()).add(v);
            });

            List<CmsTags> tagsList = db.dao().getTagsByIds(tagIds);
            tagsList.forEach(v -> {
                if (videoTagMap.containsKey(v.getId())) {
                    tagsVideoMap.put(v.getId(), new TagVideo(v.getId(), v.getTitle(), videoTagMap.get(v.getId())));
                }
            });
        }
        return new ArrayList<>(tagsVideoMap.values());
    }

    /**
     * get banner video list
     *
     * @param tagIds tag ids
     * @return banner video list
     */
    private List<BannerVideoResult> getBannerVideoList(List<Integer> tagIds) {
        List<BannerVideoResult> bannerVideoList = new ArrayList<>();
        if (!tagIds.isEmpty()) {
            List<BannerVideoResult> bannerVideoListTmp = db.dao().getBannerVideoList(tagIds, 10);
            Map<Integer, BannerVideoResult> bannerVideoMap = new HashMap<>();
            List<Integer> videoIds = new ArrayList<>();
            bannerVideoListTmp.forEach(v -> {
                videoIds.add(v.getVideoId());
                bannerVideoMap.put(v.getVideoId(), v);
            });

            List<VideoTagsInfo> videoTagsInfoList = db.dao().getVideoTagsInfoList(videoIds);
            Map<Integer, List<String>> videoTagsMap = new HashMap<>();
            videoTagsInfoList.forEach(v -> {
                videoTagsMap.computeIfAbsent(v.getVideoId(), k -> new ArrayList<>()).add(v.getTagTitle());
            });

            bannerVideoMap.values().forEach(v -> {
                v.setTags(Objects.requireNonNull(videoTagsMap.get(v.getVideoId())).toArray(new String[0]));
                v.setDescription("Shortly after installing Imgbot, you will receive a pull request with all of your images optimized. Just merge the pull request and you’re done! As you work on your project, Imgbot works alongside you to keep your images optimized.");
                v.setVideoUrl(staticUrl(v.getVideoUrl()));
                v.setVideoPic(staticUrl(v.getVideoPic()));
                bannerVideoList.add(v);
            });
        }
        return bannerVideoList;
    }

    /**
     * format static url
     *
     * @param url url
     * @return static url
     */
    private String staticUrl(String url) {
        if (url != null && !url.trim().startsWith("http")) {
            url = String.format("%s%s", staticHost, url);
        }
        return url == null ? "" : url.trim();
    }
}
