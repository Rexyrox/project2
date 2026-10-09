package au.edu.unimelb.swen.oop.mokepon.ui;


import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

// The "Game" menu: Reset and Restart Battle (only enabled during a battle) and Exit.
public class GameMenuBar extends MenuBar {

    public GameMenuBar(GameActions actions, ReadOnlyObjectProperty<ScreenState> screenState) {
        MenuItem runAway = new MenuItem("Run away");
        runAway.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
        runAway.setOnAction(event -> actions.runAway());
        runAway.disableProperty().bind(screenState.isNotEqualTo(ScreenState.BATTLE));

        MenuItem mainMenu = new MenuItem("Main menu");
        mainMenu.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN));
        mainMenu.setOnAction(event -> actions.mainMenu());
        mainMenu.disableProperty().bind(screenState.isEqualTo(ScreenState.SELECT));

        MenuItem exit = new MenuItem("Exit");
        exit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
        exit.setOnAction(event -> actions.exit());

        getMenus().add(new Menu("Game", null, runAway, mainMenu, exit));
    }
}
