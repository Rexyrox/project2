package au.edu.unimelb.swen.oop.mokepon.skill;

public final class Blinded extends StatusEffect {

    private static final int DURATION = 3;

    public Blinded() {
        super(DURATION);
    }

    @Override
    protected boolean countsOwnerTurns() {
        return true;
    }

    @Override
    public boolean blinds() {
        return true;
    }
}
