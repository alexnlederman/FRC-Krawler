package com.team2052.frckrawler.subscribers;

import com.team2052.frckrawler.db.Game;

import java.util.ArrayList;
import java.util.List;

public class GameListSubscriber extends BaseDataSubscriber<List<Game>, List<Object>> {
    @Override
    public void parseData() {
        dataToBind = new ArrayList<>(data);
    }
}
