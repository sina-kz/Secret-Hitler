package com.example.secretHitler.Models;

import com.example.secretHitler.Enums.PolicyState;
import com.example.secretHitler.Enums.Team;
import com.example.secretHitler.Utils.Numbers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameMethods {
    private static Player currentPresident;
    private static Player currentChancellor;
    private static Player previousPresident;
    private static Player previousChancellor;
    private static int presidentPointer = 0;
    private static int numberOfRejects;
    public static ArrayList<PolicyCard> skippedPolicies;
    private static ArrayList<PolicyCard> allPolicies;
    private static int numberOfFascistsUsedPolicies = 0;
    private static int numberOfLiberalsUsedPolicies = 0;
    private static boolean firstTimeCreated = true;
    private static ArrayList<Player> allPlayers;
    private static boolean vetoEnable;
    private static boolean liberalsWon;

    public static ArrayList<Player> activePlayers(ArrayList<Player> players) {
        ArrayList<Player> newPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isActive()) {
                newPlayers.add(player);
            }
        }
        return newPlayers;
    }

    public static void kill(Player player) {
        player.setActive(false);
    }

    public static void initializePolicies() {
        ArrayList<PolicyCard> policies = new ArrayList<>();
        for (int i = 0; i < Numbers.numberOfLiberalPolicies; i++) {
            PolicyCard policy = new PolicyCard(Team.LIBERAL);
            policies.add(policy);
        }
        for (int i = 0; i < Numbers.numberOfFascistPolicies; i++) {
            PolicyCard policy = new PolicyCard(Team.FASCIST);
            policies.add(policy);
        }
        Collections.shuffle(policies);
        skippedPolicies = new ArrayList<>();
        allPolicies = policies;
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

    public static ArrayList<PolicyCard> activePolicies(ArrayList<PolicyCard> policyCards) {
        ArrayList<PolicyCard> newPolicyCards = new ArrayList<>();
        for (PolicyCard policyCard : policyCards) {
            if (policyCard.getState() == PolicyState.UNUSED) {
                newPolicyCards.add(policyCard);
            }
        }
        return newPolicyCards;
    }

    public static ArrayList<PolicyCard> usedPolicies(ArrayList<PolicyCard> policyCards) {
        ArrayList<PolicyCard> usedPolicyCards = new ArrayList<>();
        for (PolicyCard policyCard : policyCards) {
            if (policyCard.getState() == PolicyState.USED) {
                usedPolicyCards.add(policyCard);
            }
        }
        return usedPolicyCards;
    }

    public static void usePolicy(PolicyCard policyCard) {
        policyCard.setState(PolicyState.USED);
        numberOfRejects = 0;
        if (policyCard.getType() == Team.LIBERAL)
            numberOfLiberalsUsedPolicies++;
        else
            numberOfFascistsUsedPolicies++;
    }

    public static void skipPolicy(PolicyCard policyCard) {
        policyCard.setState(PolicyState.PENDING);
        skippedPolicies.add(policyCard);
    }

    public static ArrayList<PolicyCard> reorderPolicies(ArrayList<PolicyCard> policyCards) {
        for (PolicyCard policyCard : skippedPolicies) {
            policyCard.setState(PolicyState.UNUSED);
        }
        policyCards.addAll(skippedPolicies);
        skippedPolicies.clear();
        Collections.shuffle(policyCards);
        return policyCards;
    }

    public static void assignTeams(ArrayList<Player> players, int numOfLiberals, int numOfFascists) {
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

    public static ArrayList<PolicyCard> pickThreePolicies(ArrayList<PolicyCard> activePolicies) {
        ArrayList<PolicyCard> pickedPolicies = new ArrayList<>();
        for (int i = 0; i < Numbers.accessibleNumberOfPolicies; i++) {
            pickedPolicies.add(activePolicies.get(i));
        }
        return pickedPolicies;
    }

    public static boolean checkWinStateForLiberals(ArrayList<Player> players) {
        if (numberOfLiberalsUsedPolicies == Numbers.liberalPoliciesToWin) {
            return true;
        }
        Player hitler = new Player();
        for (Player player : players) {
            if (player.isHitler()) {
                hitler = player;
                break;
            }
        }
        return !hitler.isActive();
    }

    public static boolean checkWinStateForFascists() {
        if (numberOfFascistsUsedPolicies == Numbers.fascistsPoliciesToWin) {
            return true;
        }
        return numberOfFascistsUsedPolicies >= Numbers.fascistsPolicesToActiveHitler && currentChancellor.isHitler();
    }

    public static void initializePresident(ArrayList<Player> players) {
        presidentPointer = new Random().nextInt(players.size());
        currentPresident = players.get(presidentPointer);
    }

    public static Player assignChancellor(ArrayList<Player> players, Player player) {
        if (players.size() > Numbers.minNumberOfPlayers) {
            if (previousChancellor == player || previousPresident == player) {
                return null;
            }
        } else if (previousChancellor == player)
            return null;
        currentChancellor = player;
        return player;
    }

    public static void completeAssignChancellor() {
        previousChancellor = currentChancellor;
        previousPresident = currentPresident;
    }

    public static void nextPresident(ArrayList<Player> players) {
        if (presidentPointer >= players.size()) {
            presidentPointer = 0;
            System.out.println("NIMA");
        } else
            presidentPointer = (presidentPointer + 1) % players.size();
        currentPresident = players.get(presidentPointer);
    }

    public static void nextPresidentWhenKill(ArrayList<Player> activePlayers) {
        currentPresident = activePlayers.get(presidentPointer % activePlayers.size());
    }

    public static void selectPresident(Player player) {
        currentPresident = player;
        presidentPointer = (presidentPointer - 1) % activePlayers(allPlayers).size();
    }

    public static Team askPlayersTeam(Player player) {
        return player.getTeam();
    }

    public static Player getCurrentPresident() {
        return currentPresident;
    }

    public static Player getCurrentChancellor() {
        return currentChancellor;
    }

    public static Player getPreviousPresident() {
        return previousPresident;
    }

    public static Player getPreviousChancellor() {
        return previousChancellor;
    }

    public static ArrayList<Player> otherPlayers(ArrayList<Player> players, Player player) {
        ArrayList<Player> others = new ArrayList<>();
        for (Player otherPlayer : players) {
            if (otherPlayer != player) {
                others.add(otherPlayer);
            }
        }
        return others;
    }

    public static void useVetoPower(ArrayList<PolicyCard> policyCards) {
        skipPolicy(policyCards.get(0));
        skipPolicy(policyCards.get(1));
    }

    public static void usePresidentPower(String power, Player selectedPlayer, ArrayList<PolicyCard> activePolicies, ArrayList<PolicyCard> peekPolicies, Team selectedPlayerTeam) {
        switch (power) {
            case "EXECUTE":
                kill(selectedPlayer);
                break;
            case "INVESTIGATE":
                selectedPlayerTeam = askPlayersTeam(selectedPlayer);
                break;
            case "ELECTION":
                selectPresident(selectedPlayer);
                break;
            case "POLICY_PEEK":
                peekPolicies = pickThreePolicies(activePolicies);
                break;
        }
    }

    public static PolicyCard threeRejectsPolicy(ArrayList<Player> activePlayers, ArrayList<PolicyCard> activePolicies) {
        numberOfRejects++;
        if (numberOfRejects < Numbers.maxNumberOfRejectedPresidents) {
            return null;
        }
        numberOfRejects = 0;
        return activePolicies.get(0);
    }

    public static ArrayList<Player> assignableChancellors(ArrayList<Player> activePlayers) {
        ArrayList<Player> others = otherPlayers(activePlayers, currentPresident);
        if (activePlayers.size() > Numbers.minNumberOfPlayers) {
            try {
                others.remove(previousPresident);
                others.remove(previousChancellor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                others.remove(previousChancellor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return others;
    }

    public static ArrayList<PolicyCard> getAllPolicies() {
        return allPolicies;
    }

    public static void setAllPolicies(ArrayList<PolicyCard> allPolicies) {
        GameMethods.allPolicies = allPolicies;
    }

    public static boolean isFirstTimeCreated() {
        return firstTimeCreated;
    }

    public static void setFirstTimeCreated(boolean firstTimeCreated) {
        GameMethods.firstTimeCreated = firstTimeCreated;
    }

    public static int getNumberOfFascistsUsedPolicies() {
        return numberOfFascistsUsedPolicies;
    }

    public static int getNumberOfLiberalsUsedPolicies() {
        return numberOfLiberalsUsedPolicies;
    }

    public static ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }

    public static void setAllPlayers(ArrayList<Player> allPlayers) {
        GameMethods.allPlayers = allPlayers;
    }

    public static boolean isVetoEnable() {
        return vetoEnable;
    }

    public static void setVetoEnable(boolean vetoEnable) {
        GameMethods.vetoEnable = vetoEnable;
    }

    public static boolean isLiberalsWon() {
        return liberalsWon;
    }

    public static void setLiberalsWon(boolean liberalsWon) {
        GameMethods.liberalsWon = liberalsWon;
    }

    public static int getPresidentPointer() {
        return presidentPointer;
    }

    public static void reinitialize() {
        currentPresident = null;
        currentChancellor = null;
        previousPresident = null;
        previousChancellor = null;
        presidentPointer = 0;
        numberOfRejects = 0;
        skippedPolicies = null;
        allPolicies = null;
        numberOfFascistsUsedPolicies = 0;
        numberOfLiberalsUsedPolicies = 0;
        firstTimeCreated = true;
        allPlayers = null;
        vetoEnable = false;
        liberalsWon = true;
    }
}
