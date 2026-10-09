package au.edu.unimelb.swen.oop.mokepon.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

final class PropertyReader {


    @FunctionalInterface
    interface IndexedReader<T> {
        T read(int index) throws DataLoadException;
    }

    private final Properties properties;

    PropertyReader(Properties properties) {
        this.properties = properties;
    }

    String text(String key) throws DataLoadException {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new DataLoadException("Missing key " + key);
        }
        return value.trim();
    }

    int number(String key) throws DataLoadException {
        return Integer.parseInt(text(key));
    }

    Position position(String key) throws DataLoadException {
        return Position.parse(text(key));
    }

    <T> List<T> indexed(String prefix, String suffix, IndexedReader<T> reader)
            throws DataLoadException {
        List<T> entries = new ArrayList<>();
        for (int index = 0; properties.containsKey(prefix + index + suffix); index++) {
            entries.add(reader.read(index));
        }
        return List.copyOf(entries);
    }
}