package au.edu.unimelb.swen.oop.mokepon.ui;

import au.edu.unimelb.swen.oop.mokepon.data.Direction;
import au.edu.unimelb.swen.oop.mokepon.data.Exit;
import au.edu.unimelb.swen.oop.mokepon.data.MapData;
import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.data.MysteryBox;
import au.edu.unimelb.swen.oop.mokepon.data.Position;
import au.edu.unimelb.swen.oop.mokepon.data.Terrain;
import au.edu.unimelb.swen.oop.mokepon.data.TerrainEffect;
import au.edu.unimelb.swen.oop.mokepon.item.ItemType;
import au.edu.unimelb.swen.oop.mokepon.model.Encounter;
import au.edu.unimelb.swen.oop.mokepon.model.GameSession;
import au.edu.unimelb.swen.oop.mokepon.model.MapMarker;
import au.edu.unimelb.swen.oop.mokepon.model.MoveListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class OverworldScreen extends Pane implements MoveListener {

    private static final double TILE_SIZE = 32;
    private static final double MAP_X = 310;
    private static final double MAP_Y = 34;

    private static final double STATS_X = 1013;
    private static final double STATS_Y = 35;
    private static final double STATS_WIDTH = 220;
    private static final double STATS_HEIGHT = 300;
    private static final Insets STATS_PADDING = new Insets(30, 30, 30, 30);
    private static final double STATS_COLUMN_GAP = 15;
    private static final double STAT_FONT_SIZE = 11;

    private static final double MOVE_X = 1050;
    private static final double MOVE_Y = 610;
    private static final double MOVE_GAP = 4;

    private static final double STATUS_X = 421;
    private static final double STATUS_Y = 706;
    private static final double STATUS_WIDTH = 425;
    private static final double STATUS_HEIGHT = 26;

    private final Assets assets;
    private final MapData map;
    private final GameSession session;
    private final GameActions actions;

    private final Pane sprites = new Pane();
    private final Label status = new Label();
    private final Label fights = statValue("");
    private final Map<ItemType, Label> itemCounts = new EnumMap<>(ItemType.class);
    private final Map<Direction, Button> moveButtons = new EnumMap<>(Direction.class);
    private final EventHandler<KeyEvent> arrowKeys = this::useArrowKey;

    private boolean dialogOpen;

    public OverworldScreen(Assets assets, MapData map, GameSession session, GameActions actions,
                           String statusMessage) {
        this.assets = assets;
        this.map = map;
        this.session = session;
        this.actions = actions;

        sprites.relocate(MAP_X, MAP_Y);
        getChildren().addAll(tileView(), sprites, statsScroll(), movementButtons(), statusBar());
        status.setText(statusMessage);
        listenForArrowKeys();
        refresh();
    }

    private void refresh() {
        sprites.getChildren().clear();
        for (MysteryBox box : session.mysteryBoxes()) {
            sprites.getChildren().add(sprite(map.mysteryBoxImage(), box.position()));
        }
        for (MapMarker marker : session.markers()) {
            sprites.getChildren().add(tintedSprite(
                    marker.appearance().imagePath(), marker.appearance().colour(),
                    marker.position()));
        }
        Exit exit = map.exit();
        sprites.getChildren().add(tintedSprite(exit.imagePath(), exit.colour(), exit.position()));
        sprites.getChildren().add(sprite(map.playerImage(), session.position()));

        fights.setText(String.valueOf(session.fightsWon()));
        for (Map.Entry<ItemType, Label> count : itemCounts.entrySet()) {
            count.getValue().setText(String.valueOf(session.inventory().count(count.getKey())));
        }
        for (Map.Entry<Direction, Button> move : moveButtons.entrySet()) {
            move.getValue().setDisable(dialogOpen || !session.canMove(move.getKey()));
        }
    }

    private void tryMove(Direction direction) {
        if (dialogOpen || !session.canMove(direction)) {
            return;
        }
        status.setText("");
        session.move(direction, this);
        refresh();
    }

    @Override
    public void encountered(Encounter encounter) {
        List<String> lines = new ArrayList<>();
        addTerrainLine(lines, "Your mokepon", session.playerMokepon(), encounter.terrain());
        addTerrainLine(lines, "Your enemy", encounter.enemy(), encounter.terrain());
        showDialog(new Modal(assets, "Encountered a " + encounter.enemy().name(),
                lines.toArray(String[]::new))
                .withButton("Fight!", () -> actions.fight(encounter))
                .withButton("Run Away!", actions::runAway));
    }

    @Override
    public void pickedUp(ItemType item) {
        status.setText("You picked up a " + item.displayName() + "!");
    }

    @Override
    public void reachedExit() {
        showDialog(new Modal(assets,
                "You finished the game! You won " + session.fightsWon() + " fights!")
                .withButton("Return to Main Menu", actions::mainMenu)
                .withButton("Quit", actions::exit));
    }

    private static void addTerrainLine(List<String> lines, String who, MokeponSpec spec,
                                       Terrain terrain) {
        TerrainEffect effect = spec.type().effectOn(terrain);
        if (effect != TerrainEffect.NEUTRAL) {
            lines.add(who + " will be at " + effect.description() + " here!");
        }
    }

    private void showDialog(Modal modal) {
        dialogOpen = true;
        modal.showOver(this);
    }

    private Pane tileView() {
        Pane tiles = new Pane();
        List<List<Terrain>> rows = map.tiles();
        for (int y = 0; y < rows.size(); y++) {
            for (int x = 0; x < rows.get(y).size(); x++) {
                String image = map.tileImages().get(rows.get(y).get(x));
                tiles.getChildren().add(sprite(image, new Position(x, y)));
            }
        }
        tiles.relocate(MAP_X, MAP_Y);
        return tiles;
    }

    private ImageView sprite(String imagePath, Position position) {
        ImageView view = new ImageView(assets.image(imagePath));
        view.setFitWidth(TILE_SIZE);
        view.setFitHeight(TILE_SIZE);
        view.relocate(position.x() * TILE_SIZE, position.y() * TILE_SIZE);
        return view;
    }

    private ImageView tintedSprite(String imagePath, String colour, Position position) {
        ImageView view = sprite(imagePath, position);
        Blend tint = new Blend(BlendMode.SRC_ATOP);
        tint.setTopInput(new ColorInput(0, 0, TILE_SIZE, TILE_SIZE, Color.web(colour)));
        view.setEffect(tint);
        return view;
    }

    private ScrollPanel statsScroll() {
        MokeponSpec spec = session.playerMokepon();
        GridPane grid = new GridPane();
        grid.setHgap(STATS_COLUMN_GAP);
        grid.setPadding(STATS_PADDING);

        int row = 0;
        grid.addRow(row++, statName("Mokepon:"), statName(spec.name()));
        grid.addRow(row++, statName("HP:"), statValue(String.valueOf(spec.maxHp())));
        grid.addRow(row++, statName("Energy:"), statValue(String.valueOf(spec.maxEnergy())));
        grid.addRow(row++, statName("Strength:"), statValue(String.valueOf(spec.strength())));
        grid.addRow(row++, statName("Accuracy:"), statValue(String.valueOf(spec.accuracy())));
        grid.addRow(row++, statName("Defence:"), statValue(String.valueOf(spec.defence())));
        grid.addRow(row++, statName("Dodge:"), statValue(String.valueOf(spec.dodge())));
        grid.addRow(row++, statName("Fights:"), fights);
        grid.add(new Separator(), 0, row++, 2, 1);
        grid.add(statName("Inventory"), 0, row++, 2, 1);
        for (ItemType type : ItemType.values()) {
            Label count = statValue("");
            itemCounts.put(type, count);
            grid.addRow(row++, statName(type.displayName() + ":"), count);
        }

        ScrollPanel panel = new ScrollPanel(assets, grid, STATS_WIDTH, STATS_HEIGHT);
        panel.relocate(STATS_X, STATS_Y);
        return panel;
    }

    private GridPane movementButtons() {
        GridPane grid = new GridPane();
        grid.setHgap(MOVE_GAP);
        grid.setVgap(MOVE_GAP);
        grid.add(moveButton(Direction.UP), 1, 0);
        grid.add(moveButton(Direction.LEFT), 0, 1);
        grid.add(moveButton(Direction.DOWN), 1, 1);
        grid.add(moveButton(Direction.RIGHT), 2, 1);
        grid.relocate(MOVE_X, MOVE_Y);
        return grid;
    }

    private Button moveButton(Direction direction) {
        Button button = new Button();
        button.setGraphic(new ImageView(assets.image(map.buttonImages().get(direction))));
        button.getStyleClass().add("move-button");
        button.setOnAction(event -> tryMove(direction));
        moveButtons.put(direction, button);
        return button;
    }

    private Label statusBar() {
        status.setPrefSize(STATUS_WIDTH, STATUS_HEIGHT);
        status.getStyleClass().add("status-bar");
        status.relocate(STATUS_X, STATUS_Y);
        return status;
    }

    private void useArrowKey(KeyEvent event) {
        Direction direction = switch (event.getCode()) {
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            case LEFT -> Direction.LEFT;
            case RIGHT -> Direction.RIGHT;
            default -> null;
        };
        if (direction != null && !dialogOpen) {
            tryMove(direction);
            event.consume();
        }
    }

    private void listenForArrowKeys() {
        sceneProperty().addListener((property, previous, current) -> {
            if (previous != null) {
                previous.removeEventFilter(KeyEvent.KEY_PRESSED, arrowKeys);
            }
            if (current != null) {
                current.addEventFilter(KeyEvent.KEY_PRESSED, arrowKeys);
            }
        });
    }


    private static Label statName(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("SansSerif", STAT_FONT_SIZE));
        label.setTextFill(Color.BLACK);
        return label;
    }


    private static Label statValue(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Monospaced", STAT_FONT_SIZE));
        label.setTextFill(Color.BLACK);
        return label;
    }
}
