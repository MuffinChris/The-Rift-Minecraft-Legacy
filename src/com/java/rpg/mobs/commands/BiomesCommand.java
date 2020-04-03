package com.java.rpg.mobs.commands;

import com.java.Main;
import com.java.rpg.classes.utility.LevelRange;
import com.java.rpg.mobs.MobEXP;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class BiomesCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Map<Biome, LevelRange> bl = MobEXP.getBL();
            if (args.length == 0) {
                int i = 0;
                Main.msg(p, "&7>>----- &eBiomes Page 1 &7-----<<");
                for (Biome b : bl.keySet()) {
                    i++;
                    if (i > 5) {
                        Main.msg(p, "&e/biomes <page> for more!");
                        return false;
                    }
                    Main.msg(p, "&eBiome: &f" + b.name() + " &e--> &8(&eLv. " + bl.get(b).getMin() + " &fto &eLv. " + bl.get(b).getMax() + "&8)");
                }
            } else {
                if (Integer.valueOf(args[0]) instanceof Integer && Integer.valueOf(args[0]) > 0) {
                    int page = Integer.valueOf(args[0]);
                    page--;
                    int i = 0;
                    Main.msg(p, "&7>>----- &eBiomes Page " + (page + 1) + " &7-----<<");
                    for (Biome b : bl.keySet()) {
                        i++;
                        if (i > 5 * page + 5) {
                            return false;
                        }
                        if (i > page * 5) {
                            Main.msg(p, "&eBiome: &f" + b.name() + " &e--> &8(&eLv. " + bl.get(b).getMin() + " &fto &eLv. " + bl.get(b).getMax() + "&8)");
                        }
                    }
                } else {
                    Main.msg(p, "&cError, please input a valid page number.");
                }
            }
        }
        return false;
    }

}
