package au.edu.unimelb.swen.oop.mokepon.data;

public enum TerrainEffect {
    ADVANTAGE(1.5, "an advantage"),
    NEUTRAL(1.0, ""),
    DISADVANTAGE(0.5, "a disadvantage");

    private final double statMultiplier;
    private final String description;

    TerrainEffect(double statMultiplier, String description) {
        this.statMultiplier = statMultiplier;
        this.description = description;
    }

    public String description() {
        return description;
    }

    public double statMultiplier() {
        return statMultiplier;
    }
}
