package com.example.mymp3player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.util.Log;
import androidx.core.app.NotificationCompat;

//i used the examples and amended them according to my
// needs: https://github.com/mdf/comp3018/tree/main/MartinBoundService/app/src/main/java/com/example/pszmdf/martinboundservice

public class MP3Service extends Service {

    private static final String CHANNEL_ID = "200";
    private static final int NOTIFICATION_ID = 011 ;
    MP3Player myPlayer = new MP3Player();
    CountProgress progressCount;

    RemoteCallbackList<MP3Binder> remoteCallbackList = new RemoteCallbackList<MP3Binder>();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("g53mdp", "onBind");
        return new MP3Binder();
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        Log.d("g53mdp", "service onCreate");
        super.onCreate();
        progressCount = new CountProgress();
    }

    public int onStartCommand(Intent intent, int flags, int startID) {
        Log.d("g53mdp", "onStartCommand");
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Log.d("g53mdp", "onTaskRemoved");
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.d("g53mdp", "service onDestroy");
        progressCount.running = false;
        progressCount = null;
        myPlayer.stop();
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("g53mdp", "service onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("g53mdp", "service onUnbind");
        return super.onUnbind(intent);
    }

    protected class CountProgress extends Thread implements Runnable {

        public int progress = 0, durationOfMusic;
        public boolean playing = false;
        public boolean running = true;


        public CountProgress() {
            this.start();
        }

        public void run() {

            while(this.running) {

                try {Thread.sleep(2000);} catch(Exception e) {return;}

                if(playing){
                    progress = myPlayer.getProgress();
                }
                doCallbacks(durationOfMusic, progress);
                Log.d("g53mdp", "Music progress " + progress);
            }
            Log.d("g53mdp", "Music progress thread exiting");
        }
    }

    public void doCallbacks(int durationOfMusic, int progress) {
        final int n = remoteCallbackList.beginBroadcast();
        for (int i=0; i<n; i++) {
            remoteCallbackList.getBroadcastItem(i).callback.musicProgressEvent(durationOfMusic, progress);
        }
        remoteCallbackList.finishBroadcast();
    }

    public MP3Service(){

    }

    private void play(){
        progressCount.playing = true;
        myPlayer.play();
    }

    private void pause(){
        progressCount.playing = false;
        myPlayer.pause();
    }

    private void stop(){
        this.resetProgress(2);
        myPlayer.stop();
    }

    private void load(String filePath){
        if(myPlayer.getState() == MP3Player.MP3PlayerState.PLAYING){
            myPlayer.stop();
        }
        myPlayer.load(filePath);
        resetProgress(1);
        progressCount.durationOfMusic = myPlayer.getDuration();
    }

    private void stopNotification(){
        stopForeground(true);
    }

    //resets progress bar
    private void resetProgress(int status){ // 1 = begin progress, anything other than 1 = don't progress because music is not playing
        if(status == 1){
            progressCount.playing = true;
        } else{
            progressCount.playing = false;
        }
        progressCount.progress = 0;
    }

    public class MP3Binder extends Binder implements IInterface {

        CallbackI callback;

        @Override
        public IBinder asBinder() {return this;}

        void callPlay(){
            MP3Service.this.play();
        }

        void callPause(){
            MP3Service.this.pause();
        }

        void callStop(){
            MP3Service.this.stop();
        }

        void callLoad(String filePath){
            MP3Service.this.load(filePath);
        }

        void callCreateNotification(){MP3Service.this.createNotification();}

        void callStopNotification(){MP3Service.this.stopNotification();}

        public void registerCallback(CallbackI callback) {
            this.callback = callback;
            remoteCallbackList.register(MP3Binder.this);
        }
        public void unregisterCallback(CallbackI callback) {
            remoteCallbackList.unregister(MP3Binder.this);
        }

    }

    //notification in status bar is created with text and icon, and also notif is used to go back to app
    private void createNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Music Player";
            String description = "Your music is playing...";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(MP3Service.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.music_icon)
                .setContentTitle("Music Player")
                .setContentText("Your music is playing...")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.music_icon, "Back to the player", pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

}
