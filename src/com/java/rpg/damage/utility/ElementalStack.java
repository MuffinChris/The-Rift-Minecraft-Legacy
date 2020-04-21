package com.java.rpg.damage.utility;

import com.java.Main;
import com.java.rpg.classes.utility.LevelRange;
import com.java.rpg.classes.utility.RPGConstants;

import java.text.DecimalFormat;

public class ElementalStack {

    private double air;
    private double earth;
    private double electric;
    private double fire;
    private double ice;
    private boolean status;

    public ElementalStack(double air, double earth, double elec, double fire, double ice) {
        this.air = air;
        this.earth = earth;
        electric = elec;
        this.fire = fire;
        this.ice = ice;
        status = true;
    }

    public ElementalStack(double air, double earth, double elec, double fire, double ice,  boolean status) {
        this.air = air;
        this.earth = earth;
        electric = elec;
        this.fire = fire;
        this.ice = ice;
        this.status = false;
    }

    public void combine(ElementalStack e) {
        air+=e.getAir();
        earth+=e.getEarth();
        electric+=e.getElectric();
        fire+=e.getFire();
        ice+=e.getIce();
    }

    public boolean getStatus() {
        return status;
    }

    public ElementalStack() {
        air = 0;
        earth = 0;
        electric = 0;
        fire = 0;
        ice = 0;
    }

    public void scaleAll(double d) {
        air*=d;
        earth*=d;
        electric*=d;
        fire*=d;
        ice*=d;
    }

    public double getPercent(String s) {
        double total = air + earth + electric + fire + ice;
        if (s.equalsIgnoreCase("air")) {
            return air/total;
        }
        if (s.equalsIgnoreCase("earth")) {
            return earth/total;
        }
        if (s.equalsIgnoreCase("electric")) {
            return electric/total;
        }
        if (s.equalsIgnoreCase("fire")) {
            return fire/total;
        }
        if (s.equalsIgnoreCase("ice")) {
            return ice/total;
        }
        return 0;
    }

    public String pickStatusEffect() {
        double total = air + earth + electric + fire + ice;
        double airChance = air/total;
        double earthChance = airChance + earth/total;
        double electricChance = earthChance + electric/total;
        double fireChance = electricChance + fire/total;
        double iceChance = fireChance + ice/total;

        if (air >= total) {
            return "AIR";
        }
        if (earth >= total) {
            return "EARTH";
        }
        if (electric >= total) {
            return "ELECTRIC";
        }
        if (fire >= total) {
            return "FIRE";
        }
        if (ice >= total) {
            return "ICE";
        }

        double percent = (new LevelRange(1, 100)).getRandomLevel() * 0.01;
        if (percent <= airChance) {
            return "AIR";
        } else if (percent <= earthChance) {
            return "EARTH";
        } else if (percent <= electricChance) {
            return "ELECTRIC";
        } else if (percent <= fireChance) {
            return "FIRE";
        } else if (percent <= iceChance) {
            return "ICE";
        } else {
            Main.so("&c&lElementalStack &r&cfailed to find valid status effect.");
            Main.so("&fAirChance: " + airChance);
            Main.so("&2EarthChance: " + earthChance);
            Main.so("&eElecChance: " + electricChance);
            Main.so("&cFireChance: " + fireChance);
            Main.so("&bIceChance: " + iceChance);
            Main.so("&5Percent: " + percent);
            Main.so("&fDefaulting to AIR.....");
            return "AIR";
        }

    }

    public double getTotal() {
        return air + earth + electric + fire + ice;
    }

    public boolean anyNonzero() {
        return (air != 0 || earth != 0 || electric != 0 || fire != 0 || ice != 0);
    }

    public double getAir() {
        return air;
    }

    public String getFancyAir() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(air * 100) + "%";
    }

    public String getFancyNumberAir() {
        DecimalFormat df = new DecimalFormat("#.##");
        return RPGConstants.air + "" + df.format(air);
    }

    public void setAir(double d) {
        air = d;
    }

    public double getEarth() {
        return earth;
    }

    public String getFancyEarth() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(earth * 100) + "%";
    }

    public String getFancyNumberEarth() {
        DecimalFormat df = new DecimalFormat("#.##");
        return RPGConstants.earth + "" + df.format(earth);
    }

    public void setEarth(double d) {
        earth = d;
    }

    public double getFire() {
        return fire;
    }

    public String getFancyFire() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(fire * 100) + "%";
    }

    public String getFancyNumberFire() {
        DecimalFormat df = new DecimalFormat("#.##");
        return RPGConstants.fire + "" + df.format(fire);
    }

    public void setFire(double d) {
        fire = d;
    }

    public double getIce() {
        return ice;
    }

    public String getFancyIce() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(ice * 100) + "%";
    }

    public String getFancyNumberIce() {
        DecimalFormat df = new DecimalFormat("#.##");
        return RPGConstants.ice + "" + df.format(ice);
    }

    public void setIce(double d) {
        ice = d;
    }

    public double getElectric() {
        return electric;
    }

    public String getFancyElectric() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(electric * 100) + "%";
    }

    public String getFancyNumberElectric() {
        DecimalFormat df = new DecimalFormat("#.##");
        return RPGConstants.electric + "" + df.format(electric);
    }

    public void setElectric(double d) {
        electric = d;
    }

    public String getCommaDelim() {
        return air + "," + earth + "," + electric + "," + fire + "," + ice;
    }

    public String getFancyStack() {
        DecimalFormat dF = new DecimalFormat("#.##");
        return (Main.color( "&f" + dF.format(air) + "&8|&2" + dF.format(earth) + "&8|&e" + dF.format(electric) + "&8|&c" + dF.format(fire) + "&8|&b" + dF.format(ice) + ""));
    }


}
