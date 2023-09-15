package com.csse3200.game.components.placeables;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.Component;

/**
 * Class for all plants in the game.
 */
public class FenceComponent extends Component {

    private boolean isGate = false;
    private boolean isOpen = false;

    private Texture texture;

    /**
     * The paths to the sound associated with the gate
     */
    private String[] sounds;

    public FenceComponent(boolean isGate, Texture texture) {
        this.texture = texture;
        this.isGate = isGate;
    }

    public FenceComponent(Texture texture) {
        this.texture = texture;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        super.create();

        // Initialise event listener for gate.
        if (isGate) {
            entity.getEvents().addListener("toggleGate", this::toggleGate);
        }

    }

    private void toggleGate() {
        isOpen = !isOpen;
        System.out.println("TOGGLE GATE");
    }

}