package au.edu.unimelb.swen.oop.mokepon.skill;

public final class Lance extends DamageSkill {

    private static final double STRENGTH_MULTIPLIER = 0.5;

    public Lance() {
        super(STRENGTH_MULTIPLIER);
    }

    @Override
    public String name() {
        return "Lance";
    }

    @Override
    public int energyCost() {
        return 4;
    }

    @Override
    protected SkillOutcome onHit(Combatant user, Combatant target, Randomiser random) {
        int damage = dealDamage(user, target);
        target.addEffect(new Bleeding(user));
        return new SkillOutcome.HitAndBleeding(target.name(), damage);
    }
}
