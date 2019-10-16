package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BiomeLevelCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Map<Biome, LevelRange> bl = MobEXP.getBL();
            Biome b = p.getLocation().getBlock().getBiome();
            Main.msg(p, "");
            Main.msg(p, "&8» &eCurrent Biome: &f" + b.name() + " &8(&eLv. " + bl.get(b).getMin() + " &fto &eLv. " + bl.get(b).getMax() + "&8)");
            Main.msg(p, "");
        }
        return false;
    }

}
