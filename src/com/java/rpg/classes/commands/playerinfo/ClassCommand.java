package com.java.rpg.classes.commands.playerinfo;

import com.java.Main;
import com.java.rpg.classes.PlayerClass;
import com.java.rpg.classes.RPGPlayer;
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

public class ClassCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            sendClassInv(p);
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.color("&cInvalid command for Console"));
        }
        return false;
    }

    public static String hpString = "&8[&c♥&8] &7HP: &f";
    public static String manaString = "&8[&b✦&8] &7Mana: &f";
    public static String manaRegenString = "&8[&b⚙&8] &7Mana Regen: &f";
    public static String apString = "&8[&b⚡&8] &7Ability Power: &f";
    public static String adString = "&8[&c⚔&8] &7Attack Damage: &f";
    public static String armorString = "&8[&4⛨&8] &7Armor: &f";
    public static String mrString = "&8[&9⚶&8] &7Magic Resist: &f";

    public void sendClassInv(Player p) {
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat dF = new DecimalFormat("#");
        RPGPlayer rp = main.getRP(p);
        Inventory playerInv = Bukkit.createInventory(null, 36, Main.color("&e&lClass Menu"));
        ArrayList<String> lore;
        ItemStack sp;
        ItemMeta spMeta;

        //-------------------------------

        sp = new ItemStack(Material.STONE);
        spMeta = sp.getItemMeta();
        lore = new ArrayList<>();

        PlayerClass pc = main.getCM().getPClassFromString("Wanderer");
        double baseHp = pc.getBaseHP();
        double hpPerLvl = pc.getHpPerLevel();
        double mana = pc.getMana();
        double manaPerLvl = pc.getManaPerLevel();
        double mreg = pc.getManaRegen();
        double mregPerLvl = pc.getManaRegenPerLevel();
        double armor = pc.getCalcArmor(0);
        double armorPerLvl = pc.getArmorPerLevel();
        double mr = pc.getCalcMR(0);
        double mrPerLvl = pc.getMagicResistPerLevel();
        double ap = pc.getBaseAP();
        double ad = pc.getBaseAD();
        double apPerLvl = pc.getAPPerLevel();
        double adPerLvl = pc.getADPerLevel();

        spMeta.setDisplayName(Main.color("&6Wanderer"));
        sp.setType(Material.LEATHER_HELMET);
        lore.add(Main.color("&aLost Soul"));
        lore.add(Main.color(""));
        lore.add(Main.color("&fThe Wanderer has no true path or focus."));
        lore.add(Main.color("&fAll wanderers eventually find a new Class."));
        lore.add(Main.color(""));
        lore.add(Main.color(hpString + df.format(baseHp) + " &8(&7+" + df.format(hpPerLvl) + "/lvl&8)"));
        lore.add(Main.color(manaString + dF.format(mana) + " &8(&7+" + df.format(manaPerLvl) + "/lvl&8)"));
        lore.add(Main.color(manaRegenString + df.format(mreg) + " &8(&7+" + df.format(mregPerLvl) + "/lvl&8)"));
        lore.add(Main.color(""));
        lore.add(Main.color(adString + dF.format(ad) + " &8(&7+" + df.format(adPerLvl) + "/lvl&8)"));
        lore.add(Main.color(apString + df.format(ap) + " &8(&7+" + df.format(apPerLvl) + "/lvl&8)"));
        lore.add(Main.color(""));
        lore.add(Main.color(armorString + dF.format(armor) + " &8(&7+" + df.format(armorPerLvl) + "/lvl&8)"));
        lore.add(Main.color(mrString + dF.format(mr) + " &8(&7+" + df.format(mrPerLvl) + "/lvl&8)"));
        lore.add(Main.color(""));
        lore.add(Main.color("&fView its Skills with &e/skills Wanderer&f."));
        spMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (ChatColor.stripColor(spMeta.getDisplayName()).equalsIgnoreCase(main.getRP(p).getPClass().getName())) {
            spMeta.addEnchant(Enchantment.MENDING, 1, true);
            spMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(4, sp);

        //-------------------------------

        sp = new ItemStack(Material.STONE);
        spMeta = sp.getItemMeta();
        lore = new ArrayList<>();

        pc = main.getCM().getPClassFromString("Pyromancer");
        baseHp = pc.getBaseHP();
        hpPerLvl = pc.getHpPerLevel();
        mana = pc.getMana();
        manaPerLvl = pc.getManaPerLevel();
        mreg = pc.getManaRegen();
        mregPerLvl = pc.getManaRegenPerLevel();
        armor = pc.getCalcArmor(0);
        armorPerLvl = pc.getArmorPerLevel();
        mr = pc.getCalcMR(0);
        mrPerLvl = pc.getMagicResistPerLevel();
        ap = pc.getBaseAP();
        ad = pc.getBaseAD();
        apPerLvl = pc.getAPPerLevel();
        adPerLvl = pc.getADPerLevel();


        spMeta.setDisplayName(Main.color("&6Pyromancer"));
        sp.setType(Material.FIRE_CHARGE);
        lore.add(Main.color("&bMagic Spellcaster"));
        lore.add(Main.color("&f"));
        lore.add(Main.color("&fPyromancer's have powerful short range skills"));
        lore.add(Main.color("&fthat decimate nearby enemies by igniting them."));
        lore.add(Main.color(""));
        lore.add(Main.color(hpString + df.format(baseHp) + " &8(&7+" + df.format(hpPerLvl) + "/lvl&8)"));
        lore.add(Main.color(manaString + dF.format(mana) + " &8(&7+" + df.format(manaPerLvl) + "/lvl&8)"));
        lore.add(Main.color(manaRegenString + df.format(mreg) + " &8(&7+" + df.format(mregPerLvl) + "/lvl&8)"));
        lore.add(Main.color(""));
        lore.add(Main.color(adString + dF.format(ad) + " &8(&7+" + df.format(adPerLvl) + "/lvl&8)"));
        lore.add(Main.color(apString + df.format(ap) + " &8(&7+" + df.format(apPerLvl) + "/lvl&8)"));
        lore.add(Main.color(""));
        lore.add(Main.color(armorString + dF.format(armor) + " &8(&7+" + df.format(armorPerLvl) + "/lvl&8)"));
        lore.add(Main.color(mrString + dF.format(mr) + " &8(&7+" + df.format(mrPerLvl) + "/lvl&8)"));
        lore.add(Main.color(""));
        lore.add(Main.color("&fView its Skills with &e/skills Pyromancer&f."));
        if (ChatColor.stripColor(spMeta.getDisplayName()).equalsIgnoreCase(main.getRP(p).getPClass().getName())) {
            spMeta.addEnchant(Enchantment.MENDING, 1, true);
            spMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(10, sp);

        //-------------------------------

        p.openInventory(playerInv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("§e§lClass Menu")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§6")) {

                    if (main.getCM().getPClassFromString(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) != null) {
                        if (!main.getPC().get(e.getWhoClicked().getUniqueId()).changeClass(main.getCM().getPClassFromString(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())))) {
                            Main.msg((Player) e.getWhoClicked(), "&cYou are already that class!");
                            ((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.5F);
                            return;
                        }
                        Main.msg((Player) e.getWhoClicked(), "&aYou have selected the " + e.getCurrentItem().getItemMeta().getDisplayName() + " &aclass!");
                        ((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    } else {
                        Main.msg((Player) e.getWhoClicked(), "&cClass does not exist.");
                    }

                    Player p = (Player) e.getWhoClicked();
                    RPGPlayer rp = main.getRP(p);
                    rp.pushFiles();
                    p.closeInventory();
                    //sendClassInv(p);
                }
            }
        }
    }

}
