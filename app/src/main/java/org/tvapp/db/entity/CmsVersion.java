package org.tvapp.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "cms_version", indices = {
        @Index(value = {"version"}, name = "idx_cms_version_version", unique = true),
        @Index(value = {"update_by"}, name = "idx_cms_version_update_by"),
        @Index(value = {"create_by"}, name = "idx_cms_version_create_by"),
        @Index(value = {"deleted_at"}, name = "idx_cms_version_deleted_at")
})
public class CmsVersion {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "version")
    @NonNull
    private String version;

    @ColumnInfo(name = "file")
    @NonNull
    private String file;

    @ColumnInfo(name = "publish")
    @NonNull
    private Integer publish;

    @ColumnInfo(name = "md5")
    private String md5;

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


    public CmsVersion(@NonNull String version, @NonNull String file, @NonNull Integer publish) {
        this.version = version;
        this.file = file;
        this.publish = publish;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getVersion() {
        return version;
    }

    public void setVersion(@NonNull String version) {
        this.version = version;
    }

    @NonNull
    public String getFile() {
        return file;
    }

    public void setFile(@NonNull String file) {
        this.file = file;
    }

    @NonNull
    public Integer getPublish() {
        return publish;
    }

    public void setPublish(@NonNull Integer publish) {
        this.publish = publish;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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
}
