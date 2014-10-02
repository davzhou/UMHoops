package com.example.umhoops;

public class ScheduleGame {
    String date, opponent, channel, time;
    int type;

    static public final int SCHEDULE_GAME = 0;
    static public final int SCHEDULE_LABEL = 1;
    static public final int SCHEDULE_B10 = 2;
    static public final int SCHEDULE_TYPES = 3; // type count

    public ScheduleGame(int type, String date, String opponent, String channel, String time) {
        this.type = type;
        this.date = date;
        this.opponent = opponent;
        this.channel = channel;
        this.time = time;
    }

    public ScheduleGame() {
        this.type = SCHEDULE_B10;
    }

}
