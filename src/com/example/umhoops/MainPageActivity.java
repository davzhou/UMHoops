package com.example.umhoops;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainPageActivity extends Activity {
    static final String MAIN_DEBUG = "Main Page";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (item.getItemId()) {
        case R.id.blogPage:
            ft.replace(R.id.mainLayout, new BlogListFragment());
            ft.commit();
            return true;
        case R.id.forumPage:
            ft.replace(R.id.mainLayout, new ForumListFragment());
            ft.commit();
            return true;
        case R.id.rosterPage:
            ft.replace(R.id.mainLayout, new RosterFragment());
            ft. commit();
            return true;
        case R.id.schedulePage:
            ft.replace(R.id.mainLayout, new ScheduleFragment());
            ft. commit();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

}
