package com.csse3200.game.components.npc;

import java.util.Random;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.entities.Entity;


public class TamableComponent extends Component {
    private int numTimesFed;
    private int tamingThreshold;
    private double tamingProbability;
    private String favouriteFood;
    private boolean isTamed;
    private final Entity player;

    public TamableComponent(Entity player, int tamingThreshold, double tamingProbability,
                            String favouriteFood) {
        this.numTimesFed = 0;
        this.tamingThreshold = tamingThreshold;
        this.tamingProbability = tamingProbability;
        this.favouriteFood = favouriteFood;
        this.isTamed = false;
        this.player = player;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("feed", this::feedAnimal);
    }

    private void feedAnimal() {

        if (isTamed) {
            return;
        }

        // Check player is holding the right item TODO: Implement checking the players item SEE
        //  TEAM 8!.
        if (player.getComponent(ItemComponent.class).getItemName() == favouriteFood) {

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

    private double generateRandomDecimal() {
        Random random = new Random();
        return random.nextDouble();
    }

    public boolean isTamed() {
        return isTamed;
    }

}
