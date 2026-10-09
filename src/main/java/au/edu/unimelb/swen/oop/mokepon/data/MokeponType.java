package au.edu.unimelb.swen.oop.mokepon.data;

public enum MokeponType {
    NORMAL(null, null),
    FIRE(Terrain.FOREST, Terrain.WATER),
    ICE(Terrain.ROCKS, Terrain.GRASS),
    WATER(Terrain.WATER, Terrain.ROCKS),
    LIGHTNING(Terrain.GRASS, Terrain.FOREST);

    private final Terrain advantage;
    private final Terrain disadvantage;

    MokeponType(Terrain advantage, Terrain disadvantage) {
        this.advantage = advantage;
        this.disadvantage = disadvantage;
    }

    // How this type fares on the given terrain.
    public TerrainEffect effectOn(Terrain terrain) {
        if (terrain == advantage) {
            return TerrainEffect.ADVANTAGE;
        }
        if (terrain == disadvantage) {
            return TerrainEffect.DISADVANTAGE;
        }
        return TerrainEffect.NEUTRAL;
    }
}
