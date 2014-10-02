package com.example.umhoops;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ForumEntryAdapter extends ArrayAdapter<ForumEntry> {

    static final String FORUM_ENTRY_ADAPTER_DEBUG = "Blog Entry Adapter";

    Picasso pi;

    public ForumEntryAdapter(Context context, ArrayList<ForumEntry> objects) {
        super(context, 0, objects);
        pi = new Picasso.Builder(getContext()).indicatorsEnabled(true).build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ForumEntryHolder holder = null;
        if (row == null) {
            holder = new ForumEntryHolder();

            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.forum_fragment_item, parent, false);

            holder.title = (TextView) row.findViewById(R.id.threadTitle);
            holder.views = (TextView) row.findViewById(R.id.threadViews);
            holder.comments = (TextView) row.findViewById(R.id.threadComments);
            holder.icon = (ImageView) row.findViewById(R.id.forumIcon);

            row.setTag(holder);
        } else {
            holder = (ForumEntryHolder) row.getTag();
        }

        ForumEntry thread = getItem(position);
        holder.title.setText(thread.title);
        holder.views.setText(thread.views);
        holder.comments.setText(thread.comments);

        // if (thread.icon == null) {
        // holder.icon.setImageDrawable(null);
        // } else {
        // holder.icon.setImageBitmap(thread.icon);
        // }

        pi.load(thread.iconLink).into(holder.icon);

        return row;
    }

    static class ForumEntryHolder {
        TextView title, views, comments;
        ImageView icon;
    }

}
