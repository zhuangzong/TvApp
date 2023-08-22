package org.tvapp.db;


import android.content.Context;

import org.tvapp.db.callback.OnGetRecommendListCallback;
import org.tvapp.db.callback.OnGetVideoDetailCallback;
import org.tvapp.db.callback.OnInitCallback;
import org.tvapp.db.callback.OnVideoSearchCallback;
import org.tvapp.db.callback.ResultCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHelper {

    private final ExecutorService executor; // thread pool
    private final DatabaseManager databaseManager; // database helper
    private final ResultCallback resultCallback;
    private final OnGetRecommendListCallback onGetRecommendListCallback;
    private final OnGetVideoDetailCallback onGetVideoDetailCallback;
    private final OnInitCallback onInitCallback;
    private final OnVideoSearchCallback onVideoSearchCallback;
    private final Context context;

    private DatabaseHelper(Builder builder) {
        this.resultCallback = builder.resultCallback;
        this.onGetRecommendListCallback = builder.onGetRecommendListCallback;
        this.onGetVideoDetailCallback = builder.onGetVideoDetailCallback;
        this.onInitCallback = builder.onInitCallback;
        this.onVideoSearchCallback = builder.onVideoSearchCallback;
        context = builder.context;
        this.executor = Executors.newSingleThreadExecutor();
        this.databaseManager = DatabaseManager.getInstance(context);
    }

    public void callInit(String params) {
        executor.submit(() -> {
            String result = databaseManager.initDatabase(params);
            onInitCallback.onInitComplete(result);
            onInitCallback.onInitComplete(result);
        });
    }

    public void callGetRecommendList(String params) {
        executor.submit(() -> {
            String result = databaseManager.getRecommendList(params);
            onGetRecommendListCallback.onGetRecommendListComplete(result);
        });
    }

    public void callGetVideoDetail(String params) {
        executor.submit(() -> {
            String result = databaseManager.getVideoDetail(params);
            onGetVideoDetailCallback.onGetVideoDetailComplete(result);
        });
    }

    public void callVideoSearch(String params) {
        executor.submit(() -> {
            String result = databaseManager.videoSearch(params);
            onVideoSearchCallback.onVideoSearchComplete(result);
        });
    }

    public void callUpdateDataVersion() {
        executor.submit(() -> {
            String result = databaseManager.updateDataVersion();
            resultCallback.onUpdateDataVersionComplete(result);
        });
    }

    public void callCheckVersionUpdate() {
        executor.submit(() -> {
            String result = databaseManager.checkVersionUpdate();
            resultCallback.onCheckVersionUpdateComplete(result);
        });
    }

    public static class Builder {
        private final Context context;
        private ResultCallback resultCallback;
        private OnGetRecommendListCallback onGetRecommendListCallback;
        private OnGetVideoDetailCallback onGetVideoDetailCallback;
        private OnInitCallback onInitCallback;
        private OnVideoSearchCallback onVideoSearchCallback;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setResultCallback(ResultCallback resultCallback) {
            this.resultCallback = resultCallback;
            return this;
        }

        public Builder setOnGetRecommendListCallback(OnGetRecommendListCallback onGetRecommendListCallback) {
            this.onGetRecommendListCallback = onGetRecommendListCallback;
            return this;
        }

        public Builder setOnGetVideoDetailCallback(OnGetVideoDetailCallback onGetVideoDetailCallback) {
            this.onGetVideoDetailCallback = onGetVideoDetailCallback;
            return this;
        }

        public Builder setOnInitCallback(OnInitCallback onInitCallback) {
            this.onInitCallback = onInitCallback;
            return this;
        }

        public Builder setOnVideoSearchCallback(OnVideoSearchCallback onVideoSearchCallback) {
            this.onVideoSearchCallback = onVideoSearchCallback;
            return this;
        }

        public DatabaseHelper build() {
            return new DatabaseHelper(this);
        }
    }
}

