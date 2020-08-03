package com.example.secretHitler.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.secretHitler.Enums.PolicyState;
import com.example.secretHitler.Enums.Team;

public class PolicyCard implements Parcelable {
    private Team type;
    private PolicyState state;

    public PolicyCard(Team type) {
        this.type = type;
        this.state = PolicyState.UNUSED;
    }

    protected PolicyCard(Parcel in) {
        state = PolicyState.valueOf(in.readString());
        type = Team.valueOf(in.readString());
    }

    public static final Creator<PolicyCard> CREATOR = new Creator<PolicyCard>() {
        @Override
        public PolicyCard createFromParcel(Parcel in) {
            return new PolicyCard(in);
        }

        @Override
        public PolicyCard[] newArray(int size) {
            return new PolicyCard[size];
        }
    };

    public Team getType() {
        return type;
    }

    public PolicyState getState() {
        return state;
    }

    public void setState(PolicyState state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(state.name());
        parcel.writeString(type.name());
    }
}
