package com.java.rpg.classes.casting;

import com.java.Main;
import com.java.rpg.classes.statuses.Stuns;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class BindCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    public String getBind(Player p, ItemStack i) {
        File pFile = new File("plugins/Rift/data/binds/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            if (!pFile.exists()) {
                pData.set(main.getRP(p).getPClass().getName() + "Binds", "");
                pData.save(pFile);
            } else {
                String className = main.getRP(p).getPClass().getName();
                if (pData.contains(className + "Binds") && !pData.getString(className + "Binds").isEmpty() && pData.getString(className + "Binds").contains(i.getType().toString())) {
                    LinkedHashMap<String, String> binds = new LinkedHashMap<>();
                    for (String s : pData.getString(className + "Binds").split(",")) {
                        String mat = s.substring(0, s.indexOf(":"));
                        String skill = s.substring(s.indexOf(":") + 1);
                        if (mat.equalsIgnoreCase(i.getType().toString())) {
                            return skill;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setBind(Player p, ItemStack i, String sk) {
        File pFile = new File("plugins/Rift/data/binds/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            String className = main.getRP(p).getPClass().getName();
            if (!pFile.exists()) {
                pData.set("Username", p.getName());
                pData.set(className + "Binds", "");
            }
            pData.set("Username", p.getName());
            if (pData.contains(className + "Binds")) {
                if (pData.getString(className + "Binds").contains(i.getType().toString())) {
                    String toReplace = "";
                    LinkedHashMap<String, String> binds = new LinkedHashMap<>();
                    for (String s : pData.getString(className + "Binds").split(",")) {
                        String mat = s.substring(0, s.indexOf(":"));
                        String skill = s.substring(s.indexOf(":") + 1);
                        if (mat.equalsIgnoreCase(i.getType().toString())) {
                            skill = sk;
                        }
                        if (skill.isEmpty()) {
                        } else {
                            toReplace = toReplace + (mat + ":" + skill + ",");
                        }
                    }
                    if (!toReplace.isEmpty()) {
                        toReplace = toReplace.substring(0, toReplace.length() - 1);
                    }
                    pData.set(className + "Binds", toReplace);
                } else {
                    if (pData.getString(className + "Binds").isEmpty()) {
                        pData.set(className + "Binds", i.getType().toString() + ":" + sk);
                    } else {
                        pData.set(className + "Binds", pData.get(className + "Binds") + "," + i.getType().toString() + ":" + sk);
                    }
                }
            } else {
                pData.set(className + "Binds", "");
                pData.save(pFile);
                setBind(p, i, sk);
                return;
            }
            pData.save(pFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {
        if (!Stuns.isStunned(e.getPlayer()) && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getHand() == EquipmentSlot.HAND) {
                if (e.getItem() != null && e.getItem().getType() != Material.AIR) {
                    if (!getBind(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand()).isEmpty()) {
                        e.getPlayer().performCommand("skill " + getBind(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand()));
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                if (p.getInventory().getItemInMainHand().getType() == Material.AIR || getBind(p, p.getInventory().getItemInMainHand()).isEmpty()) {
                    Main.msg(p, "&fUsage: /bind <skill name>");
                    Main.msg(p, "&fDesc: Bind a skill to your held item. Right-click to cast the skill.");
                    Main.msg(p, "&fClear Binds: /bind clearall, /bind clearallclasses");
                } else {
                    Main.msg(p, "&aCleared bind &f" + getBind(p, p.getInventory().getItemInMainHand()) + " &afrom &f" + p.getInventory().getItemInMainHand().getType().toString() + "&a.");
                    setBind(p, p.getInventory().getItemInMainHand(), "");
                }
            }
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("clearall")) {
                    File pFile = new File("plugins/Rift/data/binds/" + p.getUniqueId() + ".yml");
                    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                    try {
                        String className = main.getRP(p).getPClass().getName();
                        pData.set(className + "Binds", "");
                        pData.save(pFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Main.msg(p, "&aCleared all item binds.");
                } else if (args[0].equalsIgnoreCase("clearallclasses")) {
                    File pFile = new File("plugins/Rift/data/binds/" + p.getUniqueId() + ".yml");
                    try {
                        pFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Main.msg(p, "&aCleared all item binds for all classes.");
                } else {
                    if (main.getRP(p).getSkillFromName(args[0]) != null) {
                        Main.msg(p, "&aSet bind &f" + args[0] + " &ato &f" + p.getInventory().getItemInMainHand().getType().toString() + "&a.");
                        setBind(p, p.getInventory().getItemInMainHand(), args[0]);
                    } else {
                        Main.msg(p, "&cInvalid Skill Name.");
                    }
                }
            }
            return true;
        } else {
            Main.so("Not a console command!");
        }
        return false;
    }

}
