package com.java.essentials;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class SeenCommand implements CommandExecutor {


    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                if (Bukkit.getPlayerExact(args[0]) instanceof Player) {
                    Player t = Bukkit.getPlayerExact(args[0]);
                    /*long millis = t.getLastLogin();
                    String time = String.format("%d minutes and %d seconds ago.",
                            TimeUnit.MILLISECONDS.toMinutes(millis),
                            TimeUnit.MILLISECONDS.toSeconds(millis) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                    );*/
                    Main.msg(p, "&cCommand is currenty disabled.");
                    //Main.msg(p, "&8» &e" + t.getName() + " &fwas last seen " + time);
                } else {
                    Main.msg(p, "&cInvalid Target");
                }
            } else {
                Main.msg(p, "Usage: /seen <player>");
            }
        } else {
            // CONSOLE SHIT THNX!
        }
        return false;
    }

}
