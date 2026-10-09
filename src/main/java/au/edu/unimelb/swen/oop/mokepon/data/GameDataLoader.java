package au.edu.unimelb.swen.oop.mokepon.data;

import au.edu.unimelb.swen.oop.mokepon.skill.Skill;
import au.edu.unimelb.swen.oop.mokepon.skill.SkillRegistry;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


// Reads mokepon.game (a Java properties file) into a list of MokeponSpec.
public final class GameDataLoader {

    private static final String KEY_PREFIX = "mokepon.";
    private static final String SKILL_SEPARATOR = "\\|";

    private final SkillRegistry skillRegistry;

    public GameDataLoader(SkillRegistry skillRegistry) {
        this.skillRegistry = skillRegistry;
    }

    // Loads every Mokepon in dataFile; throws if the file can't be read or none are defined.
    public List<MokeponSpec> load(Path dataFile) throws DataLoadException {
        Properties properties = readProperties(dataFile);
        List<MokeponSpec> specs = new ArrayList<>();
        // Mokepon are numbered from 0 with no gaps, so the first missing index marks the end.
        for (int index = 0; properties.containsKey(key(index, "name")); index++) {
            specs.add(readSpec(properties, index));
        }
        if (specs.isEmpty()) {
            throw new DataLoadException("No Mokepon defined in " + dataFile);
        }
        return List.copyOf(specs);
    }

    // Loads dataFile as UTF-8 Java properties.
    private Properties readProperties(Path dataFile) throws DataLoadException {
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(dataFile, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException | IllegalArgumentException cause) {
            throw new DataLoadException("Could not read " + dataFile, cause);
        }
        return properties;
    }

    // Reads every field for one Mokepon index into a MokeponSpec.
    private MokeponSpec readSpec(Properties properties, int index) throws DataLoadException {
        return new MokeponSpec(
                text(properties, index, "name"),
                text(properties, index, "image"),
                number(properties, index, "hp"),
                number(properties, index, "energy"),
                number(properties, index, "strength"),
                number(properties, index, "accuracy"),
                number(properties, index, "defence"),
                number(properties, index, "dodge"),
                skills(properties, index));
    }

    // Splits the pipe-delimited skills string and resolves each name via the registry.
    private List<Skill> skills(Properties properties, int index) throws DataLoadException {
        String[] names = text(properties, index, "skills").split(SKILL_SEPARATOR);
        List<Skill> skills = new ArrayList<>(names.length);
        for (String name : names) {
            skills.add(skillRegistry.byName(name.trim()));
        }
        return skills;
    }

    // Reads and trims a required string field, failing if the key is absent.
    private String text(Properties properties, int index, String attribute) throws DataLoadException {
        String value = properties.getProperty(key(index, attribute));
        if (value == null) {
            throw new DataLoadException("Missing key " + key(index, attribute));
        }
        return value.trim();
    }

    // Reads a required whole-number field, failing if it doesn't parse.
    private int number(Properties properties, int index, String attribute) throws DataLoadException {
        try {
            return Integer.parseInt(text(properties, index, attribute));
        } catch (NumberFormatException cause) {
            throw new DataLoadException(
                    "Value of " + key(index, attribute) + " is not a whole number", cause);
        }
    }

    // Builds a "mokepon.<index>.<attribute>" property key.
    private static String key(int index, String attribute) {
        return KEY_PREFIX + index + "." + attribute;
    }

}
