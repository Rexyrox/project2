package au.edu.unimelb.swen.oop.mokepon.model;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.model.Battle;
import au.edu.unimelb.swen.oop.mokepon.skill.Combatant;
import au.edu.unimelb.swen.oop.mokepon.skill.Skill;

// One Mokepon's mutable state during a Battle: hp/energy/defence layered over its fixed spec.
public class Battler implements Combatant {

    private static final int MAXIMUM_DEFENCE = 100;

    private final MokeponSpec spec;

    private int currentHp;
    private int currentEnergy;
    private int currentDefence;

    public Battler(MokeponSpec spec) {
        this.spec = spec;
        this.currentHp = spec.maxHp();
        this.currentEnergy = spec.maxEnergy();
        this.currentDefence = spec.defence();
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

    public boolean hasFainted() {
        return currentHp == 0;
    }

    // Whether there's enough current energy to pay this skill's cost.
    public boolean canAfford(Skill skill) {
        return currentEnergy >= skill.energyCost();
    }

    public void payEnergyCost(int cost) {
        // Cost can be negative (e.g. Wait recovers energy), so clamp both ends.
        currentEnergy = clamp(currentEnergy - cost, 0, spec.maxEnergy());
    }

    @Override
    public void takeDamage(int amount) {
        currentHp = Math.max(0, currentHp - amount);
    }

    @Override
    public int raiseDefence(int points) {
        int gained = Math.min(points, MAXIMUM_DEFENCE - currentDefence);
        currentDefence += gained;
        // Return the amount actually applied (not the raw roll) so the log reports it correctly.
        return gained;
    }

    @Override
    public int currentStrength() {
        return spec.strength();
    }

    @Override
    public int currentAccuracy() {
        return spec.accuracy();
    }

    @Override
    public int currentDefence() {
        return currentDefence;
    }

    @Override
    public int currentDodge() {
        return spec.dodge();
    }

    private static int clamp(int value, int lowest, int highest) {
        return Math.max(lowest, Math.min(highest, value));
    }

}
