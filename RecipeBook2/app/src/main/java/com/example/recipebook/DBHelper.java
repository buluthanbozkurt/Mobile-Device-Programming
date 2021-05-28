package com.example.recipebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper
{
    //DBHelper constructor
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d("mdp","DBHelper Constructor");
    }

    //Create all three tables when application is FIRST run
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d("mdp","DBHelper onCreate");

        //create recipes table
        db.execSQL("CREATE TABLE recipes (\n" +
                " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                " name VARCHAR(128) NOT NULL,\n" +
                " instructions VARCHAR(128) NOT NULL,\n" +
                " rating INTEGER);");

        //create ingredients table
        db.execSQL("CREATE TABLE ingredients (\n" +
                " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "ingredientname VARCHAR(128) NOT NULL); ");

        //create recipe_ingredients table
        db.execSQL("CREATE TABLE recipe_ingredients (\n" +
                " recipe_id INT NOT NULL,\n" +
                " ingredient_id INT NOT NULL,\n" +
                " CONSTRAINT fk1 FOREIGN KEY (recipe_id) REFERENCES recipes (_id),\n" +
                " CONSTRAINT fk2 FOREIGN KEY (ingredient_id) REFERENCES ingredients (_id),\n" +
                " CONSTRAINT _id PRIMARY KEY (recipe_id, ingredient_id) );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}