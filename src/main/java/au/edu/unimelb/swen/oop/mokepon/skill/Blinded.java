package au.edu.unimelb.swen.oop.mokepon.skill;

public final class Blinded extends StatusEffect {

    private static final int DURATION = 3;

    public Blinded() {
        super(DURATION);
    }

    @Override
    public boolean blinds() {
        return true;
    }
}
