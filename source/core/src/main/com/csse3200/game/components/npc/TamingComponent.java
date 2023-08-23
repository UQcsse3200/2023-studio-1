package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

/**
 * This class represents an animals' ability to be tamed by the user.
 * TODO: Discuss ideas on how animals can be fed. Also how will the animls get this taming component.
 */

public class TamingComponent extends Component {
    /**
     * These thresholds represent the amount of points it takes
     * to tame the animals. Each animal will have a different threshold.
     */
    private int tamability; //this will indicate the tame threshold, which is how many points needed to tame animal
    private int tamestatus; //current status in taming the animal

    /*
    The 3 integers below represent the tame levels. These tame levels will decide the threshold.
     */
    private final int tamelevel1 = 1;
    private final int tamelevel2 = 2;
    private final int tamelevel3 = 3;

    /*

     */
    private final int tameThresholdLow = 50;
    private final int tameThresholdMed = 100;
    private final int tameThersholdHigh = 125;

    private Entity entity;

    public TamingComponent(int tamelevel) {
        setTamelevel((tamelevel));
        this.tamestatus = 0;
    }

    public void setTamelevel(int tameResistance ) {
        switch(tameResistance) {
            case(tamelevel1):
                this.tamability = tameThresholdLow;
                break;
            case(tamelevel2):
                this.tamability = tameThresholdMed;
                break;
            case(tamelevel3):
                this.tamability = tameThersholdHigh;
                break;
        }

    }

    public int getTamelevel() {
        return this.tamability;
    }

    public int getTameStatus() {
         return this.tamestatus;
    }



}
