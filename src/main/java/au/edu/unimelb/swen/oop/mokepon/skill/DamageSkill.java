package au.edu.unimelb.swen.oop.mokepon.skill;

// Shared hit/damage math for skills that attack the target (Bite, Tackle); subclasses just supply
// their multiplier, name and energy cost.
public abstract class DamageSkill extends TargetedSkill {
    private static final int DAMAGE_NUMERATOR = 50;

    private final double strengthMultiplier;

    protected DamageSkill(double strengthMultiplier) {
        this.strengthMultiplier = strengthMultiplier;
    }

    // Only reached after TargetedSkill's hit roll has passed.
    @Override
    protected SkillOutcome onHit(Combatant user, Combatant target, Randomiser random) {
        return new SkillOutcome.Hit(target.name(), dealDamage(user, target));
    }

    // Applies this skill's damage to the target and returns it, for subclasses that add effects.
    protected int dealDamage(Combatant user, Combatant target) {
        int damage = damage(strengthMultiplier, user, target);
        target.takeDamage(damage);
        return damage;
    }

    // Divided last so the result is truncated once, rather than once per operand.
    public static int damage(double strengthMultiplier, Combatant attacker, Combatant defender) {
        double strength = attacker.current(Stat.STRENGTH) * strengthMultiplier;
        double raw = strength * DAMAGE_NUMERATOR / defender.current(Stat.DEFENCE);
        return (int) (raw * attacker.damageMultiplier());
    }
}
