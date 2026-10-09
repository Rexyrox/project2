package au.edu.unimelb.swen.oop.mokepon.skill;

// Source of randomness for skills, abstracted behind an interface for testability.
public interface Randomiser {
    // A value in [0.0, 1.0), rolled against hit-chance formulas.
    double nextFraction();

    // A random integer in [originInclusive, boundExclusive).
    int nextInt(int originInclusive, int boundExclusive);

}
