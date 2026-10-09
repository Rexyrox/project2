package au.edu.unimelb.swen.oop.mokepon.item;

public enum ItemType {
    HEALTH("Health Tonic") {
        @Override
        public void apply(ItemTarget player, ItemTarget enemy) {
            player.restoreHp();
        }
    },
    COFFEE("Coffee") {
        @Override
        public void apply(ItemTarget player, ItemTarget enemy) {
            player.restoreEnergy();
        }
    },
    POISON("Poison") {
        @Override
        public void apply(ItemTarget player, ItemTarget enemy) {
            enemy.reduceHpToOne();
        }
    };

    private final String displayName;

    ItemType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public abstract void apply(ItemTarget player, ItemTarget enemy);
}
