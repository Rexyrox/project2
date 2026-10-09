package au.edu.unimelb.swen.oop.mokepon.ui;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;

// Screen-independent game actions, implemented by GameWindow and triggered from the menu bar
// and the select/battle screens.
public interface GameActions {
    // Returns to the Mokepon Select Screen with nothing selected.
    void reset();

    // Restarts the current battle from scratch with the same two Mokepon.
    void restartBattle();

    void exit();

    // Starts a battle with the given Mokepon as the player's choice.
    void chooseMokepon(MokeponSpec player);
}
