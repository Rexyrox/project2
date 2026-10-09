package au.edu.unimelb.swen.oop.mokepon.model;

import au.edu.unimelb.swen.oop.mokepon.item.ItemType;
import au.edu.unimelb.swen.oop.mokepon.skill.EffectEvent;
import au.edu.unimelb.swen.oop.mokepon.skill.SkillOutcome;

import java.util.List;

/** Every message shown in the battle log, so that wording and spacing live in one place. */
public class BattleLog {

    private static final String[] RESTING_REMARKS = {
            "They get a little more bored.",
            "They wonder if this is a good strategy.",
            "They think about what they will have for dinner."
    };

    private final StringBuilder entries = new StringBuilder();

    public String text() {
        return entries.toString();
    }

    public void battleBegan(String playerName, String enemyName) {
        append("Player chose " + playerName + ". Began a fight against enemy " + enemyName + "!");
    }

    // Player turn: a blank separator line, the skill-used line, then a tab-indented outcome.
    public void playerUsed(String mokeponName, String skillName, SkillOutcome outcome) {
        append("");
        append("Player's " + mokeponName + " used " + skillName + "!");
        appendIndented(describe(outcome));
    }

    // Enemy turn: the skill-used line followed directly by a tab-indented outcome.
    public void enemyUsed(String mokeponName, String skillName, SkillOutcome outcome) {
        append("Enemy " + mokeponName + " used " + skillName + "!");
        appendIndented(describe(outcome));
    }

    // Lingering effect messages from the end of a round
    public void effectEvents(List<EffectEvent> events) {
        for (EffectEvent event : events) {
            appendIndented(List.of(describe(event)));
        }
    }

    public void enemyFainted(String mokeponName) {
        appendFaint("Enemy's " + mokeponName + " fainted!", "You win");
    }

    public void playerFainted(String mokeponName) {
        appendFaint("Player's " + mokeponName + " fainted!", "You lose");
    }

    // Two blank lines, then the faint message and the win/lose result line.
    private void appendFaint(String faintMessage, String result) {
        append("");
        append("");
        append(faintMessage);
        append(result);
    }

    // Turns a SkillOutcome into the exact line of text the spec requires for it.
    private static List<String> describe(SkillOutcome outcome) {
        return switch (outcome) {
            case SkillOutcome.Missed ignored -> List.of("But they missed.");
            case SkillOutcome.Hit hit -> List.of(
                    "Hit " + hit.targetName() + " for " + hit.damage() + " damage!");
            case SkillOutcome.Rested rested -> List.of(RESTING_REMARKS[rested.remarkIndex()]);
            case SkillOutcome.StatRaised raised -> List.of(
                    "They raised their " + raised.stat().displayName()
                            + " by " + raised.points() + " points!");
            case SkillOutcome.HitAndBleeding hit -> List.of(
                    "Hit " + hit.targetName() + " for " + hit.damage()
                            + " damage! They started bleeding!");
            case SkillOutcome.StunApplied stun -> List.of(
                    stun.targetName() + " was stunned! Their damage is halved for 3 turns.");
            case SkillOutcome.BlindApplied blind -> List.of(
                    "Pocket sand blinded " + blind.targetName() + "!",
                    "Their targeted skills may miss for 3 turns.");
        };
    }

    private static String describe(EffectEvent event) {
        return switch (event) {
            case EffectEvent.BleedDamage bleed ->
                    bleed.targetName() + " took " + bleed.damage() + " bleeding damage!";
            case EffectEvent.BleedingStopped ignored -> "The bleeding stopped.";
        };
    }

    public void itemUsed(ItemType item, String playerName, String enemyName) {
        append("");
        append(switch (item) {
            case HEALTH -> "You used your " + item.displayName() + " on " + playerName
                    + ". It healed you to max hp!";
            case COFFEE -> "You used your " + item.displayName() + " on " + playerName
                    + ". It restored all your energy!";
            case POISON -> "You used your " + item.displayName() + " on " + enemyName
                    + ". It devastated them!";
        });
    }

    // Appends a line, inserting a newline first if the log already has content.
    private void append(String line) {
        if (!entries.isEmpty()) {
            entries.append(System.lineSeparator());
        }
        entries.append(line);
    }

    private void appendIndented(List<String> lines) {
        for (String line : lines) {
            append("\t" + line);
        }
    }
}
