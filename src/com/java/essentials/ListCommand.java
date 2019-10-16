package com.java.essentials;

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
            List<String> players = new ArrayList<String>();
            for (Player pl : Bukkit.getOnlinePlayers()) {
                players.add(pl.getName());
            }
            Main.msg(p, "&8» &eOnline Players: &f" + players.size() + "&8/&f" + Bukkit.getMaxPlayers());
            String out = "";
            int index = 0;
            for (String s : players) {
                if (index + 1 == players.size()) {
                    out += "&f" + s + ".";
                } else {
                    out += "&f" + s + "&f, &f";
                }
                index++;
            }
            Main.msg(p, "&8» &ePlayers: " + out);
        } else {
            List<String> players = new ArrayList<String>();
            for (Player pl : Bukkit.getOnlinePlayers()) {
                players.add(pl.getName());
            }
            Main.so( "&8» &eOnline Players: &f" + players.size() + "&8/&f" + Bukkit.getMaxPlayers());
            String out = "";
            int index = 0;
            for (String s : players) {
                if (index + 1 == players.size()) {
                    out += "&f" + s + ".";
                } else {
                    out += "&f" + s + "&f, &f";
                }
                index++;
            }
            Main.so( "&8» &ePlayers: " + out);
        }
        return false;
    }

}
