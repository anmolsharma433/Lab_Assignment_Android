package com.example.lab_assignment_android;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DatabaseDao {
    @Insert
    void insert(DatabaseLocations databaseLocations);

    @Update
    void update(DatabaseLocations databaseLocations);

    @Delete
    void delete(DatabaseLocations databaseLocations);

   @Query("select * from DatabaseLocations")
    List<DatabaseLocations> getDefault();
}
