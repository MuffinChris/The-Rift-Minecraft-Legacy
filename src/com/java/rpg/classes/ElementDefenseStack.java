package com.java.rpg.classes;

public class ElementDefenseStack {

    private double airdefense;
    private double earthdefense;
    private double electricdefense;
    private double firedefense;
    private double icedefense;
    private double waterdefense;

    public ElementDefenseStack(double air, double earth, double elec, double fire, double ice, double water) {
        airdefense = air;
        earthdefense = earth;
        electricdefense = elec;
        firedefense = fire;
        icedefense = ice;
        waterdefense = water;
    }

    public double getAirDefense() {
        return airdefense;
    }

    public void setAirDefense(double d) {
        airdefense = d;
    }

    public double getEarthDefense() {
        return earthdefense;
    }

    public void setEarthDefense(double d) {
        earthdefense = d;
    }

    public double getFireDefense() {
        return firedefense;
    }

    public void setFireDefense(double d) {
        firedefense = d;
    }

    public double getWaterDefense() {
        return waterdefense;
    }

    public void setWaterDefense(double d) {
        waterdefense = d;
    }

    public double getIceDefense() {
        return icedefense;
    }

    public void setIceDefense(double d) {
        icedefense = d;
    }

    public double getElectricDefense() {
        return electricdefense;
    }

    public void setElectricDefense(double d) {
        electricdefense = d;
    }

    public String getCommaDelim() {
        return airdefense + "," + earthdefense + "," + electricdefense + "," + firedefense + "," + icedefense + "," + waterdefense;
    }

}
