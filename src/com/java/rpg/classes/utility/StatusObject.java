package com.java.rpg.classes.utility;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StatusObject {

    private String name;
    private String flavor;

    private List<StatusValue> statuses;

    public boolean active() {
        return (!statuses.isEmpty());
    }

    public String getName() {
        return name;
    }

    public String getFlavor() {
        return flavor;
    }

    public int getValue() {
        int value = 0;
        for (StatusValue s : statuses) {
            value+=s.getValue();
        }
        return value;
    }

    public boolean allDurationless() {
        for (StatusValue sv : statuses) {
            if (!sv.getDurationless()) {
                return false;
            }
        }
        return true;
    }

    public int ticksMore() {
        int ticks = 0;
        for (StatusValue s : statuses) {
            if (!s.getDurationless()) {
                int ticksleft = s.getDuration() - Integer.valueOf(String.valueOf(Math.round(20 * 0.001 * (System.currentTimeMillis() - s.getTimestamp()))));
                ticks += ticksleft;
            }
        }
        return ticks;
    }

    public List<StatusValue> clearbasedtitle;

    public List<StatusValue> getCBT() {
        return clearbasedtitle;
    }

    public void clearBasedTitle(String title, Player p) {
        for (StatusValue s : statuses) {
            if (s.getSource().equals(title + ":" + p.getName())) {
                    //remove.add(s);
                clearbasedtitle.add(s);
            }
        }
    }

    public void clearBasedTitleDurationless(String title, Player p) {
        //List<StatusValue> remove = new ArrayList<>();
        for (StatusValue s : statuses) {
            if (s.getDurationless()) {
                if (s.getSource().equals(title + ":" + p.getName())) {
                    //remove.add(s);
                    clearbasedtitle.add(s);
                }
            }
        }
        /*
        for (StatusValue rem : clearbasedtitle) {//remove) {
            statuses.remove(rem);
        }*/
    }

    private boolean silent;

    public boolean getSilent() {
        return silent;
    }

    public StatusObject(String name, String flavor, boolean silent) {
        this.name = name;
        this.flavor = flavor;
        this.silent = silent;
        clearbasedtitle = new ArrayList<>();
        statuses = new ArrayList<>();
    }

    public List<StatusValue> getStatuses() {
        return statuses;
    }

    public void scrub() {
        name = null;
        if (statuses != null) {
            for (StatusValue s : statuses) {
                s.scrub();
            }
        }
        statuses = null;
        //clearbasedtitle.clear();
        clearbasedtitle = null;
    }

}
