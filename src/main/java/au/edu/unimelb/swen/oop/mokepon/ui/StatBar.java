package au.edu.unimelb.swen.oop.mokepon.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

// An hp/energy progress bar with an optional "current / maximum" label overlaid on top.
public class StatBar extends StackPane {

    private static final double MINIMUM_VISIBLE_FRACTION = 0.03;
    private static final double TEXT_FONT_SIZE = 11;
    private static final double BAR_CORNER_RADIUS = 5;
    private static final double FILL_INSET = 2;
    private static final double TRACK_STROKE_OPACITY = 0.5;

    private final Rectangle fill;
    private final Label text;
    private final double barWidth;

    public StatBar(double barWidth, double barHeight, Color colour, boolean labelled) {
        this.barWidth = barWidth;

        Rectangle track = new Rectangle(barWidth, barHeight, Color.WHITESMOKE);
        track.setStroke(Color.color(0, 0, 0, TRACK_STROKE_OPACITY));
        track.setArcWidth(BAR_CORNER_RADIUS);
        track.setArcHeight(BAR_CORNER_RADIUS);

        fill = new Rectangle(barWidth - FILL_INSET * 2, barHeight - FILL_INSET * 2, colour);
        fill.setArcWidth(BAR_CORNER_RADIUS);
        fill.setArcHeight(BAR_CORNER_RADIUS);
        StackPane.setMargin(fill, new Insets(FILL_INSET));

        text = new Label();
        text.setFont(Font.font("Monospaced", FontWeight.BOLD, TEXT_FONT_SIZE));
        text.setTextFill(Color.BLACK);
        text.setVisible(labelled);

        getChildren().addAll(track, fill, text);
        setAlignment(Pos.CENTER_LEFT);
        StackPane.setAlignment(text, Pos.CENTER);
        setMaxSize(barWidth, barHeight);
    }

    // Resizes the fill to current/maximum and updates the label text.
    public void setValue(int current, int maximum) {
        fill.setWidth((barWidth - FILL_INSET * 2) * visibleFraction(current, maximum));
        text.setText(current + " / " + maximum);
    }

    // A tiny sliver stays visible above 0 so the bar doesn't look empty while some value remains.
    private static double visibleFraction(int current, int maximum) {
        if (current == 0) {
            return 0;
        }
        return Math.max(MINIMUM_VISIBLE_FRACTION, (double) current / maximum);
    }
}
