package com.java.rpg.party;

import com.java.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PartyManager {

    public List<Party> parties;

    private Main main = Main.getInstance();

    public PartyManager() {
        parties = new ArrayList<>();
        main.partyStartup();
    }

    public void cleanParties() {
        if (parties.size() > 0) {
            for (int z = parties.size() - 1; z >= 0; z--) {
                if ((parties.get(z).getLeader() == null)) {
                    parties.remove(parties.get(z));
                }
            }
        }
    }

    public List<Party> getParties() {
        return parties;
    }

    public boolean hasParty(Player p) {
        if (p != null) {
            for (Party pa : parties) {
                if (pa != null && pa.getPlayers() != null && !pa.getPlayers().contains(null)) {
                    for (Player pl : pa.getPlayers()) {
                        if (pl != null && pl.equals(p)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Party getParty(Player p) {
        if (p != null) {
            for (Party pa : parties) {
                for (Player pl : pa.getPlayers()) {
                    if (pl != null && pl.equals(p)) {
                        return pa;
                    }
                }
            }
        }
        return null;
    }

    public void createParty(Player p) {
        parties.add(new Party(p));
    }

}
