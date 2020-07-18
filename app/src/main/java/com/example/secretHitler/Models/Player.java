package com.example.secretHitler.Models;

import com.example.secretHitler.Enums.Role;
import com.example.secretHitler.Enums.Team;

public class Player {
    private String name;
    private Team team;
    private boolean isHitler;
    private Role role;

    public Player(String name) {
        this.name = name;
        this.isHitler = false;
        this.role = Role.SOLDIER;
    }

    public String getName() {
        return name;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isPresident(){
        return this.role == Role.PRESIDENT;
    }

    public boolean isChancellor(){
        return this.role == Role.CHANCELLOR;
    }
}
