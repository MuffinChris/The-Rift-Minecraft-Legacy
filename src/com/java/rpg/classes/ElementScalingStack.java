package com.java.rpg.classes;

import java.text.DecimalFormat;

public class ElementScalingStack {

    private double air;
    private double earth;
    private double electric;
    private double fire;
    private double ice;
    private double water;

    public ElementScalingStack(double air, double earth, double elec, double fire, double ice, double water) {
        this.air = air;
        this.earth = earth;
        electric = elec;
        this.fire = fire;
        this.ice = ice;
        this.water = water;
    }

    public double getAir() {
        return air;
    }

    public String getFancyAir() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(air * 100) + "%";
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

    public void setFire(double d) {
        fire = d;
    }

    public double getWater() {
        return water;
    }

    public String getFancyWater() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(water * 100) + "%";
    }

    public void setWater(double d) {
        water = d;
    }

    public double getIce() {
        return ice;
    }

    public String getFancyIce() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(ice * 100) + "%";
    }

    public void setIce(double d) {
        ice = d;
    }

    public double getEletric() {
        return electric;
    }

    public String getFancyElectric() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(electric * 100) + "%";
    }

    public void setElectric(double d) {
        electric = d;
    }

}
