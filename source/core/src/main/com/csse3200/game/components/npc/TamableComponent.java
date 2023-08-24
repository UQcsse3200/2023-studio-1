package com.csse3200.game.components.npc;

import java.util.Random;

import com.csse3200.game.components.Component;


public class TameableComponent extends Component {
    private int numTimesFed;
    private int tamingThreshold;
    private double tamingProbability;
    private String favouriteFood;
    private boolean isTamed;

    public TameableComponent(int tamingThreshold, double tamingProbability, String favouriteFood) {
        this.numTimesFed = 0;
        this.tamingThreshold = tamingThreshold;
        this.tamingProbability = tamingProbability;
        this.favouriteFood = favouriteFood;
        this.isTamed = false;
    }

    public void feedAnimal() {

        // Check player is holding the right item


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
        }
        else {
            numTimesFed++;
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
