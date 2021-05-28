package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

//this activity shows the current data of the users running session

public class CurrentRunSessionActivity extends AppCompatActivity {

    static final int STOP_SESSION_ACTIVITY_CODE = 2;

    Integer currentSpeedInt, totalDistanceInt;

    private Intent serviceIntent;

    MyBroadcastReceiver receiver;

    LocationTrackerService.LocationTrackerBinder binder = null;

    TextView speedCurrent, durationCurrent, distanceCurrent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_run_session);

        //initialise the textviews
        speedCurrent = (TextView) findViewById(R.id.speedTV);
        durationCurrent = (TextView) findViewById(R.id.durationTV);
        distanceCurrent = (TextView) findViewById(R.id.currentDistanceTV);

        IntentFilter findIntent = new IntentFilter("com.example.android.runningtracker.BROADCAST");
        receiver = new MyBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, findIntent);

        serviceIntent = new Intent(this, LocationTrackerService.class);
        this.startService(serviceIntent);
        this.bindService(serviceIntent, serviceConnection, Service.BIND_AUTO_CREATE);

    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("G53MDP", "onServiceConnected");
            binder = (LocationTrackerService.LocationTrackerBinder)service;
            binder.startRunSession();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("G53MDP", "onServiceDisconnected");
            binder = null;
        }
    };

    //recieves the sent broadcast by checking intent action
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.example.android.runningtracker.BROADCAST")) {
                Log.d("G53MDP","Broadcast recieved...");
                //gets the values from the intent
                currentSpeedInt = intent.getIntExtra("currentSpeed", 0);
                totalDistanceInt = intent.getIntExtra("currentDistance", 0);
            }
            updateCurrentTextViews();
        }
    };

    //update the textviews with real time values
    private void updateCurrentTextViews(){
        speedCurrent.setText("Speed: " + currentSpeedInt);
        distanceCurrent.setText("Distance: " + totalDistanceInt);
    }

    //when stop clicked, we stop the gps via the binder and we open the activity to add to recyclerview
    public void onStopClicked(View v){
        Intent newIntent = new Intent(this, AddRunSessionActivity.class);
        startActivityForResult(newIntent, STOP_SESSION_ACTIVITY_CODE);
        binder.stopRunSession();
    }

    @Override
    protected void onPause() {
        Log.d("G53MDP", "CurrentRunSessionActivity onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("G53MDP", "CurrentRunSessionActivity onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("G53MDP", "CurrentRunSessionActivity onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null) {
            unbindService(serviceConnection);
            serviceConnection = null; }
        Log.d("G53MDP", "CurrentRunSessionActivity onDestroy");
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