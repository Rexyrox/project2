package au.edu.unimelb.swen.oop.mokepon.skill;

public enum Stat {

    HP("hp", Integer.MAX_VALUE),
    ENERGY("energy", Integer.MAX_VALUE),
    STRENGTH("strength", 100),
    ACCURACY("accuracy", 100),
    DEFENCE("defence", 100),
    DODGE("dodge", 100);

    private final String displayName;
    private final int maximum;

    Stat(String displayName, int maximum) {
        this.displayName = displayName;
        this.maximum = maximum;
    }

    public String displayName() {
        return displayName;
    }

    public int maximum() {
        return maximum;
    }
}
