package au.edu.unimelb.swen.oop.mokepon.data;

public enum Terrain {
    BARRIER('B', false),
    FOREST('F', true),
    GRASS('G', true),
    OCEAN('O', false),
    PATH('P', true),
    ROCKS('R', true),
    WATER('W', true);

    private final char code;
    private final boolean walkable;

    Terrain(char code, boolean walkable) {
        this.code = code;
        this.walkable = walkable;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public static Terrain fromCode(char code) {
        for (Terrain terrain : values()) {
            if (terrain.code == code) {
                return terrain;
            }
        }
        throw new IllegalArgumentException("Unknown tile character " + code);
    }
}
