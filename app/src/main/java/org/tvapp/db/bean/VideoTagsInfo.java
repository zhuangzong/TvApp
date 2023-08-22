package org.tvapp.db.bean;


import androidx.room.ColumnInfo;

import java.io.Serializable;

public class VideoTagsInfo implements Serializable {

    @ColumnInfo(name = "tag_id")
    private Integer tagId;
    @ColumnInfo(name = "video_id")
    private Integer videoId;
    @ColumnInfo(name = "tag_title")
    private String tagTitle;

    public VideoTagsInfo() {
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }
}
