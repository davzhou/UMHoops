package com.example.umhoops;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.umhoops.DownloadPageTask.Page;

import android.app.ListFragment;
import android.os.Bundle;

public class RosterFragment extends ListFragment implements DownloadCallBack {

    static final String ROSTER_FRAGMENT_DEBUG = "Roster Fragment";

    public final static String ROSTER_BASE_URL = "http://www.umhoops.com/information/roster/";

    private ArrayList<RosterAthlete> athletes;
    private boolean contentLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLoaded = false;
        new DownloadPageTask(ROSTER_BASE_URL, this, Page.ROSTER).execute();

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
        athletes = new ArrayList<RosterAthlete>();
        // Log.d(ROSTER_FRAGMENT_DEBUG, doc.toString());
        Elements rosterRows = doc.select("tr");
        // Log.d(ROSTER_FRAGMENT_DEBUG, "rows: " + rosterRows.size());

        for (Element row : rosterRows) {
            if (row.child(1).children().isEmpty()) {
                athletes.add(new RosterAthlete(row.child(0).text(), row.child(1).text(), row.child(2).text(), row
                        .child(3).text(), row.child(4).text(), row.child(5).text(), row.child(6).text()));
            } else {
                athletes.add(new RosterAthlete(row.child(0).text(), row.child(1).text(), row.child(2).text(), row
                        .child(3).text(), row.child(4).text(), row.child(5).text(), row.child(6).text(), row.child(1).child(0).attr("href")));
            }

        }
        contentLoaded = true;

        setupListView();
    }

    private void setupListView() {
        getListView().addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.roster_fragment_header, null));

        setListAdapter(new RosterAthleteAdapter(getActivity(), athletes));
        setListShown(true);
    }

}
