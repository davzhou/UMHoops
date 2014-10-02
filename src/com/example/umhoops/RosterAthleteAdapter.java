package com.example.umhoops;

import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RosterAthleteAdapter extends ArrayAdapter<RosterAthlete> {
    final static String BASE_URL = "http://www.umhoops.com";
    final static String LINK_TAG_OPEN = "<a href=";
    final static String LINK_TAG_CLOSE = "</a>";

    public RosterAthleteAdapter(Context context, List<RosterAthlete> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RosterAthleteHolder holder = null;
        if (row == null) {
            holder = new RosterAthleteHolder();

            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.roster_fragment_item, parent, false);

            holder.no = (TextView) row.findViewById(R.id.playerNo);
            holder.name = (TextView) row.findViewById(R.id.playerName);
            holder.ht = (TextView) row.findViewById(R.id.playerHt);
            holder.wt = (TextView) row.findViewById(R.id.playerWt);
            holder.pos = (TextView) row.findViewById(R.id.playerPos);
            holder.year = (TextView) row.findViewById(R.id.playerYear);
            holder.hs = (TextView) row.findViewById(R.id.playerHS);

            row.setTag(holder);
        } else {
            holder = (RosterAthleteHolder) row.getTag();
        }

        RosterAthlete athlete = getItem(position);
        holder.no.setText(athlete.no);

        holder.ht.setText(athlete.ht);
        holder.wt.setText(athlete.wt);
        holder.pos.setText(athlete.pos);
        holder.year.setText(athlete.year);
        holder.hs.setText(athlete.hs);

        if (athlete.link== null){
            holder.name.setText(athlete.name);
        }
        else {
            holder.name.setText(Html.fromHtml(LINK_TAG_OPEN+athlete.link+">"+athlete.name+LINK_TAG_CLOSE));
            holder.name.setMovementMethod(LinkMovementMethod.getInstance());
        }

        return row;
    }

    static class RosterAthleteHolder {
        TextView no, name, year, hs, wt, ht, pos;
    }

}
