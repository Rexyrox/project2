package au.edu.unimelb.swen.oop.mokepon.skill;

public final class PocketSand extends TargetedSkill {

    @Override
    public String name() {
        return "Pocket Sand";
    }

    @Override
    public int energyCost() {
        return 4;
    }

    @Override
    protected SkillOutcome onHit(Combatant user, Combatant target, Randomiser random) {
        target.addEffect(new Blinded());
        return new SkillOutcome.BlindApplied(target.name());
    }
}
