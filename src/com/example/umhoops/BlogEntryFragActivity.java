package com.example.umhoops;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class BlogEntryFragActivity extends Activity {

    static final String BLOG_FRAGMENT_ACTIVITY_DEBUG = "Blog Web Fragment Activity";

    private String title;

    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_entry_frag);

        Intent intent = getIntent();
        title = intent.getStringExtra(BlogListFragment.BLOG_INTENT_TITLE);

        titleView = (TextView) findViewById(R.id.entryTitle);

        titleView.setText(title);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.webFragFrame, new BlogEntryFragment());
            ft.commit();
        }
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(BLOG_FRAGMENT_ACTIVITY_DEBUG, "activity orientation: landscape");
        } else {
            Log.d(BLOG_FRAGMENT_ACTIVITY_DEBUG, "activity orientation: portrait");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blog_entry, menu);
        return true;
    }

}
