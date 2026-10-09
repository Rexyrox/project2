package au.edu.unimelb.swen.oop.mokepon;

import au.edu.unimelb.swen.oop.mokepon.data.DataLoadException;
import au.edu.unimelb.swen.oop.mokepon.data.GameData;
import au.edu.unimelb.swen.oop.mokepon.data.GameDataLoader;
import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.skill.SkillRegistry;
import au.edu.unimelb.swen.oop.mokepon.ui.Assets;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

// Application entry point: loads mokepon.game from the data= argument and opens the game window.
public class Main extends Application {
    private static final String DATA_ARGUMENT_PREFIX = "data=";

    // Loads the data file named by the data= argument and shows the game window, or prints an
    // error and terminates if the argument is missing or the file can't be loaded.
    @Override
    public void start(Stage stage) {
        Optional<String> dataArgument = dataArgument();
        if (dataArgument.isEmpty()) {
            System.out.println("Error loading " + DATA_ARGUMENT_PREFIX);
            Platform.exit();
            return;
        }

        GameData data;
        try {
            data = new GameDataLoader(new SkillRegistry()).load(Path.of(dataArgument.get()));
        } catch (DataLoadException cause) {
            System.out.println("Error loading " + dataArgument.get());
            Platform.exit();
            return;
        }
        new GameWindow(new Assets(), data).show();
    }

    // Unused; superseded by dataArgument() below.
    private Properties loadPropertiesFromArgument() throws IOException {
        String argument = getParameters().getRaw().getFirst();
        if (!argument.startsWith("data=")) {
            throw new IllegalArgumentException("Missing expected argument in the form data=relativeFilePath");
        }

        Properties properties = new Properties();
        try (var input = Files.newInputStream(Path.of(argument.substring("data=".length())))) {
            properties.load(input);
        }
        return properties;
    }

    // The value of the first "data=..." command-line argument, if present.
    private Optional<String> dataArgument() {
        return getParameters().getRaw().stream()
                .filter(argument -> argument.startsWith(DATA_ARGUMENT_PREFIX))
                .map(argument -> argument.substring(DATA_ARGUMENT_PREFIX.length()))
                .findFirst();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
