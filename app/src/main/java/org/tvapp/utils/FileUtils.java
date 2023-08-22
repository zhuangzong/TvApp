package org.tvapp.utils;


import static org.tvapp.base.Constants.DB_NAME;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;


public class FileUtils {
    private static final String BASE_URL = "https://video-1305950406.cos.ap-guangzhou.myqcloud.com/";
    private static final String LATEST_VERSION_FILE = "latest.version";
    private static final String LATEST_VERSION_APP_FILE = "latest.version.app";

    public static void copyFile(String sourcePath, String destinationPath) {
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);
        if(!Objects.requireNonNull(destinationFile.getParentFile()).exists()){
            boolean mkdirs = destinationFile.getParentFile().mkdirs();
            if (!mkdirs) {
                return;
            }
        }
        try (FileInputStream inputStream = new FileInputStream(sourceFile);
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String filePath, String content) {
        File file = new File(filePath);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File assetsToFile(Context context, String assetsName, String filePath) {
        try (InputStream inputStream = context.getAssets().open(assetsName);
             FileOutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            return new File(filePath);
        }catch (IOException e) {
            LogUtils.d("assetsToFile error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static CompletableFuture<String> downLatestVersionInfo(Context context,String checkType) {
        CompletableFuture<String> future = new CompletableFuture<>();
        String fileFromCos = String.format("cfg/latest%s", checkType);
        File file = getLatestVersionFile(context,checkType);
        try {
            String url = String.format("%s%s", BASE_URL, fileFromCos);
            OkHttpUtils.downLoadFile(url, file.getName(), file.getParent(), new OkHttpUtils.CallBack() {
                @Override
                public void requestSuccess(String result) throws Exception {
                    String versionStr = OkHttpUtils.readFile(file);
                    future.complete(versionStr);
                }

                @Override
                public void requestFailure(Request request, IOException e) {
                    future.completeExceptionally(e);
                }
            });
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }



    public static File getDbFile(Context context) {
        return new File(context.getFilesDir().getAbsolutePath() + File.separator + DB_NAME);
    }

    public static File getLatestVersionFile(Context context,String checkType) {
        String path = getDbFile(context).getParent();
        String fileName = checkType.equals(".app") ? LATEST_VERSION_APP_FILE : LATEST_VERSION_FILE;
        return new File(path, fileName);
    }

    public static String readChanWithTimeout(CompletableFuture<String> chan) {
        try {
            return chan.get(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return null;
        }
    }


    public static CompletableFuture<String> downloadDb(String fileFromCos, String path){
        CompletableFuture<String> future = new CompletableFuture<>();
        String url = String.format("%s%s", BASE_URL, fileFromCos);
        File file = new File(path);
        OkHttpUtils.downLoadFile(url, file.getName(), path, new OkHttpUtils.CallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                LogUtils.d("downloadDb requestSuccess: " + result);
                future.complete(path);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                LogUtils.d("downloadDb requestFailure: " + e.getMessage());
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
