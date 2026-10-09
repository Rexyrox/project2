package au.edu.unimelb.swen.oop.mokepon.skill;


// Defensive skill: raises the user's own defence instead of attacking; always succeeds.
public final class Bunker implements Skill {

        private static final int MINIMUM_INCREASE = 1;
        private static final int MAXIMUM_INCREASE = 10;

        @Override
        public String name() {
            return "Bunker";
        }

        @Override
        public int energyCost() {
            return 3;
        }

        // Rolls a random defence increase (1-10) and applies it to the user.
        @Override
        public SkillOutcome use(Combatant user, Combatant target, Randomiser random) {
            int roll = random.nextInt(MINIMUM_INCREASE, MAXIMUM_INCREASE + 1);
            return new SkillOutcome.DefenceRaised(user.raiseDefence(roll));
        }

}


