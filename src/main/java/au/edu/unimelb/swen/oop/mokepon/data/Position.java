package au.edu.unimelb.swen.oop.mokepon.data;

public record Position(int x, int y) {

    // Parses the data file's "x,y" format, e.g. "10,18".
    public static Position parse(String text) {
        String[] parts = text.split(",");
        return new Position(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
    }

    public Position moved(Direction direction) {
        return new Position(x + direction.dx(), y + direction.dy());
    }
}
