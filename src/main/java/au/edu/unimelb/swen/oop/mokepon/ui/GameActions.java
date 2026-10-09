package au.edu.unimelb.swen.oop.mokepon.ui;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.model.Encounter;


public interface GameActions {

    void chooseMokepon(MokeponSpec player);

    void fight(Encounter encounter);

    void runAway();

    void battleWon();

    void mainMenu();

    void exit();
}
