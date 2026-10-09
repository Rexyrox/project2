package au.edu.unimelb.swen.oop.mokepon.skill;

// The result of one skill use, precise enough for BattleLog to describe it without recomputing.
public sealed interface SkillOutcome {

    // The skill failed to hit its target.
    record Missed() implements SkillOutcome {}

    // The skill hit, dealing damage to the named target.
    record Hit(String targetName, int damage) implements SkillOutcome {}

    // The user's defence increased by this many points.
    record DefenceRaised(int points) implements SkillOutcome {}

    // The user rested; remarkIndex picks which flavour line to log.
    record Rested(int remarkIndex) implements SkillOutcome {}
}

