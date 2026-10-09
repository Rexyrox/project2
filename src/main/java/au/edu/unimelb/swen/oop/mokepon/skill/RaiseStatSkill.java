package au.edu.unimelb.swen.oop.mokepon.skill;

public final class RaiseStatSkill implements Skill {
    private static final int ENERGY_COST = 3;
    private static final int MINIMUM_INCREASE = 1;
    private static final int MAXIMUM_INCREASE = 10;

    private final String name;
    private final Stat stat;

    public RaiseStatSkill(String name, Stat stat) {
        this.name = name;
        this.stat = stat;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int energyCost() {
        return ENERGY_COST;
    }

    @Override
    public SkillOutcome use(Combatant user, Combatant target, Randomiser random) {
        // nextInt's bound is exclusive, hence + 1. The log reports the points actually gained,
        // which is less than the roll when the stat hits its maximum of 100.
        int roll = random.nextInt(MINIMUM_INCREASE, MAXIMUM_INCREASE + 1);
        return new SkillOutcome.StatRaised(stat, user.raise(stat, roll));
    }
}
