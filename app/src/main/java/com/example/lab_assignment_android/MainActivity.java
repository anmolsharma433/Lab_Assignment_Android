package com.example.lab_assignment_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //from https://www.javatpoint.com/android-hide-title-bar-example
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        setContentView(R.layout.activity_main);

        //initializing the bottom nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        //Default Fragment on startup
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MapFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               Fragment fragmentSelected = null;
                switch (menuItem.getItemId())
                {
                    case R.id.map:
                        toastMethod("Map");
                        fragmentSelected = new MapFragment();
                        break;
                    case R.id.list:
                        toastMethod("List");
                        fragmentSelected = new ListFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentSelected).commit();
                return true;
            }
        });
    }

    //Toast Function
    private void toastMethod(String message)
    {
       Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
