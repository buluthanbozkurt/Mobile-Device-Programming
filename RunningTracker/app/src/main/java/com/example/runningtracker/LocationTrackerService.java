package com.example.runningtracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class LocationTrackerService extends Service {

    double currentLocationLatitude, currentLocationLongitude, totalDistance, currentDistance, currentSpeed;
    Integer speedCurrentInt, distanceCurrentInt;

    int NOTIFICATION_ID = 007;

    private static final String CHANNEL_ID = "100";
    private static final int RESULT_OK = 4;
    private LocationTrackerBinder binder = new LocationTrackerBinder();

    LocationManager locationManager;
    LocationListener locationListener;
    Location previousLocation = null;

    public void onCreate() {
        Log.d("G53MDP", "LocationTracker onCreate");
        super.onCreate();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, // minimum time interval between updates
                    5, // minimum distance between updates, in metres
                    locationListener);
        } catch (SecurityException e) {
            Log.d("G53MDP", e.toString());
        }

    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            //we calculate the values for the location such as speed, distance
            currentLocationLatitude = location.getLatitude();
            currentLocationLongitude = location.getLongitude();

            if(previousLocation == null){
                currentDistance = 0;
                currentSpeed = 0;
                totalDistance=0;
            } else {
                currentDistance = previousLocation.distanceTo(location);
                totalDistance = totalDistance + currentDistance;
                currentSpeed = ((previousLocation.distanceTo(location)) / ((location.getTime() - previousLocation.getTime()) / 1000));

                distanceCurrentInt = (int) totalDistance;
                speedCurrentInt = (int) currentSpeed;
            }
            //reset location
            previousLocation = location;

            Log.d("G53MDP","Speed: " + currentSpeed);
            Log.d("G53MDP","Distance: " + currentDistance);

            createNotification();
            broadcastToCurrentSession();
        }

        //send data to the current session screen so we can display values for the user to see
        public void broadcastToCurrentSession() {
            Intent intent = new Intent();
            intent.setAction("com.example.android.runningtracker.BROADCAST");
            intent.putExtra("currentDistance", distanceCurrentInt);
            intent.putExtra("currentSpeed", speedCurrentInt);
            LocalBroadcastManager.getInstance(LocationTrackerService.this).sendBroadcast(intent);
            Log.d("G53MDP","Broadcasting...");
        }


        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Log.d("G53MDP", "onProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Log.d("G53MDP", "onProviderDisabled: " + provider);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("G53MDP", "LocationTracker onBind");
        registerReceiver(myReceiver, new IntentFilter("STOP"));
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        Log.d("G53MDP", "LocationTracker onUnbind");
        return true;
    }

    @Override
    public void onDestroy()
    {
        Log.d("G53MDP","LocationService onDestroy");
        super.onDestroy();
    }

    public void startGPS() {

        Log.d("G53MDP", "GPSstarted");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, // minimum time interval between updates
                    5, // minimum distance between updates, in metres
                    locationListener);
        } catch (SecurityException e) {
            Log.d("G53MDP", e.toString());
        }
    }

    private void stopGPS() {
        Log.d("G53MDP", "GPSstopped: ");
        locationManager.removeUpdates(locationListener);
    }

    public class LocationTrackerBinder extends Binder {

        public void startRunSession() {
            LocationTrackerService.this.createNotification();
            LocationTrackerService.this.startGPS();
        }

        public void stopRunSession() {

            LocationTrackerService.this.stopGPS();
        }

        public void callCreateNotification(){

            LocationTrackerService.this.createNotification();
        }


    }

    //create the notification with a button for the user to stop the app
    private void createNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Running App";
            String description = "You're moving...";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(LocationTrackerService.this, CurrentRunSessionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //intent for app to be stopped
        Intent stopIntent = new Intent("STOP");
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 4, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Running App")
                .setOnlyAlertOnce(true)
                .setContentText("Your distance travelled is: " + totalDistance)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_launcher_background, "Stop App", stopPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        startForeground(NOTIFICATION_ID, mBuilder.build());

        mBuilder.setContentText("Your distance travelled is: " + distanceCurrentInt);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    //method to end the notification
    private void stopNotification(){
        stopForeground(true);
        stopSelf();
    }

    //broadcastreciever to get pending intent from notification button
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopNotification();
        }
    };

}
