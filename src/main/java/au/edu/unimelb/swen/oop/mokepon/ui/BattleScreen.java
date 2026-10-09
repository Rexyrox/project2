package au.edu.unimelb.swen.oop.mokepon.ui;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.skill.Skill;
import au.edu.unimelb.swen.oop.mokepon.model.Battle;
import au.edu.unimelb.swen.oop.mokepon.model.Battler;
import au.edu.unimelb.swen.oop.mokepon.skill.DefaultRandomiserSource;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

// The battle screen: owns a Battle for the two chosen Mokepon and renders its current state,
// re-drawing after every player action.
public class BattleScreen extends Pane {

    private static final double NAME_FONT_SIZE = 28;
    private static final double ACTIONS_HEADING_FONT_SIZE = 24;
    private static final double ACTION_FONT_SIZE = 17;

    private static final double IMAGE_SIZE = 256;
    private static final double BAR_WIDTH = 240;
    private static final double BAR_HEIGHT = 20;
    private static final double PANEL_SPACING = 6;

    private static final double PLAYER_SCROLL_WIDTH = 500;
    private static final double PLAYER_SCROLL_HEIGHT = 450;
    private static final double PLAYER_X = 42;
    private static final double PLAYER_Y = 270;
    private static final Insets PLAYER_PADDING = new Insets(40, 35, 20, 35);

    private static final double ENEMY_SCROLL_WIDTH = 300;
    private static final double ENEMY_SCROLL_HEIGHT = 435;
    private static final double ENEMY_X = 890;
    private static final double ENEMY_Y = 30;
    private static final Insets ENEMY_PADDING = new Insets(40, 60, 40, 60);

    private static final double LOG_X = 560;
    private static final double LOG_Y = 475;
    private static final double LOG_WIDTH = 675;
    private static final double LOG_HEIGHT = 250;

    private final Assets assets;
    private final Battle battle;

    private final StatBar playerHp = new StatBar(BAR_WIDTH, BAR_HEIGHT, Color.LIMEGREEN, true);
    private final StatBar playerEnergy = new StatBar(BAR_WIDTH, BAR_HEIGHT, Color.ROYALBLUE, true);
    private final StatBar enemyHp = new StatBar(BAR_WIDTH, BAR_HEIGHT, Color.LIMEGREEN, false);
    private final StatBar enemyEnergy = new StatBar(BAR_WIDTH, BAR_HEIGHT, Color.ROYALBLUE, false);
    private final TextArea log = new TextArea();
    private final List<Button> actionButtons = new ArrayList<>();
    private final EventHandler<KeyEvent> hotkeys = this::useHotkey;

    private ScrollPanel playerScroll;
    private ScrollPanel enemyScroll;

    public BattleScreen(Assets assets, MokeponSpec player, MokeponSpec enemy) {
        this.assets = assets;
        this.battle = new Battle(player, enemy, new DefaultRandomiserSource());

        getChildren().addAll(playerPanel(), enemyPanel(), battleLog());
        listenForHotkeys();
        refresh();

    }

    // Re-reads the Battle's current state into every on-screen element.
    private void refresh() {
        Battler player = battle.player();
        Battler enemy = battle.enemy();

        playerHp.setValue(player.currentHp(), player.spec().maxHp());
        playerEnergy.setValue(player.currentEnergy(), player.spec().maxEnergy());
        enemyHp.setValue(enemy.currentHp(), enemy.spec().maxHp());
        enemyEnergy.setValue(enemy.currentEnergy(), enemy.spec().maxEnergy());

        List<Skill> skills = player.spec().skills();
        for (int index = 0; index < actionButtons.size(); index++) {
            actionButtons.get(index).setDisable(battle.isOver() || !player.canAfford(skills.get(index)));
        }

        playerScroll.setFainted(player.hasFainted());
        enemyScroll.setFainted(enemy.hasFainted());

        log.setText(battle.log().text());
        log.positionCaret(log.getLength());
        log.setScrollTop(Double.MAX_VALUE);
    }

    // Fires the action button matching a 1-4 digit key press, if it isn't disabled.
    private void useHotkey(KeyEvent event) {
        int index = hotkeyIndex(event.getCode());
        if (index < 0 || index >= actionButtons.size()) {
            return;
        }
        Button action = actionButtons.get(index);
        if (!action.isDisabled()) {
            action.fire();
        }
        event.consume();
    }

    // Maps to a position in actionButtons, built in the same order as the player's skill list.
    private static int hotkeyIndex(KeyCode code) {
        return switch (code) {
            case DIGIT1 -> 0;
            case DIGIT2 -> 1;
            case DIGIT3 -> 2;
            case DIGIT4 -> 3;
            default -> -1;
        };
    }

    // Attaches the hotkey filter to whichever Scene this screen is currently part of.
    private void listenForHotkeys() {
        sceneProperty().addListener((property, previous, current) -> {
            if (previous != null) {
                previous.removeEventFilter(KeyEvent.KEY_PRESSED, hotkeys);
            }
            if (current != null) {
                current.addEventFilter(KeyEvent.KEY_PRESSED, hotkeys);
            }
        });
    }


    // Builds the bottom-left player scroll: name, portrait, hp/energy bars and action buttons.
    private ScrollPanel playerPanel() {
        MokeponSpec spec = battle.player().spec();

        VBox mokepon = new VBox(PANEL_SPACING, mokeponImage(spec), playerHp, playerEnergy);
        mokepon.setAlignment(Pos.CENTER);

        HBox body = new HBox(PANEL_SPACING, mokepon, actions(spec));
        body.setAlignment(Pos.CENTER);

        VBox panel = new VBox(PANEL_SPACING, nameHeading(spec.name()), body);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setPadding(PLAYER_PADDING);

        playerScroll = new ScrollPanel(assets, panel, PLAYER_SCROLL_WIDTH, PLAYER_SCROLL_HEIGHT);
        playerScroll.relocate(PLAYER_X, PLAYER_Y);
        return playerScroll;
    }


    // Builds the top-right enemy scroll: name, portrait and hp/energy bars (no numeric readout).
    private ScrollPanel enemyPanel() {
        MokeponSpec spec = battle.enemy().spec();

        VBox panel = new VBox(
                PANEL_SPACING, nameHeading(spec.name()), mokeponImage(spec), enemyHp, enemyEnergy);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setPadding(ENEMY_PADDING);

        enemyScroll = new ScrollPanel(assets, panel, ENEMY_SCROLL_WIDTH, ENEMY_SCROLL_HEIGHT);
        enemyScroll.relocate(ENEMY_X, ENEMY_Y);
        return enemyScroll;
    }


    // Unused: fainted state is already driven by refresh() after every action.
    public void setPlayerFainted(boolean fainted) {
        playerScroll.setFainted(fainted);
    }

    // Unused: fainted state is already driven by refresh() after every action.
    public void setEnemyFainted(boolean fainted) {
        enemyScroll.setFainted(fainted);
    }

    // The "Actions" heading plus one button per skill the player's Mokepon knows.
    private VBox actions(MokeponSpec player) {
        Label heading = new Label("Actions");
        heading.setFont(assets.gameFont(ACTIONS_HEADING_FONT_SIZE));
        heading.setTextFill(Color.BLACK);

        VBox list = new VBox(PANEL_SPACING, heading);
        list.setAlignment(Pos.TOP_CENTER);
        for (Skill skill : player.skills()) {
            list.getChildren().add(actionButton(skill));
        }
        return list;
    }

    // A single skill button; clicking it plays the skill and refreshes the screen.
    private Button actionButton(Skill skill) {
        Button action = new Button(skill.name() + " [" + skill.energyCost() + "]");
        action.setFont(assets.gameFont(ACTION_FONT_SIZE));
        action.setTextFill(Color.BLACK);
        action.setMaxWidth(Double.MAX_VALUE);
        action.getStyleClass().add("action-button");
        action.setOnAction(event -> {
            battle.playerUses(skill);
            refresh();
        });
        actionButtons.add(action);
        return action;
    }

    private Label nameHeading(String mokeponName) {
        Label heading = new Label(mokeponName);
        heading.setFont(assets.gameFont(NAME_FONT_SIZE));
        heading.setTextFill(Color.BLACK);
        return heading;
    }

    private ImageView mokeponImage(MokeponSpec spec){
        ImageView image = new ImageView(assets.image(spec.imagePath()));
        image.setFitWidth(IMAGE_SIZE);
        image.setFitHeight(IMAGE_SIZE);
        image.setPreserveRatio(true);
        return image;
    }

    private TextArea battleLog() {
        log.setEditable(false);
        log.setFocusTraversable(false);
        log.getStyleClass().add("battle-log");
        log.setPrefSize(LOG_WIDTH, LOG_HEIGHT);
        log.relocate(LOG_X, LOG_Y);
        return log;
    }

}
