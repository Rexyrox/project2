package au.edu.unimelb.swen.oop.mokepon.ui;


import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;


/*
 * Loads assets from the filesystem, relative to cwd.
 */
public class Assets {
    private static final String HEADING_FONT_PATH = "assets/GrenzeGotisch-Black.ttf";

    private final byte[] headingFontBytes;

    public Assets() {
        this.headingFontBytes = readAllBytes(HEADING_FONT_PATH);
    }

    // Loads the GrenzeGotisch-Black font at the given point size; a fresh Font per size/label.
    public Font gameFont(double size) {
        Font font = Font.loadFont(new ByteArrayInputStream(headingFontBytes), size);
        if (font == null) {
            throw new IllegalStateException("Could not load font " + HEADING_FONT_PATH);
        }
        return font;
    }

    // Loads an image from a path relative to cwd, failing fast if it can't be read.
    public Image image(String relativePath) {
        Image image = new Image(Path.of(relativePath).toUri().toString());
        if (image.isError()) {
            throw new IllegalStateException("Could not load image " + relativePath, image.getException());
        }
        return image;
    }

    // Resolves a stylesheet path to a URI JavaFX's Scene can load.
    public String stylesheet(String relativePath) {
        return Path.of(relativePath).toUri().toString();
    }


    private static byte[] readAllBytes(String relativePath) {
        try {
            return Files.readAllBytes(Path.of(relativePath));
        } catch (IOException cause) {
            throw new UncheckedIOException("Could not read " + relativePath, cause);
        }
    }


}
