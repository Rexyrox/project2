package au.edu.unimelb.swen.oop.mokepon.model;

import au.edu.unimelb.swen.oop.mokepon.data.Direction;
import au.edu.unimelb.swen.oop.mokepon.data.GameData;
import au.edu.unimelb.swen.oop.mokepon.data.MapData;
import au.edu.unimelb.swen.oop.mokepon.data.MarkerGroup;
import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.data.MysteryBox;
import au.edu.unimelb.swen.oop.mokepon.data.Position;
import au.edu.unimelb.swen.oop.mokepon.data.Terrain;
import au.edu.unimelb.swen.oop.mokepon.item.Inventory;
import au.edu.unimelb.swen.oop.mokepon.skill.Randomiser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameSession {

    private final MokeponSpec playerMokepon;
    private final MapData map;
    private final List<MokeponSpec> allMokepon;
    private final Randomiser random;
    private final Inventory inventory = new Inventory();
    private final List<MapMarker> markers = new ArrayList<>();
    private final List<MysteryBox> mysteryBoxes;

    private Position position;
    private Position previousPosition;
    private int fightsWon;

    public GameSession(MokeponSpec playerMokepon, GameData data, Randomiser random) {
        this.playerMokepon = playerMokepon;
        this.map = data.map();
        this.allMokepon = data.mokepon();
        this.random = random;
        this.position = map.playerStart();
        this.previousPosition = position;
        this.mysteryBoxes = new ArrayList<>(map.mysteryBoxes());
        addMarkers(MarkerKind.RANDOM, map.randomMarkers());
        addMarkers(MarkerKind.STRONG, map.strongMarkers());
        addMarkers(MarkerKind.WEAK, map.weakMarkers());
    }

    public MokeponSpec playerMokepon() {
        return playerMokepon;
    }

    public Inventory inventory() {
        return inventory;
    }

    public Position position() {
        return position;
    }

    public int fightsWon() {
        return fightsWon;
    }

    public List<MapMarker> markers() {
        return Collections.unmodifiableList(markers);
    }

    public List<MysteryBox> mysteryBoxes() {
        return Collections.unmodifiableList(mysteryBoxes);
    }

    public boolean canMove(Direction direction) {
        Position next = position.moved(direction);
        return map.contains(next) && map.terrainAt(next).isWalkable();
    }

    public void move(Direction direction, MoveListener listener) {
        previousPosition = position;
        position = position.moved(direction);

        Optional<MapMarker> marker = markerAt(position);
        if (marker.isPresent()) {
            listener.encountered(encounterWith(marker.get()));
            return;
        }
        Optional<MysteryBox> box = mysteryBoxAt(position);
        if (box.isPresent()) {
            mysteryBoxes.remove(box.get());
            inventory.add(box.get().type());
            listener.pickedUp(box.get().type());
            return;
        }
        if (position.equals(map.exit().position())) {
            listener.reachedExit();
        }
    }

    public void retreat() {
        position = previousPosition;
    }

    public void defeated(MapMarker marker) {
        markers.remove(marker);
        fightsWon++;
    }

    private Optional<MapMarker> markerAt(Position target) {
        return markers.stream().filter(marker -> marker.position().equals(target)).findFirst();
    }

    private Optional<MysteryBox> mysteryBoxAt(Position target) {
        return mysteryBoxes.stream().filter(box -> box.position().equals(target)).findFirst();
    }

    private Encounter encounterWith(MapMarker marker) {
        Terrain terrain = map.terrainAt(marker.position());
        MokeponSpec enemy = marker.kind().chooseEnemy(allMokepon, terrain, random);
        return new Encounter(marker, enemy, terrain);
    }

    private void addMarkers(MarkerKind kind, MarkerGroup group) {
        for (Position markerPosition : group.positions()) {
            markers.add(new MapMarker(kind, group, markerPosition));
        }
    }
}