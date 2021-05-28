package com.example.runningtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    static final int START_SESSION_ACTIVITY_CODE = 1;

    private RunSessionViewModel mRunSessionViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialise recycler view so we can populate with data
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RunSessionAdapter adapter = new RunSessionAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRunSessionViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(RunSessionViewModel.class);

        mRunSessionViewModel.getAllRunSessions().observe(this, new Observer<List<RunSession>>() {
            @Override
            public void onChanged(@Nullable final List<RunSession> runSessions) {
                adapter.setData(runSessions);
            }
        });
    }

    //when the user clicks start to begin their run, we ask them for location permissions
    public void onStartClicked(View v){

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Intent newIntent = new Intent(this, CurrentRunSessionActivity.class);
            startActivityForResult(newIntent, START_SESSION_ACTIVITY_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
        }
    }

    @Override
    protected void onPause() {
        Log.d("G53MDP", "MainActivity onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("G53MDP", "MainActivity onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("g53mdp", "MainActivity onStart");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}