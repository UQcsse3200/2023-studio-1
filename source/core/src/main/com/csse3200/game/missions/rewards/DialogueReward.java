package com.csse3200.game.missions.rewards;

/**
 * DialogueReward is a Reward that on collect() will call the correct dialogue display with its
 * given dialogue.
 */
public class DialogueReward extends Reward {

    /**
     * The {@link String} dialogue to be displayed
     */
    private String dialogue;

    /**
     * The type of dialogue screen the dialogue will be shown on.
     */
    // private DialogueScreenType type; - based on team 3 screen implementation

    public DialogueReward(String dialogue/*, DialogueScreenType type*/) {
        super();
        this.dialogue = dialogue;
        // this.type = type;
    }

    /**
     * This method will display the dialogue on the correct dialogue screen
     */
    @Override
    public void collect() {
        setCollected();
        // trigger correct screen (based on team 3 dialogue screen)
    }
}
