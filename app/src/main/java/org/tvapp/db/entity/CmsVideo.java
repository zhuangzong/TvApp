package org.tvapp.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "cms_video", indices = {
        @Index(value = {"deleted_at"},orders = Index.Order.ASC,name = "idx_cms_video_deleted_at"),
        @Index(value = {"create_by"},orders = Index.Order.ASC,name = "idx_cms_video_create_by"),
        @Index(value = {"update_by"},orders = Index.Order.ASC,name = "idx_cms_video_update_by"),
        @Index(value = {"deleted_at"},orders = Index.Order.ASC,name = "index_cms_video_deleted_at"),
        @Index(value = {"create_by"},orders = Index.Order.ASC,name = "index_cms_video_create_by"),
        @Index(value = {"update_by"},orders = Index.Order.ASC,name = "index_cms_video_update_by"),
})
public class CmsVideo {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "title")
    @NonNull
    private String title;

    @ColumnInfo(name = "video_url")
    @NonNull
    private String videoUrl;

    @ColumnInfo(name = "video_pic")
    @NonNull
    private String videoPic;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "classify_id")
    private int classifyId;

    @ColumnInfo(name = "similar")
    private int similar;

    @ColumnInfo(name = "create_by")
    private Integer createBy;

    @ColumnInfo(name = "update_by")
    private Integer updateBy;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    @ColumnInfo(name = "deleted_at")
    private String deletedAt;

    @ColumnInfo(name = "source_type",defaultValue = "0")
    private Integer sourceType;

    public CmsVideo(Integer id, @NonNull String title, @NonNull String videoUrl, @NonNull String videoPic, int categoryId, int classifyId, int similar, int createBy, int updateBy, String createdAt, String updatedAt, String deletedAt, int sourceType) {
        this.id = id;
        this.title = title;
        this.videoUrl = videoUrl;
        this.videoPic = videoPic;
        this.categoryId = categoryId;
        this.classifyId = classifyId;
        this.similar = similar;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.sourceType = sourceType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public int getSimilar() {
        return similar;
    }

    public void setSimilar(int similar) {
        this.similar = similar;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }
}
