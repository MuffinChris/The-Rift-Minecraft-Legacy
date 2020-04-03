package com.java.rpg.classes.utility;

import com.java.Main;

import java.text.DecimalFormat;
import java.util.Random;

public class LevelRange {

    private int minlevel;
    private int maxlevel;

    public LevelRange(int min, int max) {
        minlevel = min;
        maxlevel = max;
    }

    public int getMin() {
        return minlevel;
    }

    public int getMax() {
        return maxlevel;
    }

    public int getRandomLevel() {
        DecimalFormat dF =  new DecimalFormat("#");
        return Integer.valueOf(dF.format(Math.round(Math.random() * (maxlevel - minlevel)))) + minlevel;
    }

}
