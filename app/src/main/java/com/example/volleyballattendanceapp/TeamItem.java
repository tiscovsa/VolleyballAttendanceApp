package com.example.volleyballattendanceapp;

public class TeamItem {
    private String teamName;
    private String sportName;

    public String getTeamName() { return teamName; }

    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getSportName() { return sportName; }

    public void setSportName(String sportName) { this.sportName = sportName; }

    public TeamItem(String teamName,String sportName){
        this.teamName = teamName;
        this.sportName = sportName;
    }

}
