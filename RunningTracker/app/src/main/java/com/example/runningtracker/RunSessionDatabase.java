package com.example.runningtracker;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {RunSession.class}, version = 5, exportSchema = false) // drop and recreate
public abstract class RunSessionDatabase extends RoomDatabase {

    public abstract RunSessionDAO runSessionDao();

    private static volatile RunSessionDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RunSessionDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RunSessionDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RunSessionDatabase.class, "run_session_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(createCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback createCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Log.d("g53mdp", "dboncreate");
            databaseWriteExecutor.execute(() -> {

                RunSessionDAO runSessionDao = INSTANCE.runSessionDao();
                runSessionDao.deleteAll();

                RunSession runSession = new RunSession("Tuesday Night", "Run", "Took a new route", "Good");
                RunSession runSession1 = new RunSession("Morning", "Jog", "Met friends", "Bad");

                runSessionDao.insert(runSession);
                runSessionDao.insert(runSession1);

            });
        }
    };

}
