package com.example.secretHitler.Models;

import java.util.ArrayList;

public class GameMethods {
    public static Player president;
    public static Player Chancellor;

    public static ArrayList<Player> activePlayers(ArrayList<Player> players) {
        ArrayList<Player> newPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isActive()) {
                newPlayers.add(player);
            }
        }
        return newPlayers;
    }

    public static void kill(ArrayList<Player> players, String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                player.setActive(false);
            }
        }
    }

//    public static ArrayList<PolicyCards> initializePolicies() {
//        ArrayList<PolicyCards> policies = new ArrayList<>();
//
//    }

}
