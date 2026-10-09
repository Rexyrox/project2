package au.edu.unimelb.swen.oop.mokepon.model;

import au.edu.unimelb.swen.oop.mokepon.item.ItemType;

public interface MoveListener {

    void encountered(Encounter encounter);

    void pickedUp(ItemType item);

    void reachedExit();
}