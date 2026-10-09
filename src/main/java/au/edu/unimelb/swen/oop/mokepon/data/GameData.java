package au.edu.unimelb.swen.oop.mokepon.data;

import au.edu.unimelb.swen.oop.mokepon.item.ItemType;

import java.util.List;
import java.util.Map;

public record GameData(
        List<MokeponSpec> mokepon,
        Map<MokeponType, String> typeImages,
        Map<ItemType, String> itemImages,
        MapData map) {}
