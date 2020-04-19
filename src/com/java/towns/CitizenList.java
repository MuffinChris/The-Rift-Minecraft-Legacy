package com.java.towns;

import com.java.Main;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CitizenList {

    public Map<Player, Citizen> citimap;
    public String town;

    private Main main = Main.getInstance();

    public CitizenList(){
        citimap = new HashMap<Player, Citizen>();
        town = "None";
    }
    public CitizenList(List<Citizen> l){
        citimap = new HashMap<Player, Citizen>();
        for(int i = 0; i < l.size(); i++){
            citimap.put(l.get(i).getPlayer(), l.get(i));
        }
    }
    public CitizenList(String t){
        citimap = new HashMap<Player, Citizen>();
        town = t;
    }
    public CitizenList(List<Citizen> l, String t){
        citimap = new HashMap<Player, Citizen>();
        for(int i = 0; i < l.size(); i++){
            citimap.put(l.get(i).getPlayer(), l.get(i));
        }
        town = t;
    }

    public int getRank(Player p){
        if(citimap.containsKey(p)){
            return citimap.get(p).getRank();
        }
        return -1;
    }

    public Map<Player, Citizen> getCitizenList(){
        return citimap;
    }

    public void removeCitizen(Citizen c){ // this sucks really much but i think i have to do it

        for(Player tp : citimap.keySet()) {
            if(tp.getUniqueId().equals(c.getPlayer().getUniqueId())) {
                citimap.remove(tp);
                return;
            }
        }
    }

    public void addPlayer(Player p) {
        if(citimap.containsKey(p)) return; // already exists
        citimap.put(p, main.getUUIDCitizenMap().get(p.getUniqueId())); // magic
    }
}
