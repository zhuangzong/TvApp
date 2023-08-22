package org.tvapp.db.bean;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import java.util.List;


public class TagVideo {
    @ColumnInfo(name = "tag_id")
    private int tagId;
    @ColumnInfo(name = "tag_title")
    private String tagTitle;
    @Ignore
    private List<VideoJoin> videoList;

    public TagVideo(Integer tagId, String tagTitle, List<VideoJoin> videoList) {
        this.tagId = tagId;
        this.tagTitle = tagTitle;
        this.videoList = videoList;
    }

    public TagVideo() {
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public List<VideoJoin> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoJoin> videoList) {
        this.videoList = videoList;
    }
}
