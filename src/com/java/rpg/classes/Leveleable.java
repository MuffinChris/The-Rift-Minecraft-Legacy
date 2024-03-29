package com.java.rpg.classes;

import com.destroystokyo.paper.Title;
import com.java.Main;
import com.java.rpg.holograms.Hologram;
import com.java.rpg.classes.utility.RPGConstants;
import com.java.rpg.party.Party;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Leveleable {

    private int level;
    private int maxlevel;
    private double exp;
    private double maxexp;

    // Equation
    private double expMod;
    private double expOff;
    private double expPow;

    private Player rp;

    private Main main = Main.getInstance();

    public Leveleable(int level, int maxlevel, Player rp) {
        this.level = level;
        this.maxlevel = maxlevel;
        this.rp = rp;
        expMod = RPGConstants.expMod;
        expOff = RPGConstants.expOff;
        expPow = RPGConstants.expPow;
    }

    public Leveleable() {
        this.level = 0;
        this.maxlevel = RPGConstants.maxLevel;
        this.rp = null;
        expMod = RPGConstants.expMod;
        expOff = RPGConstants.expOff;
        expPow = RPGConstants.expPow;
    }

    public void scrub() {
        rp = null;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxlevel;
    }

    public double getExp() {
        return exp;
    }

    public double getMaxExp() {
        return maxexp;
    }

    public void calcMaxExp() {
        //maxexp = Math.pow(level, expPow) * expMod + expOff;
        maxexp = Math.ceil(Math.pow(3, (level + 70.0)/10.0) - 547.0);
        //maxexp = RPGConstants.levelsExp.get(level);
    }

    public double getPercent() {
        if (Double.isNaN(exp/maxexp)) {
            return 0;
        }
        return exp / maxexp;
    }

    public String getPrettyPercent() {
        DecimalFormat dF = new DecimalFormat("#.##");
        return dF.format((100.0D * exp) / maxexp);
    }

    public String getPrettyExp() {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(exp);
    }

    public String getPrettyMaxExp() {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(maxexp);
    }

    public void levelup() {
        calcMaxExp();
        int cnt = 0;
        int lvl = level;
        while (exp >= maxexp && level < maxlevel) {
            level++;
            exp = exp - maxexp;
            calcMaxExp();
            cnt++;
        }
        if (cnt > 0) {
            levelupRewards(rp, main.getRP(rp).getPClass(), lvl, lvl + cnt);
        }
    }

    public void levelupRewards(Player p, PlayerClass playerclass, int oldlvl, int newlvl) {

        String sign = "+";
        if (newlvl < oldlvl) {
            sign = "";
        }

        DecimalFormat dF =  new DecimalFormat("#.##");
        main.getRP(p).getBoard().setInfoBossbar("&e&lLEVEL UP &7- &6" + playerclass.getName() + " &7(&f" + (oldlvl) + " &7-> &f" + newlvl + "&7)");
        Main.msg(p, "");
        Main.msg(p, "&e&lLEVEL UP &7- &6" + playerclass.getName() + " &7(&f" + (oldlvl) + " &7-> &f" + newlvl + "&7)");
        Main.msg(p, "");
        //Main.msg(p, "&e&lSTAT INCREASES:");
        int dif = newlvl - oldlvl;
        Main.msg(p, "&8» &7" + sign + dF.format(playerclass.getHpPerLevel() * dif) + " &7HP");
        Main.msg(p, "&8» &7" + sign + dF.format(playerclass.getManaPerLevel() * dif) + " &7M &8| " + "&7" + sign + dF.format(playerclass.getManaRegenPerLevel() * dif) + " &7M/s");
        Main.msg(p, "&8» &7" + sign + dF.format(playerclass.getArmorPerLevel() * dif) + " &7A &8| " + "&7" + sign + dF.format(playerclass.getMagicResistPerLevel() * dif) + " &7MR");
        Main.msg(p, "&8» &7" + sign + dF.format(playerclass.getADPerLevel() * dif) + " &7AD &8| " + "&7" + sign + dF.format(playerclass.getAPPerLevel() * dif) + " &7AP");

        /*int cnt = 0;
        while (dif > 0) {
            dif--;
            if (dif >= 0) {
                cnt+=RPGConstants.spPerLevel;
            }
        }

        if (cnt > 0) {
            Main.msg(p, "&8» &7+" + cnt + " &7SKILL POINT");
            main.getRP(p).setSP(main.getRP(p).getSP() + cnt);
        }*/

        if (newlvl >= RPGConstants.superSkillOne && oldlvl < RPGConstants.superSkillOne && newlvl < RPGConstants.superSkillTwo) {
            Main.msg(p, "&8» &7+" + 1 + " &7SKILL UPGRADE POINT");
        }
        if (newlvl >= RPGConstants.superSkillTwo && oldlvl < RPGConstants.superSkillOne) {
            Main.msg(p, "&8» &7+" + 2 + " &7SKILL UPGRADE POINT");
        }
        if (newlvl >= RPGConstants.superSkillTwo && oldlvl < RPGConstants.superSkillTwo && oldlvl >= RPGConstants.superSkillOne) {
            Main.msg(p, "&8» &7+" + 1 + " &7SKILL UPGRADE POINT");
        }

        Main.msg(p, "");
        p.sendTitle(new Title(Main.color("&e&lLEVEL UP!"), Main.color("&6" + (oldlvl) + " &f-> &6" + newlvl), 5, 40, 5));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
        main.getRP(p).pushFiles();
    }

    public void setLevel(int l) {
        level = l;
        levelup();
    }

    public void setExp(double e) {
        exp = e;
        levelup();
    }

    public void giveExpFromSource(Player p, Location t, double xp, String s) {
        if (!Double.isNaN(xp) && Double.isFinite(xp)) {
            String flavor = " &7(" + s + "&7)";
            String sign = "+";
            if (xp < 0) {
                sign = "";
            }
            if (s.length() == 0) {
                flavor = "";
            } else {
                if (s.equals("SELF")) {
                    flavor = "";
                }
                if (main.getPM().hasParty(p) && xp >= 0) {
                    Party pa = main.getPM().getParty(p);
                    int amnt = pa.getNearbyPlayersSize(p);
                    if (pa.getShare() && amnt > 0) {
                        xp *= RPGConstants.partyXpMod;
                        xp /= ((amnt + 1) * 1.0);
                        for (Player pp : pa.getNearbyPlayers(p)) {
                            RPGPlayer rp = main.getRP(pp);
                            rp.setExp(rp.getExp() + xp);
                            DecimalFormat dF = new DecimalFormat("#");
                            if (pp.equals(p)) {
                                Main.msg(pp, "   &7[+" + dF.format(xp) + "&7 XP]");
                                rp.getBoard().setInfoBossbar("&7[+" + dF.format(xp) + "&7 XP]");
                            } else {
                                Main.msg(pp, "   &7[+" + dF.format(xp) + "&7 XP]" + " &7(" + p.getName() + "&7)");
                                rp.getBoard().setInfoBossbar("&7[+" + dF.format(xp) + "&7 XP]" + " &7(" + p.getName() + "&7)");
                            }
                        }
                        return;
                    }
                }
            }
            exp += xp;
            exp = Math.max(exp, 0);
            DecimalFormat dF = new DecimalFormat("#");
            if (main.getRP(p).getSendExp()) {
                Main.msg(p, "   &7[" + sign + dF.format(xp) + "&7 XP]" + flavor);
            }
            main.getRP(p).getBoard().setInfoBossbar("&7[" + sign + dF.format(xp) + "&7 XP]" + flavor);
            Hologram magic = new Hologram(p, t, "&7[" + sign + dF.format(xp) + " XP] &7(&f" + p.getName() + "&7)", Hologram.HologramType.EXP, new ArrayList<>(Arrays.asList(p)));
            levelup();
        }
    }

}
