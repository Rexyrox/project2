package au.edu.unimelb.swen.oop.mokepon.skill;

public abstract class TargetedSkill implements Skill {

    private static final double BLIND_MISS_CHANCE = 0.5;

    @Override
    public final SkillOutcome use(Combatant user, Combatant target, Randomiser random) {
        if (!hits(user, target, random)) {
            return new SkillOutcome.Missed();
        }
        return onHit(user, target, random);
    }

    protected abstract SkillOutcome onHit(Combatant user, Combatant target, Randomiser random);

    private static boolean hits(Combatant user, Combatant target, Randomiser random) {
        double chance = hitChance(user.current(Stat.ACCURACY), target.current(Stat.DODGE));
        // Hits only if the chance is strictly greater than the roll, so an equal roll misses.
        if (chance <= random.nextFraction()) {
            return false;
        }
        // Blindness is an extra roll *after* a normal hit passes (spec 6.7.3.1).
        return !(user.isBlinded() && random.nextFraction() < BLIND_MISS_CHANCE);
    }

    private static double hitChance(int accuracy, int dodge) {
        double accuracySquared = (double) accuracy * accuracy;
        double dodgeSquared = (double) dodge * dodge;
        return accuracySquared / (accuracySquared + dodgeSquared);
    }
}
