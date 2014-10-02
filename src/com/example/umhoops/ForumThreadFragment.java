package com.example.umhoops;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.umhoops.DownloadPageTask.Page;

import android.os.Bundle;
import android.util.Log;

import android.view.View;

import android.view.View.OnClickListener;
import android.widget.TextView;
import android.app.ListFragment;
import android.content.Intent;

import android.graphics.Typeface;

public class ForumThreadFragment extends ListFragment implements DownloadCallBack {

    static final String FORUM_LIST_FRAGMENT_DEBUG = "Forum Thread Fragment";

    public final static String THREAD_POST_AUTHOR_CLASS = "Author";
    public final static String THREAD_POST_ICON_CLASS = "ProfilePhotoMedium";
    public final static String THREAD_POST_DATE_CREATED_CLASS = "DateCreated";
    public final static String THREAD_POST_CONTENT_CLASS = "Message";
    public final static String THREAD_POST_AUTHOR_INFO_CLASS = "AuthorInfo";
    public final static String THREAD_POST_AUTHOR_ROLE_CLASS = "RoleTitle";

    private int pageNum, lastPage;
    private ArrayList<ThreadPost> posts;
    private boolean contentLoaded, noPager;
    private View header;
    private String threadBaseURL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        threadBaseURL = intent.getStringExtra(ForumListFragment.THREAD_LINK_INTENT);
        new DownloadPageTask(threadBaseURL, this, Page.THREADFIRST).execute();
        threadBaseURL += "/p";
        contentLoaded = false;
        pageNum = 1;
        noPager = false;
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
    public void onDestroyView() {
        header = null;
        super.onDestroyView();
    }

    @Override
    public void callback(Document doc) {
        Elements postAuthor = doc.getElementsByClass(THREAD_POST_AUTHOR_CLASS);
        Elements postIcon = doc.getElementsByClass(THREAD_POST_ICON_CLASS);
        Elements datePosted = doc.getElementsByClass(THREAD_POST_DATE_CREATED_CLASS);
        Elements postContent = doc.getElementsByClass(THREAD_POST_CONTENT_CLASS);
        Elements authorInfo = doc.getElementsByClass(THREAD_POST_AUTHOR_INFO_CLASS);
        Elements authorRole = doc.getElementsByClass(THREAD_POST_AUTHOR_ROLE_CLASS);

        ArrayList<ThreadPost> tempPosts = new ArrayList<ThreadPost>(postAuthor.size());

        for (int i = 0; i < postAuthor.size(); i++) {
            String role = authorRole.get(i).text();
            String info = authorInfo.get(i).text();
            info = info.substring(0, info.indexOf(role));
            tempPosts.add(new ThreadPost(postAuthor.get(i).text(), datePosted.get(i).text(), postContent.get(i).html(),
                    role, info, postIcon.get(i).attr("src")));
            // new DownloadImageTask(i).execute(postIcon.get(i).attr("src"));
        }

        Log.d(FORUM_LIST_FRAGMENT_DEBUG, "# posts: " + tempPosts.size());
        posts = tempPosts;
        contentLoaded = true;

        Elements pager = doc.getElementsByClass(PagerFunctions.PAGER_CLASS);
        if (pager.size() == 0) {
            noPager = true;
        } else {
            pager = pager.get(0).children();
            lastPage = Integer.parseInt(pager.get(pager.size() - 2).text());
        }
        setupListView();
    }

    private void downloadNewPage(String pageNumString) {
        contentLoaded = false;
        setListShown(false);

        pageNum = Integer.parseInt(pageNumString);
        if (pageNum == 1) {
            new DownloadPageTask(threadBaseURL + pageNumString, this, Page.THREADFIRST).execute();
        } else {
            new DownloadPageTask(threadBaseURL + pageNumString, this, Page.THREAD).execute();
        }
        if (header != null) {
            getListView().removeHeaderView(header);
            getListView().removeFooterView(header);
            header = null;
        }
    }

    private void setupListView() {
        if (!noPager) {
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
        }

        setListAdapter(new ThreadPostAdapter(getActivity(), posts));
        setListShown(true);
    }

    // private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    // int position;
    //
    // public DownloadImageTask(int position) {
    // this.position = position;
    // }
    //
    // protected Bitmap doInBackground(String... urls) {
    // String urldisplay = urls[0];
    // Bitmap icon = null;
    // try {
    // InputStream in = new java.net.URL(urldisplay).openStream();
    // icon = BitmapFactory.decodeStream(in);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return icon;
    // }
    //
    // protected void onPostExecute(Bitmap result) {
    // ThreadPostAdapter adapter = (ThreadPostAdapter) getListAdapter();
    // adapter.getItem(position).icon = result;
    // adapter.notifyDataSetChanged();
    // }
    // }
}
