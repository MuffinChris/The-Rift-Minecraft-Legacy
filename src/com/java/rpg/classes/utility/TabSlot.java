package com.java.rpg.classes.utility;

import org.bukkit.entity.Player;

public class TabSlot {

    private int tl;
    private Player player;
    private String prefix;
    private String suffix;

    public TabSlot(int tl, Player player, String prefix, String suffix) {
        this.tl = tl;
        this.player = player;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public int getTl() {
        return tl;
    }

    public Player getPlayer() {
        return player;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void wipe() {
        player = null;
        prefix = null;
        suffix = null;
    }

    public void put(Player p, String prefix, String suffix) {
        player = p;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public void setPrefix(String s) {
        prefix = s;
    }

    public void setSuffix(String s) {
        suffix = s;
    }

}
