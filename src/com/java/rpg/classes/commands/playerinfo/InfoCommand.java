package com.java.rpg.classes.commands.playerinfo;

import com.java.Main;
import com.java.rpg.classes.utility.RPGConstants;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

public class InfoCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                sendInfoInv(p, p);
            } else {
                if (Bukkit.getPlayer(args[0]) != null) {
                    sendInfoInv(p, Bukkit.getPlayer(args[0]));
                } else {
                    Main.msg(p, "&cInvalid Player.");
                }
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.color("&cInvalid command for Console (opens GUI)"));
        }
        return false;
    }

    public void sendInfoInv(Player p, Player t) {
        RPGPlayer rp = main.getRP(t);
        Inventory playerInv = Bukkit.createInventory(null, 27, Main.color("&e&l" + t.getName() + "'s &e&lPlayer Info"));

        List<String> lore;
        ItemStack sk = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skMeta = (SkullMeta) sk.getItemMeta();
        skMeta.setOwningPlayer(t);
        skMeta.setDisplayName(Main.color("&e" + t.getName()));
        lore = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat dF = new DecimalFormat("#");
        lore.add("");
        lore.add(Main.color("&8» &eClass: &f" + rp.getPClass().getName()));
        lore.add(Main.color("&8» &eLevel: &f" + rp.getLevel()));
        lore.add(Main.color("&8» &aExp: &f" + rp.getPrettyExp() + " &8/ &f" + rp.getPrettyMaxExp() + " &8(&a" + rp.getPrettyPercent() + "%&8)"));
        lore.add("");
        if (p.equals(t) || (main.getPM().hasParty(p) && main.getPM().getParty(p).hasPlayer(t))) {
            lore.add(Main.color("&8» &cHP: &f" + df.format(t.getHealth()) + "&8/" + "&c" + df.format(t.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())));
            lore.add(Main.color("&8» &bMana: &f" + dF.format(rp.getCMana()) + "&8/&b" + dF.format(rp.getPClass().getCalcMana(rp.getLevel()))));
            lore.add(Main.color("&8» &9Mana Regen: &f" + df.format(rp.getPClass().getCalcManaRegen(rp.getLevel())) + "/s"));
            //lore.add(Main.color("&8» &dWalkspeed: &f" + dF.format(rp.getWalkspeed().getValue() + rp.getWalkSpeedS().getValue())));
            //lore.add(Main.color("&8» &eMax Armor Weight: &f" + rp.getPClass().getWeight()));
            int total = rp.calculateSP();
            lore.add(Main.color("&8» &6Skillpoints: &f" + (total) + " &8(&6" + (2 - total) + " Used&8)"));
        }
        skMeta.setLore(lore);
        sk.setItemMeta(skMeta);
        playerInv.setItem(4, sk);

        ItemStack sp;
        ItemMeta spMeta;

        sp = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        spMeta = sp.getItemMeta();
        lore = new ArrayList<>();
        spMeta.setDisplayName(" ");
        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);

        playerInv.setItem(9, sp);
        playerInv.setItem(17, sp);
        for (int i = 0; i < 4; i++) {
            playerInv.setItem(i, sp);
        }
        for (int i = 5; i < 9; i++) {
            playerInv.setItem(i, sp);
        }
        for (int i = 18; i < 27; i++) {
            playerInv.setItem(i, sp);
        }


        //-------------------------------

        sp = new ItemStack(Material.IRON_SWORD);
        spMeta = sp.getItemMeta();
        lore = new ArrayList<>();

        spMeta.setDisplayName(Main.color("&eAttack Stats"));
        sp.setType(Material.IRON_SWORD);
        df = new DecimalFormat("#.##");
        double ad = rp.getAD(); // + bonusAD from items later
        double ap = rp.getAP();
        lore.add(Main.color(""));
        lore.add(Main.color("&8» &cAttack Damage: &f" + df.format(ad)));
        lore.add(Main.color("&8» &bAbility Power: &f" + df.format(ap)));
        spMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(10, sp);

        //-------------------------------

        sp = new ItemStack(Material.STONE);
        spMeta = sp.getItemMeta();
        lore = new ArrayList<>();

        double hp = t.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double armor = rp.getArmor(); // + bonus armor etc
        double mr = rp.getMR(); // + bonus mr etc
        double air = rp.getAirDefense();
        double earth = rp.getEarthDefense();
        double electric = rp.getElectricDefense();
        double fire = rp.getFireDefense();
        double ice = rp.getIceDefense();

        String mrper = Main.color("&9" + df.format(100.0 * (1-(RPGConstants.defenseDiv/(RPGConstants.defenseDiv+mr)))) + "%");
        String amper = Main.color("&6" + df.format(100.0 * (1-(RPGConstants.defenseDiv/(RPGConstants.defenseDiv+armor)))) + "%");

        String airPer = Main.color("&f" + df.format(100.0 * (1-(RPGConstants.defenseDiv/(RPGConstants.defenseDiv+air)))) + "%");
        String earthPer = Main.color("&2" + df.format(100.0 * (1-(RPGConstants.defenseDiv/(RPGConstants.defenseDiv+earth)))) + "%");
        String electricPer = Main.color("&e" + df.format(100.0 * (1-(RPGConstants.defenseDiv/(RPGConstants.defenseDiv+electric)))) + "%");
        String firePer = Main.color("&c" + df.format(100.0 * (1-(RPGConstants.defenseDiv/(RPGConstants.defenseDiv+fire)))) + "%");
        String icePer = Main.color("&b" + df.format(100.0 * (1-(RPGConstants.defenseDiv/(RPGConstants.defenseDiv+ice)))) + "%");

        spMeta.setDisplayName(Main.color("&eDefense Stats"));
        sp.setType(Material.IRON_CHESTPLATE);
        lore.add(Main.color(""));
        lore.add(Main.color("&8[" + RPGConstants.physical + "&8] &7Hitpoints: &f" + df.format(hp)));
        lore.add(Main.color("&8[&6" + RPGConstants.armor + "&8] &7Armor: &f" + df.format(armor) + " &8(" + amper + "&8)"));
        lore.add(Main.color("&8[&9" + RPGConstants.armor + "&8] &7Magic Resist: &f" + df.format(mr) + " &8(" + mrper + "&8)"));
        lore.add("");
        lore.add(Main.color("&8[" + RPGConstants.air + "&8] &7Air Defense: &f" + df.format(air) + " &8(" + airPer + "&8)"));
        lore.add(Main.color("&8[" + RPGConstants.earth +  "&8] &7Earth Defense: &f" + df.format(earth) + " &8(" + earthPer + "&8)"));
        lore.add(Main.color("&8[" + RPGConstants.electric +  "&8] &7Electric Defense: &f" + df.format(electric) + " &8(" + electricPer + "&8)"));
        lore.add(Main.color("&8["  + RPGConstants.fire + "&8] &7Fire Defense: &f" + df.format(fire) + " &8(" + firePer + "&8)"));
        lore.add(Main.color("&8[" + RPGConstants.ice +  "&8] &7Ice Defense: &f" + df.format(ice) + " &8(" + icePer + "&8)"));


        spMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(11, sp);

        //-------------------------------
        sp = new ItemStack(Material.STONE);
        spMeta = sp.getItemMeta();
        lore = new ArrayList<>();

        spMeta.setDisplayName(Main.color("&ePlayer Stats"));
        sp.setType(Material.BOOK);
        lore.add(Main.color(""));
        lore.add(Main.color("&cNot Implemented"));
        // KDA, Blocks Traveled, etc

        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(12, sp);

        //-------------------------------
        sp = new ItemStack(Material.STONE);
        spMeta = sp.getItemMeta();
        lore = new ArrayList<>();

        spMeta.setDisplayName(Main.color("&eTown Info"));
        sp.setType(Material.BEACON);
        lore.add(Main.color(""));
        lore.add(Main.color("&cNot Implemented"));

        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(13, sp);

        //-------------------------------
        sp = new ItemStack(Material.STONE);
        spMeta = sp.getItemMeta();
        lore = new ArrayList<>();

        spMeta.setDisplayName(Main.color("&eInformation Not Implemented"));
        sp.setType(Material.STONE);
        lore.add(Main.color(""));

        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(14, sp);

        //-------------------------------
        sp = new ItemStack(Material.STONE);
        spMeta = sp.getItemMeta();
        lore = new ArrayList<>();

        spMeta.setDisplayName(Main.color("&eInformation Not Implemented"));
        sp.setType(Material.STONE);
        lore.add(Main.color(""));

        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(15, sp);

        //-------------------------------
        sp = new ItemStack(Material.STONE);
        spMeta = sp.getItemMeta();
        lore = new ArrayList<>();

        spMeta.setDisplayName(Main.color("&eInformation Not Implemented"));
        sp.setType(Material.STONE);
        lore.add(Main.color(""));

        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(16, sp);

        //-------------------------------

        p.openInventory(playerInv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("§e§lPlayer Info")) {
            e.setCancelled(true);
        }
    }

}

