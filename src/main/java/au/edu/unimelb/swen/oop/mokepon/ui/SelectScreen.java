package au.edu.unimelb.swen.oop.mokepon.ui;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponType;
import au.edu.unimelb.swen.oop.mokepon.skill.Skill;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// The Mokepon Select Screen: a list of Mokepon to choose from, a detail panel for the current
// selection, and a button to move on to the Battle Screen.
public class SelectScreen extends Pane {

    private static final String NO_SELECTION_NAME = "Select a Mokepon";
    private static final String[] STAT_NAMES = {
            "HP:", "Energy:", "Strength:", "Accuracy:", "Defence:", "Dodge:", "Skills:", "Type:"
    };

    private static final double HEADING_FONT_SIZE = 34;
    private static final double HEADING_X = 45;
    private static final double HEADING_Y = 40;

    private static final double LIST_FONT_SIZE = 20;
    private static final double LIST_SPACING = 4;
    private static final double LIST_X = 55;
    private static final double LIST_Y = 90;
    private static final double LIST_ENTRY_WIDTH = 300;
    private static final double LIST_HEIGHT = 620;
    private static final double LIST_SCROLLBAR_ALLOWANCE = 20;

    private static final double PANEL_X = 400;
    private static final double PANEL_Y = 40;
    private static final double PANEL_SPACING = 10;

    private static final double PORTRAIT_IMAGE_SIZE = 256;
    private static final double PORTRAIT_RADIUS = 175;
    private static final double PORTRAIT_BORDER_WIDTH = 2;
    private static final double NAME_FONT_SIZE = 30;

    private static final double SCROLL_WIDTH = 420;
    private static final double SCROLL_HEIGHT = 260;
    private static final double STAT_FONT_SIZE = 14;
    private static final double STAT_COLUMN_GAP = 20;
    private static final double STAT_ROW_GAP = 4;
    private static final Insets STAT_PADDING = new Insets(30, 50, 30, 50);

    private static final double CHOOSE_FONT_SIZE = 24;
    private static final double CHOOSE_BUTTON_WIDTH = 160;
    private static final double CHOOSE_GAP = 20;

    private static final double TYPE_ICON_SIZE = 24;

    private final Assets assets;
    private final GameActions actions;
    private final ToggleGroup selection = new ToggleGroup();
    private final ImageView portrait = new ImageView();
    private final Label name = new Label(NO_SELECTION_NAME);
    private final List<Label> statValues = new ArrayList<>();

    private final Map<MokeponType, String> typeImages;
    private final ImageView typeIcon = new ImageView();

    public SelectScreen(Assets assets, List<MokeponSpec> specs,
                        Map<MokeponType, String> typeImages, GameActions actions) {
        this.assets = assets;
        this.typeImages = typeImages;
        this.actions = actions;
        getChildren().addAll(heading(), mokeponList(specs), detailPanel());
        selection.selectedToggleProperty().addListener((property, previous, current) -> showSelection());
    }

    // The currently selected Mokepon, or null if none has been picked yet.
    public MokeponSpec selected() {
        return selection.getSelectedToggle() == null
                ? null
                : (MokeponSpec) selection.getSelectedToggle().getUserData();
    }

    // Refreshes the portrait, name and stats panel to match the current selection (or lack of one).
    private void showSelection() {
        MokeponSpec spec = selected();
        portrait.setImage(spec == null ? null : assets.image(spec.imagePath()));
        name.setText(spec == null ? NO_SELECTION_NAME : spec.name());

        // NORMAL has no image (spec 3.6.3), so typeImages.get returns null for it.
        String typeImagePath = spec == null ? null : typeImages.get(spec.type());
        typeIcon.setImage(typeImagePath == null ? null : assets.image(typeImagePath));
        typeIcon.setManaged(typeImagePath != null);

        String[] values = spec == null ? new String[STAT_NAMES.length] : statisticsOf(spec);
        for (int row = 0; row < statValues.size(); row++) {
            statValues.get(row).setText(values[row] == null ? "" : values[row]);
        }
    }

    // The stat values in the same order as STAT_NAMES, skills joined as a comma-separated list.
    private static String[] statisticsOf(MokeponSpec spec) {
        return new String[] {
                String.valueOf(spec.maxHp()),
                String.valueOf(spec.maxEnergy()),
                String.valueOf(spec.strength()),
                String.valueOf(spec.accuracy()),
                String.valueOf(spec.defence()),
                String.valueOf(spec.dodge()),
                spec.skills().stream().map(Skill::name).collect(Collectors.joining(", ")),
                spec.type().name()
        };
    }

    // The "Choose your Mokepon" heading, top-left.
    private Label heading() {
        Label heading = new Label("Choose your Mokepon");
        heading.setFont(assets.gameFont(HEADING_FONT_SIZE));
        heading.setTextFill(Color.BLACK);
        heading.setEffect(whiteOutline());
        heading.relocate(HEADING_X, HEADING_Y);
        return heading;
    }

    // The selectable list of Mokepon names, one toggle button per spec.
    private ScrollPane mokeponList(List<MokeponSpec> specs) {
        VBox list = new VBox(LIST_SPACING);
        for (MokeponSpec spec : specs) {
            list.getChildren().add(listEntry(spec));
        }

        ScrollPane scroll = new ScrollPane(list);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setPrefViewportWidth(LIST_ENTRY_WIDTH + LIST_SCROLLBAR_ALLOWANCE);
        scroll.setPrefViewportHeight(LIST_HEIGHT);
        scroll.getStyleClass().add("mokepon-list");
        scroll.relocate(LIST_X, LIST_Y);
        return scroll;
    }

    // One list entry; toggling it (via the shared group) drives showSelection().
    private ToggleButton listEntry (MokeponSpec spec) {
        ToggleButton entry = new ToggleButton(spec.name());
        entry.setToggleGroup(selection);
        entry.setUserData(spec);
        entry.setFont(assets.gameFont(LIST_FONT_SIZE));
        entry.setTextFill(Color.BLACK);
        entry.getStyleClass().add("mokepon-list-entry");
        entry.setPrefWidth(LIST_ENTRY_WIDTH);
        return entry;
    }

    // The portrait/name/stats/choose-button panel to the right of the list.
    private VBox detailPanel() {
        portrait.setFitWidth(PORTRAIT_IMAGE_SIZE);
        portrait.setFitHeight(PORTRAIT_IMAGE_SIZE);
        portrait.setPreserveRatio(true);

        name.setFont(assets.gameFont(NAME_FONT_SIZE));
        name.setTextFill(Color.BLACK);
        name.setEffect(whiteOutline());

        typeIcon.setFitWidth(TYPE_ICON_SIZE);
        typeIcon.setFitHeight(TYPE_ICON_SIZE);
        typeIcon.setPreserveRatio(true);
        typeIcon.setManaged(false);

        // The type icon sits after the name heading (spec 3.6.2), so they share a row.
        HBox nameRow = new HBox(PANEL_SPACING, name, typeIcon);
        nameRow.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        spacer.setMinWidth(CHOOSE_BUTTON_WIDTH);

        HBox scrollRow = new HBox(CHOOSE_GAP, spacer, statisticsScroll(), chooseButton());
        scrollRow.setAlignment(Pos.CENTER);

        VBox panel = new VBox(
                PANEL_SPACING, new StackPane(portraitBackground(), portrait), nameRow, scrollRow);
        panel.setAlignment(Pos.CENTER);
        panel.relocate(PANEL_X, PANEL_Y);
        return panel;
    }

    // The scroll.png-backed statistics table; also populates statValues for showSelection().
    private StackPane statisticsScroll() {
        ImageView background = new ImageView(assets.image("assets/scroll.png"));
        background.setFitWidth(SCROLL_WIDTH);
        background.setFitHeight(SCROLL_HEIGHT);

        GridPane statistics = new GridPane();
        statistics.setHgap(STAT_COLUMN_GAP);
        statistics.setVgap(STAT_ROW_GAP);
        statistics.setPadding(STAT_PADDING);
        for (String statName : STAT_NAMES) {
            Label value = statisticLabel("");
            statValues.add(value);
            statistics.addRow(statValues.size() - 1, statisticLabel(statName), value);
        }

        return new StackPane(background, statistics);
    }

    // The "Choose Mokepon" button; disabled until a Mokepon is selected.
    private Button chooseButton() {
        Button choose = new Button("Choose\nMokepon");
        choose.setFont(assets.gameFont(CHOOSE_FONT_SIZE));
        choose.setTextFill(Color.BLACK);
        choose.setTextAlignment(TextAlignment.CENTER);
        choose.getStyleClass().add("choose-button");
        choose.disableProperty().bind(selection.selectedToggleProperty().isNull());
        choose.setOnAction(event -> actions.chooseMokepon(selected()));
        return choose;
    }

    // The semi-transparent grey circle behind the portrait.
    private Circle portraitBackground() {
        Circle background = new Circle(PORTRAIT_RADIUS);
        background.setFill(Color.grayRgb(211, 0.5));
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(PORTRAIT_BORDER_WIDTH);
        return background;
    }

    private Label statisticLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Monospaced", STAT_FONT_SIZE));
        label.setTextFill(Color.BLACK);
        return label;
    }


    private static DropShadow whiteOutline() {
        return new DropShadow(BlurType.ONE_PASS_BOX, Color.WHITE, 7, 1.0, 0, 0);
    }

}
