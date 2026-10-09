package au.edu.unimelb.swen.oop.mokepon.model;

import au.edu.unimelb.swen.oop.mokepon.data.MokeponSpec;
import au.edu.unimelb.swen.oop.mokepon.skill.Randomiser;
import au.edu.unimelb.swen.oop.mokepon.skill.Skill;

import java.util.ArrayList;
import java.util.List;

// A single fight between a player and an enemy Mokepon, driving the turn sequence and log.
public class Battle {
    private final Battler player;
    private final Battler enemy;
    private final Randomiser random;
    private final BattleLog log = new BattleLog();

    private BattleState state = BattleState.IN_PROGRESS;

    public Battle(MokeponSpec playerSpec, MokeponSpec enemySpec, Randomiser random) {
        this.player = new Battler(playerSpec);
        this.enemy = new Battler(enemySpec);
        this.random = random;
        log.battleBegan(player.name(), enemy.name());

    }

    public Battler player() {
        return player;
    }

    public Battler enemy() {
        return enemy;
    }

    public BattleState state() {
        return state;
    }

    public BattleLog log() {
        return log;
    }

    public boolean isOver() {
        return state != BattleState.IN_PROGRESS;
    }

    // Runs one full round: the player's chosen skill, then (if nobody fainted) the enemy's turn.
    public void playerUses(Skill skill) {
        if (isOver() || !player.canAfford(skill)) {
            return;
        }

        player.payEnergyCost(skill.energyCost());
        log.playerUsed(player.name(), skill.name(), skill.use(player, enemy, random));

        if (enemy.hasFainted()) {
            state = BattleState.PLAYER_WON;
            log.enemyFainted(enemy.name());
            return;
        }

        enemyTurn();
    }

    // Picks and plays an affordable skill for the enemy, ending the battle if the player faints.
    private void enemyTurn() {
        Skill skill = affordableEnemySkill();
        enemy.payEnergyCost(skill.energyCost());
        log.enemyUsed(enemy.name(), skill.name(), skill.use(enemy, player, random));

        if(player.hasFainted()) {
            state = BattleState.PLAYER_LOST;
            log.playerFainted(player.name());
        }
    }

    // Chooses uniformly at random among the enemy's currently affordable skills.
    private Skill affordableEnemySkill() {
        List<Skill> affordable = new ArrayList<>();
        for (Skill skill : enemy.spec().skills()) {
            if (enemy.canAfford(skill)) {
                affordable.add(skill);
            }
        }
        // Relies on every Mokepon having a free or energy-recovering skill, so this is never empty.
        return affordable.get(random.nextInt(0, affordable.size()));
    }

}
