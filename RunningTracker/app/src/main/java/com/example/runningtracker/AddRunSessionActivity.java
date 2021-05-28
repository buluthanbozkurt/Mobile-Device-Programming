package com.example.runningtracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

//this is the activity which pops up after a user has stopped running (click stop)
//they should be able to give a name for this run, add notes, give a rating
//then these should be saved

public class AddRunSessionActivity extends AppCompatActivity {

    private RunSessionDatabase runSessionDB;
    private EditText notes, name;
    private String rating;
    String type;

    public static final String EXTRA_NAME = "com.example.android.runningtracker.NAME";
    public static final String EXTRA_NOTE = "com.example.android.runningtracker.NOTE";
    public static final String EXTRA_RATING = "com.example.android.runningtracker.RATING";
    public static final String EXTRA_TYPE = "com.example.android.runningtracker.TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_run_session);

        notes = findViewById(R.id.addNotesEditText);
        name = findViewById(R.id.addNameEditText);
        runSessionDB = RunSessionDatabase.getDatabase(AddRunSessionActivity.this);
        Button button = findViewById(R.id.saveButton);

        //when save button is clicked, we send an intent containing all the info
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent addIntent = new Intent();
                if (TextUtils.isEmpty(name.getText())) {
                    setResult(RESULT_CANCELED, addIntent);
                } else {
                    //get user input
                    String nameWord = name.getText().toString();
                    String noteWord = notes.getText().toString();
                    String ratingWord = getRatingUser();
                    String typeWord = type;

                    //put user input into intent
                    addIntent.putExtra(EXTRA_NAME, nameWord);
                    addIntent.putExtra(EXTRA_NOTE, noteWord);
                    addIntent.putExtra(EXTRA_RATING, ratingWord);
                    addIntent.putExtra(EXTRA_TYPE, typeWord);

                    Log.d("G53MDP", "hey" + ratingWord);

                    setResult(RESULT_OK, addIntent);
                }
                //send intent
                Intent intent = new Intent(AddRunSessionActivity.this, MainActivity.class);
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }

    //when clicked, sets rating as very bad
    public void veryBadClicked(View v) {
        this.rating = "Very Bad";
    }

    public void badClicked(View v) {
        this.rating = "Bad";
    }

    public void goodClicked(View v) {
        this.rating = "Good";
    }

    public void veryGoodClicked(View v) {
        this.rating = "Good";
    }

    // getter for rating
    public String getRatingUser() {
        return rating;
    }
}