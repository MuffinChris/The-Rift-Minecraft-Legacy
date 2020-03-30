package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class SkillCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                String flavor = main.getPC().get(p.getUniqueId()).castSkill(args[0]);
                if (flavor.equalsIgnoreCase("NoMana")) {
                    Main.msg(p, "&cNot enough mana!");
                } else if (flavor.contains("Level")) {
                    int level = Integer.valueOf(flavor.replace("Level", ""));
                    Main.msg(p, "&cYou must be level &f" + level + " &cto use that skill.");
                } else if (flavor.contains("CD:")) {
                    String cd = flavor.replace("CD:", "");
                    String skill = args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase();
                    Main.msg(p, "&c" + skill + " on cooldown: &f" + cd + "s");
                } else if (flavor.contains("Failure")) {
                    Main.msg(p, "&cInternal Skill Error. Please let Admins know what happened.");
                } else if (flavor.contains("Invalid")) {
                    Main.msg(p, "&cInvalid Skill Name!");
                } else if (flavor.contains("Warmup")) {
                    new BukkitRunnable() {
                        public void run() {

                            if (!p.isOnline()) {
                                cancel();
                                return;
                            }

                            if (p.isDead()) {
                                Skill skill = main.getPC().get(p.getUniqueId()).getSkillFromName(args[0]);
                                p.removePotionEffect(PotionEffectType.SLOW);
                                main.getRP(p).getBoard().clearChannel();
                                main.getRP(p).getStatuses().remove("Warmup" + skill.getName());
                                cancel();
                                return;
                            }

                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,999999, 2));
                            if (!main.getPC().get(p.getUniqueId()).getStatuses().contains("Warmup" + main.getPC().get(p.getUniqueId()).getSkillFromName(args[0]).getName())) {
                                p.removePotionEffect(PotionEffectType.SLOW);
                                main.getRP(p).getBoard().clearChannel();
                                cancel();
                            } else {
                                Skill skill = main.getPC().get(p.getUniqueId()).getSkillFromName(args[0]);
                                if (skill.getManaCost() > main.getMana(p)) {
                                    p.removePotionEffect(PotionEffectType.SLOW);
                                    main.getRP(p).getBoard().clearChannel();
                                    Main.msg(p, "&cOut of mana to cast " + skill.getName() + "!");
                                    main.getRP(p).getStatuses().remove("Warmup" + skill.getName());
                                    cancel();
                                } else {
                                    skill.warmup(p);
                                }
                            }
                        }
                    }.runTaskTimer(main, 0L, 1L);
                } else if (flavor.contains("AlreadyCasting")) {
                    Main.msg(p, "&cAlready casting!");
                } else if (flavor.contains("Interrupt")) {
                    p.removePotionEffect(PotionEffectType.SLOW);
                    main.getRP(p).getBoard().clearChannel();
                } else if (flavor.contains("CannotCast")) {
                    Main.msg(p, "&cYou cannot cast this skill.");
                } else if (flavor.contains("BadTarget")) {
                    Main.msg(p, "&cInvalid target.");
                } else if (flavor.contains("OutOfRangeTarget")) {
                    Main.msg(p, "&cTarget out of range!");
                } else if (flavor.contains("Stunned")) {
                    Main.msg(p, "&cYou're stunned!");
                } else if (flavor.contains("AlreadySuper")) {
                    Main.msg(p, "&cYou can only use the upgraded form of this skill.");
                } else if (flavor.contains("NotSuper")) {
                    Main.msg(p, "&cYou have not upgraded to this skill.");
                }
            } else {
                Main.msg(p, "&fUsage: /skill <skill>");
            }
        } else {
            Main.so("&cNot a console cmd");
        }
        return false;
    }

}
