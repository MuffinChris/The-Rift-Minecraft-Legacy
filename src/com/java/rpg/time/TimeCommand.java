package com.java.rpg.time;

import com.java.Main;
import com.java.rpg.classes.utility.RPGConstants;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class TimeCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                Main.msg(p, "");
                Main.msg(p, "&8" + RPGConstants.arrow + " &eTime: &f" + getTime() + " &8(&f" + getDayOfMonth() + " " + getMonth() + ", " + getYear() + "&8)");
                Main.msg(p, "");
            } else {
                if (p.hasPermission("core.admin")) {
                    if (args[0].equalsIgnoreCase("help")) {
                        Main.msg(p, "&fUsage: /customtime <startnow, sync, addday>");
                        return true;
                    } else if (args[0].equalsIgnoreCase("startnow")) {
                        long prev = getTimeMillis();
                        setTimeMillisNow();
                        Main.msg(p, "&aChanged time start in millis to &f" + System.currentTimeMillis() + " &afrom &f" + prev + "&a.");
                        p.performCommand("customtime sync");
                        return true;
                    } else if (args[0].equalsIgnoreCase("sync")) {
                        for (World w : Bukkit.getWorlds()) {
                            w.setTime(getTicks());
                        }
                        Main.msg(p, "&aSynced all world times to current Rift time.");
                        return true;
                    } else if (args[0].equalsIgnoreCase("addday")) {
                        addOneDay();
                        Main.msg(p, "&aYou added one day.");
                        p.performCommand("customtime sync");
                        return true;
                    } else {
                        Main.msg(p, "&fUsage: /customtime <startnow, sync, addday>");
                        return true;
                    }
                } else {
                    Main.msg(p, main.noperm);
                    return true;
                }
            }
        } else {
            if (args.length == 0) {
                Main.so( "");
                Main.so( "&8" + RPGConstants.arrow + " &eTime: &f" + getTime() + " &8(&f" + getDayOfMonth() + " " + getMonth() + ", " + getYear() + "&8)");
                Main.so( "");
            } else {
                if (args[0].equalsIgnoreCase("help")) {
                    Main.so( "&fUsage: /customtime <startnow, sync>");
                    return true;
                } else if (args[0].equalsIgnoreCase("startnow")) {
                    long prev = getTimeMillis();
                    setTimeMillisNow();
                    Main.so( "&aChanged time start in millis to &f" + System.currentTimeMillis() + " &afrom &f" + prev + "&a.");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "customtime sync");
                    return true;
                } else if (args[0].equalsIgnoreCase("sync")) {
                    for (World w : Bukkit.getWorlds()) {
                        w.setTime(getTicks());
                    }
                    Main.so( "&aSynced all world times to current Rift time.");
                    return true;
                } else {
                    Main.so( "&fUsage: /customtime <startnow, sync>");
                    return true;
                }
            }
        }
        return false;
    }

    public long getTicksTotal() {
        long startTime = getTimeMillis();
        return Math.round((System.currentTimeMillis() - startTime)/1000.0 * 20);
    }

    public long getTicks() {
        return getTicksTotal() % 24000;
    }

    public String getTime() {
        String time;
        long ticksToday = getTicks();
        long hour = ticksToday/1000;

        boolean am = false;

        if (hour >= 0 && hour < 6) {
            am = true;
            time = (6 + hour) + "";
        } else if (hour >= 6 && hour < 18) {
            am = false;
            if (hour != 6) {
                time = (hour - 6) + "";
            } else {
                time = (6 + hour) + "";
            }
        } else if (hour >= 18 && hour <= 24) {
            am = true;
            if (hour == 18) {
                time = (hour - 6) + "";
            } else {
                time = (hour - 18) + "";
            }
        } else {
            time = "ERROR";
            Main.so("&c&l[ERROR] &cTime was not in bounds, TicksToday = " + ticksToday + " Hour = " + hour);
        }

        long ticksThisHour = ticksToday % 1000;

        double minutes = (ticksThisHour * 1.0)/(1000.0/60.0);
        DecimalFormat df = new DecimalFormat("#");
        if (minutes > 0) {
            if (minutes < 10) {
                time = time + ":0" + df.format(minutes);
            } else {
                time = time + ":" + df.format(minutes);
            }
        } else {
            time = time + ":00";
        }

        if (am) {
            time = time + " AM";
        } else {
            time = time + " PM";
        }
        return time;
    }

    public long getTotalDays() {
        return Math.round(Math.floor(getTicksTotal() / 24000.0));
    }

    public long getYear() {
        return getTotalDays() / 365;
    }

    public enum Month {
        January(31, 31), February(28 + 31, 28), March(28 + 31 + 31, 31), April(28 + 31 + 31 + 30, 30), May(28 + 31 + 31 + 30 + 31, 31), June(28 + 31 + 31 + 30 + 31 + 30, 30), July(28 + 31 + 31 + 30 + 31 + 30 + 31, 31), August(28 + 31 + 31 + 30 + 31 + 30 + 31 + 31, 31), September(28 + 31 + 31 + 30 + 31 + 30 + 31 + 31 + 30, 30), October(28 + 31 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31, 31), November(28 + 31 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30, 30), December(28 + 31 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30 + 31, 31);

        private int day;
        private int daySelf;
        Month(int i, int ds) {
            day = i;
            daySelf = ds;
        }

        public int getVal() {
            return day;
        }

        public int getVal2() {
            return daySelf;
        }
    }

    public Month getMonth() {
        long daysThisYear = getDay() + 1;
        if (daysThisYear <= Month.January.getVal()) {
            return Month.January;
        } else if (daysThisYear <= Month.February.getVal()) {
            return Month.February;
        } else if (daysThisYear <= Month.March.getVal()) {
            return Month.March;
        } else if (daysThisYear <= Month.May.getVal()) {
            return Month.May;
        } else if (daysThisYear <= Month.June.getVal()) {
            return Month.June;
        } else if (daysThisYear <= Month.July.getVal()) {
            return Month.July;
        } else if (daysThisYear <= Month.August.getVal()) {
            return Month.August;
        } else if (daysThisYear <= Month.September.getVal()) {
            return Month.September;
        } else if (daysThisYear <= Month.October.getVal()) {
            return Month.October;
        } else if (daysThisYear <= Month.November.getVal()) {
            return Month.November;
        } else {
            return Month.December;
        }
    }

    public long getDay() {
        return getTotalDays() % 365;
    }

    public long getDayOfMonth() {
        return getTotalDays() + getMonth().getVal2() - getMonth().getVal() + 1;
    }

    public long getTimeMillis() {
        File pFile = new File("plugins/Rift/time.yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (pData.contains("Time")) {
            return pData.getLong("Time");
        } else {
            setTimeMillisNow();
            return getTimeMillis();
        }
    }

    public void setTimeMillisNow() {
        File pFile = new File("plugins/Rift/time.yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        pData.set("Time", System.currentTimeMillis());
        try {
            pData.save(pFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addOneDay() {
        File pFile = new File("plugins/Rift/time.yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        pData.set("Time", getTimeMillis() - 24000);
        try {
            pData.save(pFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
