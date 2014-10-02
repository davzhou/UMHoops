package com.example.umhoops;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ThreadPostAdapter extends ArrayAdapter<ThreadPost> {

    static final String THREAD_POST_ADAPTER_DEBUG = "Thread Post Adapter";

    static final String BASE_URL = "http://www.umhoops.com";

    Picasso pi;

    public ThreadPostAdapter(Context context, ArrayList<ThreadPost> objects) {
        super(context, 0, objects);
        pi = new Picasso.Builder(getContext()).indicatorsEnabled(true).build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ThreadPostHandler holder = null;
        if (row == null) {
            row = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.thread_fragment_item, parent, false);
            holder = new ThreadPostHandler();
            holder.author = (TextView) row.findViewById(R.id.postAuthor);
            holder.datePosted = (TextView) row.findViewById(R.id.postInfo);
//            holder.content = (TextView) row.findViewById(R.id.postContent);
            holder.icon = (ImageView) row.findViewById(R.id.forumIcon);
            holder.authorRole = (TextView) row.findViewById(R.id.authorRole);
            holder.authorInfo = (TextView) row.findViewById(R.id.authorInfo);
            holder.webContent = (WebView) row.findViewById(R.id.webPostContent);

            row.setTag(holder);
        } else {
            holder = (ThreadPostHandler) row.getTag();
        }

        ThreadPost post = getItem(position);
        holder.author.setText(post.author);
        holder.datePosted.setText(post.datePosted);
        // holder.content.setText(post.content);
        holder.authorRole.setText(post.authorRole);
        holder.authorInfo.setText(post.authorInfo);
        holder.webContent.loadData(post.webContent, "text/html", "UTF-8");

        pi.load(post.iconLink).into(holder.icon);

        // if (post.icon == null) {
        // holder.icon.setImageDrawable(null);
        // } else {
        // holder.icon.setImageBitmap(post.icon);
        // }

        return row;
    }

    static class ThreadPostHandler {
        TextView author, datePosted, content, authorInfo, authorRole;
        ImageView icon;
        WebView webContent;
    }

}
