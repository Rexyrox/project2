package au.edu.unimelb.swen.oop.mokepon.skill;

import java.util.List;

public final class Bleeding extends StatusEffect {

    private static final int DURATION = 2;
    private static final double STRENGTH_MULTIPLIER = 0.5;

    private final Combatant attacker;

    public Bleeding(Combatant attacker) {
        super(DURATION);
        this.attacker = attacker;
    }

    @Override
    protected List<EffectEvent> onRoundEnd(Combatant owner) {
        int damage = DamageSkill.damage(STRENGTH_MULTIPLIER, attacker, owner);
        owner.takeDamage(damage);
        return List.of(new EffectEvent.BleedDamage(owner.name(), damage));
    }

    @Override
    protected List<EffectEvent> onExpire(Combatant owner) {
        return List.of(new EffectEvent.BleedingStopped());
    }
}
