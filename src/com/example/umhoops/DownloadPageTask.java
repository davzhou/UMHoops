package com.example.umhoops;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadPageTask extends AsyncTask<Void, Void, Document> {
    static final String DOWNLOAD_DEBUG = "Download Async Task";
    static final int BLOG_ENTRIES_PER_PAGE = 6;
    static final int FORUM_ENTRIES_PER_PAGE = 20;
    static final int THREAD_POSTS_PER_PAGE = 20;

    public enum Page {
        BLOGLIST, BLOGENTRY, FORUMLIST, THREAD, THREADFIRST, ROSTER, SCHEDULE
    }

    Page type;
    Fragment fragment;
    String link;
    DownloadCallBack listener;

    /*    public DownloadPageTask(Page type, Fragment fragment) {
            this.type = type;
            this.fragment = fragment;

            switch (type) {
            case BLOG:
                link = "http://www.umhoops.com/";
                break;
            case FORUM:
                link = "http://www.umhoops.com/";
                break;
            case RECRUITING:
                link = "http://www.umhoops.com/recruiting/";
                break;
            }
        }*/

    public DownloadPageTask(String link, DownloadCallBack listener, Page type) {
        this.link = link;
        this.listener = listener;
        this.type = type;
    }

    /*    @Override
        protected String doInBackground(Void... params) {
            return DownloadPage.download(link);
        }*/

    @Override
    protected Document doInBackground(Void... params) {
        // Debug.startMethodTracing("downloadForum");
        String source = DownloadPage.download(link);
        // Debug.stopMethodTracing();
        // Debug.startMethodTracing("substringParse");
        StringBuilder sb = new StringBuilder();
        switch (type) {
        case BLOGLIST:
            for (int i = 0; i < BLOG_ENTRIES_PER_PAGE; i++) {
                int noEntries = source.indexOf("<div class=\"comment_count");
                if (noEntries == -1) {
                    break;
                }
                source = source.substring(noEntries);
                sb.append(source.substring(source.indexOf("<h2 class=\"entry-title\">"),
                        source.indexOf("<div class=\"twitter_byline")));
                source = source.substring(source.indexOf("<div class=\"format_text entry-content\">"));
                sb.append(source.substring(0, source.indexOf("</p>") + 4) + "</div>");
            }
            source = sb.toString();
            break;

        // BLOG ENTRY NEEDS TO BE PARSED
        case FORUMLIST:
            source = source.substring(source.indexOf("<div  id=\"PagerBefore"));
            sb.append(source.substring(0, source.indexOf("</div>") + 6));
            for (int i = 0; i < FORUM_ENTRIES_PER_PAGE; i++) {
                int noEntries = source.indexOf("<li id=\"Discussion");
                if (noEntries == -1) {
                    break;
                }
                source = source.substring(noEntries);
                sb.append(source.substring(0, source.indexOf("</li>") + 5));
                source = source.substring(source.indexOf("</li>"));
            }
            source = sb.toString();
            break;
        case THREAD:
            sb.append(source.substring(source.indexOf("<span class=\"BeforeCommentHeading"),
                    source.indexOf("<div class=\"DataBox")));
            for (int i = 0; i < THREAD_POSTS_PER_PAGE; i++) {
                int noEntries = source.indexOf("<li class=\"Item");
                if (noEntries == -1) {
                    break;
                }
                source = source.substring(noEntries);
                sb.append(source.substring(0, source.indexOf("</li>") + 5));
                source = source.substring(source.indexOf("</li>"));
            }
            source = sb.toString();
            break;
        case THREADFIRST:
            source = source.substring(source.indexOf("<div id=\"Discussion_"));
            sb.append(source.substring(0, source.indexOf("<div class=\"CommentsWrap")));
            source = source.substring(source.indexOf("<span class=\"BeforeCommentHeading"));
            sb.append(source.substring(0, source.indexOf("<div class=\"DataBox")));
            for (int i = 0; i < THREAD_POSTS_PER_PAGE; i++) {
                int noEntries = source.indexOf("<li class=\"Item");
                if (noEntries == -1) {
                    break;
                }
                source = source.substring(noEntries);
                sb.append(source.substring(0, source.indexOf("</li>") + 5));
                source = source.substring(source.indexOf("</li>"));
            }
            source = sb.toString();
            break;
        case BLOGENTRY:
            source = source.substring(source.indexOf("<div class=\"format_text entry-content"),
                    source.indexOf("<div class=\"post_tags clearfix"));
            break;
        case ROSTER:
            source = "<table>" + source.substring(source.indexOf("<tbody>"), source.indexOf("</tbody>") + 8)
                    + "</table>";
            break;
        case SCHEDULE:
            source = source.substring(source.indexOf("<table class=\"tableizer-table"), source.indexOf("</table>") + 8);
            break;
        default:
            break;
        }
        // Debug.stopMethodTracing();
        // Debug.startMethodTracing("domParse");
        Document doc = null;
        try {
            doc = Jsoup.parse(source);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Debug.stopMethodTracing();
        return doc;
    }

    @Override
    protected void onPostExecute(Document doc) {
        if (doc != null) {
            listener.callback(doc);
        } else {
            Log.e(DOWNLOAD_DEBUG, "failed to get page source");

        }
    }

}
