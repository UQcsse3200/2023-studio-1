package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.missions.cutscenes.Cutscene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class DialogueScreenCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(DialogueScreenCommand.class);

    @Override
    public boolean action(ArrayList<String> args) {
        if (!isValid(args)) {
            logger.debug("Invalid arguments received for 'SpawnDialogue' command: {}", args);
            return false;
        }
        Cutscene cutscene;
        cutscene = new Cutscene();
        cutscene.spawnCutscene();
        return true;
    };

    boolean isValid(ArrayList<String> args) {
        return args.size() == 0;
    }
}
