package com.example.secretHitler.Models;

import com.example.secretHitler.Enums.PolicyState;
import com.example.secretHitler.Enums.Team;

public class PolicyCard {
    private Team type;
    private PolicyState state;

    public PolicyCard(Team type) {
        this.type = type;
        this.state = PolicyState.UNUSED;
    }

    public Team getType() {
        return type;
    }

    public void setType(Team type) {
        this.type = type;
    }

    public PolicyState getState() {
        return state;
    }

    public void setState(PolicyState state) {
        this.state = state;
    }
}
