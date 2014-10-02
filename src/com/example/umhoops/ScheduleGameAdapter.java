package com.example.umhoops;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScheduleGameAdapter extends ArrayAdapter<ScheduleGame> {

    public ScheduleGameAdapter(Context context, List<ScheduleGame> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ScheduleGameHolder holder = null;
        int rowType = getItemViewType(position);
        if (row == null) {

            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            switch (rowType) {
            case ScheduleGame.SCHEDULE_GAME:
                row = inflater.inflate(R.layout.schedule_fragment_item, parent, false);
                holder = new ScheduleGameHolder();
                break;
            case ScheduleGame.SCHEDULE_LABEL:
                row = inflater.inflate(R.layout.schedule_fragment_label, parent, false);
                holder = new ScheduleGameHolder();
                break;
            case ScheduleGame.SCHEDULE_B10:
                row = inflater.inflate(R.layout.schedule_fragment_b10, parent, false);
                break;
            }

            if (rowType == ScheduleGame.SCHEDULE_B10) {
                return row;
            } else {
                holder.date = (TextView) row.findViewById(R.id.gameDate);
                holder.opponent = (TextView) row.findViewById(R.id.gameOpponent);
                holder.channel = (TextView) row.findViewById(R.id.gameChannel);
                holder.time = (TextView) row.findViewById(R.id.gameTime);

                row.setTag(holder);
            }
        } else {
            if (rowType == ScheduleGame.SCHEDULE_B10) {
                return row;
            } else {
                holder = (ScheduleGameHolder) row.getTag();
            }
        }

        ScheduleGame game = getItem(position);
        holder.date.setText(game.date);
        holder.opponent.setText(game.opponent);
        holder.channel.setText(game.channel);
        holder.time.setText(game.time);

        return row;
    }

    @Override
    public int getViewTypeCount() {
        return ScheduleGame.SCHEDULE_TYPES;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    static class ScheduleGameHolder {
        TextView date, opponent, channel, time;
    }
}
