package org.tvapp.db.bean;


import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class BannerVideoResult implements java.io.Serializable{

    @ColumnInfo(name = "video_id")
    private int videoId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "video_url")
    private String videoUrl;
    @ColumnInfo(name = "video_pic")
    private String videoPic;
    @ColumnInfo(name = "created_at")
    private String createdAt;
    @ColumnInfo(name = "updated_at")
    private String updatedAt;
    @Ignore
    private String[] tags;
    @Ignore
    private String description;

    public BannerVideoResult() {
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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
