package com.csse3200.game.missions.rewards;

import com.csse3200.game.missions.cutscenes.Cutscene;

/**
 * DialogueReward is a Reward that on collect() will call the correct dialogue display with its
 * given dialogue.
 */
public class DialogueReward extends Reward {

    /**
     * The {@link Cutscene} for dialogue to be displayed
     */
    private Cutscene cutscene;

    public DialogueReward(String dialogue, Cutscene.CutsceneType type) {
        super();
        this.cutscene = new Cutscene(dialogue, type);
    }

    /**
     * New {@link DialogueReward} object
     * @param cutscene For collect method
     */
    public DialogueReward(Cutscene cutscene) {
        this.cutscene = cutscene;
    }

    /**
     * This method will display the dialogue on the correct dialogue screen
     */
    @Override
    public void collect() {
        setCollected();
        cutscene.spawnCutscene();
    }

    /**
     * Getting the cutscene.
     */
    protected Cutscene getCutscene() {
        return this.cutscene;
    }
}
