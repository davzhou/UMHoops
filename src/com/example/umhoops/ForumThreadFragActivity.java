package com.example.umhoops;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ForumThreadFragActivity extends Activity {

    static final String FORUM_THREAD_ACTIVITY_DEBUG = "Thread Fragment Activity";

    private String title;

    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forum_thread);

        Intent intent = getIntent();
        title = intent.getStringExtra(ForumListFragment.THREAD_TITLE_INTENT);

        titleView = (TextView) findViewById(R.id.threadTitle);
        titleView.setText(title);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.listFragFrame, new ForumThreadFragment());
            ft.commit();
        }
    }

}
