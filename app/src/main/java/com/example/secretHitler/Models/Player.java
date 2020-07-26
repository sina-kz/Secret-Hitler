package com.example.secretHitler.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.secretHitler.Enums.Role;
import com.example.secretHitler.Enums.Team;

public class Player implements Parcelable {
    private String name;
    private Team team;
    private boolean isHitler;
    private boolean isActive;
    private Role role;

    public Player() {
    }

    public Player(String name, Team team, boolean isHitler, boolean isActive, Role role) {
        this.name = name;
        this.team = team;
        this.isHitler = isHitler;
        this.isActive = isActive;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
}