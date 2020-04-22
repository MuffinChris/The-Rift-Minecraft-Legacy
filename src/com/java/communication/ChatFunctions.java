package com.java.communication;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import com.java.towns.Citizen;
import com.java.towns.Town;
import com.java.towns.TownManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.UUID;

public class ChatFunctions implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void ping(ServerListPingEvent e) {
        File pFile = new File("plugins/Rift/motd.yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (!pData.contains("LineOne")) {
            pData.set("LineOne", "&d    The Rift");
            pData.set("LineTwo", "&d    RPG Survival");
            try {
                pData.save(pFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            e.setMotd(Main.color(pData.getString("LineOne") + "\n" + pData.getString("LineTwo")));
        }

    }

    @EventHandler
    public void joinMessage (PlayerJoinEvent e) {
        Chat c = main.getChat();
        updateName(e.getPlayer());
        String prefix = c.getPlayerPrefix(e.getPlayer());
        if (prefix.length() > 2) {
            prefix+=" ";
        }
        if (e.getPlayer().hasPlayedBefore()) {
            String join = Main.color("   &a\u25B6 &f" + prefix + e.getPlayer().getName() + " " + c.getPlayerSuffix(e.getPlayer()));
            e.setJoinMessage(join);
            Main.so(join);

            File pFile = new File("plugins/Rift/welcome.yml");
            FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
            if (!pData.contains("Welcome")) {
                pData.set("Welcome", "");
                try {
                    pData.save(pFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            String[] welcome = pData.getString("Welcome").split(":newline:");
            new BukkitRunnable() {
                public void run() {
                    for (String s : welcome) {
                        Main.msg(e.getPlayer(), s);
                    }
                }
            }.runTaskLater(main, 1L);

        } else {
            File pFile = new File("plugins/Rift/joins.yml");
            FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
            if (!pData.contains("Joins")) {
                pData.set("Joins", 1);
                try {
                    pData.save(pFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                pData.set("Joins", pData.getInt("Joins") + 1);
                try {
                    pData.save(pFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            Bukkit.broadcastMessage("");
            String join = Main.color("&8» &f" + prefix + e.getPlayer().getName() + " " + c.getPlayerSuffix(e.getPlayer()) + " &8(#" + pData.getInt("Joins") + "&8)" + " &ehas joined the server for the first time!");
            Bukkit.broadcastMessage(join);
            e.setJoinMessage("");
            Main.so("");
        }
    }

    public static void updateName(Player p) {
        Chat c = Main.getInstance().getChat();
        c.setPlayerSuffix(p, Main.color("&8[&eLv. " + Main.getInstance().getPC().get(p.getUniqueId()).getLevel() + "&8]"));
    }

    @EventHandler
    public void leaveMessage (PlayerQuitEvent e) {
        Chat c = main.getChat();
        String prefix = c.getPlayerPrefix(e.getPlayer());
        if (prefix.length() > 2) {
            prefix+=" ";
        }
        e.setQuitMessage(Main.color("   &c\u25C0 &f" + prefix + e.getPlayer().getName() + " " + c.getPlayerSuffix(e.getPlayer())));
    }

    @EventHandler
    public void onChat (AsyncPlayerChatEvent e) {
        if (!e.isCancelled()) {
            boolean partychat = false;
            if (main.getRP(e.getPlayer()).getChatChannel() == RPGPlayer.ChatChannel.Party) {
                if (main.getPM().hasParty(e.getPlayer())) {
                    partychat = true;
                }
            }
            boolean townchat = false;

            if (main.getRP(e.getPlayer()).getChatChannel() == RPGPlayer.ChatChannel.Town) {
                if (!main.getUUIDCitizenMap().get(e.getPlayer().getUniqueId()).getTown().equalsIgnoreCase(Citizen.defaultTownName)) {
                    townchat = true;
                }
            }

            Chat chat = main.getChat();
            if (e.getPlayer().hasPermission("core.chatcolor")) {
                e.setMessage(Main.color(e.getMessage()));
            }
            updateName(e.getPlayer());
            String prefix = chat.getPlayerPrefix(e.getPlayer());
            if (prefix.length() > 2) {
                prefix += " ";
            }
            if (!partychat && !townchat) {
                String format = Main.color(prefix + "%s " + chat.getPlayerSuffix(e.getPlayer()) + " &8\u00BB" + "&f %s");
                e.setFormat(format);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (e.getMessage().toLowerCase().contains(p.getName().toLowerCase()) || e.getMessage().toLowerCase().contains(p.getDisplayName().toLowerCase())) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                    }
                }
            } else if (partychat) {
                e.setCancelled(true);
                main.getPM().getParty(e.getPlayer()).sendMessage("&a&l[PARTY] &7" + (e.getPlayer().getName() + " " + chat.getPlayerSuffix(e.getPlayer()) + " &8\u00BB" + "&f " + e.getMessage()));
                Main.so("   &a&l[PARTY] &7" + (e.getPlayer().getName() + " " + chat.getPlayerSuffix(e.getPlayer()) + " &8\u00BB" + "&f " + e.getMessage()));
            } else if (townchat) {
                e.setCancelled(true);
                Town town = null;
                for (Town t : main.getTowns()) {
                    if (t.getName().equalsIgnoreCase(main.getUUIDCitizenMap().get(e.getPlayer().getUniqueId()).getTown())) {
                        town = t;
                    }
                }
                if (town == null) {
                    Main.msg(e.getPlayer(), "&cInternal Error Occurred.");
                    return;
                }
                e.setCancelled(true);
                for(UUID uid : town.getCitizenList()){
                    Main.msg(Bukkit.getPlayer(uid), "&b&l[TOWN] &7" + (e.getPlayer().getName() + " " + chat.getPlayerSuffix(e.getPlayer()) + " &8\u00BB" + "&f " + e.getMessage()));
                }
                Main.so( "   &b&l[TOWN] &7" + (e.getPlayer().getName() + " " + chat.getPlayerSuffix(e.getPlayer()) + " &8\u00BB" + "&f " + e.getMessage()));
            }
        }
    }

}
