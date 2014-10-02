package com.example.umhoops;

import android.graphics.Bitmap;

public class ForumEntry {
    public String title, views, comments, category, lastComment, link, iconLink;

    // public Bitmap icon;

    public ForumEntry(String title, String views, String comments, String category, String lastComment, String link,
            String iconLink) {
        this.title = title;
        this.views = views;
        this.comments = comments;
        this.category = category;
        this.lastComment = lastComment;
        this.link = link;
        this.iconLink = iconLink;
    }
}
