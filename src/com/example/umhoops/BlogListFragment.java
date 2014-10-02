package com.example.umhoops;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.umhoops.DownloadPageTask.Page;

public class BlogListFragment extends ListFragment implements DownloadCallBack {

    static final String BLOG_FRAGMENT_DEBUG = "Blog List Fragment";

    public final static String BLOG_ENTRY_TITLE_CLASS = "entry-title";
    public final static String BLOG_ENTRY_PUBLISH_CLASS = "published";
    public final static String BLOG_ENTRY_CONTENT = "entry-content";

    public final static String BLOG_INTENT_TITLE = "title";
    public final static String BLOG_INTENT_LINK = "url";

    public final static String BLOG_URL = "http://www.umhoops.com/";

    public final static int ENTRIES_PER_PAGE = 6;

    // private String[] entryLinks;
    // private BlogEntry[] entries;
    private ArrayList<BlogEntry> entries;
    private ArrayList<String> entryLinks;
    private boolean contentLoaded;
    private BlogEntryAdapter adapter;
    private int pageNum;
    private View footer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLoaded = false;
        adapter = null;
        pageNum = 1;
        new DownloadPageTask(BLOG_URL, this, Page.BLOGLIST).execute();
        footer = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        if (contentLoaded) {
            adapter = new BlogEntryAdapter(getActivity(), entries);
            setListAdapter(adapter);
            // adapter.setLoadingData(loadingMore);
            footer = getActivity().getLayoutInflater().inflate(R.layout.blog_fragment_footer, null);
            Button loadButton = (Button) footer.findViewById(R.id.blogListLoadButton);
            loadButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreEntries();
                }
            });

            getListView().addFooterView(footer);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.override_listview, container, false);
    }

    // public void createEntry(String source) {
    // Document doc = Jsoup.parse(source);
    //
    // Elements entryTitles = doc.getElementsByClass(BLOG_ENTRY_TITLE_CLASS);
    // Elements entryPublishInfo =
    // doc.getElementsByClass(BLOG_ENTRY_PUBLISH_CLASS);
    //
    // int numEntries = entryTitles.size();
    // entries = new BlogEntry[numEntries];
    // entryLinks = new String[numEntries];
    //
    // for (int i = 0; i < entries.length; i++) {
    // entries[i] = new BlogEntry(entryTitles.get(i).text(),
    // entryPublishInfo.get(i).text(), "placeholder");
    // entryLinks[i] = new String(entryTitles.get(i).child(0).attr("href"));
    // }
    //
    // adapter = new BlogEntryAdapter(getActivity(),
    // R.layout.blog_fragment_item, entries);
    // setListAdapter(adapter);
    //
    // }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);

        Log.d(BLOG_FRAGMENT_DEBUG, "clicked pos: " + pos);
        if (pos < entries.size()) {
            Intent intent = new Intent(getActivity(), BlogEntryFragActivity.class);
            intent.putExtra(BLOG_INTENT_TITLE, entries.get(pos).title);
            intent.putExtra(BLOG_INTENT_LINK, entryLinks.get(pos));

            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        adapter = null;
        footer = null;
        super.onDestroyView();
    }

    @Override
    public void callback(Document doc) {

        Elements entryTitles = doc.getElementsByClass(BLOG_ENTRY_TITLE_CLASS);
        Elements entryPublishInfo = doc.getElementsByClass(BLOG_ENTRY_PUBLISH_CLASS);
        Elements entryImgLinks = doc.getElementsByClass(BLOG_ENTRY_CONTENT);
        ArrayList<BlogEntry> tempEntries = new ArrayList<BlogEntry>(ENTRIES_PER_PAGE);
        ArrayList<String> tempEntryLinks = new ArrayList<String>(ENTRIES_PER_PAGE);

        for (int i = 0; i < entryTitles.size(); i++) {

            tempEntryLinks.add(new String(entryTitles.get(i).child(0).attr("href")));

            Elements entryImgLink = entryImgLinks.get(i).select("img");
            if (!entryImgLink.isEmpty()) {
                tempEntries.add(new BlogEntry(entryTitles.get(i).text(), entryPublishInfo.get(i).text(), entryImgLink.get(0).attr("src")));
//                new DownloadImageTask(i + ((pageNum - 1) * ENTRIES_PER_PAGE)).execute(entryImgLink.get(0).attr("src"));
            } else {
                tempEntries.add(new BlogEntry(entryTitles.get(i).text(), entryPublishInfo.get(i).text(), null));
//                tempEntries.get(i).pic = null;
            }
        }

        if (pageNum == 1) {
            entries = tempEntries;
            entryLinks = tempEntryLinks;
            adapter = new BlogEntryAdapter(getActivity(), entries);
            setListAdapter(adapter);
            contentLoaded = true;
            footer = getActivity().getLayoutInflater().inflate(R.layout.blog_fragment_footer, null);
            Button loadButton = (Button) footer.findViewById(R.id.blogListLoadButton);
            loadButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreEntries();
                }
            });
            getListView().addFooterView(footer);
        } else {
            entries.addAll(tempEntries);
            entryLinks.addAll(tempEntryLinks);
            // adapter.addAll(tempEntries);
            adapter.notifyDataSetChanged();
            footer.findViewById(R.id.blogListLoadingLayout).setVisibility(View.GONE);
            footer.findViewById(R.id.blogListLoadButton).setVisibility(View.VISIBLE);
        }

    }

    private void loadMoreEntries() {
        pageNum++;
        new DownloadPageTask(BLOG_URL + "/page/" + pageNum + "/", this, Page.BLOGLIST).execute();
        footer.findViewById(R.id.blogListLoadButton).setVisibility(View.GONE);
        footer.findViewById(R.id.blogListLoadingLayout).setVisibility(View.VISIBLE);
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
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            adapter.getItem(position).pic = result;
//            adapter.notifyDataSetChanged();
//        }
//    }

    // private void setOnScrollListener() {
    // getListView().setOnScrollListener(this);
    // }
    //
    // @Override
    // public void onScroll(AbsListView view, int firstVisibleItem, int
    // visibleItemCount, int totalItemCount) {
    // int lastInScreen = firstVisibleItem + visibleItemCount;
    //
    // if (lastInScreen == totalItemCount && visibleItemCount != totalItemCount)
    // {
    // Log.d(BLOG_FRAGMENT_DEBUG, "loading activated, first view: " +
    // firstVisibleItem + ", vis items: "
    // + visibleItemCount + ", total items: " + totalItemCount);
    // // adapter.setLoadingData(loadingMore);
    // adapter.notifyDataSetChanged();
    // // pageNum++;
    // // new DownloadPageTask(BLOG_URL + "/page/" + pageNum + "/", this,
    // // Page.BLOGLIST).execute();
    //
    // }
    // }
    //
    // @Override
    // public void onScrollStateChanged(AbsListView view, int scrollState) {
    // }

    // public <T> T[] concatenate(T[] A, T[] B) {
    // int aLen = A.length;
    // int bLen = B.length;
    //
    // @SuppressWarnings("unchecked")
    // T[] C = (T[]) Array.newInstance(A.getClass().getComponentType(), aLen +
    // bLen);
    // System.arraycopy(A, 0, C, 0, aLen);
    // System.arraycopy(B, 0, C, aLen, bLen);
    //
    // return C;
    // }
}
