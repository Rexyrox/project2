package au.edu.unimelb.swen.oop.mokepon.skill;

// Cheap, low-damage attack: strength multiplier 1, costs 1 energy.
public final class Bite extends DamageSkill {

    public Bite() {
        super(1);
    }

    @Override
    public String name() {
        return "Bite";
    }

    @Override
    public int energyCost() {
        return 1;
    }
}
