package com.csse3200.game.missions;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;

public abstract class Mission {

    /**
     * The name of the {@link Mission}, used to identify the {@link Mission} in-game.
     */
    private final String name;

    /**
     * Creates a {@link Mission} with the given {@link String} name.
     *
     * @param name The {@link String} name of the {@link Mission}, visible to the player in-game.
     */
    protected Mission(String name) {
        this.name = name;
    }

    /**
     * Returns the {@link String} name of this {@link Mission}.
     *
     * @return The {@link String} name of this {@link Mission}, which will be used by the player to
     *         identify the {@link Mission} in-game.
     */
    public String getName() {
        return name;
    }

    /**
     * Registers the {@link Mission} to the {@link MissionManager}, by adding all event
     * listeners to the service which the {@link Mission} needs to listen to in order to update its
     * state.
     *
     * @param missionManagerEvents A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    public abstract void registerMission(EventHandler missionManagerEvents);

    /**
     * Returns a boolean value representing whether the {@link Mission} has been completed.
     *
     * @return A boolean value representing whether the {@link Mission} has been completed.
     */
    public abstract boolean isCompleted();

    /**
     * Returns a {@link String} description of the {@link Mission}. This can involve an explanation
     * of what the player needs to do to complete the {@link Mission}, and may dynamically change
     * depending on the progress the player has made. For instance, you might choose to include in
     * your description: "You have currently harvested X out of N plants".
     *
     * @return A {@link String} description of the {@link Mission}, including any relevant details
     *         you would like the player to know about.
     */
    public abstract String getDescription();

    /**
     * Returns a short {@link String} description of the {@link Mission}. This description should be
     * at most around 50 characters. This might simply contain progress information on the
     * {@link Mission}, or a shortened form of {@link #getDescription()}.
     *
     * @return A short {@link String} description of the {@link Mission}
     */
    public abstract String getShortDescription();

    /**
     * Notifies the {@link MissionManager} that this {@link Mission} has been completed if {@link #isCompleted()}
     * returns true, else do nothing. You should call this method whenever you update the state of your {@link Mission}.
     */
    protected void notifyUpdate() {
        if (isCompleted()) {
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.MISSION_COMPLETE.name());
        }
    }

}
