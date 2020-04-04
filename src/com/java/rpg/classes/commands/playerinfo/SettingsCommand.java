package com.java.rpg.classes.commands.playerinfo;

import com.java.Main;
import com.java.rpg.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
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

public class SettingsCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void cancelClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("§e§lSETTINGS")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().hasItemMeta()) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Skill Cast Slot")) {
                    Player p = (Player) e.getWhoClicked();
                    sendSkillcastInv(p);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Send Exp in Chat")) {
                    Player p = (Player) e.getWhoClicked();

                    main.getRP(p).setSendExp(!main.getRP(p).getSendExp());
                    Main.msg(p, "&aSend Exp in Chat: &f" + main.getRP(p).getSendExp());

                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Shift-Offhand Swaps Offhand")) {
                    Player p = (Player) e.getWhoClicked();

                    main.getRP(p).setToggleOffhand(!main.getRP(p).getToggleOffhand());
                    Main.msg(p, "&aShift-Offhand swaps Offhand: &f" + main.getRP(p).getToggleOffhand());

                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                }
            }
        }

        if (e.getView().getTitle().contains("§e§lSKILLCAST SLOT")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Slot ")) {
                Player p = (Player) e.getWhoClicked();
                String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                if (name.contains("(SELECTED)")) {
                    name = name.substring(0, name.indexOf(" ("));
                }
                int slot = Integer.valueOf(name.replace("Slot ", "")) - 1;

                if (slot == main.getRP(p).getIdleSlot()) {
                    Main.msg(p, "&cYou have already selected this slot.");
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.25F);
                } else {
                    Main.msg(p, "&aYou changed your Skill Cast Center Slot to " + (slot + 1) + "!");
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                    main.getRP(p).setIdleSlot(slot);
                }

                sendSkillcastInv((Player) e.getWhoClicked());
            }
        }
    }

    public void sendSkillcastInv(Player p) {
        Inventory playerInv = Bukkit.createInventory(null, 9, Main.color("&e&lSKILLCAST SLOT"));
        ArrayList<String> lore = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            ItemStack slot = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta slotMeta = slot.getItemMeta();
            slotMeta.setDisplayName(Main.color("&eSlot " + (i + 1)));
            lore = new ArrayList<>();
            lore.add(Main.color(""));
            lore.add(Main.color("&fCenter around &eSlot " + (i + 1) + "&f."));
            lore.add(Main.color(""));

            if (i == main.getRP(p).getIdleSlot()) {
                slot = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                lore = new ArrayList<>();
                slotMeta = slot.getItemMeta();
                slotMeta.setDisplayName(Main.color("&eSlot " + (i + 1) + " &a(SELECTED)"));
                lore.add(Main.color(""));
                lore.add(Main.color("&fPress the offhand key &8(&eDefault: &fF&8)"));
                lore.add(Main.color(""));
            }

            slotMeta.setLore(lore);
            slot.setItemMeta(slotMeta);

            playerInv.setItem(0 + i, slot);
        }

        p.openInventory(playerInv);
    }

    public void sendSettingsInv(Player p) {
        Inventory playerInv = Bukkit.createInventory(null, 27, Main.color("&e&lSETTINGS MENU"));
        ArrayList<String> lore = new ArrayList<>();

        ItemStack slot = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta slotMeta = slot.getItemMeta();
        slotMeta.setDisplayName(Main.color("&eSkill Cast Slot"));
        lore.add(Main.color(""));
        lore.add(Main.color("&fChange the &edefault slot &fwhen casting!"));
        lore.add(Main.color(""));

        slotMeta.setLore(lore);
        slot.setItemMeta(slotMeta);

        playerInv.setItem(10, slot);

        lore = new ArrayList<>();

        ItemStack sendExp = new ItemStack(Material.PAPER);
        ItemMeta sendExpMeta = sendExp.getItemMeta();
        sendExpMeta.setDisplayName(Main.color("&eSend Exp in Chat"));
        lore.add(Main.color(""));
        lore.add(Main.color("&fToggle sending of Exp Messages in Chat!"));
        lore.add(Main.color(""));

        sendExpMeta.setLore(lore);
        sendExp.setItemMeta(sendExpMeta);

        playerInv.setItem(11, sendExp);

        lore = new ArrayList<>();

        ItemStack toggleOffhand = new ItemStack(Material.SHIELD);
        ItemMeta toggleOffhandMeta = toggleOffhand.getItemMeta();
        toggleOffhandMeta.setDisplayName(Main.color("&eShift-Offhand Swaps Offhand"));
        lore.add(Main.color(""));
        lore.add(Main.color("&fToggle whether crouching and using the offhand key"));
        lore.add(Main.color("&fwill swap your offhand or open the Skill Casting GUI."));
        lore.add(Main.color(""));

        toggleOffhandMeta.setLore(lore);
        toggleOffhand.setItemMeta(toggleOffhandMeta);

        playerInv.setItem(12, toggleOffhand);

        p.openInventory(playerInv);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            sendSettingsInv(p);
        }
        return false;
    }

}
