package com.example.volleyballattendanceapp;

public class TeamItem {
    private String teamName;
    private String sportName;
    private long tid;

    public TeamItem(long tid, String teamName, String sportName) {
        this.teamName = teamName;
        this.sportName = sportName;
        this.tid = tid;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTeamName() { return teamName; }

    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getSportName() { return sportName; }

    public void setSportName(String sportName) { this.sportName = sportName; }

    public TeamItem(String teamName,String sportName){
        this.teamName = teamName;
        this.sportName = sportName;
    }

}
