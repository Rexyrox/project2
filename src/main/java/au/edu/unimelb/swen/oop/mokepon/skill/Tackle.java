package au.edu.unimelb.swen.oop.mokepon.skill;

// Costlier, harder-hitting attack: strength multiplier 2, costs 5 energy.
public final class Tackle extends DamageSkill {

    public Tackle() {
        super(2);
    }

    @Override
    public String name() {
        return "Tackle";
    }

    @Override
    public int energyCost() {
        return 5;
    }
}
