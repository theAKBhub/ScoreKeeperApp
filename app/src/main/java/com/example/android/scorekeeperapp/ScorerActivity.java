package com.example.android.scorekeeperapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScorerActivity extends AppCompatActivity {

    private static final String TAG = "TAG_ScorerActivity";
    Context context;
    Button button;
    String team;

    public static final int MAX_OVERS = 5;
    public static final int MAX_BALLS_PER_OVER = 6;
    public static final int MAX_WICKETS = 10;
    public static final String TEAM_A = "Team A";
    public static final String TEAM_B = "Team B";

    int scoreTeamA = 0;
    int scoreTeamB = 0;
    int ballsTeamA = 0;
    int ballsTeamB = 0;
    int oversTeamA = 0;
    int oversTeamB = 0;
    int wicketsTeamA = 0;
    int wicketsTeamB = 0;
    boolean isEndTeamA = false;
    boolean isEndTeamB = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorer);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");

        team = message;
        TextView tvCurrentTeam = (TextView) findViewById(R.id.id_current_team);
        tvCurrentTeam.setText(team + " " + getString(R.string.msg_current));

        //addListenerOnButton();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("team_saved", team);
        outState.putInt("scoreTeamA_saved", scoreTeamA);
        outState.putInt("scoreTeamB_saved", scoreTeamB);
        outState.putInt("ballsTeamA_saved", ballsTeamA);
        outState.putInt("ballsTeamB_saved", ballsTeamB);
        outState.putInt("oversTeamA_saved", oversTeamA);
        outState.putInt("oversTeamB_saved", oversTeamB);
        outState.putInt("wicketsTeamA_saved", wicketsTeamA);
        outState.putInt("wicketsTeamB_saved", wicketsTeamB);
        outState.putBoolean("isEndTeamA_saved", isEndTeamA);
        outState.putBoolean("isEndTeamB_saved", isEndTeamB);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            team = savedInstanceState.getString("team_saved");
            scoreTeamA = savedInstanceState.getInt("scoreTeamA_saved");
            scoreTeamB = savedInstanceState.getInt("scoreTeamB_saved");
            ballsTeamA = savedInstanceState.getInt("ballsTeamA_saved");
            ballsTeamB = savedInstanceState.getInt("ballsTeamB_saved");
            oversTeamA = savedInstanceState.getInt("oversTeamA_saved");
            oversTeamB = savedInstanceState.getInt("oversTeamB_saved");
            wicketsTeamA = savedInstanceState.getInt("wicketsTeamA_saved");
            wicketsTeamB = savedInstanceState.getInt("wicketsTeamA_saved");
            isEndTeamA = savedInstanceState.getBoolean("isEndTeamA_saved");
            isEndTeamB = savedInstanceState.getBoolean("isEndTeamB_saved");

            displayScores();

            //Re-populate scores if one team has completed game
           /* if (isEndTeamA) {
                TextView tvScore = (TextView) findViewById(R.id.id_score_TeamA);
                tvScore.setText(scoreTeamA + " / " + wicketsTeamA);
                TextView tvOvers = (TextView) findViewById(R.id.id_overs_TeamA);
                tvOvers.setText(oversTeamA);
            }
            else if (isEndTeamB) {
                TextView tvScore = (TextView) findViewById(R.id.id_score_TeamA);
                tvScore.setText(scoreTeamB + " / " + wicketsTeamB);
                TextView tvOvers = (TextView) findViewById(R.id.id_overs_TeamA);
                tvOvers.setText(oversTeamB);
            }*/
            //processGameEnd();
        }
    }

    /**---------  This method is invoked when any of the run buttons is clicked  --------**/
    public void onClickProcessScores(View view) {
        int runs = 0;
        boolean hasGameEnded = false;

        switch (view.getId()) {
            case R.id.id_button_0run:
                runs = 0;
                break;
            case R.id.id_button_1run:
                runs = 1;
                break;
            case R.id.id_button_2runs:
                runs = 2;
                break;
            case R.id.id_button_3runs:
                runs = 3;
                break;
            case R.id.id_button_4runs:
                runs = 4;
                break;
            case R.id.id_button_6runs:
                runs = 6;
                break;
        }
        incrementScores(runs);
        incrementBalls();
        displayScores();
        hasGameEnded = checkEndofGame();

        if (hasGameEnded) {
            processGameEnd();
        }
    }

    /**---------  This method is invoked when wicket button is clicked  --------**/
    public void onClickProcessWickets(View view) {
        boolean hasGameEnded = false;

        incrementWickets();
        incrementBalls();
        displayScores();
        hasGameEnded = checkEndofGame();

        if (hasGameEnded) {
            processGameEnd();
        }
    }

    /**---------  Increments scores  --------**/
    public void incrementScores(int runs) {
        if (team.equals(TEAM_A)) {
            scoreTeamA += runs;
            if (isEndTeamB && (scoreTeamA > scoreTeamB)) {
                isEndTeamA = true;
                displayScores();
                processGameEnd();
            }
        }
        else if (team.equals(TEAM_B)) {
            scoreTeamB += runs;
            if (isEndTeamA && (scoreTeamB > scoreTeamA)) {
                isEndTeamB = true;
                displayScores();
                processGameEnd();
            }
        }
    }

    /**---------  Increments Wickets  --------**/
    public void incrementWickets() {
        if (team.equals(TEAM_A)) {
            wicketsTeamA++;
        }
        else if (team.equals(TEAM_B)) {
            wicketsTeamB++;
        }
    }

    /**---------  Increments balls  --------**/
    public void incrementBalls() {
        if (team.equals(TEAM_A)) {
            ballsTeamA++;
            oversTeamA += incrementOvers(ballsTeamA);
            //Log.v(TAG, "overs = " + oversTeamA);
        }
        else if (team.equals(TEAM_B)) {
            ballsTeamB++;
            oversTeamB += incrementOvers(ballsTeamB);
        }
    }

    /**---------  Increment Overs  ----------**/
    public int incrementOvers(int balls) {
        int overs = 0;

        if (balls < MAX_BALLS_PER_OVER) {
            overs = 0;
        }
        else {
            if (balls % MAX_BALLS_PER_OVER == 0) {
                overs++;
            }
        }
        return overs;
    }

    /**---------  Display scores  --------**/
    public void displayScores() {
        String overs_disp = "";
        String score_disp = "";

        if (team.equals(TEAM_A)) {
            overs_disp = displayOvers(ballsTeamA, oversTeamA);
            score_disp += scoreTeamA;
            TextView tvScore = (TextView) findViewById(R.id.id_score_TeamA);
            tvScore.setText("" + score_disp + " / " + wicketsTeamA);
            TextView tvOvers = (TextView) findViewById(R.id.id_overs_TeamA);
            tvOvers.setText(overs_disp);
        }
        else if (team.equals(TEAM_B)) {
            overs_disp = displayOvers(ballsTeamB, oversTeamB);
            score_disp += scoreTeamB;
            TextView tvScore = (TextView) findViewById(R.id.id_score_TeamB);
            tvScore.setText("" + score_disp + " / " + wicketsTeamB);
            TextView tvOvers = (TextView) findViewById(R.id.id_overs_TeamB);
            tvOvers.setText(overs_disp);
        }
    }

    /**---------  Display overs  --------**/
    public String displayOvers(int balls, int overs) {
        String display_over = "(";

        if (balls < MAX_BALLS_PER_OVER) {
            display_over += overs + "." + balls;
        }
        else {
            int ball_in_over = balls % MAX_BALLS_PER_OVER;
            display_over += overs + "." +  ball_in_over;
        }
        display_over += ")";
        return display_over;
    }

    /**--------- Check if end of team game -----------**/
    public boolean checkEndofGame() {
        int overs_played = 0;
        int wickets_dropped = 0;
        boolean isEndofTeam = false;

        if (team.equals(TEAM_A)) {
           overs_played = oversTeamA;
           wickets_dropped = wicketsTeamA;
        }
        else if (team.equals(TEAM_B)) {
            overs_played = oversTeamB;
            wickets_dropped = wicketsTeamB;
        }

        if ((overs_played > MAX_OVERS) || (wickets_dropped == MAX_WICKETS )) {
            isEndofTeam = true;
        }
        return isEndofTeam;
    }

    /**----------  Display message when game for a team has ended  ----------**/
    public void processGameEnd() {
        String game_msg = "";

        TextView tvCurrentTeam = (TextView) findViewById(R.id.id_current_team);

        //Message for end of game for first team
        game_msg += getString(R.string.msg_endgame);
        game_msg += " " + team + "\n";

        //Change team
        if (team.equals(TEAM_A)) {
            team = TEAM_B;
            isEndTeamA = true;
        }
        else if (team.equals(TEAM_B)) {
            team = TEAM_A;
            isEndTeamB = true;
        }

        if (isEndTeamA && isEndTeamB) { //Both teams have played
            game_msg += getString(R.string.msg_endgame);
            game_msg += " " + team + "\n";

            LinearLayout layoutRunsRow1 = (LinearLayout) findViewById(R.id.id_runs_row1);
            LinearLayout layoutRunsRow2 = (LinearLayout) findViewById(R.id.id_runs_row2);

            //Disables score buttons
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

            //Disable wicket button
            Button btnWicket = (Button) findViewById(R.id.id_button_wicket);
            btnWicket.setBackgroundResource(R.drawable.button_grey);
            btnWicket.setEnabled(false);

            displayResult(); //Display winner of the match

            //Show New Match button
            Button btnNewMatch = (Button) findViewById(R.id.id_button_new);
            btnNewMatch.setVisibility(View.VISIBLE);
            addListenerOnButton();
        }
        else {
            game_msg += team + " ";
            game_msg += getString(R.string.msg_current);
        }

        //Display message
        tvCurrentTeam.setText(game_msg);
    }

    public void displayResult() {
        String winner = "";
        String msg_result = "";

        if (scoreTeamA == scoreTeamB) {
            msg_result += getString(R.string.msg_draw) + "\n";
        }
        else {
            winner = (scoreTeamA > scoreTeamB) ? TEAM_A : TEAM_B;
            msg_result += "\n" + winner + " " + getString(R.string.msg_result) + "\n";
        }

        TextView tvResult = (TextView) findViewById(R.id.id_result);
        tvResult.setText(msg_result);
    }

    /**-------   Invokes Main Activity Using Intent -------**/
    public void addListenerOnButton() {
        final Context context = this;
        button = (Button) findViewById(R.id.id_button_new);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
