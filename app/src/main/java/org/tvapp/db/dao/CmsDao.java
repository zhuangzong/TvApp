package org.tvapp.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RoomWarnings;


import org.tvapp.db.bean.BannerVideoResult;
import org.tvapp.db.bean.VideoJoin;
import org.tvapp.db.bean.VideoTagsInfo;
import org.tvapp.db.entity.CmsCategoryTag;
import org.tvapp.db.entity.CmsTags;
import org.tvapp.db.entity.CmsVersion;
import org.tvapp.db.entity.CmsVideo;

import java.util.List;

@Dao
public interface CmsDao {

    // Get all videos by id
    @Query("SELECT * FROM cms_video WHERE id = :id")
    CmsVideo getVideoById(int id);

    // Get Category Tags by Category Id
    @Query("SELECT * FROM cms_category_tag WHERE category_id = :categoryId")
    List<CmsCategoryTag> getCategoryTags(int categoryId);

    // Get all videos by tag ids
    @Query("SELECT cr.tag_id, cr.video_id, cv.title, cv.video_url, cv.video_pic, cv.similar, cv.created_at, cv.updated_at " +
            "FROM cms_relations cr " +
            "INNER JOIN cms_video cv ON cr.video_id = cv.id " +
            "WHERE cr.tag_id IN (:tagIds) AND cr.deleted_at IS NULL AND cv.deleted_at IS NULL " +
            "LIMIT :limit")
    List<VideoJoin> getVideosByTagIds(List<Integer> tagIds, int limit);

    // Get all tags by tag ids
    @Query("SELECT * FROM cms_tags WHERE id IN (:tagIds) AND deleted_at IS NULL")
    List<CmsTags> getTagsByIds(List<Integer> tagIds);

    // Get banner video list by tag ids
    @Query("SELECT cr.tag_id, cr.video_id, cv.title, cv.video_url, cv.video_pic, cv.similar, cv.created_at, cv.updated_at " +
            "FROM cms_relations cr " +
            "INNER JOIN cms_video cv ON cr.video_id = cv.id " +
            "WHERE cr.tag_id IN (:tagIds) AND cr.deleted_at IS NULL AND cv.deleted_at IS NULL " +
            "LIMIT :limit")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    List<BannerVideoResult> getBannerVideoList(List<Integer> tagIds, int limit);

    // Get video tags info list by video ids
    @Query("SELECT ct.title as tag_title, cr.tag_id, cr.video_id " +
            "FROM cms_tags ct " +
            "INNER JOIN cms_relations cr ON ct.id = cr.tag_id AND cr.video_id IN (:videoIds) AND cr.deleted_at IS NULL AND ct.deleted_at IS NULL")
    List<VideoTagsInfo> getVideoTagsInfoList(List<Integer> videoIds);

    // Get video tags info by video id
    @Query("SELECT ct.title as tag_title, cr.tag_id, cr.video_id " +
            "FROM cms_tags ct " +
            "INNER JOIN cms_relations cr ON ct.id = cr.tag_id AND cr.video_id = :videoId AND cr.deleted_at IS NULL AND ct.deleted_at IS NULL")
    List<VideoTagsInfo> getVideoTagsInfo(int videoId);

    // Get video list by title
    @Query("SELECT * FROM cms_video WHERE title LIKE :title AND deleted_at IS NULL LIMIT :limit")
    List<CmsVideo> getVideoListByTitle(String title, int limit);

    @Query("SELECT * FROM cms_version WHERE publish = 1 AND deleted_at IS NULL ORDER BY id DESC LIMIT 1")
    CmsVersion currentDataVersion();

}
