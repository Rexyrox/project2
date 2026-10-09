package au.edu.unimelb.swen.oop.mokepon.data;

import au.edu.unimelb.swen.oop.mokepon.item.ItemType;
import au.edu.unimelb.swen.oop.mokepon.skill.Skill;
import au.edu.unimelb.swen.oop.mokepon.skill.SkillRegistry;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;


// Reads mokepon.game (a Java properties file) into GameData.
public final class GameDataLoader {

    private static final String SKILL_SEPARATOR = "\\|";

    private final SkillRegistry skillRegistry;

    public GameDataLoader(SkillRegistry skillRegistry) {
        this.skillRegistry = skillRegistry;
    }

    public GameData load(Path dataFile) throws DataLoadException {
        PropertyReader reader = new PropertyReader(readProperties(dataFile));
        try {
            List<MokeponSpec> mokepon =
                    reader.indexed("mokepon.", ".name", index -> readSpec(reader, index));
            if (mokepon.isEmpty()) {
                throw new DataLoadException("No Mokepon defined in " + dataFile);
            }
            return new GameData(mokepon, typeImages(reader), itemImages(reader), readMap(reader));
        } catch (IllegalArgumentException cause) {
            // Covers enum valueOf, Terrain.fromCode, SkillRegistry.byName and Integer.parseInt
            // (NumberFormatException is a subclass), so Main only has to handle one exception.
            throw new DataLoadException("Invalid value in " + dataFile, cause);
        }
    }

    private Properties readProperties(Path dataFile) throws DataLoadException {
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(dataFile, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException | IllegalArgumentException cause) {
            throw new DataLoadException("Could not read " + dataFile, cause);
        }
        return properties;
    }

    private MokeponSpec readSpec(PropertyReader reader, int index) throws DataLoadException {
        String prefix = "mokepon." + index + ".";
        return new MokeponSpec(
                reader.text(prefix + "name"),
                MokeponType.valueOf(reader.text(prefix + "type")),
                reader.text(prefix + "image"),
                reader.number(prefix + "hp"),
                reader.number(prefix + "energy"),
                reader.number(prefix + "strength"),
                reader.number(prefix + "accuracy"),
                reader.number(prefix + "defence"),
                reader.number(prefix + "dodge"),
                skills(reader.text(prefix + "skills")));
    }

    // Splits the pipe-delimited skills string and resolves each name via the registry.
    private List<Skill> skills(String names) {
        List<Skill> skills = new ArrayList<>();
        for (String name : names.split(SKILL_SEPARATOR)) {
            skills.add(skillRegistry.byName(name.trim()));
        }
        return skills;
    }

    // One image per non-normal type; NORMAL has none (spec 3.4).
    private static Map<MokeponType, String> typeImages(PropertyReader reader)
            throws DataLoadException {
        Map<MokeponType, String> images = new EnumMap<>(MokeponType.class);
        for (MokeponType type : MokeponType.values()) {
            if (type != MokeponType.NORMAL) {
                images.put(type, reader.text("mokepon.type." + lowerCase(type)));
            }
        }
        return images;
    }

    private static Map<ItemType, String> itemImages(PropertyReader reader)
            throws DataLoadException {
        Map<ItemType, String> images = new EnumMap<>(ItemType.class);
        for (ItemType type : ItemType.values()) {
            images.put(type, reader.text("items." + type.dataKey()));
        }
        return images;
    }

    private static MapData readMap(PropertyReader reader) throws DataLoadException {
        List<List<Terrain>> tiles =
                reader.indexed("map.tiles.", "", index -> tileRow(reader.text("map.tiles." + index)));
        return new MapData(
                tiles,
                tileImages(reader),
                reader.text("map.player.image"),
                reader.position("map.player.startingPos"),
                buttonImages(reader),
                markerGroup(reader, "map.mokeponRandom"),
                markerGroup(reader, "map.mokeponStrong"),
                markerGroup(reader, "map.mokeponWeak"),
                reader.text("map.items.image"),
                reader.indexed("map.items.", ".type", index -> mysteryBox(reader, index)),
                new Exit(
                        reader.text("map.exit.image"),
                        reader.text("map.exit.colour"),
                        reader.position("map.exit.location")));
    }

    // One map.tiles.# string, one Terrain per character (column 0 first).
    private static List<Terrain> tileRow(String row) {
        List<Terrain> tiles = new ArrayList<>();
        for (char code : row.toCharArray()) {
            tiles.add(Terrain.fromCode(code));
        }
        return List.copyOf(tiles);
    }

    private static Map<Terrain, String> tileImages(PropertyReader reader)
            throws DataLoadException {
        Map<Terrain, String> images = new EnumMap<>(Terrain.class);
        for (Terrain terrain : Terrain.values()) {
            images.put(terrain, reader.text("map.tiles.images." + lowerCase(terrain)));
        }
        return images;
    }

    private static Map<Direction, String> buttonImages(PropertyReader reader)
            throws DataLoadException {
        Map<Direction, String> images = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            images.put(direction, reader.text("map.buttons." + lowerCase(direction)));
        }
        return images;
    }

    // e.g. prefix "map.mokeponRandom" reads .0, .1, ... plus .image and .colour.
    private static MarkerGroup markerGroup(PropertyReader reader, String prefix)
            throws DataLoadException {
        return new MarkerGroup(
                reader.text(prefix + ".image"),
                reader.text(prefix + ".colour"),
                reader.indexed(prefix + ".", "", index -> reader.position(prefix + "." + index)));
    }

    private static MysteryBox mysteryBox(PropertyReader reader, int index)
            throws DataLoadException {
        String prefix = "map.items." + index + ".";
        return new MysteryBox(
                ItemType.valueOf(reader.text(prefix + "type")),
                reader.position(prefix + "location"));
    }

    // Enum names as used in data-file keys, e.g. GRASS -> "grass". Locale.ROOT avoids surprises
    // on machines with non-English default locales.
    private static String lowerCase(Enum<?> constant) {
        return constant.name().toLowerCase(Locale.ROOT);
    }
}
