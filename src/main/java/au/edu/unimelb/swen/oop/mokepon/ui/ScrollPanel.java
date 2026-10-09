package au.edu.unimelb.swen.oop.mokepon.ui;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

// A scroll.png background with arbitrary contents stacked on top, plus an optional fainted tint.
public class ScrollPanel extends StackPane {

    private static final Color FAINTED_COLOUR = Color.web("#b41e1e", 0.35);

    private final ImageView scroll;
    private final Blend faintedTint;

    public ScrollPanel(Assets assets, Pane contents, double width, double height) {
        scroll = new ImageView(assets.image("assets/scroll.png"));
        scroll.setFitWidth(width);
        scroll.setFitHeight(height);

        // SRC_ATOP masks the colour overlay to the scroll image's own shape, not a plain rectangle.
        faintedTint = new Blend(BlendMode.SRC_ATOP);
        faintedTint.setTopInput(new ColorInput(0, 0, width, height, FAINTED_COLOUR));

        getChildren().addAll(scroll, contents);
        setMaxSize(width, height);
    }

    // Toggles the red fainted overlay on the scroll image.
    public void setFainted(boolean fainted) {
        scroll.setEffect(fainted ? faintedTint : null);
    }
}
