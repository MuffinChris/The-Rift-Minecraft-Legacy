package com.java.rpg.classes.commands.player;

import com.java.Main;
import com.java.rpg.classes.PlayerClass;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SkillsCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        if (args.length == 0) {
            sendSkillsInv(p, "");
            return true;
        }
        if (main.getCM().getPClassFromString(args[0]) != null) {
            sendSkillsInv(p, main.getCM().getPClassFromString(args[0]).getName());
            return true;
        }

        Main.msg(p, "&cInvalid Class, use /class to see available Classes!");
        return false;
    }

    public void sendSkillsInv(Player p, String c) {
        RPGPlayer rp = main.getRP(p);
        PlayerClass pclass;
        if (c.isEmpty()) {
            pclass = rp.getPClass();
        } else {
            pclass = main.getCM().getPClassFromString(c);
        }
        Inventory playerInv = Bukkit.createInventory(null, 45, Main.color("&e&l" + pclass.getName() + " &e&lSkills"));

        ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta phM = ph.getItemMeta();
        phM.setDisplayName(" ");
        ph.setItemMeta(phM);
        for (int i = 0; i < playerInv.getSize(); i++) {
            playerInv.setItem(i, ph);
        }

        ph = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        phM = ph.getItemMeta();
        phM.setDisplayName(" ");
        ph.setItemMeta(phM);
        for (int i = 20; i < 20 + pclass.getSkills().size(); i++) {
            playerInv.setItem(i, ph);
        }

        List<String> lore;
        ItemStack sp;
        ItemMeta spMeta;

        if (c.isEmpty()) {
            sp = new ItemStack(Material.NETHER_STAR);
            spMeta = sp.getItemMeta();
            spMeta.setDisplayName(Main.color("&6Skillpoints: &f" + rp.calculateSP()));
            lore = new ArrayList<>();
            lore.add(Main.color("&fClick a non-upgraded spell to upgrade it!"));
            lore.add(Main.color("&fUnlocked at levels &e" + RPGConstants.superSkillOne + " &fand &e" + RPGConstants.superSkillTwo + "&f."));
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

            ItemStack info = new ItemStack(Material.BOOK);
            ItemMeta infoM = info.getItemMeta();
            List<String> infoLore = new ArrayList<>();
            infoLore.add(Main.color("&fTo cast skills either:"));
            infoLore.add("");
            infoLore.add(Main.color("&fPress the Offhand Key &8(&eDefault F&8)"));
            infoLore.add(Main.color("&fUse binds &8(&e/bind <skill>&8)"));
            infoLore.add(Main.color("&fCast with commands &8(&e/skill <skill>&8)"));
            infoM.setLore(infoLore);
            infoM.setDisplayName(Main.color("&eCasting Guide"));
            info.setItemMeta(infoM);
            playerInv.setItem(27, info);
        }


        int i = 11;
        for (Skill s : pclass.getSkills()) {
            Material mat = s.getSkillIcon();
            if (main.getSkillLevel(p, s.getName()) > 0) {
                mat = Material.GRAY_DYE;
            }
            sp = new ItemStack(mat);
            spMeta = sp.getItemMeta();
            if (c.isEmpty() && main.getSkillLevel(p, s.getName()) == 0 && s.getLevel() <= rp.getLevel()) {
                spMeta.addEnchant(Enchantment.MENDING, 1, true);
                spMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            boolean upgraded = main.getSkillLevel(p, s.getName()) > 0;
            String displayName = "&e&l" + s.getName();
            String lockStatus = "&aUNLOCKED";
            if (upgraded) {
                displayName = "&e&l" + s.getName();
                lockStatus = "&bUPGRADED";
            }

            spMeta.setDisplayName(Main.color(displayName));
            lore = new ArrayList<>();
            if (c.isEmpty()) {
                if (s.getLevel() <= rp.getLevel()) {
                    lore.add(Main.color(lockStatus));
                    lore.add(Main.color(""));
                    if (s.isUpgradeable()) {
                        lore.add(Main.color("&bUpgraded Form: &7" + s.getUpgradedSkill().getName()));
                        lore.add("");
                    } else {
                        lore.add(Main.color("&bNo Upgrade"));
                        lore.add("");
                    }
                } else {
                    lore.add(Main.color("&cLOCKED &8(&cLVL &f" + s.getLevel() + "&8)"));
                    lore.add(Main.color(""));
                }
            } else {
                if (s.isUpgradeable()) {
                    lore.add(Main.color("&bUpgraded Form: &7" + s.getUpgradedSkill().getName()));
                    lore.add("");
                } else {
                    lore.add(Main.color("&bNo Upgrade"));
                    lore.add("");
                }
                lore.add(Main.color("&eUnlock Level: &7" + s.getLevel()));
                lore.add("");
            }

            DecimalFormat dF = new DecimalFormat("#.##");
            if (s.getManaCost() > 0) {
                lore.add(Main.color("&bMana Cost: &7" + s.getManaCost()));
            }
            if (s.getToggleMana() > 0) {
                lore.add(Main.color("&bToggle Mana Cost: &7" + s.getToggleMana()));
            }
            if (s.getToggleTicks() > 0) {
                lore.add(Main.color("&eToggle Tick Rate: &7" + dF.format((s.getToggleTicks() * 1.0) / 20.0) + " seconds"));
            }
            if (s.getWarmup() > 0) {
                lore.add(Main.color("&eWarmup: &7" + dF.format((s.getWarmup() * 1.0) / 20.0) + " seconds"));
            }
            if (s.getCooldown() > 0) {
                lore.add(Main.color("&eCooldown: &7" + dF.format((s.getCooldown() * 1.0) / 20.0) + " seconds"));
            }
            if (s.getToggleCooldown() != s.getCooldown()) {
                lore.add(Main.color("&eToggle Cooldown: &7" + dF.format((s.getToggleCooldown() * 1.0) / 20.0) + " seconds"));
            }
            lore.add(Main.color(""));
            if (c.isEmpty()) {
                for (String st : s.getDescription(p)) {
                    lore.add(Main.color(st));
                }
            } else {
                for (String st : s.getDescription()) {
                    lore.add(Main.color(st));
                }
            }

            spMeta.setLore(lore);
            sp.setItemMeta(spMeta);
            playerInv.setItem(i, sp);
            i++;
        }
        i = 29;
        for (Skill s : pclass.getUpgradedSkills()) {
            Material mat = Material.GRAY_DYE;
            if (main.getSkillLevel(p, s.getName()) > 0 || !c.isEmpty()) {
                mat = s.getSkillIcon();
            }
            sp = new ItemStack(mat);
            spMeta = sp.getItemMeta();
            if (!c.isEmpty() || main.getSkillLevel(p, s.getName()) > 0) {
                spMeta.addEnchant(Enchantment.MENDING, 1, true);
                spMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            boolean upgraded = main.getSkillLevel(p, s.getName()) > 0;
            String displayName = "&b&l" + s.getName();
            String lockStatus = "&cLOCKED &8(&c1 SP&8)";
            if (upgraded) {
                displayName = "&b&l" + s.getName();
                lockStatus = "&aUNLOCKED";
            }

            spMeta.setDisplayName(Main.color(displayName));
            lore = new ArrayList<>();
            if (c.isEmpty()) {
                if (s.getLevel() <= rp.getLevel()) {
                    lore.add(Main.color(lockStatus));
                    lore.add(Main.color(""));
                } else {
                    lore.add(Main.color("&cLOCKED &8(&cLVL &f" + s.getLevel() + "&8)"));
                    lore.add(Main.color(""));
                }
            } else {
                if (s.isUpgradeable()) {
                    lore.add(Main.color("&bUpgraded Form: &7" + s.getUpgradedSkill().getName()));
                    lore.add("");
                } else {
                    lore.add(Main.color("&bNo Upgrade"));
                    lore.add("");
                }
                lore.add(Main.color("&eUnlock Level: &7" + s.getLevel()));
                lore.add("");
            }
            DecimalFormat dF = new DecimalFormat("#.##");
            if (s.getManaCost() > 0) {
                lore.add(Main.color("&bMana Cost: &7" + s.getManaCost()));
            }
            if (s.getToggleMana() > 0) {
                lore.add(Main.color("&bToggle Mana Cost: &7" + s.getToggleMana()));
            }
            if (s.getToggleTicks() > 0) {
                lore.add(Main.color("&eToggle Tick Rate: &7" + dF.format((s.getToggleTicks() * 1.0) / 20.0) + " seconds"));
            }
            if (s.getWarmup() > 0) {
                lore.add(Main.color("&eWarmup: &7" + dF.format((s.getWarmup() * 1.0) / 20.0) + " seconds"));
            }
            if (s.getCooldown() > 0) {
                lore.add(Main.color("&eCooldown: &7" + dF.format((s.getCooldown() * 1.0) / 20.0) + " seconds"));
            }
            if (s.getToggleCooldown() != s.getCooldown()) {
                lore.add(Main.color("&eToggle Cooldown: &7" + dF.format((s.getToggleCooldown() * 1.0) / 20.0) + " seconds"));
            }
            lore.add(Main.color(""));
            if (c.isEmpty()) {
                for (String st : s.getDescription(p)) {
                    lore.add(Main.color(st));
                }
            } else {
                for (String st : s.getDescription()) {
                    lore.add(Main.color(st));
                }
            }

            spMeta.setLore(lore);
            sp.setItemMeta(spMeta);
            playerInv.setItem(i, sp);
            i++;
        }
        p.openInventory(playerInv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("§e§lSkills")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                if (e.getCurrentItem().getItemMeta().getLore().contains("§cLOCKED §8(§c1 SP§8)")) {
                    Player p = (Player) e.getWhoClicked();
                    RPGPlayer rp = main.getRP(p);
                    int total = rp.calculateSP();
                    if (total > 0) {
                        Skill s = rp.getSkillFromUpgradedSkill(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                        if (s != null) {
                            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
                            rp.getSkillLevels().replace(s.getName(), 1);
                            rp.pushFiles();
                            p.closeInventory();
                            sendSkillsInv(p, "");
                        } else {
                            Main.so("&cERROR: Failed to upgrade &4" + p.getName() + "&c's skill &4" + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()) + "&c.");
                        }
                    } else {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.5F);
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
        }
    }

}
