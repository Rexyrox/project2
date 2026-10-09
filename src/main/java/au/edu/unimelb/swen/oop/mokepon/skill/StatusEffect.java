package au.edu.unimelb.swen.oop.mokepon.skill;

import java.util.ArrayList;
import java.util.List;

public abstract class StatusEffect {

    private int turnsRemaining;

    protected StatusEffect(int turns) {
        this.turnsRemaining = turns;
    }

    // Effects that change how the owner acts count down after each of the owner's own turns, so
    // they last the same number of turns whether applied before or after the owner acted.
    public final void afterOwnerTurn() {
        if (countsOwnerTurns()) {
            turnsRemaining--;
        }
    }

    public final List<EffectEvent> endOfRound(Combatant owner) {
        if (countsOwnerTurns()) {
            return List.of();
        }
        List<EffectEvent> events = new ArrayList<>(onRoundEnd(owner));
        turnsRemaining--;
        if (isFinished()) {
            events.addAll(onExpire(owner));
        }
        return events;
    }

    public boolean isFinished() {
        return turnsRemaining <= 0;
    }

    protected boolean countsOwnerTurns() {
        return false;
    }

    protected List<EffectEvent> onRoundEnd(Combatant owner) {
        return List.of();
    }

    protected List<EffectEvent> onExpire(Combatant owner) {
        return List.of();
    }

    public boolean blinds() {
        return false;
    }

    public double damageMultiplier() {
        return 1.0;
    }
}
