package com.team2052.frckrawler.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.team2052.frckrawler.R;
import com.team2052.frckrawler.activities.BaseActivity;
import com.team2052.frckrawler.background.DeleteGameTask;
import com.team2052.frckrawler.database.MetricHelper;
import com.team2052.frckrawler.db.Game;
import com.team2052.frckrawler.listeners.RefreshListener;
import com.team2052.frckrawler.util.Util;

/**
 * Created by adam on 6/14/15.
 */
public class GameInfoFragment extends BaseFragment implements RefreshListener {
    public static final String GAME_ID = "GAME_ID";

    private Game mGame;

    public static GameInfoFragment newInstance(long game_id) {
        Bundle args = new Bundle();
        args.putLong(GAME_ID, game_id);
        GameInfoFragment fragment = new GameInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mGame = mDbManager.getGamesTable().load(getArguments().getLong(GAME_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_info, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                buildDeleteDialog().show();
                break;
            case R.id.menu_edit:
                buildEditDialog().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public AlertDialog buildDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Game?");
        builder.setMessage("Are you sure you want to delete this game?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            new DeleteGameTask(getActivity(), mGame, true).execute();
        });
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

    public AlertDialog buildEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AppCompatEditText name = new AppCompatEditText(getActivity());
        name.setText(mGame.getName());
        int padding = Util.getPixelsFromDp(getActivity(), 16);
        name.setPadding(padding, padding, padding, padding);
        builder.setView(name);
        builder.setTitle("Edit Game");
        builder.setPositiveButton("Ok", (dialog, which) -> {
            mGame.setName(name.getText().toString());
            mGame.update();
            ((BaseActivity) getActivity()).setActionBarSubtitle(mGame.getName());
        });
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

    @Override
    public void refresh() {
        new GetGameInfo().execute();
    }

    public class GetGameInfo extends AsyncTask<Void, Void, Void> {
        int numOfMatchMetrics = 0;
        int numOfPitMetrics = 0;
        int numOfEvents = 0;
        int numOfRobots = 0;

        @Override
        protected Void doInBackground(Void... params) {
            numOfMatchMetrics = mDbManager.getMetricsTable().getNumberOfMetrics(mGame, MetricHelper.MATCH_PERF_METRICS);
            numOfPitMetrics = mDbManager.getMetricsTable().getNumberOfMetrics(mGame, MetricHelper.ROBOT_METRICS);
            mGame.resetEventList();
            mGame.resetRobotList();
            numOfEvents = mGame.getEventList().size();
            numOfRobots = mGame.getRobotList().size();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            /*binding.setNumOfEvents(numOfEvents);
            binding.setNumOfMatchMetrics(numOfMatchMetrics);
            binding.setNumOfPitMetrics(numOfPitMetrics);
            binding.setNumOfRobots(numOfRobots);*/
        }
    }


}