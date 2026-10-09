package au.edu.unimelb.swen.oop.mokepon.skill;

// The result of one skill use, precise enough for BattleLog to describe it without recomputing.
public sealed interface SkillOutcome {

    // The skill failed to hit its target.
    record Missed() implements SkillOutcome {}

    // The skill hit, dealing damage to the named target.
    record Hit(String targetName, int damage) implements SkillOutcome {}

    // The user's stat increased by this many points.
    record StatRaised(Stat stat, int points) implements SkillOutcome {}

    // The user rested; remarkIndex picks which flavour line to log.
    record Rested(int remarkIndex) implements SkillOutcome {}

    // The skill hit for damage and also started the target bleeding.
    record HitAndBleeding(String targetName, int damage) implements SkillOutcome {}

    // The target was stunned, halving its damage for a few turns.
    record StunApplied(String targetName) implements SkillOutcome {}

    // The target was blinded, so its targeted skills may miss for a few turns.
    record BlindApplied(String targetName) implements SkillOutcome {}
}
