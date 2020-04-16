package com.java.rpg.classes.towns;

import com.java.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;



public class Town {
    /*
    Ranks go from 0 to 5 inclusive (default names subject to change)
    */
    private List<String> ranks = new ArrayList<String> (){
        {
        add("Newbie");
        add("Recruit");
        add("Veteran");
        add("Admin");
        add("Co-Owner");
        add("Owner");
        }
    };
    private CitizenList cl;

    public Town(Player p){

    }

    public int getRank(Player p){
        return cl.citimap.get(p).getRank();
    }
    public String getRankName(Player p) { return ranks.get(cl.getRank(p)); }
    
    public void invite(Player inviter, Player reciever){
        if(cl.getRank(inviter) >= 3){
            //reciever gets an invite
            return;
        }
        //inviter is not high enough rank
        return;
    }
    public void kick(Player kicker, Player reciever){
        
    }
    public void promote(Player promoter, Player reciever){
        
    }
}
