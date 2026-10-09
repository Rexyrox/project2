package au.edu.unimelb.swen.oop.mokepon.item;

public enum ItemType {
    HEALTH("Health Tonic", "heal") {
        @Override
        public void apply(ItemTarget player, ItemTarget enemy) {
            player.restoreHp();
        }
    },
    COFFEE("Coffee", "coffee") {
        @Override
        public void apply(ItemTarget player, ItemTarget enemy) {
            player.restoreEnergy();
        }
    },
    POISON("Poison", "poison") {
        @Override
        public void apply(ItemTarget player, ItemTarget enemy) {
            enemy.reduceHpToOne();
        }
    };

    private final String displayName;
    // The suffix of this item's items.* image key, which doesn't always match the enum name
    // (HEALTH is items.heal).
    private final String dataKey;

    ItemType(String displayName, String dataKey) {
        this.displayName = displayName;
        this.dataKey = dataKey;
    }

    public String displayName() {
        return displayName;
    }

    public String dataKey() {
        return dataKey;
    }

    public abstract void apply(ItemTarget player, ItemTarget enemy);
}
