package com.example.android.scorekeeperapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import static com.example.android.scorekeeperapp.R.id.button_start;

/**
 * Class - MainActivity
 * This class prompts for the team to start the game from first Activity (activity_main.xml)
 * and invokes second Activity (activity_scorer.xml) with an Intent with selected team name as
 * the parameter
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // All UI components
    private TextView mTextViewRules;
    private TextView mTextViewPrompt;
    private Button mButtonStart;
    private Spinner mSpinnerTeam;

    // Various identifiers
    private Typeface mCustomFont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        mTextViewRules = (TextView) findViewById(R.id.text_rules);
        mTextViewPrompt = (TextView) findViewById(R.id.text_prompt);
        mButtonStart = (Button) findViewById(button_start);
        mSpinnerTeam = (Spinner) findViewById(R.id.select_team);

        // Set custom typeface
        mCustomFont = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.ttf");
        setCustomTypeface();

        mButtonStart.setOnClickListener(this);
    }

    /**
     * Invokes methods for individual call to action buttons
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start:
                startMatch();
                break;
        }
    }

    /**
     * This method sets custom font for all views
     */
    public void setCustomTypeface() {
        mTextViewRules.setTypeface(mCustomFont);
        mTextViewPrompt.setTypeface(mCustomFont);
        mButtonStart.setTypeface(mCustomFont);
    }

    /**
     * Invokes Scorer Activity with selected team name as the passed argument
     */
    public void startMatch() {
        final Context context = this;
        String selectedTeam = "";

        selectedTeam += mSpinnerTeam.getSelectedItem();

        Intent intent = new Intent(context, ScorerActivity.class);
        intent.putExtra("message", selectedTeam);
        startActivity(intent);
    }

}