package au.edu.unimelb.swen.oop.mokepon.item;

public interface ItemTarget {
    String name();
    void restoreHp();
    void restoreEnergy();
    void reduceHpToOne();
}
