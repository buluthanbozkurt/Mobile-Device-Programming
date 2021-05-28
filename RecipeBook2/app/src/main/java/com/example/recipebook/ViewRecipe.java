package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ViewRecipe extends AppCompatActivity {
    int selectedRecipe, newRatingRecipe;
    String DBRecipeName;
    String DBRecipeInstruct;
    String DBRecipeIngredients;
    Float DBRecipeRating;
    EditText ratingEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        ratingEdit = findViewById(R.id.newRating);

        selectedRecipe = getIntent().getIntExtra("recipeID",1);

        fillTextViews();
    }

    public void fillTextViews(){
        TextView recipeName = (TextView) findViewById(R.id.recipeNameTV);
        TextView instructions = (TextView) findViewById(R.id.instructionsTV);
        TextView ingredients = (TextView) findViewById(R.id.ingredientsTV);
        TextView currentRating = (TextView) findViewById(R.id.ratingTV);

        String[] data = new String[]
                {
                        RecipeContract.RECIPE_ID,
                        RecipeContract.RECIPE_NAME,
                        RecipeContract.RECIPE_INSTRUCTIONS,
                        RecipeContract.RECIPE_RATING
                };

        String[] data1 = new String[]
                {
                        RecipeContract.INGREDIENT_ID,
                        RecipeContract.INGREDIENT_NAME
                };

        Cursor cursor = getContentResolver().query(RecipeContract.RECIPE_URI, data,"_id = "+ selectedRecipe, null, RecipeContract.RECIPE_RATING);

        if (cursor.moveToFirst()) {
            DBRecipeName = cursor.getString(cursor.getColumnIndex(RecipeContract.RECIPE_NAME));
            DBRecipeInstruct = cursor.getString(cursor.getColumnIndex(RecipeContract.RECIPE_INSTRUCTIONS));
            DBRecipeRating = cursor.getFloat(cursor.getColumnIndex(RecipeContract.RECIPE_RATING));
        }

        recipeName.setText(DBRecipeName);
        instructions.setText(DBRecipeInstruct);

        String myFloat=Float.toString(DBRecipeRating);
        currentRating.setText(myFloat);
    }

    public void updateRatingButton(View v){

        String newRatingText = ratingEdit.getText().toString();
        int newRating = Integer.parseInt(newRatingText);
        ContentValues newValues = new ContentValues();

        newValues.put("rating", newRating);
        ContentResolver cr = getContentResolver();
        cr.update(RecipeContract.RECIPE_URI, newValues, "" + RecipeContract.RECIPE_ID + "=" + selectedRecipe, null);

        Log.d("ADebugTag", "Value: " + newRatingRecipe);
        finish();
    }

}