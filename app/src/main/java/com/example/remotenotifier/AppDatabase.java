package com.example.remotenotifier;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {SenderEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

//    private static AppDatabase instance = null;
//
//    protected AppDatabase(){}

    public abstract SenderDao senderDao();

//    public static synchronized AppDatabase getInstance() {
//        if(null == instance){
//            instance = new AppDatabase() {
//                @Override
//                public SenderDao senderDao() {
//                    return null;
//                }
//
//                @NonNull
//                @Override
//                protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
//                    return null;
//                }
//
//                @NonNull
//                @Override
//                protected InvalidationTracker createInvalidationTracker() {
//                    return null;
//                }
//
//                @Override
//                public void clearAllTables() {
//
//                }
//            };
//        }
//        return instance;
//    }
}
