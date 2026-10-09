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

    public GameMenuBar(GameActions actions,ReadOnlyObjectProperty<ScreenState> screenState) {
        MenuItem reset = new MenuItem("Reset");
        reset.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
        reset.setOnAction(event -> actions.reset());

        MenuItem restartBattle = new MenuItem("Restart Battle");
        restartBattle.setAccelerator(new KeyCodeCombination(KeyCode.R));
        restartBattle.setOnAction(event -> actions.restartBattle());

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> actions.exit());

        reset.disableProperty().bind(screenState.isNotEqualTo(ScreenState.BATTLE));
        restartBattle.disableProperty().bind(screenState.isNotEqualTo(ScreenState.BATTLE));


        getMenus().add(new Menu("Game", null, reset, restartBattle, exit));
    }
}
