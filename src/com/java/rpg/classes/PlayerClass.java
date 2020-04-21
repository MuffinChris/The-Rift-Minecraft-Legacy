package com.java.rpg.classes;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;

import java.util.ArrayList;
import java.util.Collections;
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

    private ElementalStack eDefense;

    public ElementalStack getEDefense() {
        return eDefense;
    }

    private ElementalStack eDefenseScaling;
    public ElementalStack getEDefenseScaling() {
        return eDefenseScaling;
    }

    private double armorPerLevel;
    private double magicResistPerLevel;

    private double baseAD;
    private double baseAP;

    private double adPerLevel;
    private double apPerLevel;

    public double getADPerLevel() {
        return adPerLevel;
    }

    public double getAPPerLevel() {
        return apPerLevel;
    }

    public double getBaseAD() {
        return baseAD;
    }

    public double getBaseAP() {
        return baseAP;
    }

    public double getArmorPerLevel() {
        return armorPerLevel;
    }

    public double getMagicResistPerLevel() {
        return magicResistPerLevel;
    }

    private int weight;

    public enum ResourceType
    {
        MANA
    }
    private ResourceType resourceType;
    public ResourceType getResourceType() {
        return resourceType;
    }

    private List<Skill> skills;

    public PlayerClass(String name, String fancyname, double basehp, double hpPerLevel, ResourceType resourceType, double mana, double manaPerLevel, double manaRegen, double manaRegenPerLevel, String weapon, double baseDmg, double ad, double ap, double adperlevel, double apperlevel, double armor, double magicresist, double armorPerLevel, double magicResistPerLevel, List<Skill> skills, int weight, ElementalStack eDef, ElementalStack eDefScale) {
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
        this.resourceType = resourceType;
        baseAD = ad;
        baseAP = ap;
        adPerLevel = adperlevel;
        apPerLevel = apperlevel;
        eDefense = eDef;
        eDefenseScaling = eDefScale;
    }

    public double getCalcAD(int level) {
        return baseAD + adPerLevel * level;
    }

    public double getCalcAP(int level) {
        return baseAP + apPerLevel * level;
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

    public List<Skill> getUpgradedSkills() {
        List<Skill> upgraded = new ArrayList<>();
        upgraded.addAll(skills);
        return upgraded;
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
