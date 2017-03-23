package com.example.android.scorekeeperapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button button, button_start;
    String team;
    TextView textviewToss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textviewToss = (TextView) findViewById(R.id.id_msg_toss);

        button_start = (Button) findViewById(R.id.id_button_start);
        button_start.setVisibility(View.INVISIBLE);
        addListenerOnButton();
    }

    /**
     * Invokes Scorer Activity Using Intent
     */
    public void addListenerOnButton() {
        final Context context = this;
        button = (Button) findViewById(R.id.id_button_start);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ScorerActivity.class);
                intent.putExtra("message", team);
                startActivity(intent);
            }
        });
    }

    /**
     * Picks a number between 1 and 2 randomly.
     * Team A wins toss if 1, Team B wins toss if 2
     */
    public void onClickToss(View view) {
        int min = 1;
        int max = 2;
        String message_toss;

        Random rand = new Random();
        int toss_value = rand.nextInt((max - min) + 1) + min;

        if (toss_value == 1) {
            team = getString(R.string.teamA);
        }
        else if (toss_value == 2) {
            team = getString(R.string.teamB);
        }
        message_toss = team  + " " + getString(R.string.msg_toss);
        textviewToss.setText(message_toss);

        button_start.setVisibility(View.VISIBLE);
    }
}