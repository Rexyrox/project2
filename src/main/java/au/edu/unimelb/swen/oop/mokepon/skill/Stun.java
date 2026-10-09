package au.edu.unimelb.swen.oop.mokepon.skill;

public final class Stun extends TargetedSkill {

    @Override
    public String name() {
        return "Stun";
    }

    @Override
    public int energyCost() {
        return 4;
    }

    @Override
    protected SkillOutcome onHit(Combatant user, Combatant target, Randomiser random) {
        target.addEffect(new Stunned());
        return new SkillOutcome.StunApplied(target.name());
    }
}
