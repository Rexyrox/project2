package au.edu.unimelb.swen.oop.mokepon.data;

import java.util.List;
import java.util.Map;

public record MapData(
        List<List<Terrain>> tiles,
        Map<Terrain, String> tileImages,
        String playerImage,
        Position playerStart,
        Map<Direction, String> buttonImages,
        MarkerGroup randomMarkers,
        MarkerGroup strongMarkers,
        MarkerGroup weakMarkers,
        String mysteryBoxImage,
        List<MysteryBox> mysteryBoxes,
        Exit exit) {

    public Terrain terrainAt(Position position) {
        return tiles.get(position.y()).get(position.x());
    }

    public boolean contains(Position position) {
        return position.y() >= 0 && position.y() < tiles.size()
                && position.x() >= 0 && position.x() < tiles.get(position.y()).size();
    }
}
