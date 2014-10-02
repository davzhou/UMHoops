package com.example.umhoops;

public class DraftAthlete {
    private final static String FULL_URL = "http://";
    private final static String BASE_URL = "http://www.umhoops.com";
    public String year, rnd, pick, name, team, link;

    public DraftAthlete(String year, String rnd, String pick, String name, String team, String link) {
//        if (year.contains("1") || year.contains("2")) {
//            this.year = year;
//        } else {
//            this.year = "";
//        }
        this.year = year;
        this.rnd = rnd;
        this.pick = pick;
        this.name = name;
        this.team = team;
        if (link.indexOf(FULL_URL) == -1) {
            this.link = BASE_URL + link;
        } else {
            this.link = link;
        }
    }

    public DraftAthlete(String year, String rnd, String pick, String name, String team) {
        this.year = year;
        this.rnd = rnd;
        this.pick = pick;
        this.name = name;
        this.team = team;
    }

}
