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
        String dialogue1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        cutscene = new Cutscene(dialogue1, Cutscene.CutsceneType.RADIO);
        cutscene.spawnCutscene();
        return true;
    }

    boolean isValid(ArrayList<String> args) {
        return args.isEmpty();  // Using isEmpty() to check if the list is empty
    }}
