package com.java.rpg.modifiers.utility;

public class PhysicalStack {

    public double impact;
    public double puncture;
    public double slash;

    public PhysicalStack() {
        this(0, 0, 0);
    }

    public PhysicalStack(double p, double i, double s) {
        impact = i;
        puncture = p;
        slash = s;
    }

    public double getImpact() {
        return impact;
    }

    public double getPuncture() {
        return puncture;
    }

    public double getSlash() {
        return slash;
    }

    public void setImpact(double i) {
        impact = i;
    }

    public void setSlash (double s) {
        slash = s;
    }

    public void setPuncture(double p) {
        puncture = p;
    }

    public double getTotal() {
        return slash + puncture + impact;
    }

    public String getCommaDelim() {
        return puncture + "," + impact + "," + slash ;
    }

}
