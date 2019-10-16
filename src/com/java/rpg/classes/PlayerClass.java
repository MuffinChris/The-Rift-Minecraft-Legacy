package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.Material;

import java.util.List;

public class PlayerClass {

    private String name;
    private String fancyname;

    private double basehp;
    private double hpPerLevel;

    private double mana;
    private double manaPerLevel;

    private double manaRegen;
    private double manaRegenPerLevel;

    private String weapon;
    private double baseDmg;

    private double armor;
    private double magicresist;

    private double armorPerLevel;
    private double magicResistPerLevel;

    public double getArmorPerLevel() {
        return armorPerLevel;
    }

    public double getMagicResistPerLevel() {
        return magicResistPerLevel;
    }

    private int weight;

    private String resourceName = "Mana";
    private String fancyResourceName = "&bMana";

    private List<Skill> skills;

    public PlayerClass(String name, String fancyname, double basehp, double hpPerLevel, double mana, double manaPerLevel, double manaRegen, double manaRegenPerLevel, String weapon, double baseDmg, double armor, double magicresist, double armorPerLevel, double magicResistPerLevel, List<Skill> skills, int weight) {
        this.name = name;
        this.fancyname = fancyname;
        this.basehp = basehp;
        this.hpPerLevel = hpPerLevel;
        this.mana = mana;
        this.manaPerLevel = manaPerLevel;
        this.manaRegen = manaRegen;
        this.manaRegenPerLevel = manaRegenPerLevel;
        this.skills = skills;
        this.weapon = weapon;
        this.baseDmg = baseDmg;
        this.armor = armor;
        this.magicresist = magicresist;
        this.armorPerLevel = armorPerLevel;
        this.magicResistPerLevel = magicResistPerLevel;
        this.weight = weight;
    }

    public void setWeight(int w) {
        weight = w;
    }

    public int getWeight() {
        return weight;
    }

    public double getCalcArmor(int level) {
        return armor + level * armorPerLevel;
    }

    public double getCalcMR(int level) {
        return magicresist + level * magicResistPerLevel;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public String getName() {
        return name;
    }

    public String getFancyName() {
        return Main.color(fancyname);
    }

    public double getBaseHP() {
        return basehp;
    }

    public double getHpPerLevel() {
        return hpPerLevel;
    }

    public double getMana() {
        return mana;
    }

    public double getManaPerLevel() {
        return manaPerLevel;
    }

    public double getManaRegen() {
        return manaRegen;
    }

    public double getManaRegenPerLevel() {
        return manaRegenPerLevel;
    }

    public double getBaseDmg() {
        return baseDmg;
    }

    public String getWeapon() {
        return weapon;
    }

    public double getCalcHP(int level) {
        return hpPerLevel * level + basehp;
    }

    public double getCalcMana(int level) {
        return manaPerLevel * level + mana;
    }

    public double getCalcManaRegen(int level) {
        return manaRegenPerLevel * level + manaRegen;
    }

}
