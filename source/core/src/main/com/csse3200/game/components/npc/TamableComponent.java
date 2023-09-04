package com.csse3200.game.components.npc;

import java.util.Random;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.entities.Entity;

/**
 * This the class for Tameable Component. These components should
 * only apply to animals. Tameable stats can be found in the NPCs.json file
 */
public class TamableComponent extends Component {
    private int numTimesFed; //tracker for how many times animal has been fed
    private int tamingThreshold; //tame threshold of the animal (found in NPCs.json)
    private double tamingProbability; //probability of taming the animal (found in NPC.json)
    private String favouriteFood;  //animals favourite food (found in NPC.json)
    private boolean isTamed;
    private final Entity player;
    private Random random = new Random();  //https://rules.sonarsource.com/java/RSPEC-2119/

    /**
     * Constructor for the Tameable Component class
     * @param player The main player/user entity
     * @param tamingThreshold An integer that number of times the animal can be fed to tame it.
     * @param tamingProbability A double that represents chances to tame the animal.
     * @param favouriteFood The animals favourite food.
     */
    public TamableComponent(Entity player, int tamingThreshold, double tamingProbability,
                            String favouriteFood) {
        this.numTimesFed = 0;
        this.tamingThreshold = tamingThreshold;
        this.tamingProbability = tamingProbability;
        this.favouriteFood = favouriteFood;
        this.isTamed = false;
        this.player = player;
    }

    /**
     * Create the event and add it to the event hashmap so the function can be called
     * and the player can observe the effects.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("feed", this::feedAnimal);
    }

    /**
     * Function to be called from the event handler.
     * This is the player's attempt to feed and tame the animal.
     * How the taming process works:
     * - The player can only feed the animal, its favourite food.
     * - When fed, a random double decimal will be generated. If this
     *   decimal exceeds the animal's tame probability, the animal will then be tamed.
     *  - If not the player can continue feeding the animal.
     *  - When number of times fed exceeds the tame threshold, the animal will then automatically
     *    be tamed.
     *
     * Note:
     * This has dependencies on the players team and Items team as player must interact with animal
     * and item must be used to feed the animal.
     */
    private void feedAnimal() {

        if (isTamed) {
            return;
        }

        // Check player is holding the right item TODO: Implement checking the players item SEE
        //  TEAM 8!.
        if (player.getComponent(ItemComponent.class).getItemName().equals(favouriteFood)) {

            // Generate RNG number for taming
            double randomDecimal = generateRandomDecimal();

            // Try and tame the animal
            // Check how many times the player has tried to tame the animal
            // If player has already tried enough times, tame the animal (prevents frustration).
            if (numTimesFed > tamingThreshold) {
                isTamed = true;
            }
            // Use RNG to try and tame the animal
            else if (randomDecimal < tamingProbability) {
                isTamed = true;
            } else {
                numTimesFed++;
            }

            // Remove the food from the players inventory TODO: WAIT FOR TEAM 8
            // player.getComponent(ItemComponent.class).dispose();

        }
    }

    /**
     * Function is used to generate a random double decimal when the animal has been fed.
     * @return random double
     */
    private double generateRandomDecimal() { 
        return this.random.nextDouble(); //https://rules.sonarsource.com/java/RSPEC-2119/
    }

    /**
     * Function is used to check to see if the animal has been tamed.
     * @return True if the animal has been tamed, else false.
     */
    public boolean isTamed() {
        return isTamed;
    }
}
