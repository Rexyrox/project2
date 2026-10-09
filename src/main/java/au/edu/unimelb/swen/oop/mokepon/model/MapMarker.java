package au.edu.unimelb.swen.oop.mokepon.model;

import au.edu.unimelb.swen.oop.mokepon.data.MarkerGroup;
import au.edu.unimelb.swen.oop.mokepon.data.Position;

public record MapMarker(MarkerKind kind, MarkerGroup appearance, Position position) {}
