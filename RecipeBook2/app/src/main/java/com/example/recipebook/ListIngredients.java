package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListIngredients extends AppCompatActivity {

    public static final int ADD_CODE = 2;
    public static final int VIEW_RECIPE_CODE = 1;
    String order = RecipeContract.RECIPE_NAME + " ASC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ingredients);
        populateIngredientsList();
    }

    //populate the list of ingredients
    public void populateIngredientsList() {

        String[] columns = new String[]
                {
                        RecipeContract.INGREDIENT_ID,
                        RecipeContract.INGREDIENT_NAME
                };

        String[] colsToDisplay = new String[]
                {
                        RecipeContract.INGREDIENT_NAME,
                };

        int[] colResIds = new int[]
                {
                        R.id.ingredientName,
                };

        ContentResolver cr = getContentResolver();

        final Cursor c = cr.query(RecipeContract.INGREDIENT_URI,
                columns,
                null,
                null,
                null);

        ListView ingredientsListView = (ListView) findViewById(R.id.ingredientsListView);
        ingredientsListView.setAdapter(new SimpleCursorAdapter(this,
                R.layout.my_ingredient_row,
                c, colsToDisplay,
                colResIds, 0));
    }


    @Override
    public void onResume() {
        super.onResume();
        populateIngredientsList();
    }


}