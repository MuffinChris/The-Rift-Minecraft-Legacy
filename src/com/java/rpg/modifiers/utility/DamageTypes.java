package com.java.rpg.modifiers.utility;

import com.java.rpg.modifiers.utility.Damage;

import java.util.ArrayList;
import java.util.List;

public class DamageTypes {

    private List<Damage> damages;

    public DamageTypes() {
        damages = new ArrayList<>();
    }

    public List<Damage> getDamages() {
        return damages;
    }

}
