package com.java.rpg.classes.utility;

public class StatusValue {

    private int value;
    private String source;
    private long timestamp;
    private int duration;
    private boolean durationless;

    public StatusValue(String source, int value, int duration, long timestamp, boolean durationless) {
        this.source = source;
        this.value = value;
        this.duration = duration;
        this.timestamp = timestamp;
        this.durationless = durationless;
    }

    public void setValue(int i) {
        value = i;
    }

    public boolean getDurationless() {
        return durationless;
    }

    public int getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public int getDuration() {
        return duration;
    }

    public void scrub() {
        source = null;
    }

}
