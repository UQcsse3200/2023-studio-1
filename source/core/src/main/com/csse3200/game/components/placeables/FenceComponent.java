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

    private static final String[] textures_gate_open= {
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_d_u_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_d_u_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_d_u_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_r_l_o.png",
            "images/placeable/fences/g_r_l_o.png",
    };

    private static final String[] textures_gate_closed= {
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_d_u.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_d_u.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_d_u.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_r_l.png",
            "images/placeable/fences/g_r_l.png",
    };


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
    
        if (isGate) {
            configGate();
            entity.getEvents().addListener("interact", this::toggleGate);
            entity.getEvents().addListener("reconfigure", this::configGate);
            return;
        }

        configFence();
        entity.getEvents().addListener("reconfigure", this::configFence);

        // Notify all adjacent of this placement
        this.connectedEntityComponent.notifyAdjacent();
    }

    /**
     * Sets this fence's texture orientation based off the adjacent fences.
     */
    public void configFence() {
        // get index into texture array based on surrounding sprinklers
        byte orientation = this.connectedEntityComponent.getAdjacentBitmap();
        // now set the texture.
        entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_fence[orientation]);
    }

    /**
     * Sets this gate's texture orientation based off the adjacent fences.
     */
    public void configGate() {
        // get index into texture array based on surrounding sprinklers
        byte orientation = this.connectedEntityComponent.getAdjacentBitmap();
        // now set the texture.
        if (isOpen) {
            entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_gate_open[orientation]);
            return;
        }
        entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_gate_closed[orientation]);
    }

    private void toggleGate() {
        isOpen = !isOpen;

        if (isOpen) {
            //this.currentTexture.setTexture(openGatePath);
            this.entity.getComponent(ColliderComponent.class).setSensor(true);
            configGate();
            return;
        }
        this.entity.getComponent(ColliderComponent.class).setSensor(false);

        // Update our texture accordingly
        configGate();
    }
}