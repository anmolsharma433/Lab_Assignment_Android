package com.example.lab_assignment_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lab_assignment_android.Database.GetNearbyPlaceData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    //variables
    private static final String TAG = "MapsFragment";
    private GoogleMap mMap;
    public final int REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    Double latitude, longitude;
    int mapType = GoogleMap.MAP_TYPE_NORMAL;
    String url;
    final int RADIUS = 1500;
    double destLAt,destLong;
    //map


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.map, fragment).commit();
        }

        fragment.getMapAsync(this);

        getUserLocation();
        if (!checkPermission()) {
            requestPermission();
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }


        //search bar
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if(autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields( Arrays.asList( Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS,Place.Field.ADDRESS_COMPONENTS));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    LatLng latLng = place.getLatLng();
                    if (latLng != null) {
                        mMap.addMarker(new MarkerOptions().position(latLng)
                                .title(place.getName())
                                .snippet(place.getAddress())
                                .icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_GREEN ) ));
                        CameraPosition cameraPosition = CameraPosition.builder()
                                .target( latLng )
                                .zoom( 15 )
                                .bearing( 0 )
                                .tilt( 45 )
                                .build();
                        mMap.animateCamera( CameraUpdateFactory.newCameraPosition( cameraPosition ) );

                    }
                }

                @Override
                public void onError(@NonNull Status status) {

                }
            });
        }
        String apikey = "AIzaSyC__PTaY-owTjlo9M1VGm33Nh8dp9sygI8";

        if(!Places.isInitialized())
        {
            Places.initialize(getActivity(), getString(R.string.api_key_places));
        }
        PlacesClient placesClient = Places.createClient(getActivity());

    }


    //map functions
    private Boolean checkPermission() {
        int permissionState = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

    }

    private void getUserLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    //new
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("You are here!"));
                    CameraPosition cameraPosition = CameraPosition.builder()
                            .target(userLocation)
                            .zoom(15)
                            .bearing(0)
                            .tilt(45)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        };


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map_fragment, container, false);
        final ImageButton normal;
        final ImageButton sattelite;
        final ImageButton terrain;
        normal = view.findViewById(R.id.normal_mapType);
        sattelite = view.findViewById(R.id.sattelite_maptype);
        terrain = view.findViewById(R.id.terrain_maptype);

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapType = GoogleMap.MAP_TYPE_NORMAL;
                onMapReady(mMap);
            }
        });

        sattelite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("i am in sattellite");
                mapType = GoogleMap.MAP_TYPE_SATELLITE;
                onMapReady(mMap);
            }
        });

        terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapType = GoogleMap.MAP_TYPE_TERRAIN;
                onMapReady(mMap);
            }
        });

        //buttons
        Button Restaurants , gas, atm, mueseums,cafes;
        Restaurants = view.findViewById(R.id.restaurant);
        Restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                url = getUrl( latitude, longitude, "restaurant" );
                Log.i("MainActivity", url);
                // setmarkers( url );
                Object[] dataTransfer = new Object[2];
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                GetNearbyPlaceData getNearbyPlaceData = new GetNearbyPlaceData();
                getNearbyPlaceData.execute(dataTransfer);
               // Toast.makeText( this, "Restautants", Toast.LENGTH_SHORT ).show();
            }
        });

        //atm
        atm = view.findViewById(R.id.atm);
        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                url = getUrl( latitude, longitude, "atm" );
                Log.i("MainActivity", url);
                // setmarkers( url );
                Object[] dataTransfer = new Object[2];
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                GetNearbyPlaceData getNearbyPlaceData = new GetNearbyPlaceData();
                getNearbyPlaceData.execute(dataTransfer);
                // Toast.makeText( this, "Restautants", Toast.LENGTH_SHORT ).show();
            }
        });

        //mueseums
        mueseums = view.findViewById(R.id.mueseum);
        mueseums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                url = getUrl( latitude, longitude, "mueseum" );
                Log.i("MainActivity", url);
                // setmarkers( url );
                Object[] dataTransfer = new Object[2];
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                GetNearbyPlaceData getNearbyPlaceData = new GetNearbyPlaceData();
                getNearbyPlaceData.execute(dataTransfer);
                // Toast.makeText( this, "Restautants", Toast.LENGTH_SHORT ).show();
            }
        });

        //cafes
        cafes = view.findViewById(R.id.cafe);
        cafes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                url = getUrl( latitude, longitude, "Cafe" );
                Log.i("MainActivity", url);
                // setmarkers( url );
                Object[] dataTransfer = new Object[2];
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                GetNearbyPlaceData getNearbyPlaceData = new GetNearbyPlaceData();
                getNearbyPlaceData.execute(dataTransfer);
            }
        });

        //Gas
        gas = view.findViewById(R.id.gas_station);
        gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                url = getUrl( latitude, longitude, "gas" );
                Log.i("MainActivity", url);
                // setmarkers( url );
                Object[] dataTransfer = new Object[2];
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                GetNearbyPlaceData getNearbyPlaceData = new GetNearbyPlaceData();
                getNearbyPlaceData.execute(dataTransfer);
            }
        });

        return view;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMyLocationEnabled(true);
        setStoredMarkers();
        mMap.setMapType(mapType);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                //calendar

                userLocationsDB userLocationsDB = com.example.lab_assignment_android.userLocationsDB.getInstance(getContext());
                List<DatabaseLocations> locations = userLocationsDB.daoObjct().getDefault();

                DatabaseLocations location = new DatabaseLocations(locations.size() + 1, latLng.latitude, latLng.longitude);

                String address = getAddress(latLng.latitude, latLng.longitude);
                location.setAddress(address);
                userLocationsDB.daoObjct().insert(location);

                Toast.makeText(getContext(), "Size" + locations.size(), Toast.LENGTH_SHORT).show();

                Toast.makeText(getContext(), address, Toast.LENGTH_SHORT).show();
                setStoredMarkers();


            }
        });




    }

    private void setStoredMarkers() {

        userLocationsDB placesDB = userLocationsDB.getInstance(getContext());
        List<DatabaseLocations> locations = placesDB.daoObjct().getDefault();


        for (int i = 0; i < locations.size(); i++) {
            LatLng latLng = new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude());
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(latLng)
                    .zoom(15)
                    .bearing(0)
                    .tilt(45)
                    .build();

            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .title(locations.get(i).getAddress())
                    .draggable(true)
                    .snippet("DESTINATION")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
        }
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses!=null && addresses.size() > 0 ) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0)).append(" ");
                result.append(address.getThoroughfare()).append(" ");
                result.append(address.getLocality()).append(" ");
                result.append(address.getPostalCode()).append(" ");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }


        return result.toString();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder placeUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?"  );
        placeUrl.append( "location=" + latitude + "," + longitude );
        placeUrl.append( "&radius=" + RADIUS );
        placeUrl.append( "&type=" + nearbyPlace );
        //placeUrl.append( "&keyword=cruise" );
        placeUrl.append( "&key=" + getString(R.string.api_key ));
        return placeUrl.toString();
    }

}
