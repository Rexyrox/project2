package au.edu.unimelb.swen.oop.mokepon.ui;

import au.edu.unimelb.swen.oop.mokepon.item.ItemType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class ItemButton extends Button {

    private static final double IMAGE_SIZE = 32;
    private static final double COUNT_FONT_SIZE = 24;
    private static final double SPACING = 4;

    private final ItemType type;
    private final Label count = new Label();

    public ItemButton(Assets assets, ItemType type, String imagePath) {
        this.type = type;

        ImageView image = new ImageView(assets.image(imagePath));
        image.setFitWidth(IMAGE_SIZE);
        image.setFitHeight(IMAGE_SIZE);
        image.setPreserveRatio(true);

        count.setFont(assets.gameFont(COUNT_FONT_SIZE));
        count.setTextFill(Color.BLACK);

        VBox content = new VBox(SPACING, image, count);
        content.setAlignment(Pos.CENTER);
        setGraphic(content);
        getStyleClass().add("item-button");
    }

    public ItemType type() {
        return type;
    }

    public void update(int quantity, boolean usable) {
        count.setText(String.valueOf(quantity));
        setDisable(!usable);
    }
}