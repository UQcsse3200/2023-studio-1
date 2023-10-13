package com.csse3200.game.components.inventory;

import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;

/* Credit: LibGDX TooltipManager
* */
public class InstantTooltipManager extends com.badlogic.gdx.scenes.scene2d.ui.TooltipManager {
    static private InstantTooltipManager instance;
    static private Files files;

    /** Seconds from when an actor is hovered to when the tooltip is shown. Default is 2. Call {@link #hideAll()} after changing to
     * reset internal state. */

    /** Once a tooltip is shown, this is used instead of {@link #initialTime}. Default is 0. */
    public float subsequentTime = 0;
    /** Seconds to use {@link #subsequentTime}. Default is 1.5. */
    public float resetTime = 0f;
    /** If false, tooltips will not be shown. Default is true. */
    public boolean enabled = true;
    /** If false, tooltips will be shown without animations. Default is true. */
    public boolean animations = true;
    /** The maximum width of a {@link TextTooltip}. The label will wrap if needed. Default is Integer.MAX_VALUE. */
    public float maxWidth = Integer.MAX_VALUE;
    /** The distance from the mouse position to offset the tooltip actor. Default is 15,19. */
    public float offsetX = 15, offsetY = 19;
    /** The distance from the tooltip actor position to the edge of the screen where the actor will be shown on the other side of
     * the mouse cursor. Default is 7. */
    public float edgeDistance = 7;


    float time = initialTime;
    final Task resetTask = new Task() {
        public void run () {
            time = initialTime;
        }
    };
    public Stage stage;
    @Override
    public void enter (Tooltip tooltip) {
        super.enter(tooltip);
    }
    @Override
    protected void showAction (Tooltip tooltip) {
        float actionTime = animations ? (time > 0 ? 0.5f : 0.15f) : 0.1f;
        tooltip.getContainer().addAction(parallel(fadeIn(actionTime, fade), scaleTo(1, 1, actionTime, Interpolation.fade)));

    }
    @Override
    protected void hideAction (Tooltip tooltip) {
        tooltip.getContainer()
                .addAction(sequence(parallel(scaleTo(0.05f, 0.05f, 0, Interpolation.fade)), removeActor()));

    }

    static public InstantTooltipManager getInstance () {
        if (files == null || files != Gdx.files) {
            files = Gdx.files;
            instance = new InstantTooltipManager();
        }
        return instance;
    }
}
