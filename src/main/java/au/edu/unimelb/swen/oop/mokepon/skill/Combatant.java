package au.edu.unimelb.swen.oop.mokepon.skill;

// The battle-facing view of a Mokepon that skills read from and act on.
public interface Combatant {

    String name();

    int current(Stat stat);

    // Raises a stat by up to points (capped at the maximum); returns the amount actually gained.
    int raise(Stat stat, int points);

    // Reduces hp by amount, clamped so it never drops below 0.
    void takeDamage(int amount);

    double damageMultiplier();

    void addEffect(StatusEffect effect);

    boolean isBlinded();
}
