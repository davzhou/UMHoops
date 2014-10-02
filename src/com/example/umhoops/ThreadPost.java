package com.example.umhoops;

import android.text.Html;
import android.text.Spanned;

public class ThreadPost {
    public String author, datePosted, authorRole, authorInfo, iconLink, webContent;
    public Spanned content;
//    public Bitmap icon;

    public ThreadPost(String author, String datePosted, String content, String authorRole, String authorInfo, String iconLink) {
        this.author = author;
        this.datePosted = datePosted;
        this.webContent = content;
        this.content = Html.fromHtml(content);
        this.authorRole = authorRole;
        this.authorInfo = authorInfo;
        this.iconLink = iconLink;
    }
}
