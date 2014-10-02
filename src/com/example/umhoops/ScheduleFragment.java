package com.example.umhoops;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.umhoops.DownloadPageTask.Page;

import android.app.ListFragment;
import android.os.Bundle;

public class ScheduleFragment extends ListFragment implements DownloadCallBack {

    static final String SCHEDULE_FRAGMENT_DEBUG = "Schedule Fragment";

    public final static String SCHEDULE_BASE_URL = "http://www.umhoops.com/2013-14-michigan-basketball-schedule/";

    private boolean contentLoaded;
    private ArrayList<ScheduleGame> games;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLoaded = false;
        new DownloadPageTask(SCHEDULE_BASE_URL, this, Page.SCHEDULE).execute();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        if (contentLoaded) {
            setupListView();
        }
    }

    @Override
    public void callback(Document doc) {
        games = new ArrayList<ScheduleGame>();

        games.add(new ScheduleGame(ScheduleGame.SCHEDULE_LABEL, "Date", "Opponent", "TV", "Time"));

        Elements scheduleRows = doc.select("tr");

        for (Element row : scheduleRows) {
            if (!row.select("td").isEmpty()) {
                games.add(new ScheduleGame(ScheduleGame.SCHEDULE_GAME, row.child(0).text(), row.child(1).text(), row
                        .child(2).text(), row.child(3).text()));
            } else {
                if (row.select("th").size() == 1) {
                    games.add(new ScheduleGame());
                }
            }

        }
        contentLoaded = true;

        setupListView();
    }

    private void setupListView() {
        setListAdapter(new ScheduleGameAdapter(getActivity(), games));
        setListShown(true);
    }
}
