package org.tvapp.db.bean;


import androidx.room.ColumnInfo;

import java.io.Serializable;

public class VideoJoin implements Serializable {

    @ColumnInfo(name = "tag_id")
    private int tagId;
    @ColumnInfo(name = "video_id")
    private int videoId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "video_url")
    private String videoUrl;
    @ColumnInfo(name = "video_pic")
    private String videoPic;
    @ColumnInfo(name = "similar")
    private int similar;
    @ColumnInfo(name = "created_at")
    private String createdAt;
    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoPic() {
        return videoPic;
    }

    public void setVideoPic(String videoPic) {
        this.videoPic = videoPic;
    }

    public int getSimilar() {
        return similar;
    }

    public void setSimilar(int similar) {
        this.similar = similar;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
