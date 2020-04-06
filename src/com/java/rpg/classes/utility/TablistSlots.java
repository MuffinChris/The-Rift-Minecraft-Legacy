package com.java.rpg.classes.utility;

import java.util.ArrayList;
import java.util.List;

public class TablistSlots {

    private List<TabSlot> ts;

    public TablistSlots() {
        ts = new ArrayList<>();
    }

    public List<TabSlot> getTs() {
        return ts;
    }

    public void scrub() {
        List<TabSlot> rem = new ArrayList<>();
        for (TabSlot t : ts) {
            t.wipe();
            rem.add(t);
        }
        for (TabSlot t : rem) {
            ts.remove(t);
        }
        ts = null;
    }

}
