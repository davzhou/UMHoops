package com.example.umhoops;


import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.umhoops.DownloadPageTask.Page;

import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ForumListFragment extends ListFragment implements DownloadCallBack {

    static final String FORUM_LIST_FRAGMENT_DEBUG = "Forum List Fragment";

    public final static String FORUM_BASE_URL = "http://forum.umhoops.com/discussions/p";
    public final static String FORUM_ENTRY_TITLE_CLASS = "Title";
    public final static String FORUM_ENTRY_VIEWS_CLASS = "ViewCount";
    public final static String FORUM_ENTRY_COMMENTS_CLASS = "CommentCount";
    public final static String FORUM_ENTRY_LAST_COMMENT_CLASS = "LastCommentBy";
    public final static String FORUM_ENTRY_CATEGORY_CLASS = "Category";
    public final static String FORUM_ENTRY_ICON_CLASS = "ProfilePhotoMedium";


    public final static String THREAD_TITLE_INTENT = "TITLE";
    public final static String THREAD_LINK_INTENT = "LINK";

    public final static int ENTRIES_PER_PAGE = 20;


    private int pageNum, lastPage;
    private ArrayList<ForumEntry> entries;
    private View header;
    private boolean contentLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNum = 1;
        new DownloadPageTask(FORUM_BASE_URL + pageNum, this, Page.FORUMLIST).execute();
        header = null;
        entries = null;
        contentLoaded = false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        if (contentLoaded) {
            setupListView();
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.override_listview, container, false);
//    }

    @Override
    public void onDestroyView() {
        header = null;
        super.onDestroyView();
    }

    @Override
    public void callback(Document doc) {
        // Debug.startMethodTracing("forumListCallback");

        Elements entryTitles = doc.getElementsByClass(FORUM_ENTRY_TITLE_CLASS);
        Elements entryViews = doc.getElementsByClass(FORUM_ENTRY_VIEWS_CLASS);
        Elements entryComments = doc.getElementsByClass(FORUM_ENTRY_COMMENTS_CLASS);
        Elements entryLastComment = doc.getElementsByClass(FORUM_ENTRY_LAST_COMMENT_CLASS);
        Elements entryCategory = doc.getElementsByClass(FORUM_ENTRY_CATEGORY_CLASS);
        Elements entryIcon = doc.getElementsByClass(FORUM_ENTRY_ICON_CLASS);
        Elements pager = doc.getElementsByClass(PagerFunctions.PAGER_CLASS);

        ArrayList<ForumEntry> tempEntries = new ArrayList<ForumEntry>(entryTitles.size());

        for (int i = 0; i < entryTitles.size(); i++) {
            tempEntries.add(new ForumEntry(entryTitles.get(i).child(0).text(), entryViews.get(i).child(0).text(),
                    entryComments.get(i).child(0).text(), entryCategory.get(i).text(), entryLastComment.get(i).text(),
                    entryTitles.get(i).child(0).attr("href"), entryIcon.get(i).attr("src")));
//            new DownloadImageTask(i).execute(entryIcon.get(i).attr("src"));
        }

        entries = tempEntries;

        contentLoaded = true;
        // Debug.stopMethodTracing();

        Elements pagerNodes = pager.get(0).children();
        lastPage = Integer.parseInt(pagerNodes.get(pagerNodes.size() - 2).text());
        setupListView();

    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        if (pos - 1 < entries.size() && pos != 0) {
            Intent intent = new Intent(getActivity(), ForumThreadFragActivity.class);
            intent.putExtra(THREAD_TITLE_INTENT, entries.get(pos - 1).title);
            intent.putExtra(THREAD_LINK_INTENT, entries.get(pos - 1).link);

            Log.d(FORUM_LIST_FRAGMENT_DEBUG, "pos: " + (pos - 1) + ", title: " + entries.get(pos - 1).title
                    + ", link: " + entries.get(pos - 1).link);

            startActivity(intent);
        }
    }

    private void downloadNewPage(String pageNumString) {
        contentLoaded = false;
        setListShown(false);
        new DownloadPageTask(FORUM_BASE_URL + pageNumString, this, Page.FORUMLIST).execute();
        pageNum = Integer.parseInt(pageNumString);
        if (header != null) {
            getListView().removeHeaderView(header);
            getListView().removeFooterView(header);
            header = null;
        }

        Log.d(FORUM_LIST_FRAGMENT_DEBUG, "clicked on page: " + pageNum);
    }

    private void setupListView() {

        Log.d(FORUM_LIST_FRAGMENT_DEBUG, "# of entries: " + entries.size());

        header = getActivity().getLayoutInflater().inflate(R.layout.page_navi_header, null);
        header.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        TextView[] pageView = new TextView[PagerFunctions.PAGE_LINKS];
        pageView[0] = (TextView) header.findViewById(R.id.page1);
        pageView[1] = (TextView) header.findViewById(R.id.page2);
        pageView[2] = (TextView) header.findViewById(R.id.page3);
        pageView[3] = (TextView) header.findViewById(R.id.page4);
        pageView[4] = (TextView) header.findViewById(R.id.page5);

        int[] pages = PagerFunctions.pageOptions(pageNum, lastPage);
        for (int i = 0; i < pages.length; i++) {
            pageView[i].setText(Integer.toString(pages[i]));
            if (pages[i] == pageNum) {
                pageView[i].setTypeface(null, Typeface.BOLD);
                pageView[i].setEnabled(false);
                pageView[i].setTextSize(16);
            } else {
                pageView[i].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadNewPage(((TextView) v).getText().toString());

                    }
                });
            }
        }
        for (int i = pages.length; i < PagerFunctions.PAGE_LINKS; i++) {
            pageView[i].setVisibility(View.GONE);
        }
        if (pageNum == 1) {
            header.findViewById(R.id.prevPage).setEnabled(false);
        } else {
            header.findViewById(R.id.prevPage).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadNewPage(Integer.toString(pageNum - 1));

                }
            });
        }
        if (pageNum == lastPage) {
            header.findViewById(R.id.nextPage).setEnabled(false);
        } else {
            header.findViewById(R.id.nextPage).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadNewPage(Integer.toString(pageNum + 1));
                }
            });
        }
        getListView().addHeaderView(header);
        getListView().addFooterView(header);

        setListAdapter(new ForumEntryAdapter(getActivity(), entries));
        setListShown(true);
    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        int position;
//
//        public DownloadImageTask(int position) {
//            this.position = position;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap icon = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                icon = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return icon;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            ForumEntryAdapter adapter = (ForumEntryAdapter) getListAdapter();
//            adapter.getItem(position).icon = result;
//            adapter.notifyDataSetChanged();
//        }
//    }


}
