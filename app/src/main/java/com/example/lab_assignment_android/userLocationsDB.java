package com.example.lab_assignment_android;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

@Database(entities = DatabaseLocations.class,exportSchema = false,version = 1)
public abstract class userLocationsDB extends RoomDatabase {

    public static final String DB_NAME = "user_database";

    private static  userLocationsDB uInstance;


    public static userLocationsDB getInstance(Context context)
    {
        if(uInstance == null)
        {
            uInstance = Room.databaseBuilder(context.getApplicationContext(), userLocationsDB.class,userLocationsDB.DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return uInstance;
    }
    public abstract DatabaseDao daoObjct();

}
