package com.csse3200.game.components.placeables;

import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;

public class FenceComponent extends Component {

    private ConnectedEntityUtility connectedEntityUtility;

    /**
     * Texture paths for different fence orientation.
     * The order of this array is very important, correct order ensures a fence gets the correct texture.
     */
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

    private static final String IMAGE_GATE_OPEN = "images/placeable/fences/g_r_l_o.png";
    private static final String IMAGE_GATE_DUPLICATE = "images/placeable/fences/g_d_u_o.png";

    private static final String[] textures_gate_open = {
            IMAGE_GATE_OPEN,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_DUPLICATE,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_DUPLICATE,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_DUPLICATE,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_OPEN,
            IMAGE_GATE_OPEN,
    };

    private static final String GATE_RIGHT_LEFT_CLOSED = "images/placeable/fences/g_r_l.png";
    private static final String GATE_DOWN_UP_CLOSED = "images/placeable/fences/g_d_u.png";

    private static final String[] textures_gate_closed = {
            GATE_RIGHT_LEFT_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_DOWN_UP_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_DOWN_UP_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_DOWN_UP_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
            GATE_RIGHT_LEFT_CLOSED,
    };


    private boolean isGate = false;
    private boolean isOpen = false;

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
        this.connectedEntityUtility = new ConnectedEntityUtility(entity);
        entity.getEvents().addListener("onDestroy", this::onDestroy);

        if (isGate) {
            configGate();
            entity.getEvents().addListener("interact", this::toggleGate);
            entity.getEvents().addListener("reconfigure", this::configGate);
            this.connectedEntityUtility.notifyAdjacent();
            return;
        }

        configFence();
        entity.getEvents().addListener("reconfigure", this::configFence);

        // Notify all adjacent of this placement
        this.connectedEntityUtility.notifyAdjacent();
    }

    /**
     * Sets this fence's texture orientation based off the adjacent fences.
     */
    public void configFence() {
        // get index into texture array based on surrounding sprinklers
        byte orientation = this.connectedEntityUtility.getAdjacentBitmap();
        // now set the texture.
        entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_fence[orientation]);
    }

    /**
     * Sets this gate's texture orientation based off the adjacent fences.
     */
    public void configGate() {
        // get index into texture array based on surrounding sprinklers
        byte orientation = this.connectedEntityUtility.getAdjacentBitmap();
        // now set the texture.
        if (isOpen) {
            entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_gate_open[orientation]);
            return;
        }
        entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_gate_closed[orientation]);
    }

    private void toggleGate() {
        
        /* Play the interact sound */
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.GATE_INTERACT);
        } catch (Exception e) {
            /* Catch exception when sound service not available */
        }

        isOpen = !isOpen;

        if (isOpen) {
            this.entity.getComponent(ColliderComponent.class).setSensor(true);
            configGate();
            return;
        }
        this.entity.getComponent(ColliderComponent.class).setSensor(false);
        // Update our texture accordingly
        configGate();
    }

    /**
     * Destroys the connections to neighbouring fences or gates
     */
    private void onDestroy() {
        entity.getEvents().trigger("destroyConnections");
    }
}