package com.java.rpg.party;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Party {

    private Player leader;
    private List<Player> players;
    private boolean pvp;
    private boolean share;
    private int shareradius = 64;

    private Main main = Main.getInstance();

    public int getShareRadius() {
        return shareradius;
    }

    public Party(Player p) {
        leader = p;
        players = new ArrayList<Player>();
        players.add(p);
        pvp = false;
        share = true;
    }

    public int getNearbyPlayersSize(Player p) {
        int total = 0;
        for (Player pp : getPlayers()) {
            if (p.getWorld().getName().equals(pp.getWorld().getName()) && p.getLocation().distance(pp.getLocation()) <= shareradius) {
                if (!pp.equals(p)) {
                    total++;
                }
            }
        }
        return total;
    }

    public List<Player> getNearbyPlayers(Player p) {
        List<Player> pls = new ArrayList<>();
        for (Player pp : getPlayers()) {
            if (p.getLocation().distance(pp.getLocation()) <= shareradius) {
                pls.add(pp);
            }
        }
        return pls;
    }

    public void addPlayer(Player p) {
        if (players.size() > 34) {
            sendMessage("&8» &e&l" + p.getName() + " &ftried to join the party, but the party is full.");
            return;
        }
        players.add(p);
        sendMessage("&8» &e&l" + p.getName() + " &fhas joined the party!");
    }

    public void removePlayer(Player p) {
        sendMessage("&8» &e&l" + p.getName() + " &fhas left the party!");
        if (p.equals(leader)) {
            disband();
        }
        players.remove(p);
    }

    public void kickPlayer(Player p) {
        sendMessage("&8» &e&l" + p.getName() + " &fhas been kicked from the party!");
        if (p.equals(leader)) {
            disband();
        }
        players.remove(p);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPvp(boolean p) {
        pvp = p;
    }

    public void setShare(boolean p) {
        share = p;
    }

    public boolean getPvp() {
        return pvp;
    }

    public boolean getShare() {
        return share;
    }

    public void disband() {
        sendMessage("&8» &e&l" + leader.getName() + " &fhas disbanded the party!");
        for (Player p : players) {
            main.getRP(p).setChatChannel(RPGPlayer.ChatChannel.Global);
        }
        leader = null;
        players = null;
    }

    public void sendMessage(String s) {
        for (Player p : players) {
            p.sendMessage(Main.color(s));
        }
    }

    public boolean hasPlayer(Player p) {
        return players.contains(p);
    }

    public Player getLeader() {
        return leader;
    }

}
