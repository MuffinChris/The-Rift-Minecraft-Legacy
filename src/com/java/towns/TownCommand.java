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
import org.bukkit.scheduler.BukkitRunnable;
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
            if (main.getUUIDCitizenMap().get(p.getUniqueId()).getTown().equals("None"))
                sendTownlessInv(p);
            else
                sendTownInv(p);
            return true;
        }
        else if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("create")) {
                return CreateNewTown(p);
            }
            else if(args[0].equalsIgnoreCase("leave"))
            {
                return LeaveTown(p);
            }
            else if(args[0].equalsIgnoreCase("invite"))
            {
                // TODO: implement
            }
            else if(args[0].equalsIgnoreCase("kick"))
            {
                // TODO: implement
            }
            else if(args[0].equalsIgnoreCase("promote"))
            {
                // TODO: implement
            }
        }

        return false;
    }

    private ItemStack getRemoveTownItemStack() {
        ItemStack sp = new ItemStack(Material.TNT);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&4Delete Town"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to delete your current town!"));
                add(Main.color("&6Must be Owner rank"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getLeaveTownItemStack() {
        ItemStack sp = new ItemStack(Material.BARRIER);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&aLeave Town"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to leave your current town!"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getInviteItemStack() {
        ItemStack sp = new ItemStack(Material.EMERALD);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&aInvite"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to invite a player!"));
                add(Main.color("&6Must have permission to invite"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getKickItemStack() {
        ItemStack sp = new ItemStack(Material.REDSTONE);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&cKick"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to kick player!"));
                add(Main.color("&6Must have permission to kick"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getPromoteItemStack() {
        ItemStack sp = new ItemStack(Material.GREEN_DYE);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&9Promote"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to demote a player!"));
                add(Main.color("&6Must be higher than promotion rank"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getDemoteItemStack() {
        ItemStack sp = new ItemStack(Material.RED_DYE);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&dDemote"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to demote a player!"));
                add(Main.color("&6Must be higher rank than player"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    public void sendTownInv(Player p) {
        String townName = main.getUUIDCitizenMap().get(p.getUniqueId()).getTown();
        Inventory menu = Bukkit.createInventory(null, 36, Main.color("&e&l" + townName + " Menu"));

        // delete town
        menu.setItem(10, getRemoveTownItemStack());
        // leave town
        menu.setItem(11, getLeaveTownItemStack());


        // town list

        // town leaderboard

        // town search

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

    public void sendTownlessInv(Player p) {
        Inventory menu = Bukkit.createInventory(null, 27, Main.color("&e&lTown Menu"));

        // town create
        menu.setItem(10, getNewTownItemStack());

        // town list

        // town leaderboard

        // town search

        ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta phM = ph.getItemMeta();
        phM.setDisplayName(" ");
        ph.setItemMeta(phM);
        for (int i = 0; i < 9; i++) {
            menu.setItem(i, ph);
        }
        for (int i = 27; i < 36; i++) {
            menu.setItem(i, ph);
        }

        p.openInventory(menu);
        p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
    }

    private boolean CreateNewTown(Player p) {
        Main.msg(p, Main.color("&l&eEnter Town Name (A-Z and spaces only): "));

        main.getUUIDCitizenMap().get(p.getUniqueId()).setCreationStatus("Prompted");
        new BukkitRunnable() {
            public void run() {
                Citizen mc = main.getUUIDCitizenMap().get(p.getUniqueId());
                if (!mc.getCreationStatus().equals("Normal")) {
                    mc.setCreationStatus("Normal");
                    Main.msg(p, Main.color("&4Prompt Timed Out."));
                }
            }
        }.runTaskLater(Main.getInstance(), 20 * 60);

        return true;

    }

    private boolean LeaveTown(Player p) {

        // TODO: if the owner of a town leaves -- then either the town should disband or the max level role needs to be transferred (randomly to a maxlvl -1 member?)
        Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());

        if(c.getTown().equalsIgnoreCase("None")) {
            Main.msg(p, "&4You aren't in any town!");
            return false;
        }

        Town t = null;
        for(Town et : main.getTowns()) {
            if(et.getName().equals(c.getTown())) {
                t = et;
                break;
            }
        }
        c.setTown("None"); // default values
        c.setRank(-1); // %
        Main.msg(p, "&4You have successfully left " + t.getName());

        t.getCitizenList().removeCitizen(p);
        if(t.getCitizenList().citimap.size() == 0)
        {
            // there are no more people in this town -- delete it
            main.getTowns().remove(t);
            List<String> fullTowns = main.getFullTownList();
            fullTowns.remove(t.getName());
            main.setFullTownList(fullTowns);

            Bukkit.broadcastMessage(Main.color("&l&4 Town " + t.getName() + " has been disbanded!"));
        }

        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("§e§lTown Menu")) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);

            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Create New Town")) {
                CreateNewTown((Player) e.getWhoClicked());
            }

            e.getWhoClicked().closeInventory();
        } else if (e.getView().getTitle().contains("§e§l" + main.getUUIDCitizenMap().get(e.getWhoClicked().getUniqueId()).getTown() + " Menu")) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);

            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Leave Town")) {
                LeaveTown((Player) e.getWhoClicked());
            }

        }
    }
}