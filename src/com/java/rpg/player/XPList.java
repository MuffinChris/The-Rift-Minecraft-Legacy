package com.java.rpg.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XPList {

    private Map<String, Double> dmg;

    public XPList() {
        dmg = new HashMap<>();
    }

    public void removeDc() {
        List<String> rem = new ArrayList<>();
        for (String s : dmg.keySet()) {
            if (Bukkit.getPlayer(s) == null) {
                rem.add(s);
            }
        }
        for (String s : rem) {
            dmg.remove(s);
        }
    }

    public Map<String, Double> getDmg() {
        return dmg;
    }

    public void addDamage(Player p, double d) {
        removeDc();
        if (dmg.containsKey(p.getName())) {
            dmg.replace(p.getName(), d + dmg.get(p.getName()));
        } else {
            dmg.put(p.getName(), d);
        }
    }

    public Map<Player, Double> getPercentages() {
        removeDc();
        Map<Player, Double> per = new HashMap<>();
        double total = 0;
        for (Double d : dmg.values()) {
            total+=d;
        }
        for (String s : dmg.keySet()) {
            if (per.containsKey(Bukkit.getPlayer(s))) {
                per.replace(Bukkit.getPlayer(s), per.get(Bukkit.getPlayer(s)) + dmg.get(s));
            } else {
                if (Bukkit.getPlayer(s) instanceof Player) {
                    per.put(Bukkit.getPlayer(s), dmg.get(s));
                }
            }
        }

        for (Player p : per.keySet()) {
            per.replace(p, per.get(p)/total);
        }
        return per;
    }

    public String getIndivPer(Player p) {
        removeDc();
        Map<Player, Double> per = getPercentages();
        DecimalFormat dF = new DecimalFormat("#.##");
        for (Player pl : per.keySet()) {
            if (pl.equals(p)) {
                return dF.format(per.get(pl) * 100) + "%";
            }
        }
        return "0%";
    }

    public void scrub() {
        dmg.clear();
    }

}
