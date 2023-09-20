package com.csse3200.game.components.placeables;

import java.util.HashMap;

import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;

public class FenceComponent extends Component {

    private ConnectedEntityComponent connectedEntityComponent;
    /**
     * Texture paths for different fence orientation.
     * The order of this array is very important, correct order ensures a fence gets the correct texture.
     */ // TODO add texture desc.
    private static final String[] textures_fence= {
            "images/placeable/fences/f.png",
            "images/placeable/fences/f_l.png",
            "images/placeable/fences/f_r.png",
            "images/placeable/fences/f_r_l.png",
            "images/placeable/fences/f_d.png",
            "images/placeable/fences/f_d_l.png",
            "images/placeable/fences/f_r_d.png",
            "images/placeable/fences/f_r_d_l.png",
            "images/placeable/fences/f_u.png",
            "images/placeable/fences/f_l_u.png",
            "images/placeable/fences/f_r_u.png",
            "images/placeable/fences/f_r_l_u.png",
            "images/placeable/fences/f_d_u.png",
            "images/placeable/fences/f_d_l_u.png",
            "images/placeable/fences/f_r_d_u.png",
            "images/placeable/fences/f_r_d_l_u.png",
    };

    private static final String[] textures_gate= {
            "images/placeable/fences/gate.png",
            "images/placeable/fences/gate_open.png"
    };

    private String texturePath;
    private String closedGatePath = textures_gate[0];
    private String openGatePath = textures_gate[1];
    private String fenceBase = "";

    private boolean isGate = false;
    private boolean isOpen = false;

    /**
     * The paths to the sound associated with the gate
     */
    private String[] sounds;

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
        this.connectedEntityComponent = new ConnectedEntityComponent(entity);
        // leaving the gate out of the texture configuring for now.
        if (!this.isGate) {
            configFence();
            entity.getEvents().addListener("reconfigure", this::configFence);
        }
        // Initialise event listener for gate.
        if (isGate) {
            entity.getEvents().addListener("interact", this::toggleGate);
            //texturePath = closedGatePath;
        }

        /* Check if we placed next to another gate and update accordingly */
        //this.updateTexture();

    }

    /**
     * Sets this fence's texture orientation based off the adjacent fences.
     * TODO add gates.
     */
    public void configFence() {
        // get index into texture array based on surrounding sprinklers
        byte orientation = this.connectedEntityComponent.getAdjacentBitmap();
        // now set the texture.
        entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_fence[orientation]);
    }

    private void toggleGate() {
        isOpen = !isOpen;

        if (isOpen) {
            //this.currentTexture.setTexture(openGatePath);
            entity.getComponent(DynamicTextureRenderComponent.class).setTexture(openGatePath);
            this.entity.getComponent(ColliderComponent.class).setSensor(true);
            return;
        }
        this.entity.getComponent(ColliderComponent.class).setSensor(false);
        entity.getComponent(DynamicTextureRenderComponent.class).setTexture(closedGatePath);
        //this.currentTexture.setTexture(closedGatePath);
    }

    private void updateTexturePath() {

    }

    /*
    public void updateTexture() {
        this.currentTexture.setTexture(this.texturePath);
    }
     */

}