package au.edu.unimelb.swen.oop.mokepon.data;

import au.edu.unimelb.swen.oop.mokepon.skill.Skill;
import au.edu.unimelb.swen.oop.mokepon.skill.Stat;

import java.util.List;

// The fixed, as-loaded-from-mokepon.game definition of one kind of Mokepon (not its battle state).
public record MokeponSpec(
        String name,
        MokeponType type,
        String imagePath,
        int maxHp,
        int maxEnergy,
        int strength,
        int accuracy,
        int defence,
        int dodge,
        List<Skill> skills) {

    // Defends against external mutation of the skills list.
    public MokeponSpec {
        skills = List.copyOf(skills);
    }

    public int base(Stat stat) {
        return switch (stat) {
            case HP -> maxHp;
            case ENERGY -> maxEnergy;
            case STRENGTH -> strength;
            case ACCURACY -> accuracy;
            case DEFENCE -> defence;
            case DODGE -> dodge;
        };
    }
}
