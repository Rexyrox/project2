
package au.edu.unimelb.swen.oop.mokepon.skill;

// A battle action a Mokepon can use against an opponent.
public interface Skill {

    // Name shown on its action button; also the name matched against a Mokepon's skills list.
    String name();

    // Energy deducted from the user on use; may be negative to recover energy instead.
    int energyCost();

    // Applies the skill's effect to user/target and reports what happened, for the battle log.
    SkillOutcome use(Combatant user, Combatant target, Randomiser random);
}

