package au.edu.unimelb.swen.oop.mokepon.skill;

// Recovery skill: costs no energy (recovers some instead) and always succeeds; this is the
// guaranteed affordable skill every Mokepon relies on when nothing else can be used.
public final class Wait implements Skill {

    private static final int REMARK_COUNT = 3;

    @Override
    public String name() {
        return "Wait";
    }

    @Override
    public int energyCost() {
        return -2;
    }

    // Picks one of the flavour-text remarks at random; the actual energy recovery is applied by
    // Battler.payEnergyCost using energyCost().
    @Override
    public SkillOutcome use(Combatant user, Combatant target, Randomiser random) {
        return new SkillOutcome.Rested(random.nextInt(0, REMARK_COUNT));
    }
}
