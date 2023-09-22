package com.csse3200.game.components.ship;

import com.csse3200.game.components.Component;

import static java.lang.Math.min;

public class ShipProgressComponent extends Component {
    static int maximum_repair = 20;
    private int progress;

    /**
     * This component will handle the Ship's internal state of repair. It will listen for addPart events on the Ship
     * Entity, which will pass the number of Ship Part Items the player is using on the ship, and increment its
     * internal state of repair by that amount. It will also trigger a progressUpdated event on the Ship Entity along
     * with the current state of repair for other components to use.
     */
    @Override
    public void create() {
        this.progress = 0;
        // listen to add artefact call
        entity.getEvents().addListener("addArtefact", this::incrementProgress);
    }

    /**
     * Update the progress of the ship repair by incrementing it by however many artefacts the player 'used' on the
     * ship. This will also call a progressUpdate event on the Ship entity it is attached to.
     */
    private void incrementProgress(int amount) {
        if (this.progress < maximum_repair) {
            // Bound maximum repair state
            this.progress = min(this.progress + amount, maximum_repair);
            // TODO: Only send progress update if repair actually happened
        }
    }

    /**
     * Update the progress of the ship's repair by decrementing it - this would be used by the bonus shipeater entity.
     */
    private void decrementProgress(int amount) {
        // Bound progress to being no less than 0
        this.progress -= Math.max(progress, 0);
    }
}
