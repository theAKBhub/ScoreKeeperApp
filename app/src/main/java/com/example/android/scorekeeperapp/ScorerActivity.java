package com.example.android.scorekeeperapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScorerActivity extends AppCompatActivity implements View.OnClickListener {

    final Context mContext = this;

    public static final int MAX_OVERS = 5;
    public static final int MAX_BALLS_PER_OVER = 6;
    public static final int MAX_WICKETS = 10;

    static final String STATE_TEAMINDEX = "mCurrTeamIndex_svd";
    static final String STATE_MATCHSTATUS = "mMatchStatus_svd";
    static final String STATE_SCORES = "mArrayScores_svd";
    static final String STATE_BALLS = "mArrayBalls_svd";
    static final String STATE_OVERS = "mArrayOvers_svd";
    static final String STATE_WICKETS = "mArrayWickets_svd";
    static final String STATE_EXTRAS = "mArrayExtras_svd";
    static final String STATE_OVERSDISP = "mArrayOversDisp_svd";

    private String mSelectedTeam;
    private String mFirstTeam;
    private Typeface mCustomFont;
    private int mCurrTeamIndex = 0;
    private int mMatchStatus = 0;

    private int [] mArrayScores = new int[2];
    private int [] mArrayBalls = new int[2];
    private int [] mArrayOvers = new int[2];
    private int [] mArrayWickets = new int[2];
    private int [] mArrayExtras = {0, 0};
    private String [] mArrayOversDisp = {"0.0", "0.0"};
    private String [] mArrayTeams = new String[2];

    TextView mTextViewTeamMsg;
    TextView mTextViewScoreTeamA;
    TextView mTextViewScoreTeamB;
    TextView mTextViewOversTeamA;
    TextView mTextViewOversTeamB;
    TextView mTextViewExtrasTeamA;
    TextView mTextViewExtrasTeamB;

    Button mButtonScoreZero;
    Button mButtonScoreOne;
    Button mButtonScoreTwo;
    Button mButtonScoreThree;
    Button mButtonScoreFour;
    Button mButtonScoreSix;
    Button mButtonWicket;
    Button mButtonWide;
    Button mButtonNoBall;
    Button mButtonNewMatch;
    Button mButtonReset;

    LinearLayout mLayoutRunsRow1;
    LinearLayout mLayoutRunsRow2;
    LinearLayout mLayoutRunsRow3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorer);

        Bundle bundle = getIntent().getExtras();
        mSelectedTeam = bundle.getString("message");
        mFirstTeam = mSelectedTeam;

        // Initialize UI components
        mTextViewTeamMsg = (TextView) findViewById(R.id.text_team_msg);
        mTextViewScoreTeamA = (TextView) findViewById(R.id.text_score_teamA);
        mTextViewScoreTeamB = (TextView) findViewById(R.id.text_score_teamB);
        mTextViewOversTeamA = (TextView) findViewById(R.id.text_overs_teamA);
        mTextViewOversTeamB = (TextView) findViewById(R.id.text_overs_teamB);
        mTextViewExtrasTeamA = (TextView) findViewById(R.id.text_extras_teamA);
        mTextViewExtrasTeamB = (TextView) findViewById(R.id.text_extras_teamB);

        mButtonScoreZero = (Button) findViewById(R.id.button_0run);
        mButtonScoreOne = (Button) findViewById(R.id.button_1run);
        mButtonScoreTwo = (Button) findViewById(R.id.button_2runs);
        mButtonScoreThree = (Button) findViewById(R.id.button_3runs);
        mButtonScoreFour = (Button) findViewById(R.id.button_4runs);
        mButtonScoreSix = (Button) findViewById(R.id.button_6runs);
        mButtonWicket = (Button) findViewById(R.id.button_wicket);
        mButtonNoBall = (Button) findViewById(R.id.button_noball);
        mButtonWide = (Button) findViewById(R.id.button_wide);
        mButtonNewMatch = (Button) findViewById(R.id.button_new);
        mButtonReset = (Button) findViewById(R.id.button_reset);

        mLayoutRunsRow1 = (LinearLayout) findViewById(R.id.llayout_runs_row1);
        mLayoutRunsRow2 = (LinearLayout) findViewById(R.id.llayout_runs_row2);
        mLayoutRunsRow3 = (LinearLayout) findViewById(R.id.llayout_runs_row3);

        mButtonScoreZero.setOnClickListener(this);
        mButtonScoreOne.setOnClickListener(this);
        mButtonScoreTwo.setOnClickListener(this);
        mButtonScoreThree.setOnClickListener(this);
        mButtonScoreFour.setOnClickListener(this);
        mButtonScoreSix.setOnClickListener(this);
        mButtonWicket.setOnClickListener(this);
        mButtonNoBall.setOnClickListener(this);
        mButtonWide.setOnClickListener(this);
        mButtonNewMatch.setOnClickListener(this);
        mButtonReset.setOnClickListener(this);

        // Set custom typeface
        mCustomFont = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.ttf");
        setCustomTypeface();

        setTeamIndex();
        displayTeamInfo();

        if (savedInstanceState == null) {
            displayExtras();
        }
    }

    /**
     * This method sets custom font for all views
     */
    public void setCustomTypeface() {
        mTextViewTeamMsg.setTypeface(mCustomFont);
        mTextViewScoreTeamA.setTypeface(mCustomFont);
        mTextViewScoreTeamB.setTypeface(mCustomFont);
        mTextViewOversTeamA.setTypeface(mCustomFont);
        mTextViewOversTeamB.setTypeface(mCustomFont);
    }

    /**
     * Save Instance
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_TEAMINDEX, mCurrTeamIndex);
        outState.putInt(STATE_MATCHSTATUS, mMatchStatus);
        outState.putIntArray(STATE_SCORES, mArrayScores);
        outState.putIntArray(STATE_BALLS, mArrayBalls);
        outState.putIntArray(STATE_OVERS, mArrayOvers);
        outState.putIntArray(STATE_WICKETS, mArrayWickets);
        outState.putIntArray(STATE_EXTRAS, mArrayExtras);
        outState.putStringArray(STATE_OVERSDISP, mArrayOversDisp);
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
            mCurrTeamIndex = savedInstanceState.getInt(STATE_TEAMINDEX);
            mMatchStatus = savedInstanceState.getInt(STATE_MATCHSTATUS);
            mArrayScores = savedInstanceState.getIntArray(STATE_SCORES);
            mArrayBalls = savedInstanceState.getIntArray(STATE_BALLS);
            mArrayOvers = savedInstanceState.getIntArray(STATE_OVERS);
            mArrayWickets = savedInstanceState.getIntArray(STATE_WICKETS);
            mArrayExtras = savedInstanceState.getIntArray(STATE_EXTRAS);
            mArrayOversDisp = savedInstanceState.getStringArray(STATE_OVERSDISP);
        }
        displayTeamInfo();
        displayScore();
        displayOvers();
        displayExtras();
        if (mMatchStatus == 2) {
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
            case R.id.button_0run:
                setScores(0);
                break;
            case R.id.button_1run:
                setScores(1);
                break;
            case R.id.button_2runs:
                setScores(2);
                break;
            case R.id.button_3runs:
                setScores(3);
                break;
            case R.id.button_4runs:
                setScores(4);
                break;
            case R.id.button_6runs:
                setScores(6);
                break;
            case R.id.button_wicket:
                processWickets();
                break;
            case R.id.button_wide:
            case R.id.button_noball:
                processExtraRuns();
                break;
            case R.id.button_new:
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button_reset:
                resetScores();
                break;
        }
    }

    /**
     * This method processes the scores when any run button is clicked
     * @param runs
     */
    public void setScores(int runs) {
        mArrayScores[mCurrTeamIndex] = mArrayScores[mCurrTeamIndex] + runs;
        mArrayBalls[mCurrTeamIndex] = mArrayBalls[mCurrTeamIndex] + 1;

        incrementOvers();
        saveOversPlayed();
        displayScore();
        displayOvers();
        displayExtras();
        checkEndofGame();
    }

    /**
     * This method processes the extra scores in the event of "Wide" or "No Ball"
     */
    public void processExtraRuns() {
        mArrayScores[mCurrTeamIndex] = mArrayScores[mCurrTeamIndex] + 1;
        mArrayExtras[mCurrTeamIndex] = mArrayExtras[mCurrTeamIndex] + 1;

        incrementOvers();
        saveOversPlayed();
        displayScore();
        displayOvers();
        displayExtras();
        checkEndofGame();
    }

    /**
     * This method is invoked when wicket button is clicked
     */
    public void processWickets() {
        mArrayWickets[mCurrTeamIndex] = mArrayWickets[mCurrTeamIndex] + 1;
        mArrayBalls[mCurrTeamIndex] = mArrayBalls[mCurrTeamIndex] + 1;

        incrementOvers();
        saveOversPlayed();
        displayScore();
        displayOvers();
        displayExtras();
        checkEndofGame();
    }

    /**
     * This method increments number of overs for current team
     */
    public void incrementOvers() {
        if (mArrayBalls[mCurrTeamIndex]  < MAX_BALLS_PER_OVER) {
            mArrayOvers[mCurrTeamIndex] = 0;
        } else {
            if ((mArrayBalls[mCurrTeamIndex] % MAX_BALLS_PER_OVER) == 0) {
                mArrayOvers[mCurrTeamIndex] = mArrayOvers[mCurrTeamIndex] + 1;
            }
        }
    }

    /**
     * Sets team index depending on current player - 0 if Team A, 1 if Team B
     * This index is used throughout for all arrays
     */
    public void setTeamIndex() {
        mArrayTeams = getResources().getStringArray(R.array.array_team);

        if (mSelectedTeam.equals(mArrayTeams[0])) { //Team A is current player
            mCurrTeamIndex = 0;
        } else { //Team B is current player
            mCurrTeamIndex = 1;
        }
    }

    /**
     * This method saves overs played by current team in format 0.0
     */
    public void saveOversPlayed() {
        String display_over = "";
        int balls_in_over = 0;

        display_over += mArrayOvers[mCurrTeamIndex] + ".";

        if (mArrayBalls[mCurrTeamIndex]  < MAX_BALLS_PER_OVER) {
            display_over += mArrayBalls[mCurrTeamIndex];
        } else {
            balls_in_over = mArrayBalls[mCurrTeamIndex] % MAX_BALLS_PER_OVER;
            display_over += balls_in_over;
        }
        mArrayOversDisp[mCurrTeamIndex] = display_over;
    }

    /**
     * This method checks if end of game for a team, and also if it's end of match
     */
    public void checkEndofGame() {
        int prevTeamIndex = 0;
        int nextTeamIndex = 0;

        if (mMatchStatus == 2) { // 2nd team completed playing but didn't exceed 1st team's score
            displayResult();

        } else if (mMatchStatus == 1) { //first team has played
            prevTeamIndex = (mCurrTeamIndex == 0) ? 1 : 0;   //gets index of previous team

            //END MATCH if score of current team crosses score of previous team
            if ((mArrayScores[mCurrTeamIndex] > mArrayScores[prevTeamIndex]) ||
                    (mArrayOvers[mCurrTeamIndex] == MAX_OVERS) ||
                    (mArrayWickets[mCurrTeamIndex] == MAX_WICKETS))    {
                mMatchStatus = 2; // end of match. both teams have completed playing.

                disableScoreButtons();
                displayResult();
            }

        } else if (mMatchStatus == 0) { //first team is playing
            if ((mArrayOvers[mCurrTeamIndex] == MAX_OVERS) ||
                    (mArrayWickets[mCurrTeamIndex] == MAX_WICKETS)) {
                mMatchStatus = 1;
                displayTeamInfo();
                nextTeamIndex = (mCurrTeamIndex == 0) ? 1 : 0;   //sets index of next team to play
                mCurrTeamIndex = nextTeamIndex;
            }
        }
    }

    /**
     * This method disables all score buttons at end of the match
     */
    public void disableScoreButtons() {
        for ( int i = 0; i < mLayoutRunsRow1.getChildCount();  i++ ) {
            View view = mLayoutRunsRow1.getChildAt(i);
            view.setBackgroundResource(R.color.colorButtonDisabled);
            view.setEnabled(false);
        }
        for ( int i = 0; i < mLayoutRunsRow2.getChildCount();  i++ ) {
            View view = mLayoutRunsRow2.getChildAt(i);
            view.setBackgroundResource(R.color.colorButtonDisabled);
            view.setEnabled(false);
        }
        for ( int i = 0; i < mLayoutRunsRow3.getChildCount();  i++ ) {
            View view = mLayoutRunsRow3.getChildAt(i);
            view.setBackgroundResource(R.color.colorButtonDisabled);
            view.setEnabled(false);
        }
        mButtonReset.setBackgroundResource(R.color.colorButtonDisabled);
        mButtonReset.setEnabled(false);
    }

    /**
     * This method displays previous and current team names at the top
     */
    public void displayTeamInfo() {
        String display_msg = "";
        String team_played = "";
        String team_current = "";

        if (mMatchStatus == 0) { //1st team playing
            display_msg = getString(R.string.msg_current, mSelectedTeam);

        } else if (mMatchStatus == 1) { //1st team completed playing. 2nd team playing.
                if (mCurrTeamIndex == 0) {
                    team_played = mArrayTeams[0];
                    team_current = mArrayTeams[1];
                } else if (mCurrTeamIndex == 1) {
                    team_played = mArrayTeams[1];
                    team_current = mArrayTeams[0];
                }
                display_msg = getString(R.string.msg_endgame, team_played) + "\n";
                display_msg += getString(R.string.msg_current, team_current);

        } else if (mMatchStatus == 2) {
            display_msg = getString(R.string.msg_endgame, mArrayTeams[0]);
            display_msg += "\n";
            display_msg += getString(R.string.msg_endgame, mArrayTeams[1]);
        }

        mTextViewTeamMsg.setText(display_msg);
    }

    /**
     * This method displays scores in the format Runs / Wickets
     */
    public void displayScore() {
        mTextViewScoreTeamA.setText(mArrayScores[0] + " / " + mArrayWickets[0]);
        mTextViewScoreTeamB.setText(mArrayScores[1] + " / " + mArrayWickets[1]);
    }

    /**
     * This method displays overs played
     */
    public void displayOvers() {
        mTextViewOversTeamA.setText("(" + mArrayOversDisp[0] + ")");
        mTextViewOversTeamB.setText("(" + mArrayOversDisp[1] + ")");
    }

    /**
     * This method displays final result and enables New Match button
     */
    public void displayResult() {
        String display_msg_result = "";

        if (mArrayScores[0] == mArrayScores[1]) {
            display_msg_result = getString(R.string.msg_draw);
        } else if (mArrayScores[0] > mArrayScores[1]) {
            display_msg_result = getString(R.string.msg_result, mArrayTeams[0]);
        } else if (mArrayScores[0] < mArrayScores[1]) {
            display_msg_result = getString(R.string.msg_result, mArrayTeams[1]);
        }

        mTextViewTeamMsg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBrownLight));
        mTextViewTeamMsg.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
        mTextViewTeamMsg.setText(display_msg_result);
        mButtonNewMatch.setVisibility(View.VISIBLE);
    }

    /**
     * This method displays the extra runs scored by team
     */
    public void displayExtras() {
        mTextViewExtrasTeamA.setText(getString(R.string.text_extras, mArrayExtras[0]));
        mTextViewExtrasTeamB.setText(getString(R.string.text_extras, mArrayExtras[1]));
    }

    /**
     * This method resets all scores
     */
    public void resetScores() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme);
        builder.setMessage(getString(R.string.text_alert))
                .setTitle(getString(R.string.title_alert));

        builder.setNegativeButton(getString(R.string.action_dialog_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close the dialog box and do nothing
                dialog.cancel();
            }
        });

        builder.setPositiveButton(getString(R.string.action_dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                mSelectedTeam = mFirstTeam; // Set the first team to play
                mMatchStatus = 0; // No team has played yet

                for (int i = 0; i < 2; i++) { // reset all scores
                    mArrayScores[i] = 0;
                    mArrayBalls[i] = 0;
                    mArrayOvers[i] = 0;
                    mArrayWickets[i] = 0;
                    mArrayExtras[i] = 0;
                    mArrayTeams[i] = "";
                }
                mArrayOversDisp[0] = "0.0";
                mArrayOversDisp[1] = "0.0";

                setTeamIndex();
                displayTeamInfo();
                displayScore();
                displayOvers();
                displayExtras();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
