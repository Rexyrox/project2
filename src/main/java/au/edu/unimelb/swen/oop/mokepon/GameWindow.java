package au.edu.unimelb.swen.oop.mokepon;

import au.edu.unimelb.swen.oop.mokepon.data.GameData;
import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.model.Battle;
import au.edu.unimelb.swen.oop.mokepon.model.Encounter;
import au.edu.unimelb.swen.oop.mokepon.model.GameSession;
import au.edu.unimelb.swen.oop.mokepon.skill.DefaultRandomiserSource;
import au.edu.unimelb.swen.oop.mokepon.skill.Randomiser;
import au.edu.unimelb.swen.oop.mokepon.ui.Assets;
import au.edu.unimelb.swen.oop.mokepon.ui.BattleScreen;
import au.edu.unimelb.swen.oop.mokepon.ui.GameActions;
import au.edu.unimelb.swen.oop.mokepon.ui.GameMenuBar;
import au.edu.unimelb.swen.oop.mokepon.ui.OverworldScreen;
import au.edu.unimelb.swen.oop.mokepon.ui.ScreenState;
import au.edu.unimelb.swen.oop.mokepon.ui.SelectScreen;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// The main window: owns the current screen (Select, Overworld or Battle) and the GameSession
// that carries the player's progress between them.
public class GameWindow extends Stage implements GameActions {

    private static final double SCREEN_WIDTH = 1280;
    private static final double SCREEN_HEIGHT = 768;

    private final Assets assets;
    private final GameData data;
    private final Randomiser randomiser = new DefaultRandomiserSource();
    private final StackPane content = new StackPane();
    private final ObjectProperty<ScreenState> screenState =
            new SimpleObjectProperty<>(ScreenState.SELECT);
    // False once a battle has a result, so Run away can't undo a win or a loss.
    private final BooleanProperty battleInProgress = new SimpleBooleanProperty(false);

    private GameSession session;
    private Encounter currentEncounter;

    public GameWindow(Assets assets, GameData data) {
        this.assets = assets;
        this.data = data;

        setTitle("Mokepon");
        setResizable(false);

        content.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        content.setBackground(background());

        BorderPane frame = new BorderPane();
        frame.setTop(new GameMenuBar(this, screenState, battleInProgress));
        frame.setCenter(content);

        setScene(new Scene(frame));
        getScene().getStylesheets().add(assets.stylesheet("assets/mokepon.css"));
        sizeToScene();

        showSelectScreen();
    }

    @Override
    public void chooseMokepon(MokeponSpec player) {
        session = new GameSession(player, data, randomiser);
        showOverworldScreen("");
    }

    @Override
    public void fight(Encounter encounter) {
        currentEncounter = encounter;
        Battle battle = new Battle(session.playerMokepon(), encounter.enemy(),
                encounter.terrain(), session.inventory(), randomiser);
        showScreen(new BattleScreen(assets, battle, data.itemImages(), this), ScreenState.BATTLE);
    }

    @Override
    public void runAway() {
        session.retreat();
        showOverworldScreen("");
    }

    @Override
    public void battleEnded() {
        battleInProgress.set(false);
    }

    @Override
    public void battleWon() {
        session.defeated(currentEncounter.marker());
        showOverworldScreen("You beat the enemy " + currentEncounter.enemy().name() + "!");
    }

    @Override
    public void mainMenu() {
        session = null;
        currentEncounter = null;
        showSelectScreen();
    }

    @Override
    public void exit() {
        Platform.exit();
    }

    private void showSelectScreen() {
        showScreen(new SelectScreen(assets, data.mokepon(), data.typeImages(), this),
                ScreenState.SELECT);
    }

    private void showOverworldScreen(String statusMessage) {
        showScreen(new OverworldScreen(assets, data.map(), session, this, statusMessage),
                ScreenState.OVERWORLD);
    }

    // Swaps in a new screen at the fixed game size and records which screen is now showing.
    private void showScreen(Pane screen, ScreenState newState) {
        screen.setMinSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        screen.setMaxSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        content.getChildren().setAll(screen);
        screenState.set(newState);
        battleInProgress.set(newState == ScreenState.BATTLE);
    }

    // bg.png stretched to fill the fixed game size, used behind every screen.
    private Background background() {
        return new Background(new BackgroundImage(
                assets.image("assets/bg.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(SCREEN_WIDTH, SCREEN_HEIGHT, false, false, false, false)));
    }
}
