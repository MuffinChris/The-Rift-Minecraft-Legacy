package com.java.rpg.classes.utility;

import java.util.LinkedHashMap;

public class RPGConstants {

    public static String physical = "&c❤";
    public static String trued = "&d♦";
    public static String magic = "&b✦";
    public static String air = "&f✸";
    public static String earth = "&2❈";
    public static String electric = "&e⚡";
    public static String fire = "&c✴";
    public static String ice = "&b❆";
    //public static String water = "&3✾";
    public static String slash = "&c⒜";
    public static String puncture = "&c⒝";
    public static String impact = "&c⒞";
    public static String attackDamage = "&c⚔";
    public static String abilityPower = "&b⒟";

    public static String xp = "XP]";

    public static String[] damages = new String[]{physical, trued, magic, air, earth, electric, fire, ice, slash, puncture, impact, xp};

    public static String armor = "⛨";
    public static String weak = "&6⚶";
    public static String strong = "&6⛨";

    public static double punctureCritChance = 0.25;
    public static double punctureCritDamage = 2.5;
    public static double punctureArmorPenPercentage = 0.7;

    public static double slashCritChance = 0.05;
    public static double slashCritDamage = 1.75;
    public static double slashArmorPenPercentage = 0.9;

    public static double xpEnvVal = 0.5;
    public static double veryHighEnv = 0.2;

    public static double expMod = 60;
    public static double expOff = 500;
    public static double expPow = 2.7;

    public static double mobHpBase = 500;
    public static double mobHpLevelPow = 2;
    public static double mobHpLevelScalar = 5;
    public static double mobHpBasePow = 0.8;

    public static double mobXpBase =50;
    public static double mobXpPow =2.4;
    public static double mobXpScalar=0.25;

    public static double mobDmgLevelPow = 2;
    public static double mobDmgLevelScalar = 4;

    public static double defaultHP = 1000;

    public static double defenseDiv = 500.0;

    public static double partyXpMod = 1.5;

    public static double baseCritModifier = 2.0;
    public static double baseCritChance = 0.05;

    public static double baseStatusChance = 0.10;

    public static String defaultClassName = "Wanderer";

    public static int superSkillOne = 65;
    public static int superSkillTwo = 85;
    public static int maxLevel = 100;

    public static int reducedExpLevelMod = 3;
    public static double reducedExpLevelPow = 2.5;


    public RPGConstants() {
    }

}
