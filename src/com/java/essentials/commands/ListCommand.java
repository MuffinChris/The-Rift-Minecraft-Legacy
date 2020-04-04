package com.java.essentials.commands;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Main.msg(p, "&8» &eOnline Players: &f" + Bukkit.getOnlinePlayers().size() + "&8/&f" + Bukkit.getMaxPlayers());
            String ownerout = "";
            String adminout = "";
            String modout = "";
            String helperout = "";
            String playerout = "";

            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("core.owner") && !pl.isOp() || pl.getName().equalsIgnoreCase("eChris")) {
                    ownerout+="&f" + pl.getName() + "&f, &f";
                } else if (pl.hasPermission("core.admin")) {
                    adminout+="&f" + pl.getName() + "&f, &f";
                } else if (pl.hasPermission("core.mod")) {
                    modout+="&f" + pl.getName() + "&f, &f";
                } else if (pl.hasPermission("core.helper")) {
                    helperout+="&f" + pl.getName() + "&f, &f";
                } else {
                    playerout+="&f" + pl.getName() + "&f, &f";
                }
            }

            if (!ownerout.isEmpty()) {
                ownerout = ownerout.substring(0, ownerout.length() - 6);
                Main.msg(p, "&8» &6Owners&8: " + ownerout);
            }

            if (!adminout.isEmpty()) {
                adminout = adminout.substring(0, adminout.length() - 6);
                Main.msg(p, "&8» &cAdmins&8: " + adminout);
            }

            if (!modout.isEmpty()) {
                modout = modout.substring(0, modout.length() - 6);
                Main.msg(p, "&8» &bModerators&8: " + modout);
            }

            if (!helperout.isEmpty()) {
                helperout = helperout.substring(0, helperout.length() - 6);
                Main.msg(p, "&8» &dHelpers&8: " + helperout);
            }

            if (!playerout.isEmpty()) {
                playerout = playerout.substring(0, playerout.length() - 6);
                Main.msg(p, "&8» &ePlayers&8: " + playerout);
            }
        } else {
            Main.so( "&8» &eOnline Players: &f" + Bukkit.getOnlinePlayers().size() + "&8/&f" + Bukkit.getMaxPlayers());
            String ownerout = "";
            String adminout = "";
            String modout = "";
            String helperout = "";
            String playerout = "";

            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("core.owner") && !pl.isOp() || pl.getName().equalsIgnoreCase("eChris")) {
                    ownerout+="&f" + pl.getName() + "&f, &f";
                } else if (pl.hasPermission("core.admin")) {
                    adminout+="&f" + pl.getName() + "&f, &f";
                } else if (pl.hasPermission("core.mod")) {
                    modout+="&f" + pl.getName() + "&f, &f";
                } else if (pl.hasPermission("core.helper")) {
                    helperout+="&f" + pl.getName() + "&f, &f";
                } else {
                    playerout+="&f" + pl.getName() + "&f, &f";
                }
            }

            if (!ownerout.isEmpty()) {
                ownerout = ownerout.substring(0, ownerout.length() - 6);
                Main.so( "&8» &6Owners&8: " + ownerout);
            }

            if (!adminout.isEmpty()) {
                adminout = adminout.substring(0, adminout.length() - 6);
                Main.so( "&8» &cAdmins&8: " + adminout);
            }

            if (!modout.isEmpty()) {
                modout = modout.substring(0, modout.length() - 6);
                Main.so( "&8» &bModerators&8: " + modout);
            }

            if (!helperout.isEmpty()) {
                helperout = helperout.substring(0, helperout.length() - 6);
                Main.so( "&8» &dHelpers&8: " + helperout);
            }

            if (!playerout.isEmpty()) {
                playerout = playerout.substring(0, playerout.length() - 6);
                Main.so( "&8» &ePlayers&8: " + playerout);
            }
        }
        return false;
    }

}
