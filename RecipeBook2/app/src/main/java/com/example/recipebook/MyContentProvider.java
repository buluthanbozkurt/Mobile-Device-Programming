package com.example.recipebook;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class MyContentProvider extends ContentProvider {

    DBHelper dbHelper = null;
    private static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "recipe",1);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "recipe/#",2);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "ingredient",3);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "ingredient/#",4);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "recipeingredient",5);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "recipeingredient/#",6);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "*",7);
    }

    @Override
    public boolean onCreate() {
        Log.d("mdp","MyContentProvider onCreate");
        this.dbHelper = new DBHelper(this.getContext(),"mydb",null,1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch(uriMatcher.match(uri)) {
            case 2:
                selection = "_id = " + uri.getLastPathSegment();
            case 1:
                return db.query("recipes", projection, selection, selectionArgs, null, null, sortOrder);
            case 4:
                selection = "_id = " + uri.getLastPathSegment();
            case 3:
                return db.query("ingredients", projection, selection, selectionArgs, null, null, null);
            case 6:
                selection = "_id = " + uri.getLastPathSegment();
            case 5:
                return db.query("recipe_ingredients", projection, selection, selectionArgs, null, null, null);
            default:
                return null;
        }    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        String contentType;

        if (uri.getLastPathSegment()==null) {
            contentType = RecipeContract.CONTENT_TYPE_MULTIPLE;
        } else {
            contentType = RecipeContract.CONTENT_TYPE_SINGLE;
        }
        return contentType;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String tableName;

        switch (uriMatcher.match(uri)) {
            case 1:
                tableName = "recipes";
                break;
            case 2:
                tableName = "recipes";
                break;
            case 3:
                tableName = "ingredients";
                break;
            case 4:
                tableName = "ingredients";
                break;
            case 5:
                tableName = "recipe_ingredients";
                break;
            case 6:
                tableName = "recipe_ingredients";
                break;
            default:
                tableName = "recipes";
                break;
        }

        long id = db.insert(tableName, null, values);
        db.close();
        Uri nu = ContentUris.withAppendedId(uri, id);
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(nu, null);

        return nu;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.update(RecipeContract.RECIPE_TABLE, values, selection, selectionArgs);
        return 0;
    }
}
