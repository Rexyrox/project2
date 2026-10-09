package au.edu.unimelb.swen.oop.mokepon.skill;

import java.util.ArrayList;
import java.util.List;

public abstract class StatusEffect {

    private int turnsRemaining;

    protected StatusEffect(int turns) {
        this.turnsRemaining = turns;
    }

    public final List<EffectEvent> endOfRound(Combatant owner) {
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
