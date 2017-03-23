package com.example.android.scorekeeperapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.android.scorekeeperapp.R.string.msg_result;

public class ScorerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG_ScorerActivity";
    Context context;
    String team;

    public static final int MAX_OVERS = 5;
    public static final int MAX_BALLS_PER_OVER = 6;
    public static final int MAX_WICKETS = 10;

    int currTeamIndex = 0;
    int matchStatus = 0;

    int [] arrayScores = new int[2];
    int [] arrayBalls = new int[2];
    int [] arrayOvers = new int[2];
    int [] arrayWickets = new int[2];
    String [] arrayOversDisp = {"0.0", "0.0"};

    TextView tvCurrentTeam;
    TextView tvScoreTeamA;
    TextView tvScoreTeamB;
    TextView tvOversTeamA;
    TextView tvOversTeamB;
    TextView tvResult;

    Button btnScoreZero;
    Button btnScoreOne;
    Button btnScoreTwo;
    Button btnScoreThree;
    Button btnScoreFour;
    Button btnScoreSix;
    Button btnWicket;
    Button btnNewMatch;

    LinearLayout layoutRunsRow1;
    LinearLayout layoutRunsRow2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorer);

        Bundle bundle = getIntent().getExtras();
        team = bundle.getString("message");

        tvCurrentTeam = (TextView) findViewById(R.id.id_current_team);
        tvScoreTeamA = (TextView) findViewById(R.id.id_score_TeamA);
        tvScoreTeamB = (TextView) findViewById(R.id.id_score_TeamB);
        tvOversTeamA = (TextView) findViewById(R.id.id_overs_TeamA);
        tvOversTeamB = (TextView) findViewById(R.id.id_overs_TeamB);
        tvResult = (TextView) findViewById(R.id.id_result);

        btnScoreZero = (Button) findViewById(R.id.id_button_0run);
        btnScoreOne = (Button) findViewById(R.id.id_button_1run);
        btnScoreTwo = (Button) findViewById(R.id.id_button_2runs);
        btnScoreThree = (Button) findViewById(R.id.id_button_3runs);
        btnScoreFour = (Button) findViewById(R.id.id_button_4runs);
        btnScoreSix = (Button) findViewById(R.id.id_button_6runs);
        btnWicket = (Button) findViewById(R.id.id_button_wicket);
        btnNewMatch = (Button) findViewById(R.id.id_button_new);

        layoutRunsRow1 = (LinearLayout) findViewById(R.id.id_runs_row1);
        layoutRunsRow2 = (LinearLayout) findViewById(R.id.id_runs_row2);

        btnScoreZero.setOnClickListener(this);
        btnScoreOne.setOnClickListener(this);
        btnScoreTwo.setOnClickListener(this);
        btnScoreThree.setOnClickListener(this);
        btnScoreFour.setOnClickListener(this);
        btnScoreSix.setOnClickListener(this);
        btnWicket.setOnClickListener(this);
        btnNewMatch.setOnClickListener(this);

        setTeamIndex();
        displayTeamInfo();

    }


    /**
     * Save Instance
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currTeamIndex_svd", currTeamIndex);
        outState.putInt("matchStatus_svd", matchStatus);
        outState.putIntArray("arrayScores_svd", arrayScores);
        outState.putIntArray("arrayBalls_svd", arrayBalls);
        outState.putIntArray("arrayOvers_svd", arrayOvers);
        outState.putIntArray("arrayWickets_svd", arrayWickets);
        outState.putStringArray("arrayOversDisp_svd", arrayOversDisp);
        super.onSaveInstanceState(outState);
    }


    /**
     * Restore Saved Instance
     * @param savedInstanceState
     */

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            currTeamIndex = savedInstanceState.getInt("currTeamIndex_svd");
            matchStatus = savedInstanceState.getInt("matchStatus_svd");
            arrayScores = savedInstanceState.getIntArray("arrayScores_svd");
            arrayBalls = savedInstanceState.getIntArray("arrayBalls_svd");
            arrayOvers = savedInstanceState.getIntArray("arrayOvers_svd");
            arrayWickets = savedInstanceState.getIntArray("arrayWickets_svd");
            arrayOversDisp = savedInstanceState.getStringArray("arrayOversDisp_svd");
        }
        displayTeamInfo();
        displayScore();
        displayOvers();
        if (matchStatus == 2) {
            disableScoreButtons();
            displayResult();
        }
    }


    /**
     * Invokes methods based on button clicked
     */
    @Override
    public void onClick(View view) {
        final Context context = this;

        switch (view.getId()) {
            case R.id.id_button_0run:
                setScores(0);
                break;
            case R.id.id_button_1run:
                setScores(1);
                break;
            case R.id.id_button_2runs:
                setScores(2);
                break;
            case R.id.id_button_3runs:
                setScores(3);
                break;
            case R.id.id_button_4runs:
                setScores(4);
                break;
            case R.id.id_button_6runs:
                setScores(6);
                break;
            case R.id.id_button_wicket:
                processWickets();
                break;
            case R.id.id_button_new:
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * This method process the scores when any run button is clicked
     */
    public void setScores(int runs) {
        arrayScores[currTeamIndex] = arrayScores[currTeamIndex] + runs;
        arrayBalls[currTeamIndex] = arrayBalls[currTeamIndex] + 1;

        incrementOvers();
        displayScore();
        saveOversPlayed();
        displayOvers();
        checkEndofGame();
    }

    /**
     * This method is invoked when wicket button is clicked
     */
    public void processWickets() {
        arrayWickets[currTeamIndex] = arrayWickets[currTeamIndex] + 1;
        arrayBalls[currTeamIndex] = arrayBalls[currTeamIndex] + 1;

        incrementOvers();
        displayScore();
        saveOversPlayed();
        displayOvers();
        checkEndofGame();
    }


    /**
     * This method increments overs for current team
     */
    public void incrementOvers() {
        if (arrayBalls[currTeamIndex]  < MAX_BALLS_PER_OVER) {
            arrayOvers[currTeamIndex] = 0;
        }
        else {
            if ((arrayBalls[currTeamIndex] % MAX_BALLS_PER_OVER) == 0) {
                arrayOvers[currTeamIndex] = arrayOvers[currTeamIndex] + 1;
            }
        }
    }

    /**
     * Sets team index depending on current player - 0 if Team A, 1 if Team B
     * This index is used throughout for all arrays
     */
    public void setTeamIndex() {
        if (team.equals(getString(R.string.teamA))) { //Team A is current player
            currTeamIndex = 0;
        }
        else { //Team B is current player
            currTeamIndex = 1;
        }
    }


    /**
     * This method saves overs played by current team in format 0.0
     */
    public void saveOversPlayed() {
        String display_over = "";
        int balls_in_over = 0;

        display_over += arrayOvers[currTeamIndex] + ".";

        if (arrayBalls[currTeamIndex]  < MAX_BALLS_PER_OVER) {
            display_over += arrayBalls[currTeamIndex];
        }
        else {
            balls_in_over = arrayBalls[currTeamIndex] % MAX_BALLS_PER_OVER;
            display_over += balls_in_over;
        }

        arrayOversDisp[currTeamIndex] = display_over;
    }

    /**
     * This method checks if end of game for a team, and also if it's end of match
     */
    public void checkEndofGame() {
        int prevTeamIndex = 0;
        int nextTeamIndex = 0;

        if (matchStatus == 2) {
            displayResult();

        }
        else if (matchStatus == 1) { //first team has played
            prevTeamIndex = (currTeamIndex == 0) ? 1 : 0;   //gets index of previous team
            displayTeamInfo();

            if ((arrayScores[currTeamIndex] > arrayScores[prevTeamIndex]) ||
                    (arrayOvers[currTeamIndex] > MAX_OVERS) ||
                    (arrayWickets[currTeamIndex] == MAX_WICKETS))    {
                //END MATCH if score of current team crosses score of previous team
                matchStatus = 2; // end of match. both teams have completed playing.

                disableScoreButtons();
                displayResult();
            }
        }
        else if (matchStatus == 0) { //first team is playing
            if ((arrayOvers[currTeamIndex] > MAX_OVERS) ||
                    (arrayWickets[currTeamIndex] == MAX_WICKETS)) {
                matchStatus = 1;
                displayTeamInfo();
                nextTeamIndex = (currTeamIndex == 0) ? 1 : 0;   //sets index of next team to play
                currTeamIndex = nextTeamIndex;
            }
        }
    }


    /**
     * This method disables all score buttons at end of the match
     */
    public void disableScoreButtons() {
        for ( int i = 0; i < layoutRunsRow1.getChildCount();  i++ ){
            View view = layoutRunsRow1.getChildAt(i);
            view.setBackgroundResource(R.drawable.button_grey);
            view.setEnabled(false);
        }
        for ( int i = 0; i < layoutRunsRow2.getChildCount();  i++ ){
            View view = layoutRunsRow2.getChildAt(i);
            view.setBackgroundResource(R.drawable.button_grey);
            view.setEnabled(false);
        }
        btnWicket.setBackgroundResource(R.drawable.button_grey);
        btnWicket.setEnabled(false);
    }


    /**
     * This method displays previous and current team names at the top
     */
    public void displayTeamInfo() {
        String display_msg = "";
        String team_played = "";

        if (matchStatus == 0) { //1st team playing
            display_msg = team + " " + getString(R.string.msg_current);
        }
        else if (matchStatus == 1) { //1st team completed playing. 2nd team playing.
            if (currTeamIndex == 0) {
                team_played = getString(R.string.teamA);
                team = getString(R.string.teamB);
            }
            else if (currTeamIndex == 1) {
                team_played = getString(R.string.teamB);
                team = getString(R.string.teamA);
            }
            display_msg = getString(R.string.msg_endgame) + " " + team_played + "\n";
            display_msg += team + " " + getString(R.string.msg_current);
        }
        else if (matchStatus == 2) {
            display_msg = getString(R.string.msg_endgame) + " " + getString(R.string.teamA);
            display_msg += "\n";
            display_msg += getString(R.string.msg_endgame) + " " + getString(R.string.teamB);
        }

        tvCurrentTeam.setText(display_msg);
    }


    /**
     * This method displays scores in the format Runs / Wickets
     */
    public void displayScore() {
        tvScoreTeamA.setText(arrayScores[0] + " / " + arrayWickets[0]);
        tvScoreTeamB.setText(arrayScores[1] + " / " + arrayWickets[1]);
    }


    /**
     * This method displays overs played
     */
    public void displayOvers() {
        tvOversTeamA.setText("(" + arrayOversDisp[0] + ")");
        tvOversTeamB.setText("(" + arrayOversDisp[1] + ")");
    }


    /**
     * This method displays final result and enables New Match button
     */
    public void displayResult() {
        String display_msg_result = "";

        if (arrayScores[0] == arrayScores[1]) {
            display_msg_result = "\n" + getString(R.string.msg_draw) + "\n";
        }
        else if (arrayScores[0] > arrayScores[1]) {
            display_msg_result = "\n" + getString(R.string.teamA) + " " + getString(msg_result) + "\n";
        }
        else if (arrayScores[0] < arrayScores[1]) {
            display_msg_result = "\n" + getString(R.string.teamB) + " " + getString(msg_result) + "\n";
        }

        tvResult.setText(display_msg_result);

        btnNewMatch.setVisibility(View.VISIBLE);
    }

}
