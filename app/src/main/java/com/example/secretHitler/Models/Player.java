package com.example.secretHitler.Models;

import com.example.secretHitler.Enums.Role;
import com.example.secretHitler.Enums.Team;

public class Player {
    private String name;
    private Team team;
    private boolean isHitler;
    private boolean isActive;
    private boolean isNewPresident;
    private boolean previousPresident;
    private boolean previousChancellor;
    private Role role;

    public Player() {
    }

    public Player(String name, Team team, boolean isHitler, boolean isActive, Role role) {
        this.name = name;
        this.team = team;
        this.isHitler = isHitler;
        this.isActive = isActive;
        this.role = role;
        this.isNewPresident = false;
        this.previousChancellor = false;
        this.previousPresident = false;
    }

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

    public boolean isNewPresident() {
        return isNewPresident;
    }

    public void setNewPresident(boolean newPresident) {
        isNewPresident = newPresident;
    }

    public boolean isPreviousPresident() {
        return previousPresident;
    }

    public void setPreviousPresident(boolean previousPresident) {
        this.previousPresident = previousPresident;
    }

    public boolean isPreviousChancellor() {
        return previousChancellor;
    }

    public void setPreviousChancellor(boolean previousChancellor) {
        this.previousChancellor = previousChancellor;
    }
}