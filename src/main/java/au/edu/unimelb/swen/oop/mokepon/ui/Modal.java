package au.edu.unimelb.swen.oop.mokepon.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Modal extends StackPane {

    private static final Color OVERLAY = Color.rgb(0, 0, 0, 0.45);
    private static final double HEADING_FONT_SIZE = 32;
    private static final double SPACING = 15;
    private static final Insets DIALOG_PADDING = new Insets(25, 35, 25, 35);

    private final HBox buttons = new HBox(SPACING);

    public Modal(Assets assets, String heading, String... messages) {
        // The overlay covers the whole screen, so it also swallows clicks meant for the screen
        // behind it.
        setBackground(new Background(new BackgroundFill(OVERLAY, null, null)));

        Label title = new Label(heading);
        title.setFont(assets.gameFont(HEADING_FONT_SIZE));
        title.setTextFill(Color.BLACK);

        VBox dialog = new VBox(SPACING, title);
        for (String message : messages) {
            dialog.getChildren().add(new Label(message));
        }
        buttons.setAlignment(Pos.CENTER);
        dialog.getChildren().add(buttons);
        dialog.setAlignment(Pos.CENTER);
        dialog.setPadding(DIALOG_PADDING);
        // Stops the StackPane stretching the dialog to fill the screen.
        dialog.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        dialog.getStyleClass().add("modal-dialog");

        getChildren().add(dialog);
    }

    public Modal withButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(event -> action.run());
        buttons.getChildren().add(button);
        return this;
    }

    public void showOver(Pane screen) {
        prefWidthProperty().bind(screen.widthProperty());
        prefHeightProperty().bind(screen.heightProperty());
        screen.getChildren().add(this);
    }
}
