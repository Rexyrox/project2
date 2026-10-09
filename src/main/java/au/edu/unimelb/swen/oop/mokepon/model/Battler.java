package au.edu.unimelb.swen.oop.mokepon.model;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.item.ItemTarget;
import au.edu.unimelb.swen.oop.mokepon.skill.Combatant;
import au.edu.unimelb.swen.oop.mokepon.skill.EffectEvent;
import au.edu.unimelb.swen.oop.mokepon.skill.Skill;
import au.edu.unimelb.swen.oop.mokepon.skill.Stat;
import au.edu.unimelb.swen.oop.mokepon.skill.StatusEffect;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

// One Mokepon's mutable state during a Battle: hp/energy/defence/accuracy/dodge/strength
// layered over its fixed spec.
public class Battler implements Combatant, ItemTarget {

    private final MokeponSpec spec;

    private final Map<Stat, Integer> stats = new EnumMap<>(Stat.class);
    private final List<StatusEffect> effects = new ArrayList<>();
    private int currentHp;
    private int currentEnergy;

    public Battler(MokeponSpec spec, double terrainMultiplier) {
        this.spec = spec;
        // Terrain advantage/disadvantage scales every stat for this battle only, rounded and
        // capped at the stat's legal maximum (spec 5.6.2.7.3-5.6.2.7.5).
        for (Stat stat : Stat.values()) {
            int adjusted = (int) Math.round(spec.base(stat) * terrainMultiplier);
            stats.put(stat, Math.min(adjusted, stat.maximum()));
        }
        currentHp = maxHp();
        currentEnergy = maxEnergy();
    }

    public MokeponSpec spec() {
        return spec;
    }

    @Override
    public String name() {
        return spec.name();
    }

    public int currentHp() {
        return currentHp;
    }

    public int currentEnergy() {
        return currentEnergy;
    }

    public int maxHp() {
        return stats.get(Stat.HP);
    }

    public int maxEnergy() {
        return stats.get(Stat.ENERGY);
    }

    public boolean hasFainted() {
        return currentHp == 0;
    }

    public boolean canAfford(Skill skill) {
        return currentEnergy >= skill.energyCost();
    }

    public void payEnergyCost(int cost) {
        // Cost can be negative (e.g. Wait recovers energy), so clamp both ends.
        currentEnergy = clamp(currentEnergy - cost, 0, maxEnergy());
    }

    @Override
    public void takeDamage(int amount) {
        currentHp = Math.max(0, currentHp - amount);
    }

    @Override
    public int current(Stat stat) {
        return stats.get(stat);
    }

    @Override
    public int raise(Stat stat, int points) {
        int current = stats.get(stat);
        int gained = Math.min(points, stat.maximum() - current);
        stats.put(stat, current + gained);
        return gained;
    }

    @Override
    public void addEffect(StatusEffect effect) {
        // Re-applying an effect the Mokepon already has restarts it rather than stacking.
        effects.removeIf(existing -> existing.getClass() == effect.getClass());
        effects.add(effect);
    }

    @Override
    public boolean isBlinded() {
        for (StatusEffect effect : effects) {
            if (effect.blinds()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double damageMultiplier() {
        double multiplier = 1.0;
        for (StatusEffect effect : effects) {
            multiplier *= effect.damageMultiplier();
        }
        return multiplier;
    }

    @Override
    public void restoreHp() {
        currentHp = maxHp();
    }

    @Override
    public void restoreEnergy() {
        currentEnergy = maxEnergy();
    }

    @Override
    public void reduceHpToOne() {
        currentHp = 1;
    }

    // Called after this Mokepon uses a skill; see StatusEffect.afterOwnerTurn.
    public void turnTaken() {
        for (StatusEffect effect : effects) {
            effect.afterOwnerTurn();
        }
        effects.removeIf(StatusEffect::isFinished);
    }

    // Ticks every effect once, then drops any that have run out.
    public List<EffectEvent> endOfRound() {
        List<EffectEvent> events = new ArrayList<>();
        for (StatusEffect effect : effects) {
            events.addAll(effect.endOfRound(this));
        }
        effects.removeIf(StatusEffect::isFinished);
        return events;
    }

    private static int clamp(int value, int lowest, int highest) {
        return Math.max(lowest, Math.min(highest, value));
    }
}
