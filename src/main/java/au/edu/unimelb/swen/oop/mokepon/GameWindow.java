package au.edu.unimelb.swen.oop.mokepon;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.item.Inventory;
import au.edu.unimelb.swen.oop.mokepon.ui.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
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
import java.util.List;
import java.util.Random;


// The main window: owns the current screen (Select or Battle) and the choices that carry
// across screens (which Mokepon the player picked, and the randomly chosen enemy).
public class GameWindow extends Stage implements GameActions {

    private static final double SCREEN_WIDTH = 1280;
    private static final double SCREEN_HEIGHT = 768;

    private final Assets assets;
    private final List<MokeponSpec> mokepon;
    private final StackPane content = new StackPane();
    private Inventory inventory = new Inventory();
    private final Random random = new Random();
    private final ObjectProperty<ScreenState> screenState =
            new SimpleObjectProperty<>(ScreenState.SELECT);

    private MokeponSpec playerMokepon;
    private MokeponSpec enemyMokepon;

    public GameWindow(Assets assets, List<MokeponSpec> mokepon) {
        this.assets = assets;
        this.mokepon = mokepon;

        setTitle("Mokepon");
        setResizable(false);

        content.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        content.setBackground(background());

        BorderPane frame = new BorderPane();
        frame.setTop(new GameMenuBar(this, screenState));
        frame.setCenter(content);

        setScene(new Scene(frame));
        getScene().getStylesheets().add(assets.stylesheet("assets/mokepon.css"));
        sizeToScene();

        showSelectScreen();
    }

    // Picks a random enemy independently of the player's choice, then enters the Battle Screen.
    @Override
    public void chooseMokepon(MokeponSpec player) {
        playerMokepon = player;
        enemyMokepon = mokepon.get(random.nextInt(mokepon.size()));
        showBattleScreen();
    }

    // Clears the current selection and returns to the Select Screen.
    @Override
    public void reset() {
        playerMokepon = null;
        enemyMokepon = null;
        inventory = new Inventory();
        showSelectScreen();
    }

    @Override
    public void restartBattle() {
        // Rebuilding the screen (same specs) is what resets hp/energy/fainted state and the log.
        if (playerMokepon != null) {
            showBattleScreen();
        }
    }

    @Override
    public void exit() {
        Platform.exit();
    }

    private void showSelectScreen() {
        showScreen(new SelectScreen(assets, mokepon, this), ScreenState.SELECT);
    }

    private void showBattleScreen() {
        showScreen(new BattleScreen(assets, playerMokepon, enemyMokepon, inventory),
                ScreenState.BATTLE);
    }

    // Swaps in a new screen at the fixed game size and records which screen is now showing.
    private void showScreen(Pane screen, ScreenState newState) {
        screen.setMinSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        screen.setMaxSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        content.getChildren().setAll(screen);
        screenState.set(newState);
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

