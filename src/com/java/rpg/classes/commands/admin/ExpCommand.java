package com.java.rpg.classes.commands.admin;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.RPGConstants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.DecimalFormat;

public class ExpCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    //need to make a rebalance skills method to check skill lvl level and sp and reset it all



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DecimalFormat df = new DecimalFormat("#");
            if (p.hasPermission("core.admin")) {
                if (cmd.getLabel().equalsIgnoreCase("setlevel")) {
                    if (args.length == 3) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (main.getCM().getPClassFromString(args[2]) != null) {
                                String className = main.getCM().getPClassFromString(args[2]).getName();
                                if (Integer.valueOf(args[1]) instanceof Integer) {
                                    tl.pushFiles();
                                    int val = Integer.valueOf(args[1]);
                                    val = Math.min(RPGConstants.maxLevel, val);
                                    File pFile = new File("plugins/Rift/data/classes/" + target.getUniqueId() + ".yml");
                                    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                                    try {
                                        if (pData.contains(className + "Level")) {
                                            int level = pData.getInt(className + "Level");
                                            if (val > level) {

                                            } else {
                                                if (level >= RPGConstants.superSkillTwo && val < RPGConstants.superSkillTwo) {
                                                    String output = "";
                                                    for (Skill s : main.getCM().getPClassFromString(args[2]).getSkills()) {
                                                        output+=s.getName() + "-0,";
                                                    }
                                                    if (output.contains(",")) {
                                                        output = output.substring(0, output.length() - 1);
                                                    }
                                                    pData.set(className + "Skills", output);
                                                } else if (level >= RPGConstants.superSkillOne && val < RPGConstants.superSkillOne) {
                                                    String output = "";
                                                    for (Skill s : main.getCM().getPClassFromString(args[2]).getSkills()) {
                                                        output+=s.getName() + "-0,";
                                                    }
                                                    if (output.contains(",")) {
                                                        output = output.substring(0, output.length() - 1);
                                                    }
                                                    pData.set(className + "Skills", output);
                                                }
                                            }
                                            pData.set(className + "Level", val);
                                        } else {
                                            pData.set(className + "Level", val);
                                        }
                                        pData.save(pFile);
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    tl.pullFiles();
                                    Main.msg(p, "&eSet &6" + target.getName() + "'s " + args[2] + " &elevel to " + val + ".");
                                } else {
                                    Main.msg(p, "&cInvalid Value");
                                }
                            } else {
                                Main.msg(p, "&cInvalid Class");
                            }
                        } else {
                            Main.msg(p, "&cInvalid Player");
                        }
                    } else
                    if (args.length == 2) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (Integer.valueOf(args[1]) instanceof Integer) {
                                int val = Integer.valueOf(args[1]);
                                val = Math.min(tl.getMaxLevel(), val);
                                if (val > tl.getLevel()) {
                                    tl.levelupRewards(target, tl.getPClass(), tl.getLevel(), val);
                                } else {
                                    if (tl.getLevel() >= RPGConstants.superSkillTwo && val < RPGConstants.superSkillTwo) {
                                        for (String s : tl.getSkillLevels().keySet()) {
                                            tl.getSkillLevels().replace(s, 0);
                                        }
                                        tl.pushFiles();
                                    } else if (tl.getLevel() >= RPGConstants.superSkillOne && val < RPGConstants.superSkillOne) {
                                        for (String s : tl.getSkillLevels().keySet()) {
                                            tl.getSkillLevels().replace(s, 0);
                                        }
                                        tl.pushFiles();
                                    }
                                }
                                tl.setLevel(val);
                                Main.msg(p, "&eSet &6" + target.getName() + "'s &elevel to " + val + ".");
                            } else {
                                Main.msg(p, "&cInvalid Value");
                            }
                        } else {
                            Main.msg(p, "&cInvalid Player");
                        }
                    } else {
                        Main.msg(p, "Usage: /setlevel <player> <level> [<class>]");
                    }
                }
                if (cmd.getLabel().equalsIgnoreCase("addlevel")) {
                    if (args.length == 2) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (Integer.valueOf(args[1]) instanceof Integer) {
                                int times = Integer.valueOf(args[1]);
                                int newlevel = Math.min(tl.getMaxLevel(), times + tl.getLevel());
                                if (newlevel != tl.getLevel()) {
                                    tl.levelupRewards(target, tl.getPClass(), tl.getLevel(), newlevel);
                                }
                                tl.setLevel(newlevel);
                                Main.msg(p, "&eSet &6" + target.getName() + "'s &elevel to " + newlevel + ".");
                            } else {
                                Main.msg(p, "&cInvalid Value");
                            }
                        } else {
                            Main.msg(p, "&cInvalid Player");
                        }
                    } else {
                        Main.msg(p, "Usage: /addlevel <player> <level>");
                    }
                }
                if (cmd.getLabel().equalsIgnoreCase("setexp")) {
                    if (args.length == 3) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (main.getCM().getPClassFromString(args[2]) != null) {
                                String className = main.getCM().getPClassFromString(args[2]).getName();
                                if (Double.valueOf(args[1]) instanceof Double) {
                                    double exp = Double.valueOf(args[1]);
                                    tl.pushFiles();
                                    File pFile = new File("plugins/Rift/data/classes/" + target.getUniqueId() + ".yml");
                                    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                                    try {
                                        pData.set(className + "Exp", exp);
                                        pData.save(pFile);
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    Main.msg(p, "&eSet &6" + target.getName() + "'s " + args[2] + " &eEXP to " + df.format(Double.valueOf(args[1])) + ".");
                                    tl.pullFiles();
                                } else {
                                    Main.msg(p, "&cInvalid Value");
                                }
                            } else {
                                Main.msg(p, "&cInvalid Class");
                            }
                        } else {
                            Main.msg(p, "&cInvalid Player");
                        }
                    } else
                    if (args.length == 2) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (Double.valueOf(args[1]) instanceof Double) {
                                tl.setExp(Double.valueOf(args[1]));
                                Main.msg(p, "&eSet &6" + target.getName() + "'s &eEXP to " + df.format(Double.valueOf(args[1])) + ".");
                            } else {
                                Main.msg(p, "&cInvalid Value");
                            }
                        } else {
                            Main.msg(p, "&cInvalid Player");
                        }
                    } else {
                        Main.msg(p, "Usage: /setexp <player> <exp> [<class>]");
                    }
                }
                if (cmd.getLabel().equalsIgnoreCase("addexp")) {
                    if (args.length == 2) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (Double.valueOf(args[1]) instanceof Double) {
                                tl.giveExpFromSource(target, target.getLocation(), Double.valueOf(args[1]), "");
                                Main.msg(p, "&eGave &6" + target.getName() + " &e" + df.format(Double.valueOf(args[1])) + " &eEXP.");
                            } else {
                                Main.msg(p, "&cInvalid Value");
                            }
                        } else {
                            Main.msg(p, "&cInvalid Player");
                        }
                    } else {
                        Main.msg(p, "Usage: /addexp <player> <exp>");
                    }
                }
            } else {
                Main.msg(p, main.noperm);
            }
        } else {
            DecimalFormat df = new DecimalFormat("#");
                if (cmd.getLabel().equalsIgnoreCase("setlevel")) {
                    if (args.length == 3) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (main.getCM().getPClassFromString(args[2]) != null) {
                                String className = main.getCM().getPClassFromString(args[2]).getName();
                                if (Integer.valueOf(args[1]) instanceof Integer) {
                                    tl.pushFiles();
                                    int val = Integer.valueOf(args[1]);
                                    val = Math.min(RPGConstants.maxLevel, val);
                                    File pFile = new File("plugins/Rift/data/classes/" + target.getUniqueId() + ".yml");
                                    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                                    try {
                                        if (pData.contains(className + "Level")) {
                                            int level = pData.getInt(className + "Level");
                                            if (val > level) {

                                            } else {
                                                if (level >= RPGConstants.superSkillTwo && val < RPGConstants.superSkillTwo) {
                                                    String output = "";
                                                    for (Skill s : main.getCM().getPClassFromString(args[2]).getSkills()) {
                                                        output+=s.getName() + "-0,";
                                                    }
                                                    if (output.contains(",")) {
                                                        output = output.substring(0, output.length() - 1);
                                                    }
                                                    pData.set(className + "Skills", output);
                                                } else if (level >= RPGConstants.superSkillOne && val < RPGConstants.superSkillOne) {
                                                    String output = "";
                                                    for (Skill s : main.getCM().getPClassFromString(args[2]).getSkills()) {
                                                        output+=s.getName() + "-0,";
                                                    }
                                                    if (output.contains(",")) {
                                                        output = output.substring(0, output.length() - 1);
                                                    }
                                                    pData.set(className + "Skills", output);
                                                }
                                            }
                                            pData.set(className + "Level", val);
                                        } else {
                                            pData.set(className + "Level", val);
                                        }
                                        pData.save(pFile);
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    tl.pullFiles();
                                    Main.so( "&eSet &6" + target.getName() + "'s " + args[2] + " &elevel to " + val + ".");
                                } else {
                                    Main.so( "&cInvalid Value");
                                }
                            } else {
                                Main.so( "&cInvalid Class");
                            }
                        } else {
                            Main.so( "&cInvalid Player");
                        }
                    } else
                    if (args.length == 2) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (Integer.valueOf(args[1]) instanceof Integer) {
                                int val = Integer.valueOf(args[1]);
                                val = Math.min(tl.getMaxLevel(), val);
                                if (val > tl.getLevel()) {
                                    tl.levelupRewards(target, tl.getPClass(), tl.getLevel(), val);
                                } else {
                                    if (tl.getLevel() >= RPGConstants.superSkillTwo && val < RPGConstants.superSkillTwo) {
                                        for (String s : tl.getSkillLevels().keySet()) {
                                            tl.getSkillLevels().replace(s, 0);
                                        }
                                        tl.pushFiles();
                                    } else if (tl.getLevel() >= RPGConstants.superSkillOne && val < RPGConstants.superSkillOne) {
                                        for (String s : tl.getSkillLevels().keySet()) {
                                            tl.getSkillLevels().replace(s, 0);
                                        }
                                        tl.pushFiles();
                                    }
                                }
                                tl.setLevel(val);
                                tl.levelup();
                                Main.so( "&eSet &6" + target.getName() + "'s &elevel to " + val + ".");
                            } else {
                                Main.so( "&cInvalid Value");
                            }
                        } else {
                            Main.so( "&cInvalid Player");
                        }
                    } else {
                        Main.so( "Usage: /setlevel <player> <level> [<class>]");
                    }
                }
                if (cmd.getLabel().equalsIgnoreCase("addlevel")) {
                    if (args.length == 2) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (Integer.valueOf(args[1]) instanceof Integer) {
                                int times = Integer.valueOf(args[1]);
                                int newlevel = Math.min(tl.getMaxLevel(), times + tl.getLevel());
                                if (newlevel != tl.getLevel()) {
                                    tl.levelupRewards(target, tl.getPClass(), tl.getLevel(), newlevel);
                                }
                                tl.setLevel(newlevel);
                                Main.so( "&eSet &6" + target.getName() + "'s &elevel to " + newlevel + ".");
                            } else {
                                Main.so( "&cInvalid Value");
                            }
                        } else {
                            Main.so( "&cInvalid Player");
                        }
                    } else {
                        Main.so( "Usage: /addlevel <player> <level>");
                    }
                }
                if (cmd.getLabel().equalsIgnoreCase("setexp")) {
                    if (args.length == 3) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (main.getCM().getPClassFromString(args[2]) != null) {
                                String className = main.getCM().getPClassFromString(args[2]).getName();
                                if (Double.valueOf(args[1]) instanceof Double) {
                                    tl.pushFiles();
                                    double exp = Double.valueOf(args[1]);
                                    File pFile = new File("plugins/Rift/data/classes/" + target.getUniqueId() + ".yml");
                                    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                                    try {
                                        pData.set(className + "Exp", exp);
                                        pData.save(pFile);
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    tl.pullFiles();
                                    Main.so( "&eSet &6" + target.getName() + "'s " + args[2] + " &eEXP to " + df.format(Double.valueOf(args[1])) + ".");
                                } else {
                                    Main.so( "&cInvalid Value");
                                }
                            } else {
                                Main.so( "&cInvalid Class");
                            }
                        } else {
                            Main.so( "&cInvalid Player");
                        }
                    } else
                    if (args.length == 2) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (Double.valueOf(args[1]) instanceof Double) {
                                tl.setExp(Double.valueOf(args[1]));
                                Main.so( "&eSet &6" + target.getName() + "'s &eEXP to " + df.format(Double.valueOf(args[1])) + ".");
                            } else {
                                Main.so( "&cInvalid Value");
                            }
                        } else {
                            Main.so( "&cInvalid Player");
                        }
                    } else {
                        Main.so( "Usage: /setexp <player> <exp> [<class>]");
                    }
                }
                if (cmd.getLabel().equalsIgnoreCase("addexp")) {
                    if (args.length == 2) {
                        if (Bukkit.getPlayer(args[0]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[0]);
                            RPGPlayer tl = main.getRP(target);
                            if (Double.valueOf(args[1]) instanceof Double) {
                                tl.giveExpFromSource(target, target.getLocation(), Double.valueOf(args[1]), "");
                                Main.so( "&eGave &6" + target.getName() + " &e" + df.format(Double.valueOf(args[1])) + " &eEXP.");
                            } else {
                                Main.so( "&cInvalid Value");
                            }
                        } else {
                            Main.so( "&cInvalid Player");
                        }
                    } else {
                        Main.so( "Usage: /addexp <player> <exp>");
                    }
                }
        }
        return false;
    }

}
