package com.java.rpg.classes.commands.playerinfo;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import net.minecraft.server.v1_15_R1.EnchantmentMending;
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
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

            ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta phM = ph.getItemMeta();
            phM.setDisplayName(" ");
            ph.setItemMeta(phM);
            for (int i = 0; i < 9; i++) {
                playerInv.setItem(i, ph);
            }
            for (int i = 27; i < 36; i++) {
                playerInv.setItem(i, ph);
            }

            for (int i = 10; i <= 19; i+=9) {
                playerInv.setItem(i, ph);
            }

            for (int i = 16; i <= 25; i+=9) {
                playerInv.setItem(i, ph);
            }

            playerInv.setItem(26, ph);

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

            playerInv.setItem(17, info);

            int i = 11;
            int index = 0;
            for (Skill s : rp.getPClass().getSkills()) {
                Material mat = s.getSkillIcon();
                if (main.getSkillLevel(p, s.getName()) > 0) {
                    mat = Material.GRAY_DYE;
                }
                sp = new ItemStack(mat);
                spMeta = sp.getItemMeta();
                if (main.getSkillLevel(p, s.getName()) == 0 && s.getLevel() <= rp.getLevel()) {
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
            i = 20;
            index = 0;
            for (Skill s : rp.getPClass().getSuperSkills()) {
                Material mat = Material.GRAY_DYE;
                if (main.getSkillLevel(p, s.getName()) > 0) {
                    mat = s.getSkillIcon();
                }
                sp = new ItemStack(mat);
                spMeta = sp.getItemMeta();
                if (main.getSkillLevel(p, s.getName()) > 0) {
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
            for (int i = 0; i < 9; i++) {
                ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
                ItemMeta phM = ph.getItemMeta();
                phM.setDisplayName(" ");
                ph.setItemMeta(phM);
                playerInv.setItem(i, ph);
            }
            for (int i = 27; i < 36; i++) {
                ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
                ItemMeta phM = ph.getItemMeta();
                phM.setDisplayName(" ");
                ph.setItemMeta(phM);
                playerInv.setItem(i, ph);
            }

            for (int i = 10; i <= 19; i+=9) {
                ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
                ItemMeta phM = ph.getItemMeta();
                phM.setDisplayName(" ");
                ph.setItemMeta(phM);
                playerInv.setItem(i, ph);
            }
            for (int i = 9; i <= 18; i+=9) {
                ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
                ItemMeta phM = ph.getItemMeta();
                phM.setDisplayName(" ");
                ph.setItemMeta(phM);
                playerInv.setItem(i, ph);
            }

            for (int i = 16; i <= 25; i+=9) {
                ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
                ItemMeta phM = ph.getItemMeta();
                phM.setDisplayName(" ");
                ph.setItemMeta(phM);
                playerInv.setItem(i, ph);
            }

            for (int i = 17; i <= 26; i+=9) {
                ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
                ItemMeta phM = ph.getItemMeta();
                phM.setDisplayName(" ");
                ph.setItemMeta(phM);
                playerInv.setItem(i, ph);
            }

            int i = 11;
            int index = 0;
            for (Skill s : main.getCM().getPClassFromString(c).getSkills()) {
                Material mat = s.getSkillIcon();
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
            i = 20;
            index = 0;
            for (Skill s : main.getCM().getPClassFromString(c).getSuperSkills()) {
                Material mat = s.getSkillIcon();
                ItemStack sp = new ItemStack(mat);
                ItemMeta spMeta = sp.getItemMeta();

                String displayName = "&b&l" + s.getName();

                spMeta.setDisplayName(Main.color(displayName));
                spMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                spMeta.addEnchant(Enchantment.MENDING, 1, true);
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
        }
    }

}
