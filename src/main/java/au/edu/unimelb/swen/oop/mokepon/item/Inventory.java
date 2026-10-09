package au.edu.unimelb.swen.oop.mokepon.item;

import java.util.EnumMap;
import java.util.Map;

public class Inventory {

    private final Map<ItemType, Integer> counts = new EnumMap<>(ItemType.class);

    public Inventory() {
        for (ItemType type : ItemType.values()) {
            counts.put(type, 0);
        }
    }

    public void add(ItemType type) {
        counts.put(type, counts.get(type) + 1);
    }

    public int count(ItemType type) {
        return counts.get(type);
    }

    public boolean has(ItemType type) {
        return count(type) > 0;
    }

    public void use(ItemType type) {
        if (!has(type)) {
            throw new IllegalStateException("No " + type.displayName() + " left");
        }
        counts.put(type, counts.get(type) - 1);
    }
}
