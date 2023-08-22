package org.tvapp.db.bean;


import org.tvapp.db.entity.CmsVideo;

public class CmsVideoItem {

    private CmsVideo cmsVideo;
    private String date;

    public CmsVideoItem() {
    }

    public CmsVideoItem(CmsVideo cmsVideo, String date) {
        this.cmsVideo = cmsVideo;
        this.date = date;
    }

    public CmsVideo getCmsVideo() {
        return cmsVideo;
    }

    public void setCmsVideo(CmsVideo cmsVideo) {
        this.cmsVideo = cmsVideo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
