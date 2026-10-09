package au.edu.unimelb.swen.oop.mokepon.skill;

// The battle-facing view of a Mokepon that skills read from and act on.
public interface Combatant {

    String name();

    int currentStrength();

    int currentAccuracy();

    int currentDefence();

    int currentDodge();

    // Reduces hp by amount, clamped so it never drops below 0.
    void takeDamage(int amount);

    // Raises defence by up to points (capped at the maximum); returns the amount actually gained.
    int raiseDefence(int points);

}
