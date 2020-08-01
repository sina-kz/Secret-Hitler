package com.example.secretHitler.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.secretHitler.Enums.Team;

import java.util.ArrayList;

public class Player implements Parcelable {
    private String name;
    private Team team;
    private boolean isHitler;
    private boolean isActive;
    private boolean won;

    public Player() {
        isHitler = false;
        isActive = true;
        won = false;
    }

    protected Player(Parcel in) {
        name = in.readString();
        team = Team.valueOf(in.readString());
        isHitler = in.readByte() != 0;
        isActive = in.readByte() != 0;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isHitler() {
        return isHitler;
    }

    public void setHitler(boolean hitler) {
        isHitler = hitler;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(team.name());
        parcel.writeByte((byte) (isHitler ? 1 : 0));
        parcel.writeByte((byte) (isActive ? 1 : 0));
    }

    public String teammatesString(ArrayList<Player> playerTeammates) {
        StringBuilder teammates = new StringBuilder();
        if (this.getTeam() == Team.LIBERAL) {
            return teammates.toString();
        }
        if (this.isHitler) {
            teammates.append("تو هیتلر هستی!");
            return teammates.toString();
        }
        Player hitler = new Player();
        teammates.append("یاران تو");
        String prefix = "";
        for (Player player : playerTeammates) {
            if (player.isHitler)
                hitler = player;
            teammates.append(prefix);
            prefix = "،";
            teammates.append(" ").append(player.getName());
        }
        teammates.append(" هستند.\n");
        teammates.append("هیتلر ").append(hitler.getName()).append(" هست!");
        return teammates.toString();
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        won = won;
    }
}