package com.example.lab_assignment_android;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "DatabaseLocations")
public class DatabaseLocations implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "address")
    private String address = "";

    protected DatabaseLocations(Parcel in) {
        id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        date = in.readString();
    }

    public DatabaseLocations(int id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.CANADA);
        date = formatter.format(new Date());
    }

    public static final Creator<DatabaseLocations> CREATOR = new Creator<DatabaseLocations>() {
        @Override
        public DatabaseLocations createFromParcel(Parcel in) {
            return new DatabaseLocations(in);
        }

        @Override
        public DatabaseLocations[] newArray(int size) {
            return new DatabaseLocations[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(date);
    }
}
