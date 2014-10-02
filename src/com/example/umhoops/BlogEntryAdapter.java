package com.example.umhoops;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BlogEntryAdapter extends ArrayAdapter<BlogEntry> {

    // private static final int TYPE_ENTRY = 0;
    // private static final int TYPE_LOADING = 1;
    // private static final int TYPE_MAX_COUNT = 2;
    static final String BLOG_ENTRY_ADAPTER_DEBUG = "Blog Entry Adapter";

    // BlogEntry data[];
    // boolean loadingData;

    Picasso pi;

    public BlogEntryAdapter(Context context, ArrayList<BlogEntry> entries) {
        super(context, 0, entries);

        pi = new Picasso.Builder(getContext()).indicatorsEnabled(true).build();
    }

    // public void setLoadingData(boolean loadingData) {
    // this.loadingData = loadingData;
    // }

    // @Override
    // public int getCount() {
    // if (loadingData) {
    // return data.length + 1;
    // } else {
    // return data.length;
    // }
    // }

    // @Override
    // public int getItemViewType(int position) {
    // if (position < data.length) {
    // return TYPE_ENTRY;
    // } else {
    // return TYPE_LOADING;
    // }
    // }

    // @Override
    // public int getViewTypeCount() {
    // return TYPE_MAX_COUNT;
    // }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BlogEntryHolder holder = null;
        // int type = getItemViewType(position);
        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            holder = new BlogEntryHolder();

            row = inflater.inflate(R.layout.blog_fragment_item, parent, false);
            holder.title = (TextView) row.findViewById(R.id.entryTitle);
            holder.publishInfo = (TextView) row.findViewById(R.id.publishInfo);
            holder.image = (ImageView) row.findViewById(R.id.blogIcon);

            // switch (type) {
            // case TYPE_ENTRY:
            // row = inflater.inflate(R.layout.blog_fragment_item, parent,
            // false);
            // holder.title = (TextView) row.findViewById(R.id.entryTitle);
            // holder.publishInfo = (TextView)
            // row.findViewById(R.id.publishInfo);
            // holder.image = (ImageView) row.findViewById(R.id.blogIcon);
            // break;
            // case TYPE_LOADING:
            // row = inflater.inflate(R.layout.blog_fragment_loading_item,
            // parent, false);
            // break;
            // }

            row.setTag(holder);
        } else {
            holder = (BlogEntryHolder) row.getTag();
        }

        BlogEntry entry = getItem(position);
        holder.title.setText(entry.title);
        holder.publishInfo.setText(entry.publishInfo);
        if (entry.picLink == null) {
            holder.image.setImageDrawable(null);
        } else {
            pi.load(entry.picLink).into(holder.image);
        }

        return row;
    }

    static class BlogEntryHolder {
        TextView title, publishInfo;
        ImageView image;
    }
}
