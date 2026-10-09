package au.edu.unimelb.swen.oop.mokepon.skill;

import java.util.Random;

// Randomiser backed by java.util.Random, used for real gameplay (not a fixed test source).
public class DefaultRandomiserSource implements Randomiser {
    private final Random random = new Random();

    @Override
    public double nextFraction() {
        return random.nextDouble();
    }

    @Override
    public int nextInt(int originInclusive, int boundExclusive) {
        return random.nextInt(originInclusive, boundExclusive);
    }

}
