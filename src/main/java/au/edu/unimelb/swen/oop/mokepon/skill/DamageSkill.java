package au.edu.unimelb.swen.oop.mokepon.skill;

// Shared hit/damage math for skills that attack the target (Bite, Tackle); subclasses just supply
// their multiplier, name and energy cost.
public abstract class DamageSkill implements Skill {
    private static final int DAMAGE_NUMERATOR = 50;

    private final int strengthMultiplier;

    protected DamageSkill(int strengthMultiplier) {
        this.strengthMultiplier = strengthMultiplier;
    }

    // Rolls to hit, then applies damage to the target if it does.
    @Override
    public SkillOutcome use(Combatant user, Combatant target, Randomiser random) {
        // Hits only if the chance is strictly greater than the roll, so an equal roll misses.
        if (hitChance(user.currentAccuracy(), target.currentDodge()) <= random.nextFraction()) {
            return new SkillOutcome.Missed();
        }
        int damage = damageAgainst(user.currentStrength(), target.currentDefence());
        target.takeDamage(damage);
        return new SkillOutcome.Hit(target.name(), damage);
    }

    // Probability of hitting: accuracy^2 / (accuracy^2 + dodge^2).
    private static double hitChance(int accuracy, int dodge) {
        double accuracySquared = (double) accuracy * accuracy;
        double dodgeSquared = (double) dodge * dodge;
        return accuracySquared / (accuracySquared + dodgeSquared);
    }

    // Divided last so the result is truncated once, rather than once per operand.
    private int damageAgainst(int strength, int defence) {
        return strengthMultiplier * strength * DAMAGE_NUMERATOR / defence;
    }
}
