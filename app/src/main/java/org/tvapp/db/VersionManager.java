package org.tvapp.db;



import static org.tvapp.base.Constants.DB_CHECK_TYPE;

import android.content.Context;

import com.google.gson.Gson;

import org.tvapp.db.bean.ExportVersionInfo;
import org.tvapp.db.bean.BaseResult;
import org.tvapp.db.entity.CmsVersion;
import org.tvapp.utils.FileUtils;
import org.tvapp.utils.LogUtils;
import org.tvapp.utils.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VersionManager {



    private final Context context;
    private final AppDatabase db;
    private final Gson gson = new Gson();
    private static volatile VersionManager instance;

    private VersionManager(Context context, AppDatabase db) {
        this.context = context.getApplicationContext();
        this.db = db;
    }

    public static VersionManager getInstance(Context context, AppDatabase db) {
        if (instance == null) {
            synchronized (VersionManager.class) {
                if (instance == null) {
                    instance = new VersionManager(context, db);
                }
            }
        }
        return instance;
    }

    public String initVersion(ExportVersionInfo versionFromCos) {
        Map<String, Object> data = new HashMap<>();
        CmsVersion curVersionInfo = currentDataVersion();

        if (!curVersionInfo.getVersion().equals(versionFromCos.getVersion())) {
            data.put("update", true);
            data.put("newVersion", versionFromCos.getVersion());
        }
        data.put("msg", "init ok");
        data.put("curVersion", curVersionInfo.getVersion());
        LogUtils.d("initVersion data: " + data);
        return BaseResult.successResult(data);
    }

    public String updateDataVersion() {
        try {
            CmsVersion curVersionInfo = currentDataVersion();
            String input = OkHttpUtils.readFile(FileUtils.getLatestVersionFile(context, DB_CHECK_TYPE));
            ExportVersionInfo latestVersionInfo = gson.fromJson(input, ExportVersionInfo.class);
            boolean success = updateVersion(curVersionInfo, latestVersionInfo);
            if (!success) {
                return BaseResult.errorResult("update failed");
            }
            return BaseResult.successResult("update success");
        } catch (Exception e) {
            LogUtils.d("updateDataVersion failed: " + e.getMessage());
            return BaseResult.errorResult("update failed");
        }
    }

    private boolean updateVersion(CmsVersion curVersion, ExportVersionInfo versionFromCos) {
        String videoDbRootPath = FileUtils.getDbFile(context).getParent();
        String fileFromCos = FileUtils.getDbFile(context).getAbsolutePath();
        String save2FileNewVersion = String.format("%s/tmp/%s.bin.tmp", videoDbRootPath, versionFromCos.getVersion());
        CompletableFuture<String> _downloadDbChan = new CompletableFuture<>();
        FileUtils.downloadDb(versionFromCos.getFile(), save2FileNewVersion)
                .thenAccept(_downloadDbChan::complete)
                .exceptionally(e -> {
                    _downloadDbChan.completeExceptionally(e);
                    return null;
                });
        String chanResult = FileUtils.readChanWithTimeout(_downloadDbChan);
        if (chanResult == null) {
            return false;
        }

        db.close();

        String currDbTmp = String.format("%s/tmp/db.bin.%s", videoDbRootPath, curVersion.getVersion());
        FileUtils.copyFile(fileFromCos, currDbTmp);

//        if (!new File(videoDbRootPath).exists()) {
//            System.out.println("updateVersion CopyFile failed NoExist");
//            return false;
//        }
//
//        String backupNewVersion = String.format("%s/backup/%s.db.bin", videoDbRootPath, versionFromCos.getVersion());
//
//        FileUtils.copyFile(save2FileNewVersion, backupNewVersion);
//
//        String lockFile = String.format("%s/lock", videoDbRootPath);
//
//        FileUtils.writeFile(lockFile, String.format("%s done", versionFromCos.getVersion()));

        File save2FileNewVersionFile = new File(save2FileNewVersion);
        if (save2FileNewVersionFile.exists()) {
            boolean delete = save2FileNewVersionFile.delete();
            if (!delete) {
                System.out.println("updateVersion delete failed");
                return false;
            }
        }

        return true;
    }

    public String checkVersionUpdate() {
        Map<String, Object> data = new HashMap<>();
        data.put("msg", "failed");
        data.put("update", false);
        data.put("curVersion", "");
        data.put("newVersion", "");

        CompletableFuture<String> _checkVersionChan = new CompletableFuture<>();
        FileUtils.downLatestVersionInfo(context, DB_CHECK_TYPE)
                .thenAccept(_checkVersionChan::complete)
                .exceptionally(e -> {
                    _checkVersionChan.completeExceptionally(e);
                    return null;
                });
        CmsVersion curVersionInfo = currentDataVersion();
        String chanResult = FileUtils.readChanWithTimeout(_checkVersionChan);
        if (chanResult != null) {
            ExportVersionInfo versionFromCos;
            try {
                versionFromCos = new Gson().fromJson(chanResult, ExportVersionInfo.class);
            } catch (Exception e) {
                System.out.println("VideoInit json.Unmarshal versionInfoFromCos failed: " + e.getMessage());
                return BaseResult.errorResult("init failed");
            }

            if (!curVersionInfo.getVersion().equals(versionFromCos.getVersion())) {
                data.put("update", true);
                data.put("newVersion", versionFromCos.getVersion());
            }
        }
        data.put("msg", "fix some bugs");
        data.put("curVersion", curVersionInfo.getVersion());
        LogUtils.d("checkVersionUpdate data: " + data);
        return BaseResult.successResult(data);


    }

    private CmsVersion currentDataVersion() {
        return db.dao().currentDataVersion();
    }


}
