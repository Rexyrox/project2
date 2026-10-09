package au.edu.unimelb.swen.oop.mokepon.model;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.data.Terrain;

// A marker the player has stepped onto: the enemy it produced and the terrain they'd fight on.
public record Encounter(MapMarker marker, MokeponSpec enemy, Terrain terrain) {}

