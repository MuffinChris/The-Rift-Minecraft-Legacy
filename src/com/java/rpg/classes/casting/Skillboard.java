package com.java.rpg.classes.casting;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusObject;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Skillboard {

    private Main main = Main.getInstance();

    private UUID uuid;
    private BossBar skillCastingBar;
    private BossBar bossbarTwo_5;
    private BossBar statusesBossbar;
    private BossBar infoBossbar;
    private BossBar hpbarBossbar;
    private BossBar bossbarThree_6;
    private boolean skillbar;
    List<Integer> prefixes = new ArrayList<>();

    private Objective obj;

    public void setHPBar(String s) {
        if (hpbarBossbar != null) {
            hpbarBossbar.setTitle(Main.color(s));
        }
    }

    public String getHPBar() {
        return hpbarBossbar.getTitle();
    }

    public void setHPBarVisible() {
        hpbarBossbar.setColor(BarColor.RED);
    }

    public void setHPBarHide() {
        hpbarBossbar.setColor(BarColor.YELLOW);
    }

    public void setInfoBossbar(String s) {
        if (infoBossbar != null) {
            infoBossbar.setTitle(Main.color(s));
            new BukkitRunnable() {
                public void run() {
                    if (infoBossbar != null) {
                        infoBossbar.setTitle("");
                    }
                }
            }.runTaskLater(Main.getInstance(), 80L);
        }
    }

    public boolean getSkillbar() {
        return skillbar;
    }

    public void setSkillbar(boolean bool) {
        skillbar = bool;
    }

    public void toggleSkillbar() {
        skillbar = !skillbar;
    }

    public void statusUpdate() {
        String statuses = "";
        DecimalFormat dF = new DecimalFormat("#.00");
        for (StatusObject so : main.getRP(Bukkit.getPlayer(uuid)).getSo()) {
            if (so.active() && !so.getSilent()) {
                if (so.getName().equals("PStrength")) {
                    statuses += "&e" + so.getFlavor() + "&8: &6" + so.getValue() + "%&f, ";
                } else if (so.getName().equals("AP")) {
                    statuses += "&e" + so.getFlavor() + "&8: &6" + so.getValue() + "&f, ";
                } else if (so.getName().equals("Walkspeed")) {
                    statuses += "&e" + so.getFlavor() + "&8: &6" + so.getValue() + "&f, ";
                } else {
                    if (so.allDurationless()) {
                        statuses += "&e" + so.getFlavor() + "&f, ";
                    } else {
                        double ticks = Math.round((so.ticksMore()/20.0)*4f)/4f;
                        statuses += "&e" + so.getFlavor() + "&8: &6" + dF.format(ticks) + "s&f, ";
                    }
                }
            }
        }
        if (statuses.contains(",")) {
            statuses = statuses.substring(0, statuses.length() - 2);
        }

        statusesBossbar.setTitle(Main.color(statuses));
    }

    public void updateSkillbar() {
        if (skillbar) {
            Player p = Bukkit.getPlayer(uuid);
            String output = "";
            int slot = 1;
            List<Skill> pSkills = main.getRP(p).getSkillsCastable();
            for (Skill s : pSkills) {
                if (slot == main.getRP(p).getIdleSlot() + 1) {
                    slot++;
                }
                String name = s.getName();
                boolean nolevel = false;
                boolean cd = false;
                boolean manacost = false;
                if (main.getRP(p).getLevel() < s.getLevel()) {
                    name = "&c" + s.getName();
                    nolevel = true;
                } else if (main.getRP(p).getCooldown(s).contains("CD:")) {
                    name = "&c" + s.getName();
                    cd = true;
                } else if (main.getMana(p) < s.getManaCost()) {
                    name = "&c" + s.getName();
                    manacost = true;
                }
                output+="&e" + name;
                if (nolevel) {
                    output+=" &8<&cLv. " + s.getLevel() + "&8> || ";
                } else if (cd) {
                    output+=" &8<&f" + main.getRP(p).getCooldown(s).replace("CD:", "") + "s" + "&8> || ";
                } else if (manacost) {
                    output+=" &8<&b" + s.getManaCost() + " M" + "&8> || ";
                } else {
                    output+=" &8<&6" + slot + "&8> || ";
                }
                slot++;
            }
            if (output.contains("||")) {
                output = output.substring(0, output.length() - 4);
            }

            skillCastingBar.setTitle(Main.color(output));

            /*ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(Main.color("&aSkill Item"));
            meta.setLore(lore);
            meta.setDisplayName(Main.color(output));
            item.setItemMeta(meta);
            if (Bukkit.getPlayer(uuid).getInventory().getHeldItemSlot() == 0) {
                Bukkit.getPlayer(uuid).getInventory().setHeldItemSlot(8);
            } else {
                Bukkit.getPlayer(uuid).getInventory().setHeldItemSlot(0);
                Bukkit.getPlayer(uuid).getInventory().setItemInMainHand(item);
            }*/

        } else {
            skillCastingBar.setTitle("");
        }
    }

    public void setScoreBoard(Player player) {
        /*Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = board.registerNewObjective("ServerName", "dummy", Main.color("&e&lSkill Cooldowns"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score cds = obj.getScore(Main.color(""));
        cds.setScore(15);
        prefixes = new ArrayList<>();
        prefixes.add(9);
        prefixes.add(8);
        prefixes.add(7);
        prefixes.add(6);
        prefixes.add(5);
        prefixes.add(4);
        for (int i : prefixes) {
            Team skillOne = board.registerNewTeam("skill" + i);
            skillOne.addEntry(Main.color("&" + i));
            skillOne.setPrefix(Main.color(""));
            obj.getScore(Main.color("&" + i)).setScore(i);
        }

        player.setScoreboard(board);*/
    }

    public void updateScoreboard(Player p) {
        /*Scoreboard board = p.getScoreboard();
        for (int i = 9; i > 9 - main.getPC().get(p.getUniqueId()).getCooldowns().size(); i--) {
            int index = 9 - i;
            String name = main.getPC().get(p.getUniqueId()).getCooldowns().keySet().toArray()[index].toString();
            String output = "";
            output += "&e" + name + ": &f" + main.getPC().get(p.getUniqueId()).getCooldown(main.getPC().get(p.getUniqueId()).getSkillFromName(name)).replace("CD:", "") + "s";
            board.getTeam("skill" + i).setPrefix(Main.color(output));
            obj.getScore(Main.color("&" + i)).setScore(i);
        }
        for (int i = 9 - main.getPC().get(p.getUniqueId()).getCooldowns().size(); i >= 4; i--) {
            board.getTeam("skill" + i).setPrefix("");
        }*/
    }

    public Skillboard(Player p) {
        skillbar = false;
        uuid = p.getUniqueId();
        statusesBossbar = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        bossbarTwo_5 = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        infoBossbar = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        skillCastingBar = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        hpbarBossbar = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        bossbarThree_6 = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        /*bossbarPH2 = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        bossbarPH3 = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        bossbarPH4 = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        bossbarPH5 = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        bossbarPH6 = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);*/
        uuid = p.getUniqueId();
        hpbarBossbar.addPlayer(p);
        skillCastingBar.addPlayer(p);
        infoBossbar.addPlayer(p);
        statusesBossbar.addPlayer(p);
        bossbarTwo_5.addPlayer(p);
        bossbarThree_6.addPlayer(p);

        setScoreBoard(p);
        update();
        hpbarBossbar.setVisible(true);
        skillCastingBar.setVisible(true);
        statusesBossbar.setVisible(true);
        infoBossbar.setVisible(true);
        /*bossbarPH2.setVisible(true);
        bossbarPH3.setVisible(true);
        bossbarPH4.setVisible(true);
        bossbarPH5.setVisible(true);
        bossbarPH6.setVisible(true);*/
        bossbarTwo_5.setVisible(true);
        bossbarThree_6.setVisible(true);
    }

    public void scrub() {
        uuid = null;
        skillCastingBar = null;
        bossbarTwo_5 = null;
        statusesBossbar = null;
        infoBossbar = null;
        hpbarBossbar = null;
        bossbarThree_6 = null;
        /*bossbarPH2 = null;
        bossbarPH3 = null;
        bossbarPH4 = null;
        bossbarPH5 = null;
        bossbarPH6 = null;*/
        prefixes = null;
    }

    public void update() {
        Player p = Bukkit.getPlayer(uuid);
        if (main.getPC().get(uuid) != null) {
            /*double mana = main.getMana(p);
            double maxmana = main.getPC().get(uuid).getPClass().getCalcMana(main.getPC().get(uuid).getLevel());
            double manaprogress = Math.max((1.0D * mana) / (1.0D * maxmana), 0.0);
            bossbar.setProgress(Math.min(manaprogress, 0.99));*/
            /*String title = "";
            for (int i = main.getPC().get(uuid).getCooldowns().keySet().size() - 1; i >= 0; i--) {
                String name = main.getPC().get(uuid).getCooldowns().keySet().toArray()[i].toString();
                if (main.getPC().get(uuid).getCooldown(main.getPC().get(uuid).getSkillFromName(name)).contains("CD:")) {
                    if (title != "") {
                        title += "  ";
                    }
                    title += Main.color("&e" + name + ": &f" + main.getPC().get(uuid).getCooldown(main.getPC().get(uuid).getSkillFromName(name)).replace("CD:", "") + "s");
                }
            }
            bossbar.setTitle(title);*/
            updateScoreboard(p);
        }
    }

    public void updateWarmup(Skill s, int warmup) {

        infoBossbar.setTitle(Main.color("&bChanneling ") + s.getName() + "...");
        new BukkitRunnable() {
            public void run() {
                if (infoBossbar != null && infoBossbar.getTitle().equalsIgnoreCase(Main.color("&bChanneling ") + s.getName() + "...")) {
                    infoBossbar.setTitle("");
                }
            }
        }.runTaskLater(Main.getInstance(), warmup);


    }

    public void updateCast(Skill s) {

        infoBossbar.setTitle(Main.color("&bCasted ") + s.getName() + "...");
        new BukkitRunnable() {
            public void run() {
                if (infoBossbar != null && infoBossbar.getTitle().equalsIgnoreCase(Main.color("&bCasted ") + s.getName() + "...")) {
                    infoBossbar.setTitle("");
                }
            }
        }.runTaskLater(Main.getInstance(), 40L);
    }

    public void clearChannel() {

        if (infoBossbar.getTitle().contains("Channeling")) {
            infoBossbar.setTitle("");
        }
    }

    public void clearBoard() {

        infoBossbar.setTitle("");


    }

    public void updateToggle(Skill s) {

        if (infoBossbar.getTitle().equals("")) {
            infoBossbar.setTitle(Main.color("&bEnabled ") + s.getName() + "");
        }
    }

    public void endToggle(Skill s) {
        if (infoBossbar.getTitle().contains("Enabled")) {
            infoBossbar.setTitle("");
        }
    }


}
