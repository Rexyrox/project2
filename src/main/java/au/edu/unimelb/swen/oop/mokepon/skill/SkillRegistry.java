package au.edu.unimelb.swen.oop.mokepon.skill;

import java.util.LinkedHashMap;
import java.util.Map;

// All skills the game knows about, looked up by the case-sensitive name used in mokepon.game.
public final class SkillRegistry {

    private final Map<String, Skill> skillsByName = new LinkedHashMap<>();

    public SkillRegistry() {
        register(new Bite());
        register(new Tackle());
        register(new Bunker());
        register(new Wait());
    }

    // Looks up a skill by its exact name; throws if mokepon.game references one that doesn't exist.
    public Skill byName(String name) {
        Skill skill = skillsByName.get(name);
        if (skill == null) {
            throw new IllegalArgumentException("Unknown skill " + name);
        }
        return skill;
    }

    private void register(Skill skill) {
        skillsByName.put(skill.name(), skill);
    }

}

