package com.example.lab_assignment_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationHolder>{
    private List<DatabaseLocations> locations;
    Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_list_cell,parent,false);
        return new LocationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        DatabaseLocations currentFavLocation = locations.get(position);
        holder.address.setText("Address : " + currentFavLocation.getAddress());
        holder.latitude.setText("Latitude : " + currentFavLocation.getLatitude());
        holder.longitude.setText("Longitude : " + currentFavLocation.getLongitude());
        holder.date.setText("Date : " + currentFavLocation.getDate());
    }

    public void setLocations(List<DatabaseLocations> locations){
        this.locations = locations;
        //notifyDataSetChanged();
    }

    public List<DatabaseLocations> getFavLocationList() {
        return locations;
    }

    @Override
    public int getItemCount() {
        System.out.println(locations.size());
        return locations.size();
    }



    public class LocationHolder extends RecyclerView.ViewHolder {
        private TextView address,latitude,longitude,date;
        private ImageButton directions;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            date =itemView.findViewById(R.id.date);

        }
    }

    public void deleteItem(int position) {

        DatabaseLocations places = locations.get(position);
        userLocationsDB userDatabase = userLocationsDB.getInstance(context);
        userDatabase.daoObjct().delete(places);
        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
        locations.remove(position);
        notifyDataSetChanged();
    }




}
