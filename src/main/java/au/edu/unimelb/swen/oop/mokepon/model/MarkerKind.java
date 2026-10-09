package au.edu.unimelb.swen.oop.mokepon.model;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.data.Terrain;
import au.edu.unimelb.swen.oop.mokepon.data.TerrainEffect;
import au.edu.unimelb.swen.oop.mokepon.skill.Randomiser;
import java.util.List;


public enum MarkerKind {
    RANDOM {
        @Override
        protected boolean suits(MokeponSpec spec, Terrain terrain) {
            return true;
        }
    },
    STRONG {
        @Override
        protected boolean suits(MokeponSpec spec, Terrain terrain) {
            return spec.type().effectOn(terrain) == TerrainEffect.ADVANTAGE;
        }
    },
    WEAK {
        @Override
        protected boolean suits(MokeponSpec spec, Terrain terrain) {
            return spec.type().effectOn(terrain) == TerrainEffect.DISADVANTAGE;
        }
    };

    protected abstract boolean suits(MokeponSpec spec, Terrain terrain);

    public MokeponSpec chooseEnemy(List<MokeponSpec> all, Terrain terrain, Randomiser random) {
        List<MokeponSpec> candidates = all.stream().filter(spec -> suits(spec, terrain)).toList();
        List<MokeponSpec> pool = candidates.isEmpty() ? all : candidates;
        return pool.get(random.nextInt(0, pool.size()));
    }
}