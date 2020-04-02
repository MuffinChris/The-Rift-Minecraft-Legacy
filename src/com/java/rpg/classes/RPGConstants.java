package com.java.rpg.classes;

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
    public static String water = "&3✾";

    public static String[] damages = new String[]{physical, trued, magic, air, earth, electric, fire, ice, water};

    public static String armor = "⛨";
    public static String weak = "&6⚶";
    public static String strong = "&6⛨";

    public static double expMod = 60;
    public static double expOff = 500;
    public static double expPow = 2.7;

    public static double mobHpBase = 1250;
    public static double mobHpLevelPow = 2;
    public static double mobHpLevelScalar = 5;
    public static double mobHpBasePow = 0.8;

    public static double mobXpBase =50;
    public static double mobXpPow =2.4;
    public static double mobXpScalar=0.25;

    public static double mobDmgLevelPow = 2;
    public static double mobDmgLevelScalar = 4;

    public static double defaultHP = 1000;
    public static double baseDmg = 10;

    public static double defenseDiv = 500.0;

    public static double partyXpMod = 1.5;

    public static double baseCritModifier = 2.0;
    public static double baseCritChance = 0.05;

    public static double baseStatusChance = 0.10;

    public static String defaultClassName = "Wanderer";

    public static LinkedHashMap<Integer, Double> levelsExp;
    public static LinkedHashMap<Integer, Double> mobExp;

    public RPGConstants() {
        levelsExp = new LinkedHashMap<>();
        mobExp = new LinkedHashMap<>();

        levelsExp.put(0, 500.0);
        levelsExp.put(1, 650.0);
        levelsExp.put(2, 800.0);
        levelsExp.put(3, 1100.0);
        levelsExp.put(4, 1300.0);
        levelsExp.put(5, 1500.0);

        mobExp.put(0, 100.0);
        mobExp.put(1, 115.0);
        mobExp.put(2, 130.0);
        mobExp.put(3, 145.0);
        mobExp.put(4, 160.0);
        mobExp.put(5, 185.0);

        levelsExp.put(6, 1900.0);
        levelsExp.put(7, 2400.0);
        levelsExp.put(8, 3000.0);
        levelsExp.put(9, 3800.0);
        levelsExp.put(10, 5000.0);

        mobExp.put(6, 200.0);
        mobExp.put(7, 220.0);
        mobExp.put(8, 240.0);
        mobExp.put(9, 260.0);
        mobExp.put(10, 280.0);

        levelsExp.put(11, 6500.0);
        levelsExp.put(12, 8200.0);
        levelsExp.put(13, 10000.0);
        levelsExp.put(14, 12000.0);
        levelsExp.put(15, 15000.0);

        mobExp.put(11, 310.0);
        mobExp.put(12, 340.0);
        mobExp.put(13, 370.0);
        mobExp.put(14, 400.0);
        mobExp.put(15, 430.0);

        levelsExp.put(16, 19000.0);
        levelsExp.put(17, 25000.0);
        levelsExp.put(18, 30000.0);
        levelsExp.put(19, 36000.0);
        levelsExp.put(20, 43000.0);

        mobExp.put(16, 460.0);
        mobExp.put(17, 500.0);
        mobExp.put(18, 530.0);
        mobExp.put(19, 560.0);
        mobExp.put(20, 580.0);

        levelsExp.put(21, 50000.0);
        levelsExp.put(22, 60000.0);
        levelsExp.put(23, 75000.0);
        levelsExp.put(24, 100000.0);
        levelsExp.put(25, 125000.0);

        mobExp.put(21, 620.0);
        mobExp.put(22, 700.0);
        mobExp.put(23, 780.0);
        mobExp.put(24, 860.0);
        mobExp.put(25, 1000.0);

        levelsExp.put(26, 160000.0);
        levelsExp.put(27, 190000.0);
        levelsExp.put(28, 300000.0);
        levelsExp.put(29, 360000.0);
        levelsExp.put(30, 410000.0);

        mobExp.put(26, 1200.0);
        mobExp.put(27, 1400.0);
        mobExp.put(28, 1700.0);
        mobExp.put(29, 2000.0);
        mobExp.put(30, 2400.0);

        levelsExp.put(31, 550000.0);
        levelsExp.put(32, 700000.0);
        levelsExp.put(33, 900000.0);
        levelsExp.put(34, 1100000.0);
        levelsExp.put(35, 1400000.0);

        mobExp.put(31, 3000.0);
        mobExp.put(32, 3900.0);
        mobExp.put(33, 4800.0);
        mobExp.put(34, 6000.0);
        mobExp.put(35, 7000.0);

        levelsExp.put(36, 1800000.0);
        levelsExp.put(37, 2200000.0);
        levelsExp.put(38, 2650000.0);
        levelsExp.put(39, 3500000.0);
        levelsExp.put(40, 5000000.0);

        mobExp.put(36, 9000.0);
        mobExp.put(37, 11000.0);
        mobExp.put(38, 13000.0);
        mobExp.put(39, 15000.0);
        mobExp.put(40, 17000.0);

        levelsExp.put(41, 7000000.0);
        levelsExp.put(42, 9500000.0);
        levelsExp.put(43, 12000000.0);
        levelsExp.put(44, 18000000.0);
        levelsExp.put(45, 25000000.0);

        mobExp.put(41, 19000.0);
        mobExp.put(42, 21000.0);
        mobExp.put(43, 23000.0);
        mobExp.put(44, 26000.0);
        mobExp.put(45, 29000.0);

        levelsExp.put(46, 35000000.0);
        levelsExp.put(47, 50000000.0);
        levelsExp.put(48, 75000000.0);
        levelsExp.put(49, 100000000.0);
        levelsExp.put(50, 500000000.0);

        mobExp.put(46, 35000.0);
        mobExp.put(47, 42000.0);
        mobExp.put(48, 50000.0);
        mobExp.put(49, 59000.0);
        mobExp.put(50, 70000.0);
    }

}
