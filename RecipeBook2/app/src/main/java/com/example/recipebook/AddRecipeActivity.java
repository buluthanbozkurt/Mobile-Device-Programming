package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
    }

    //the button which you need to click to add the recipe
    public void addButtonClicked(View v){

        //name of recipe edit text
        EditText recipeTitle = (EditText) findViewById(R.id.recipeNameEditText);
        String name = recipeTitle.getText().toString();

        //instructions
        EditText recipeInstruct = (EditText) findViewById(R.id.instructionsEditText);
        String instructions = recipeInstruct.getText().toString();



        //rating
        EditText recipeRating = (EditText) findViewById(R.id.ratingEditText);
        String rating = recipeRating.getText().toString();

        String[] ingredientsList = ingredients.split("\\r?\\n");

        ContentValues values = new ContentValues();
        values.put(RecipeContract.RECIPE_NAME, name);
        values.put(RecipeContract.RECIPE_INSTRUCTIONS, instructions);
        values.put(RecipeContract.RECIPE_RATING, rating);

        ContentValues ingrValues = new ContentValues();

        //put ingredients
        for(String ingredient : ingredientsList) {
            ingrValues.put(RecipeContract.INGREDIENT_NAME, ingredient);
        }

        getContentResolver().insert(RecipeContract.INGREDIENT_URI, ingrValues);
        getContentResolver().insert(RecipeContract.RECIPE_URI, values);

        Log.d("g53mdp", "add recipe");

        finish();
    }
}