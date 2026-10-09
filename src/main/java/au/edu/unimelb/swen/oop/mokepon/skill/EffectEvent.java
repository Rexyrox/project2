package au.edu.unimelb.swen.oop.mokepon.skill;

public sealed interface EffectEvent {

    record BleedDamage(String targetName, int damage) implements EffectEvent {}

    record BleedingStopped() implements EffectEvent {}
}
