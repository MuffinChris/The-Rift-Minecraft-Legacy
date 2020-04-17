package com.java.towns;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.RPGConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TownCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    private boolean is(String comp, String s) {
        String[] strings = s.split(",");
        for (String str : strings) {
            if (str.equalsIgnoreCase(comp)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;

        if (args.length == 0) {
            sendTownlessInv(p);
            return true;
        }

        if (args[0].equals("create")) {

        } else if (args[0].equals("invite")) {

        } else {

        }

        return true;
    }

    private ItemStack getNewTownItemStack() {
        ItemStack sp = new ItemStack(Material.NETHER_STAR);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&6Create New Town"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to create a town!"));
            }
        });

        spMeta.addEnchant(Enchantment.MENDING, 1, true);
        spMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        sp.setItemMeta(spMeta);

        return sp;
    }
    private ItemStack getRemoveTownItemStack() {
        ItemStack sp = new ItemStack(Material.BARRIER);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&4Delete Town"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to delete your current town!"));
                add(Main.color("&6 Must be Owner rank"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }
    private ItemStack getInviteItemStack() {
        ItemStack sp = new ItemStack(Material.EMERALD_BLOCK);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&aInvite"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to invite a player!"));
                add(Main.color("&6 Must have permission to invite"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }
    private ItemStack getKickItemStack() {
        ItemStack sp = new ItemStack(Material.REDSTONE_BLOCK);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&cKick"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to kick player!"));
                add(Main.color("&6 Must have permission to kick"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }
    private ItemStack getPromoteItemStack() {
        ItemStack sp = new ItemStack(Material.LAPIS_BLOCK);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&9Promote"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to demote a player!"));
                add(Main.color("&6 Must be at least as high as promotion rank"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }
    private ItemStack getDemoteItemStack() {
        ItemStack sp = new ItemStack(Material.END_STONE);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&dDemote"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to demote a player!"));
                add(Main.color("&6 Must be higher rank than player"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }


    public void sendTownlessInv(Player p) {
        Inventory menu = Bukkit.createInventory(null, 27, Main.color("&e&lTown Menu"));

        menu.setItem(10, getNewTownItemStack());
        menu.setItem(11, getRemoveTownItemStack());

        // invite
        menu.setItem(13, getInviteItemStack());
        // kick
        menu.setItem(14, getKickItemStack());
        // promote
        menu.setItem(15, getPromoteItemStack());
        // demote
        menu.setItem(16, getDemoteItemStack());

        ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta phM = ph.getItemMeta();
        phM.setDisplayName(" ");
        ph.setItemMeta(phM);
        for (int i = 0; i < 9; i++) {
            menu.setItem(i, ph);
        }
        for (int i = 18; i <= 26; i++) {
            menu.setItem(i, ph);
        }

        p.openInventory(menu);
        p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
       /* if (e.getView().getTitle().contains("§e§lSkills")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                if (e.getCurrentItem().getItemMeta().getLore().contains("§cLOCKED §8(§c1 SP§8)")) {
                    Player p = (Player) e.getWhoClicked();
                    RPGPlayer rp = main.getRP(p);
                    int total = rp.calculateSP();
                    if (total > 0) {
                        Skill s = rp.getSkillFromSuper(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                        if (s != null) {
                            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
                            rp.getSkillLevels().replace(s.getName(), 1);
                            rp.pushFiles();
                            p.closeInventory();
                            sendSkillsInv(p, "");
                        } else {
                            Main.so("&cERROR: Failed to upgrade &4" + p.getName() + "&c's skill &4" + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()) + "&c.");
                        }
                    }
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§cReset Skillpoints")) {
                    Player p = (Player) e.getWhoClicked();
                    RPGPlayer rp = main.getRP(p);
                    p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0F, 1.0F);
                    for (String s : rp.getSkillLevels().keySet()) {
                        rp.getSkillLevels().replace(s, 0);
                    }
                    rp.pushFiles();
                    p.closeInventory();
                    sendSkillsInv(p, "");
                }
            }
        }*/
    }
}