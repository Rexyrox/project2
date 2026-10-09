package au.edu.unimelb.swen.oop.mokepon.data;

import java.util.List;

public record MarkerGroup(String imagePath, String colour, List<Position> positions) {

    public MarkerGroup {
        positions = List.copyOf(positions);
    }
}
