package com.example.mymp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

//created by psybb4 BULUTHAN BOZKURT 14326878


//i used the examples and amended them according to my
// needs: https://github.com/mdf/comp3018/tree/main/MartinBoundService/app/src/main/java/com/example/pszmdf/martinboundservice

public class MainActivity extends AppCompatActivity {

    private  MP3Service.MP3Binder myService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView lv = (ListView) findViewById(R.id.listMusic);

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.IS_MUSIC + "!= 0", null, null);

        lv.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[] { MediaStore.Audio.Media.DATA}, new int[] { android.R.id.text1 }));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter,
                                    View myView,
                                    int myItemInt,
                                    long mylng) {
            Cursor c = (Cursor) lv.getItemAtPosition(myItemInt);
            String uri = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
            myService.callLoad(uri);
            myService.callCreateNotification();
            }
        });

        Intent intent = new Intent(MainActivity.this, MP3Service.class);
        this.startService(intent);
        this.bindService(intent, myServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = (MP3Service.MP3Binder) service;
            Log.d("g53mdp", "onServiceConnected");
            myService.registerCallback(callback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService.unregisterCallback(callback);
            Log.d("g53mdp", "onServiceDisconnected");
            myService = null;
        }
    };

    //set progress of music and also the max duration of the music
    CallbackI callback = new CallbackI() {
        @Override
        public void musicProgressEvent(final int durationOfMusic, final int progress) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressBar musicProgressBar = (ProgressBar) findViewById(R.id.progressBar);
                    //Log.d("g53mdp", String.valueOf(durationOfMusic));
                    musicProgressBar.setMax(durationOfMusic);
                    musicProgressBar.setProgress(progress);
                }
            });
        }
    };

    public void onPlayClicked(View v){
        if(myService != null){
            myService.callPlay();
        }
        Log.d("g53mdp", "onPlay");
    }

    public void onPauseClicked(View v){
        if(myService != null) {
            myService.callPause();
        }
        Log.d("g53mdp", "onPause");
    }

    public void onStopClicked(View v){
        if(myService != null) {
            myService.callStop();
        }
        myService.callStopNotification();
        Log.d("g53mdp", "onStop");
    }

    //activity lifecycle
    @Override
    protected void onPause() {
        Log.d("g53mdp", "MainActivity onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("g53mdp", "MainActivity onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("g53mdp", "MainActivity onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myServiceConnection != null) {
            unbindService(myServiceConnection);
            myServiceConnection = null; }
       Log.d("g53mdp", "MainActivity onDestroy");
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