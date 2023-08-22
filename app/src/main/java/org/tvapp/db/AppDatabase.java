package org.tvapp.db;


import static org.tvapp.base.Constants.DB_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;


import org.tvapp.db.dao.CmsDao;
import org.tvapp.db.entity.CmsCategory;
import org.tvapp.db.entity.CmsCategoryTag;
import org.tvapp.db.entity.CmsClassify;
import org.tvapp.db.entity.CmsRelations;
import org.tvapp.db.entity.CmsTags;
import org.tvapp.db.entity.CmsVersion;
import org.tvapp.db.entity.CmsVideo;

import java.io.File;


@Database(
        entities = {CmsVideo.class,
                CmsCategory.class,
                CmsCategoryTag.class,
                CmsRelations.class,
                CmsClassify.class,
                CmsVersion.class,
                CmsTags.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {

    // get dao object to operate database table data
    public abstract CmsDao dao();

    // create database from file
    public static AppDatabase createFromFile(Context context, File dbFile) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .createFromFile(dbFile)
                .fallbackToDestructiveMigration()
                .build();
    }

}



