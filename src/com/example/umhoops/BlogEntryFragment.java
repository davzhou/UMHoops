package com.example.umhoops;

import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.umhoops.BlogListFragment;
import com.example.umhoops.DownloadCallBack;
import com.example.umhoops.DownloadPageTask;
import com.example.umhoops.DownloadPageTask.Page;

public class BlogEntryFragment extends Fragment implements DownloadCallBack {

    static final String BLOG_FRAGMENT_DEBUG = "Blog Web Fragment";
    static final String YT_BASE_URL = "www.youtube.com/embed/";
    static final String BASE_URL = "http://www.umhoops.com";
    static final String IFRAME_YT_SELECT = "iframe[src*=www.youtube.com/embed/]";
    static final String PAGE_CONTENT = "content";

    private static final String PAGE_LOADED = "source";

    WebView contentWebView;
    String pageContent;
    float scrollPercentage;
    int viewHeight, contentHeight;
    boolean pageLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageLoaded = false;
        scrollPercentage = 0;
        // if (savedInstanceState != null) {
        // pageLoaded = savedInstanceState.getBoolean(PAGE_LOADED);
        // Log.d(BLOG_FRAGMENT_DEBUG, "previous load: "+pageLoaded);
        // }
        if (!pageLoaded) {
            Intent intent = getActivity().getIntent();

            new DownloadPageTask(intent.getStringExtra(BlogListFragment.BLOG_INTENT_LINK), this, Page.BLOGENTRY)
                    .execute();
        }
        // else {
        // pageContent = savedInstanceState.getString(PAGE_CONTENT);
        // Log.d(BLOG_FRAGMENT_DEBUG, "PAGE CONTENT: " + pageContent);
        // }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // saveOrientation();
        View v = inflater.inflate(R.layout.fragment_webview, container, false);

        contentWebView = (WebView) v.findViewById(R.id.webView);

        contentWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // Load the URLs inside the WebView, not in the external web browser

        // if (pageContent != null) {
        // contentWebView.loadData(pageContent, "text/html; charset=UTF-8",
        // null);
        // }

        contentWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        contentWebView.getSettings().setSupportZoom(true);
        contentWebView.getSettings().setDisplayZoomControls(false);
        contentWebView.getSettings().setBuiltInZoomControls(true);

        contentWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        contentWebView.setScrollbarFadingEnabled(true);
        contentWebView.getSettings().setLoadsImagesAutomatically(true);

        contentWebView.setWebViewClient(new BlogEntryWebViewClient(
                (ProgressBar) v.findViewById(R.id.blogEntryProgress), contentWebView));
        if (pageLoaded) {
            contentWebView.loadDataWithBaseURL(BASE_URL, pageContent, "text/html", "UTF-8", null);

            // contentWebView.restoreState(savedInstanceState);
        }

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (pageLoaded) {
            scrollPercentage = calculatePercentage();
        }

        // outState.putBoolean(PAGE_LOADED, pageLoaded);
        // if (pageLoaded && contentWebView != null) {
        // Log.d(BLOG_FRAGMENT_DEBUG, "saved");
        // contentWebView.saveState(outState);
        // outState.putString(PAGE_CONTENT, pageContent);
        // }
    }

    @Override
    public void onDestroyView() {

        contentWebView.destroy();
        contentWebView = null;

        super.onDestroyView();
    }

    @Override
    public void callback(Document doc) {
        Elements youtubeLinks = doc.select(IFRAME_YT_SELECT);
        for (Element ytLink : youtubeLinks) {
            Element replaceLink = new Element(Tag.valueOf("a"), BASE_URL);
            String videoId = parseIframeTag(ytLink.attr("src"));
            replaceLink.attr("href", "vnd.youtube:" + videoId);
            Element replacePic = new Element(Tag.valueOf("img"), BASE_URL);
            replacePic.attr("src", "file:///android_asset/youtube-play-button.png");
            replacePic.attr("style", "background:url(http://img.youtube.com/vi/" + videoId + "/0.jpg); "
                    + "background-size:100%; background-position:center");
            replaceLink.appendChild(replacePic);

            ytLink.replaceWith(replaceLink);
        }
        pageContent = doc.html();
        pageLoaded = true;
        // contentView.setText(entryContent.first().text());
        // contentWebView.loadData(entryContent.first().outerHtml(),
        // "text/html; charset=UTF-8", null);
        if (contentWebView != null) {
            contentWebView.loadDataWithBaseURL(BASE_URL, pageContent, "text/html", "UTF-8", null);
        }
    }

    public String parseIframeTag(String url) {
        String videoId = url.substring(url.indexOf(YT_BASE_URL)+YT_BASE_URL.length());
        if (videoId.contains("/")) {
            videoId = videoId.substring(0, videoId.indexOf("/"));
        }
        if (videoId.contains("?")) {
            videoId = videoId.substring(0, videoId.indexOf("?"));
        }
        // String parsedUrl = "<a href='vnd.youtube:" + videoId + "'>";
        // parsedUrl +=
        // "<img class='youtubeimg' src='file:///android_asset/youtube-play-button.png' style='background:url(http://img.youtube.com/vi/"
        // + videoId + "/0.jpg)'/></a>";
        return videoId;
    }

    private float calculatePercentage() {

        float currentScrollPosition = contentWebView.getScrollY();

        float percentWebview;

        percentWebview = currentScrollPosition / (2 * contentHeight - viewHeight);

        // Log.d(BLOG_FRAGMENT_DEBUG, "---------");
        // Log.d(BLOG_FRAGMENT_DEBUG, "old content height: " + contentHeight);
        // Log.d(BLOG_FRAGMENT_DEBUG, "old view height: " + viewHeight);
        // Log.d(BLOG_FRAGMENT_DEBUG, "percentage: " + percentWebview);

        return percentWebview;
    }

    private void calculateAndSetScroll() {

        contentWebView.scrollTo(0, (int) ((2 * contentHeight - viewHeight) * scrollPercentage));

        // Log.d(BLOG_FRAGMENT_DEBUG, "scroll to: " + scrollTo);
        //
        // return scrollTo;
    }

    // private void saveOrientation(){
    // if (getResources().getConfiguration().orientation ==
    // Configuration.ORIENTATION_LANDSCAPE) {
    // isLandscape = true;
    // }
    // else{
    // isLandscape = false;
    // }
    // }

    private void saveWebViewDimensions() {
        viewHeight = contentWebView.getHeight();
        contentHeight = contentWebView.getContentHeight();

        // Log.d(BLOG_FRAGMENT_DEBUG, "---------");
        // Log.d(BLOG_FRAGMENT_DEBUG, "save content height: " + contentHeight);
        // Log.d(BLOG_FRAGMENT_DEBUG, "save view height: " + viewHeight);
    }

    private class BlogEntryWebViewClient extends WebViewClient {
        private ProgressBar spinner;
        private WebView content;

        public BlogEntryWebViewClient(ProgressBar spinner, WebView content) {
            this.spinner = spinner;
            this.content = content;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (pageLoaded) {

                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saveWebViewDimensions();
                        calculateAndSetScroll();

                    }
                }, 200);

            }
            super.onPageFinished(view, url);

            spinner.setVisibility(View.GONE);
        }
    }
}
