package au.edu.unimelb.swen.oop.mokepon.model;

import au.edu.unimelb.swen.oop.mokepon.skill.SkillOutcome;

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

    // The entry message shown when a battle starts.
    public void battleBegan(String playerName, String enemyName) {
        append("Player chose " + playerName + ". Began a fight against enemy " + enemyName + "!");
    }

    // Player turn: a blank separator line, the skill-used line, then a tab-indented outcome.
    public void playerUsed(String mokeponName, String skillName, SkillOutcome outcome) {
        append("");
        append("Player's " + mokeponName + " used " + skillName + "!");
        append("\t" + describe(outcome));
    }

    // Enemy turn: the skill-used line followed directly by a tab-indented outcome.
    public void enemyUsed(String mokeponName, String skillName, SkillOutcome outcome) {
        append("Enemy " + mokeponName + " used " + skillName + "!");
        append("\t" + describe(outcome));
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
    private static String describe(SkillOutcome outcome) {
        return switch (outcome) {
            case SkillOutcome.Missed ignored -> "But they missed.";
            case SkillOutcome.Hit hit -> "Hit " + hit.targetName() + " for " + hit.damage() + " damage!";
            case SkillOutcome.DefenceRaised raised ->
                    "They raised their defence by " + raised.points() + " points!";
            case SkillOutcome.Rested rested -> RESTING_REMARKS[rested.remarkIndex()];
        };
    }

    // Appends a line, inserting a newline first if the log already has content.
    private void append(String line) {
        if (!entries.isEmpty()) {
            entries.append(System.lineSeparator());
        }
        entries.append(line);
    }
}
