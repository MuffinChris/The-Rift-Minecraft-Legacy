package com.java.communication;

import com.java.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerinfoListener implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void pInfo(PlayerJoinEvent e) {
        main.getMsg().setPinf(e.getPlayer(), new playerinfo(e.getPlayer()));
    }

    @EventHandler
    public void pInfoLeave(PlayerQuitEvent e) {
        if (main.getMsg().getPinfs().containsKey(e.getPlayer().getUniqueId())) {
            main.getMsg().getPinfs().get(e.getPlayer().getUniqueId()).scrub();
            main.getMsg().getPinfs().remove(e.getPlayer().getUniqueId());
        }
    }

}
