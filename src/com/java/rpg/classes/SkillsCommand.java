package com.java.rpg.classes;

import com.java.Main;
import com.java.rpg.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SkillsCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                sendSkillsInv(p, "");
            } else {
                if (main.getCM().getPClassFromString(args[0]) != null) {
                    sendSkillsInv(p, main.getCM().getPClassFromString(args[0]).getName());
                } else {
                    Main.msg(p, "&cInvalid Class, use /class to see available Classes!");
                }
            }
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
        }
        return false;
    }

    public void sendSkillsInv(Player p, String c) {
        if (c.isEmpty()) {
            RPGPlayer rp = main.getRP(p);
            Inventory playerInv = Bukkit.createInventory(null, 36, Main.color("&e&l" + rp.getPClass().getName() + " &e&lSkills"));
            ArrayList<String> lore;//
            ItemStack sp = new ItemStack(Material.NETHER_STAR);
            ItemMeta spMeta = sp.getItemMeta();
            spMeta.setDisplayName(Main.color("&6Skillpoints: &f" + rp.calculateSP()));
            lore = new ArrayList<>();
            lore.add(Main.color("&fClick a non-upgraded spell to upgrade it!"));
            spMeta.setLore(lore);
            sp.setItemMeta(spMeta);
            playerInv.setItem(9, sp);

            sp = new ItemStack(Material.BARRIER);
            spMeta = sp.getItemMeta();
            spMeta.setDisplayName(Main.color("&cReset Skillpoints"));
            lore = new ArrayList<>();
            lore.add(Main.color("&fReset your skillpoint allocations."));
            spMeta.setLore(lore);
            sp.setItemMeta(spMeta);
            playerInv.setItem(18, sp);


            int i = 10;
            int index = 0;
            for (Skill s : rp.getPClass().getSkills()) {
                Material mat = Material.GREEN_STAINED_GLASS_PANE;
                if (main.getSkillLevel(p, s.getName()) > 0) {
                    mat = Material.GRAY_STAINED_GLASS_PANE;
                }
                sp = new ItemStack(mat);
                spMeta = sp.getItemMeta();

                boolean upgraded = main.getSkillLevel(p, s.getName()) > 0;
                String displayName = "&e&l" + s.getName();
                String lockStatus = "&aUNLOCKED";
                if (upgraded) {
                    displayName = "&e&l" + s.getName();
                    lockStatus = "&bUPGRADED";
                }

                spMeta.setDisplayName(Main.color(displayName));
                lore = new ArrayList<>();
                if (s.getLevel() <= rp.getLevel()) {
                    lore.add(Main.color(lockStatus));
                    lore.add(Main.color(""));
                    if (main.getRP(p).getPClass().getSuperSkills().size() > index) {
                        lore.add(Main.color("&bUpgraded Form: &f" + main.getRP(p).getPClass().getSuperSkills().get(index).getName()));
                        lore.add("");
                    } else {
                        lore.add(Main.color("&bUpgrade not Implemented"));
                        lore.add("");
                    }
                } else {
                    sp.setType(Material.GRAY_STAINED_GLASS_PANE);
                    lore.add(Main.color("&cLOCKED &8(&cLVL " + s.getLevel() + "&8)"));
                    lore.add(Main.color(""));
                }

                index++;

                DecimalFormat dF = new DecimalFormat("#.##");
                if (s.getManaCost() > 0) {
                    lore.add(Main.color("&bMana Cost: &f" + s.getManaCost()));
                }
                if (s.getToggleMana() > 0) {
                    lore.add(Main.color("&bToggle Mana Cost: &f" + s.getToggleMana()));
                }
                if (s.getToggleTicks() > 0) {
                    lore.add(Main.color("&eToggle Tick Rate: &f" + dF.format((s.getToggleTicks() * 1.0) / 20.0) + " seconds"));
                }
                if (s.getWarmup() > 0) {
                    lore.add(Main.color("&eWarmup: &f" + dF.format((s.getWarmup() * 1.0) / 20.0) + " seconds"));
                }
                if (s.getCooldown() > 0) {
                    lore.add(Main.color("&eCooldown: &f" + dF.format((s.getCooldown() * 1.0) / 20.0) + " seconds"));
                }
                lore.add(Main.color(""));
                for (String st : s.getDescription(p)) {
                    lore.add(Main.color(st));
                }

                spMeta.setLore(lore);
                sp.setItemMeta(spMeta);
                playerInv.setItem(i, sp);
                i++;
            }
            i = 19;
            index = 0;
            for (Skill s : rp.getPClass().getSuperSkills()) {
                Material mat = Material.GRAY_STAINED_GLASS_PANE;
                if (main.getSkillLevel(p, s.getName()) > 0) {
                    mat = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
                }
                sp = new ItemStack(mat);
                spMeta = sp.getItemMeta();

                boolean upgraded = main.getSkillLevel(p, s.getName()) > 0;
                String displayName = "&b&l" + s.getName();
                String lockStatus = "&cLOCKED &8(&c1 SP&8)";
                if (upgraded) {
                    displayName = "&b&l" + s.getName();
                    lockStatus = "&aUNLOCKED";
                }

                spMeta.setDisplayName(Main.color(displayName));
                lore = new ArrayList<>();
                if (s.getLevel() <= rp.getLevel()) {
                    lore.add(Main.color(lockStatus));
                    lore.add(Main.color(""));
                } else {
                    lore.add(Main.color("&cLOCKED &8(&cLVL " + s.getLevel() + "&8)"));
                    lore.add(Main.color(""));
                }

                index++;

                DecimalFormat dF = new DecimalFormat("#.##");
                if (s.getManaCost() > 0) {
                    lore.add(Main.color("&bMana Cost: &f" + s.getManaCost()));
                }
                if (s.getToggleMana() > 0) {
                    lore.add(Main.color("&bToggle Mana Cost: &f" + s.getToggleMana()));
                }
                if (s.getToggleTicks() > 0) {
                    lore.add(Main.color("&eToggle Tick Rate: &f" + dF.format((s.getToggleTicks() * 1.0) / 20.0) + " seconds"));
                }
                if (s.getWarmup() > 0) {
                    lore.add(Main.color("&eWarmup: &f" + dF.format((s.getWarmup() * 1.0) / 20.0) + " seconds"));
                }
                if (s.getCooldown() > 0) {
                    lore.add(Main.color("&eCooldown: &f" + dF.format((s.getCooldown() * 1.0) / 20.0) + " seconds"));
                }
                lore.add(Main.color(""));
                for (String st : s.getDescription(p)) {
                    lore.add(Main.color(st));
                }

                spMeta.setLore(lore);
                sp.setItemMeta(spMeta);
                playerInv.setItem(i, sp);
                i++;
            }
            p.openInventory(playerInv);
        } else {
            Inventory playerInv = Bukkit.createInventory(null, 36, Main.color("&e&l" + c + " &e&lSkills"));
            ArrayList<String> lore;//
            int i = 10;
            int index = 0;
            for (Skill s : main.getCM().getPClassFromString(c).getSkills()) {
                Material mat = Material.GREEN_STAINED_GLASS_PANE;
                ItemStack sp = new ItemStack(mat);
                ItemMeta spMeta = sp.getItemMeta();
                String displayName = "&e&l" + s.getName();

                spMeta.setDisplayName(Main.color(displayName));
                lore = new ArrayList<>();
                if (main.getCM().getPClassFromString(c).getSuperSkills().size() > index) {
                    lore.add(Main.color("&bUpgraded Form: &f" + main.getRP(p).getPClass().getSuperSkills().get(index).getName()));
                    lore.add("");
                } else {
                    lore.add(Main.color("&bUpgrade not Implemented"));
                    lore.add("");
                }

                index++;

                DecimalFormat dF = new DecimalFormat("#.##");
                if (s.getManaCost() > 0) {
                    lore.add(Main.color("&bMana Cost: &f" + s.getManaCost()));
                }
                if (s.getToggleMana() > 0) {
                    lore.add(Main.color("&bToggle Mana Cost: &f" + s.getToggleMana()));
                }
                if (s.getToggleTicks() > 0) {
                    lore.add(Main.color("&eToggle Tick Rate: &f" + dF.format((s.getToggleTicks() * 1.0) / 20.0) + " seconds"));
                }
                if (s.getWarmup() > 0) {
                    lore.add(Main.color("&eWarmup: &f" + dF.format((s.getWarmup() * 1.0) / 20.0) + " seconds"));
                }
                if (s.getCooldown() > 0) {
                    lore.add(Main.color("&eCooldown: &f" + dF.format((s.getCooldown() * 1.0) / 20.0) + " seconds"));
                }
                lore.add(Main.color(""));
                for (String st : s.getDescription(p)) {
                    lore.add(Main.color(st));
                }

                spMeta.setLore(lore);
                sp.setItemMeta(spMeta);
                playerInv.setItem(i, sp);
                i++;
            }
            i = 19;
            index = 0;
            for (Skill s : main.getCM().getPClassFromString(c).getSuperSkills()) {
                Material mat = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
                ItemStack sp = new ItemStack(mat);
                ItemMeta spMeta = sp.getItemMeta();

                String displayName = "&b&l" + s.getName();

                spMeta.setDisplayName(Main.color(displayName));
                lore = new ArrayList<>();
                lore.add(Main.color("&bUpgraded Skill"));
                lore.add(Main.color(""));

                index++;

                DecimalFormat dF = new DecimalFormat("#.##");
                if (s.getManaCost() > 0) {
                    lore.add(Main.color("&bMana Cost: &f" + s.getManaCost()));
                }
                if (s.getToggleMana() > 0) {
                    lore.add(Main.color("&bToggle Mana Cost: &f" + s.getToggleMana()));
                }
                if (s.getToggleTicks() > 0) {
                    lore.add(Main.color("&eToggle Tick Rate: &f" + dF.format((s.getToggleTicks() * 1.0) / 20.0) + " seconds"));
                }
                if (s.getWarmup() > 0) {
                    lore.add(Main.color("&eWarmup: &f" + dF.format((s.getWarmup() * 1.0) / 20.0) + " seconds"));
                }
                if (s.getCooldown() > 0) {
                    lore.add(Main.color("&eCooldown: &f" + dF.format((s.getCooldown() * 1.0) / 20.0) + " seconds"));
                }
                lore.add(Main.color(""));
                for (String st : s.getDescription(p)) {
                    lore.add(Main.color(st));
                }

                spMeta.setLore(lore);
                sp.setItemMeta(spMeta);
                playerInv.setItem(i, sp);
                i++;
            }
            p.openInventory(playerInv);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("§e§lSkills")) {
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
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
            e.setCancelled(true);
        }
    }

}
