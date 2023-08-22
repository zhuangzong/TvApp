package org.tvapp.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "cms_category_tag", indices = {
        @Index(value = {"update_by"}, name = "idx_cms_category_tag_update_by"),
        @Index(value = {"create_by"}, name = "idx_cms_category_tag_create_by"),
        @Index(value = {"deleted_at"}, name = "idx_cms_category_tag_deleted_at")
})
public class CmsCategoryTag {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "tag_id")
    @NonNull
    private Integer tagId;

    @ColumnInfo(name = "category_id")
    @NonNull
    private Integer categoryId;

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


    public CmsCategoryTag(@NonNull Integer tagId, @NonNull Integer categoryId) {
        this.tagId = tagId;
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(@NonNull Integer tagId) {
        this.tagId = tagId;
    }

    @NonNull
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull Integer categoryId) {
        this.categoryId = categoryId;
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
