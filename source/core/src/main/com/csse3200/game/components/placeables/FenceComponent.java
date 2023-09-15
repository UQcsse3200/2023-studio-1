package com.csse3200.game.components.placeables;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;

public class FenceComponent extends Component {

    private boolean isGate = false;
    private boolean isOpen = false;

    /* Current texture of the entity */
    private DynamicTextureRenderComponent currentTexture;

    /**
     * The paths to the sound associated with the gate
     */
    private String[] sounds;

    private String closedGatePath = "images/plants/misc/tobacco_seed.png";
    private String openGatePath = "images/egg.png";
    private String fencePath = "";

    public FenceComponent(boolean isGate) {
        this.isGate = isGate;
    }

    public FenceComponent() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        super.create();

        this.currentTexture = entity.getComponent(DynamicTextureRenderComponent.class);

        // Initialise event listener for gate.
        if (isGate) {
            entity.getEvents().addListener("toggleGate", this::toggleGate);
            this.currentTexture.setTexture(closedGatePath);
        } else {
            this.currentTexture.setTexture(fencePath);
        }

    }

    private void toggleGate() {
        isOpen = !isOpen;

        if (isOpen) {
            this.currentTexture.setTexture(openGatePath);
            return;
        }

        this.currentTexture.setTexture(closedGatePath);
    }

}