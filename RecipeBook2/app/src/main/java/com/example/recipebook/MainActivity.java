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

public class MainActivity extends AppCompatActivity {

    public static final int ADD_CODE = 2;
    public static final int VIEW_RECIPE_CODE = 1;
    public static final int INGREDIENT_CODE = 3;
    String order = RecipeContract.RECIPE_NAME + " ASC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateRecipeList();
    }

    //populate the list of recipes listview
    public void populateRecipeList() {

        String[] columns = new String[]
                {
                        RecipeContract.RECIPE_ID,
                        RecipeContract.RECIPE_NAME,
                        RecipeContract.RECIPE_RATING
                };

        String[] colsToDisplay = new String[]
                {
                        RecipeContract.RECIPE_NAME,
                        RecipeContract.RECIPE_RATING
                };

        int[] colResIds = new int[]
                {
                        R.id.recipeTitle,
                        R.id.recipeRating
                };

        ContentResolver cr = getContentResolver();

        final Cursor c = cr.query(RecipeContract.RECIPE_URI,
                columns,
                null,
                null,
                order);

        ListView recipeListView = (ListView) findViewById(R.id.recipeListView);
        recipeListView.setAdapter(new SimpleCursorAdapter(this,
                R.layout.my_row,
                c, colsToDisplay,
                colResIds, 0));

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(MainActivity.this, ViewRecipe.class);
                int item = c.getInt(c.getColumnIndex(RecipeContract.RECIPE_ID));
                intent.putExtra("recipeID", item);
                startActivityForResult(intent, VIEW_RECIPE_CODE);
            }
        });

    }

    public void addRecipeButton(View v)
    {
        Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
        startActivityForResult(intent, ADD_CODE);
    }

    public void ingredientsListButton(View v)
    {
        Intent intent = new Intent(MainActivity.this, ListIngredients.class);
        startActivityForResult(intent, INGREDIENT_CODE);
    }

    public void sortByRating(View v)
    {
        order = RecipeContract.RECIPE_RATING + " DESC";
        populateRecipeList();
    }

    public void sortByTitle(View v)
    {
        order = RecipeContract.RECIPE_NAME + " ASC";
        populateRecipeList();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateRecipeList();
    }
}