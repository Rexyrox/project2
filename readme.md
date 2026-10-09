# SWEN20003 Semester 2, 2026
# Project 2
# Mokepon

## Assumptions

Encounters and markers

- A new enemy is picked each time the player steps onto a marker. Running away and stepping back on can give a different Mokepon.
- If no Mokepon in the data file has an advantage on a strong marker's terrain, any Mokepon is used. The same applies to weak markers and disadvantage.
- The enemy can be the same kind of Mokepon as the player's.
- Both Mokepon's terrain effects are worked out from the tile the marker is on.
- After a win, the player stays on the tile where the defeated marker was.

Overworld movement

- When a tile has more than one thing on it, a marker takes priority. A mystery box on the exit tile is picked up, and then the exit still triggers. A marker on the exit tile starts an encounter, and the exit doesn't trigger.
- Nothing triggers on the starting tile until the player moves.
- While an encounter or exit popup is showing, the player can't move.
- After the exit popup appears, the player can only return to the main menu or quit.
- The stats scroll shows the Mokepon's base stats, not terrain-adjusted or in-battle values.
- The map, inventory, fight count and position carry over between battles in the same game. Returning to the main menu starts a fresh game.

Battle

- Terrain-adjusted stats round .5 upwards. Strength, accuracy, defence and dodge are capped at 100, and HP and energy have no upper limit.
- Aim, Swole and Nimble log the points actually gained. At the cap this can be "They raised their accuracy by 0 points!".
- Each bleeding tick logs "{name} took {int} bleeding damage!", indented like other outcome lines. The spec only gives the "The bleeding stopped." line.
- Bleed damage uses the attacker's strength at the time of each tick, so a Swole boost after the Lance hit increases it.
- Stun halves all damage the stunned Mokepon deals, including bleed damage it caused.
- Stun and Pocket Sand last 3 of the affected Mokepon's own turns, whichever side applied them.
- Bleeding ticks at the end of the round it was applied and the round after. It's counted in rounds, not the bleeding Mokepon's turns.
- Using an effect on a Mokepon that already has it restarts the effect rather than stacking it.
- Using an item doesn't count as a turn. Effects don't tick down and the enemy doesn't act.
- An item's log message is preceded by a blank line, the same as a player skill.
- All three item buttons always appear on the Battle Screen with their quantity, and are disabled at 0, rather than only showing items that have been collected.
- The enemy picks uniformly at random from the skills it can currently afford. Every Mokepon is assumed to have Wait (or another skill it can always afford), so this list is never empty.
- Once a battle has a result, "Run away" (CTRL+R) is disabled so it can't undo the win or loss. "Main menu" stays available.

Carried over from Project 1

- Two blank lines come before the fainted-Mokepon and result messages in the battle log. The spec's {two} token was treated as a formatting mistake.

## AI Statement
Claude was used throughout development of this project, including:

* Discussing and suggesting the class design/architecture
* Debugging compile errors and fixing functional bugs found during manual testing and code review
* Writing code comments explaining non-obvious behaviour.
* Reviewing the implementation against the assignment's marking rubric.
* Cleaned dead code and style
* Running tests

All AI-suggested code and design decisions were reviewed and understood before being kept; no code
was accepted purely because a tool produced it.


## Code References

* None