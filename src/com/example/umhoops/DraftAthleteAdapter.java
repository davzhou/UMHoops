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

public class DraftAthleteAdapter extends ArrayAdapter<DraftAthlete> {


    final static String LINK_TAG_OPEN = "<a href=";
    final static String LINK_TAG_CLOSE = "</a>";

    public DraftAthleteAdapter(Context context, List<DraftAthlete> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DraftAthleteHolder holder = null;
        if (row == null) {
            holder = new DraftAthleteHolder();

            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.draft_fragment_item, parent, false);

            holder.year = (TextView) row.findViewById(R.id.draftYear);
            holder.rnd = (TextView) row.findViewById(R.id.draftRnd);
            holder.pick = (TextView) row.findViewById(R.id.draftPick);
            holder.name = (TextView) row.findViewById(R.id.draftName);
            holder.team = (TextView) row.findViewById(R.id.draftTeam);

            row.setTag(holder);
        } else {
            holder = (DraftAthleteHolder) row.getTag();
        }

        DraftAthlete athlete = getItem(position);
        holder.year.setText(athlete.year);
        holder.rnd.setText(athlete.rnd);
        holder.pick.setText(athlete.pick);
        holder.team.setText(athlete.team);

        if (athlete.link== null){
            holder.name.setText(athlete.name);
        }
        else {
            holder.name.setText(Html.fromHtml(LINK_TAG_OPEN+athlete.link+">"+athlete.name+LINK_TAG_CLOSE));
            holder.name.setMovementMethod(LinkMovementMethod.getInstance());
        }

        return row;
    }

    static class DraftAthleteHolder{
        TextView year, rnd, pick, name, team;
    }
}
