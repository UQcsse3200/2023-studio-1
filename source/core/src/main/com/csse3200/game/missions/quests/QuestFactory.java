package com.csse3200.game.missions.quests;

import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.missions.rewards.DialogueReward;
import com.csse3200.game.missions.rewards.ItemReward;
import com.csse3200.game.missions.rewards.MultiReward;
import com.csse3200.game.missions.rewards.QuestReward;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestFactory {

    public static final String actIMainQuestName = "An Agreement";
    public static final String firstContactQuestName = "First Contact";
    public static final String clearingYourMessQuestName = "Clearing Your Mess";
    public static final String actIIMainQuestName = "Making Contact";
    public static final String actIIIMainQuestName = "Weather the Storm";

    public static AutoQuest createFirstContactQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createClearingYourMessQuest());
        questsToActivate.add(createActIMainQuest());

        String dialogue = """
                WHAT HAVE YOU DONE!!!
                {WAIT}
                CLEAR UP THIS MESS AT ONCE, OR {COLOUR=red}THERE WILL BE CONSEQUENCES{COLOUR=white}!!!
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createShovel())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new AutoQuest(firstContactQuestName, reward, "Wake up after your crash landing.");
    }

    public static ClearDebrisQuest createClearingYourMessQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();

        String dialogue = """
                Good. But your {WAIT} "landing" {WAIT} has completely destroyed my crops!
                {WAIT}
                Take this hoe and these seeds and start replanting the crops you destroyed.
                {WAIT}
                You have 4 hours to plant 12 Cosmic Corn.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createHoe(), ItemFactory.createCosmicCobSeed())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new ClearDebrisQuest(clearingYourMessQuestName, 15, reward, 2, true);
    }

    public static MainQuest createActIMainQuest() {
        List<Quest> questsToAdd = new ArrayList<>();

        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createActIIMainQuest());

        String dialogue = """
                Well done, human. You have shown me that you can be trusted, and that our cooperation may be mutually beneficial.
                {WAIT}
                Now, {WAIT=0.5} let's see if we can do something about that Ship of yours.
                I understand you likely did not intend to crash here - my guess is the recent {COLOUR=red}Solar Surge{COLOUR=white} is to blame?
                {WAIT}
                You begin to explain what happened, how you lost contact with the Mothership, how your controls failed and how you fell into an uncontrolled descent onto the planet's surface.
                {WAIT}
                I see... {WAIT=1} The storm has also devastated our planet, and is part of the reason I was sent here.
                {WAIT}
                As you continue to go over the details, a ravenous anxiety begins to claw at your chest as the possibility of you becoming stranded forever becomes a terrifying possibility.
                {WAIT}
                ... TO BE ADDED
                """;

        Set<String> requiredQuests = new HashSet<>();
        requiredQuests.add(clearingYourMessQuestName);

        MultiReward reward = new MultiReward(List.of(
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));

        return new MainQuest(actIMainQuestName, requiredQuests, reward, 5, "gain ALIEN NPC's trust");
    }

    public static MainQuest createActIIMainQuest() {
        List<Quest> questsToAdd = new ArrayList<>();

        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createActIIIMainQuest());

        String dialogue = """
                With the final pieces of the ship in place, a faint static buzzes from the radio.
                {WAIT}
                You tune the radio into the Mothership's frequency, and send your SOS message, in hopes it will be heard...
                {WAIT=2}
                "Hello, is that you PLAYER NAME?"
                {WAIT}
                You eagerly reply...
                {WAIT=0.5}
                "Hello? {WAIT} We're struggling to hear what you are saying."
                {WAIT}
                "Listen, if you can hear us, we're in major trouble. Our sensors have been picking up a major increase in solar activity."
                {WAIT}
                "We predict in 15 days there will be a Solar Storm so intense that it could wipe out our entire fleet."
                {WAIT}
                "Our only chance is to land on your planet, but even then our life-support systems will likely fail."
                {WAIT}
                "We need you to do all you can to ensure the atmosphere is at least survivable for us when we land."
                {WAIT}
                Taken aback by all this information, you attempt to speak to them again, but just as you go to speak, the sky lights up with the power of the sun, and the radio cuts out...
                """;

        Set<String> requiredQuests = new HashSet<>();

        MultiReward reward = new MultiReward(List.of(
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));

        return new MainQuest(actIIMainQuestName, requiredQuests, reward, 10, "make connection with the Mothership");
    }

    public static MainQuest createActIIIMainQuest() {
        Set<String> requiredQuests = new HashSet<>();
        MultiReward reward = new MultiReward(List.of());
        return new MainQuest(actIIIMainQuestName, requiredQuests, reward, 15, "weather the storm");
    }

    public static FertiliseCropTilesQuest createHaberHobbyist() {
        // To be decided
        ItemReward reward = new ItemReward(new ArrayList<>());
        return new FertiliseCropTilesQuest("Haber Hobbyist", reward, 24, 10);
    }

    public static FertiliseCropTilesQuest createFertiliserFanatic() {
        // To be decided
        ItemReward reward = new ItemReward(new ArrayList<>());
        return new FertiliseCropTilesQuest("Fertiliser Fanatic", reward, 48, 40);
    }

}
