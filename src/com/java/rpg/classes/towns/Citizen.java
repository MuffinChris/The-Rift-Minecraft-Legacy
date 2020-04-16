package com.java.rpg.classes.towns;

import com.java.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Citizen {
    private int r; //Rank in town, -1 if no town
    private Player p;
    private String town; //Player's town, current "Randos" if townless

    public Citizen(Player pl){
        this.p = pl; this.r = -1; this.town = "Randos";
    }
    public Citizen(Player pl, int rnk){
        this.p = pl; this.r = rnk;
    }
    public Citizen(Player pl, int rnk, String t){
        this.p = pl; this.r = rnk; this.town = t;
    }

    public Player getPlayer(){
        return p;
    }
    public int getRank(){
        return r;
    }
    public String getTown(){
        return town;
    }


    public void invite(Player recieve){

    }
    public void kick(Player recieve){

    }
}
