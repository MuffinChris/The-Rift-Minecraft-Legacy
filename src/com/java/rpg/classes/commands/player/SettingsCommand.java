package com.java.rpg.classes.commands.player;

import com.java.Main;
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

                    p.closeInventory();
                    sendSettingsInv(p);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Exp Messages")) {
                    Player p = (Player) e.getWhoClicked();

                    main.getRP(p).setSendExp(!main.getRP(p).getSendExp());
                    Main.msg(p, "&aExp Messages: &f" + ("" + main.getRP(p).getSendExp()).toUpperCase());

                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);

                    p.closeInventory();
                    sendSettingsInv(p);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Crouch Offhand Key")) {
                    Player p = (Player) e.getWhoClicked();

                    main.getRP(p).setToggleOffhand(!main.getRP(p).getToggleOffhand());
                    Main.msg(p, "&aCrouch Offhand Key: &f" + ("" + main.getRP(p).getToggleOffhand()).toUpperCase());

                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);

                    p.closeInventory();
                    sendSettingsInv(p);
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
        lore.add(Main.color("&fCurrently &eSlot " + (main.getRP(p).getIdleSlot() + 1) + "&f."));
        lore.add(Main.color("&fChange the &eslot &fmoved to when casting &8(&eOffhand Key&8)"));
        lore.add(Main.color(""));

        slotMeta.setLore(lore);
        slot.setItemMeta(slotMeta);

        playerInv.setItem(10, slot);

        lore = new ArrayList<>();

        ItemStack sendExp = new ItemStack(Material.PAPER);
        ItemMeta sendExpMeta = sendExp.getItemMeta();
        sendExpMeta.setDisplayName(Main.color("&eExp Messages &8(&e" + ("" + main.getRP(p).getSendExp()).toUpperCase() + "&8)"));
        lore.add(Main.color(""));
        lore.add(Main.color("&fToggle sending of Exp Gain messages in Chat."));
        lore.add(Main.color(""));

        sendExpMeta.setLore(lore);
        sendExp.setItemMeta(sendExpMeta);

        playerInv.setItem(11, sendExp);

        lore = new ArrayList<>();

        ItemStack toggleOffhand = new ItemStack(Material.SHIELD);
        ItemMeta toggleOffhandMeta = toggleOffhand.getItemMeta();
        toggleOffhandMeta.setDisplayName(Main.color("&eCrouch Offhand Key &8(&e" + ("" + main.getRP(p).getToggleOffhand()).toUpperCase() + "&8)"));
        lore.add(Main.color(""));
        lore.add(Main.color("&fToggle whether crouching and using the offhand key"));
        lore.add(Main.color("&fwill &eswap your offhand &for open the &eSkill Casting Menu&f."));
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
