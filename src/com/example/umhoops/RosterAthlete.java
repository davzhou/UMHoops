package com.example.umhoops;

public class RosterAthlete {
    private final static String BASE_URL = "http://www.umhoops.com";
    public String no, ht, wt, hs, name, year, pos, link;

    public RosterAthlete(String no, String name, String ht, String wt, String pos, String year, String hs) {
        this.no = no;
        this.name = name;
        this.ht = ht;
        this.wt = wt;
        this.pos = pos;
        this.year = year;
        this.hs = hs;
        this.link = null;
    }

    public RosterAthlete(String no, String name, String ht, String wt, String pos, String year, String hs, String link) {
        this.no = no;
        this.name = name;
        this.ht = ht;
        this.wt = wt;
        this.pos = pos;
        this.year = year;
        this.hs = hs;
        if (link.indexOf(BASE_URL) == -1) {
            this.link = BASE_URL + link;
        } else {
            this.link = link;
        }

    }

}
