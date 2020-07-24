package com.example.secretHitler.Models;

import com.example.secretHitler.Enums.PolicyState;
import com.example.secretHitler.Enums.Team;
import com.example.secretHitler.Utils.Numbers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameMethods {
    public static Player president;
    public static Player Chancellor;
    public static ArrayList<PolicyCards> skipped;

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

    public static ArrayList<PolicyCards> initializePolicies() {
        ArrayList<PolicyCards> policies = new ArrayList<>();
        for (int i = 0; i < Numbers.numberOfLiberalPolicies; i++) {
            PolicyCards policy = new PolicyCards(Team.LIBERAL);
            policies.add(policy);
        }
        for (int i = 0; i < Numbers.numberOfFascistPolicies; i++) {
            PolicyCards policy = new PolicyCards(Team.FASCIST);
            policies.add(policy);
        }
        Collections.shuffle(policies);
        skipped = new ArrayList<>();
        return policies;
    }

    public static ArrayList<Player> teammatesToBeShown(ArrayList<Player> players, Player player) {
        ArrayList<Player> teammates = new ArrayList<>();
        if (player.getTeam() == Team.FASCIST && !player.isHitler()) {
            for (Player otherPlayer : players) {
                if (player != otherPlayer && player.getTeam() == otherPlayer.getTeam()) {
                    teammates.add(otherPlayer);
                }
            }
            return teammates;
        }
        return null;
    }

    public static ArrayList<PolicyCards> activePolicies(ArrayList<PolicyCards> policyCards) {
        ArrayList<PolicyCards> newPolicyCards = new ArrayList<>();
        for (PolicyCards policyCard : policyCards) {
            if (policyCard.getState() == PolicyState.UNUSED) {
                newPolicyCards.add(policyCard);
            }
        }
        return newPolicyCards;
    }

    public static void usePolicy(ArrayList<PolicyCards> policyCards, PolicyCards policyCard) {
        policyCard.setState(PolicyState.USED);
        policyCards.remove(policyCard);
    }

    public static void skipPolicy(PolicyCards policyCard) {
        policyCard.setState(PolicyState.PENDING);
        skipped.add(policyCard);
    }

    public static ArrayList<PolicyCards> reorderPolicies(ArrayList<PolicyCards> policyCards) {
        policyCards.addAll(skipped);
        skipped.clear();
        Collections.shuffle(policyCards);
        return policyCards;
    }

    public static void assignRoles(ArrayList<Player> players, int numOfLiberals, int numOfFascists) {
        int i = 0;
        int j = 0;
        int counter = 0;
        double limit = (double) numOfLiberals / (numOfLiberals + numOfFascists);
        while (i != numOfLiberals && j != numOfFascists) {
            double random = Math.random();
            Player player = players.get(counter);
            if (random < limit) {
                player.setTeam(Team.LIBERAL);
                i++;
            } else {
                player.setTeam(Team.FASCIST);
                j++;
            }
            counter++;
        }
        if (i == numOfLiberals) {
            for (; counter < numOfLiberals + numOfFascists; counter++) {
                players.get(counter).setTeam(Team.FASCIST);
            }
        } else {
            for (; counter < numOfLiberals + numOfFascists; counter++) {
                players.get(counter).setTeam(Team.LIBERAL);
            }
        }
        ArrayList<Player> fascists = fascistsTeam(players);
        Random r = new Random();
        int hitlerIndex = r.nextInt(fascists.size());
        Player hitler = fascists.get(hitlerIndex);
        for (Player player : players) {
            if (player.getName().equals(hitler.getName())) {
                player.setHitler(true);
            }
        }
    }

    public static ArrayList<Player> fascistsTeam(ArrayList<Player> players) {
        ArrayList<Player> fascists = new ArrayList<>();
        for (Player player : players) {
            if (player.getTeam() == Team.FASCIST) {
                fascists.add(player);
            }
        }
        return fascists;
    }

    public static ArrayList<Player> liberalsTeam(ArrayList<Player> players) {
        ArrayList<Player> liberals = new ArrayList<>();
        for (Player player : players) {
            if (player.getTeam() == Team.LIBERAL) {
                liberals.add(player);
            }
        }
        return liberals;
    }
}
