package au.edu.unimelb.swen.oop.mokepon.skill;

public final class Stunned extends StatusEffect {

    private static final int DURATION = 3;
    private static final double DAMAGE_MULTIPLIER = 0.5;

    public Stunned() {
        super(DURATION);
    }

    @Override
    protected boolean countsOwnerTurns() {
        return true;
    }

    @Override
    public double damageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }
}
