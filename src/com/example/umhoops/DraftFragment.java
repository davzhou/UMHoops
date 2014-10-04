package com.example.umhoops;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.umhoops.DownloadPageTask.Page;

import android.app.ListFragment;
import android.os.Bundle;

public class DraftFragment extends ListFragment implements DownloadCallBack {

    static final String DRAFT_FRAGMENT_DEBUG = "Draft Fragment";

    public final static String DRAFT_BASE_URL = "http://www.umhoops.com/history/draft-picks/";

    private ArrayList<DraftAthlete> athletes;
    private boolean contentLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLoaded = false;
        new DownloadPageTask(DRAFT_BASE_URL, this, Page.DRAFT).execute();

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
        athletes = new ArrayList<DraftAthlete>();
        // Log.d(ROSTER_FRAGMENT_DEBUG, doc.toString());
        Elements rosterRows = doc.select("tr");
        // Log.d(ROSTER_FRAGMENT_DEBUG, "rows: " + rosterRows.size());

        for (Element row : rosterRows) {
            if (!row.select("td").isEmpty()) {
                if (row.child(3).children().isEmpty()) {
                    athletes.add(new DraftAthlete(row.child(0).text(), row.child(1).text(), row.child(2).text(), row
                            .child(3).text(), row.child(4).text()));
                } else {
                    athletes.add(new DraftAthlete(row.child(0).text(), row.child(1).text(), row.child(2).text(), row
                            .child(3).text(), row.child(4).text(), row.child(3).child(0).attr("href")));
                }
            }
        }
        contentLoaded = true;

        setupListView();

    }

    private void setupListView() {
        getListView().addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.draft_fragment_header, null));
        setListAdapter(new DraftAthleteAdapter(getActivity(), athletes));
        setListShown(true);
    }

}
