package com.example.lab_assignment_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class ListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setHasFixedSize(true);

        //LocationsAdapter locationsAdapter = new LocationsAdapter();
        //recyclerView.setAdapter(locationsAdapter);
        userLocationsDB userLocationsDB = com.example.lab_assignment_android.userLocationsDB.getInstance(getContext());
        List<DatabaseLocations> databaseLocationsList = userLocationsDB.daoObjct().getDefault();

        if(databaseLocationsList.size()!=0) {
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            LocationsAdapter locationAdapter = new LocationsAdapter();


            locationAdapter.setLocations(databaseLocationsList);
            locationAdapter.context = getContext();
            recyclerView.setAdapter(locationAdapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

           ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new SwipeToDeleteCallbackForList(locationAdapter));
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }

    }
}