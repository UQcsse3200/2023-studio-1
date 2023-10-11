package com.csse3200.game.components.placeables;

import com.csse3200.game.components.Component;
import com.csse3200.game.missions.cutscenes.Cutscene;

public class DialogueComponent extends Component {

    private final static String dialogue = """
            Hey pookie, how are you?
            Hmm, not much of a talker huh?
            You look lost.
            I can fix that...
            Talk to me again and I can take you away from here.
            """;

    private boolean talked;

    @Override
    public void create() {
        entity.getEvents().addListener("interact", this::interact);
    }

    private void interact() {
        if (talked) {
            fly();
        }
        talk();
    }

    private void fly() {
        // Fly away with DJ Khaled
    }

    private void talk() {
        Cutscene cutscene = new Cutscene(dialogue, Cutscene.CutsceneType.PLACEABLE);
        cutscene.spawnCutscene();
        talked = true;
    }
}
