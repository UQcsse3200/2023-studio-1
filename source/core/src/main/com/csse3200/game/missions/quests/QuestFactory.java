package com.csse3200.game.missions.quests;

import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.TractorFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.*;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestFactory {

    public static final String firstContactQuestName = "First Contact";
    public static final String clearingYourMessQuestName = "Clearing Your Mess";
    public static final String sowingYourFirstSeedsQuestName = "Sowing Your First Seeds";
    public static final String reapingYourRewardsQuestName = "Reaping Your Rewards";
    public static final String makingFriendsQuestName = "Making Friends";
    public static final String actIMainQuestName = "An Agreement";
    public static final String actIIMainQuestName = "Making Contact";
    public static final String actIIIMainQuestName = "Weather the Storm";
    public static final String TRACTOR_GO_BRRRRRR = "Tractor Go BRRRRRR";

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
        questsToActivate.add(createSowingYourFirstSeedsQuest());
        questsToActivate.add(createTractorQuest());

        String dialogue = """
                Good. But your {WAIT} "landing" {WAIT} has completely destroyed my crops!
                {WAIT}
                Take this hoe and these seeds and start replanting the crops you destroyed.
                {WAIT}
                You have 6 hours to plant 12 Cosmic Corn.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createHoe(), ItemFactory.createCosmicCobSeed())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new ClearDebrisQuest(clearingYourMessQuestName, reward, 15);
    }

    public static PlantInteractionQuest createSowingYourFirstSeedsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createReapingYourRewardsQuest());

        String dialogue = """
                Impressive. I see that you can follow basic instructions.
                {WAIT}
                *sigh*
                {WAIT}
                I apologise for my hostility.
                {WAIT}
                My name is ALIEN NPC. I am an engineer of the ALIEN SPECIES people.
                {WAIT}
                I think I might be able to help you out, and get you back to wherever you came from. However, your crash still destroyed all that I was working on. So I'm not going to help you until you show that you are willing to help me.
                {WAIT}
                I have a few more tasks for you to do, and then I might consider helping you out.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createScythe(), ItemFactory.createWateringcan())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new PlantInteractionQuest(sowingYourFirstSeedsQuestName, reward, MissionManager.MissionEvent.PLANT_CROP,
                Set.of("Cosmic Cob"), 12);
    }

    public static PlantInteractionQuest createReapingYourRewardsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createMakingFriendsQuest());

        String dialogue = """
                Ahhh, well done. The Cosmic Cob is a favourite of my people.
                {WAIT}
                I thank you, truly.
                {WAIT}
                But there are more plants that were in my original collection before it was destroyed.
                {WAIT}
                Atomic Algae <TO BE ADDED>
                {WAIT}
                Also...
                {WAIT}
                You might find your harvested crops desirable by our local wildlife.
                {WAIT}
                Who knows, maybe you will be able to get something from them if you treat them nicely.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createAtomicAlgaeSeed())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new PlantInteractionQuest(reapingYourRewardsQuestName, reward, MissionManager.MissionEvent.HARVEST_CROP,
                Set.of("Cosmic Cob"), 12);
    }

    public static MissionCompleteQuest createTractorQuest() {
        String dialogue = """
                Traktor Go BRRRR!!!
                """;
        MultiReward reward = new MultiReward(List.of(
                new EntityReward(List.of(TractorFactory.createTractor(ServiceLocator.getGameArea().getPlayer()))),
                new DialogueReward(dialogue)
        ));
        return new MissionCompleteQuest(TRACTOR_GO_BRRRRRR, reward, 1);
    }

    public static TameAnimalsQuest createMakingFriendsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();

        String dialogue = """
                So you've met our kind fauna.
                {WAIT}
                Good. You're starting to learn.
                {WAIT}
                How about you try to use their gifts next?
                {WAIT}
                Oh... and one more thing.
                {WAIT}
                Be aware - not all of our fauna take so kindly to aliens from outer space.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createAloeVeraSeed())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new TameAnimalsQuest(makingFriendsQuestName, reward, 3);
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
        requiredQuests.add(sowingYourFirstSeedsQuestName);

        MultiReward reward = new MultiReward(List.of(
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));

        return new MainQuest(actIMainQuestName, reward, 5, requiredQuests, "gain ALIEN NPC's trust");
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

        return new MainQuest(actIIMainQuestName, reward, 10, requiredQuests, "make connection with the Mothership");
    }

    public static MainQuest createActIIIMainQuest() {
        Set<String> requiredQuests = new HashSet<>();
        MultiReward reward = new MultiReward(List.of());
        return new MainQuest(actIIIMainQuestName, reward, 15, requiredQuests, "weather the storm");
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
