package com.csse3200.game.missions.quests;

import com.csse3200.game.areas.weather.SolarSurgeEvent;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.TractorFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.*;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestFactory {

    public static final String FIRST_CONTACT_QUEST_NAME = "First Contact";
    public static final String CLEARING_YOUR_MESS_QUEST_NAME = "Clearing Your Mess";
    public static final String SOWING_YOUR_FIRST_SEEDS_QUEST_NAME = "Sowing Your First Seeds";
    public static final String REAPING_YOUR_REWARDS_QUEST_NAME = "Reaping Your Rewards";
    public static final String MAKING_FRIENDS_QUEST_NAME = "Making Friends";
    public static final String FERTILISING_FIESTA_QUEST_NAME = "Fertilising Fiesta";
    public static final String ALIENS_ATTACK_QUEST_NAME = "Aliens Attack";
    public static final String ACT_I_MAIN_QUEST_NAME = "An Agreement";
    public static final String CONNECTION_QUEST_NAME = "Connection";
    public static final String HOME_SICK_QUEST_NAME = "Home Sick";
    public static final String SHIP_REPAIRS_QUEST_NAME = "Ship Repairs";
    public static final String BRINGING_IT_ALL_TOGETHER_QUEST_NAME = "Bringing It All Together";
    public static final String ACT_II_MAIN_QUEST_NAME = "Making Contact";
    public static final String AN_IMMINENT_THREAT_QUEST_NAME = "An Imminent Threat";
    public static final String AIR_AND_ALGAE_QUEST_NAME = "Air and Algae";
    public static final String STRATOSPHERIC_SENTINEL_QUEST_NAME = "Stratospheric Sentinel";
    public static final String ACT_III_MAIN_QUEST_NAME = "Weather the Storm";
    public static final String TRACTOR_GO_BRRRRRR = "Tractor Go BRRRRRR";

    private QuestFactory() {
        // This class should not be instantiated - if it is, do nothing
    }

    /**
     * Creates the First Contact {@link AutoQuest}
     * @return - the First Contact Quest
     */
    public static AutoQuest createFirstContactQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createClearingYourMessQuest());
        questsToActivate.add(createActIMainQuest());

        String dialogue = """
                As you come to consciousness, you notice a towering {COLOR=#76428A}ALIEN CREATURE{COLOR=WHITE} with what you can only describe as a vicious scowl standing over you. {SHAKE}"WHAT HAVE YOU DONE!!! {WAIT}CLEAR THIS MESS UP AT ONCE, OR {COLOR=RED}THERE WILL BE CONSEQUENCES{COLOR=WHITE}!!!"{ENDSHAKE}
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createShovel())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new AutoQuest(FIRST_CONTACT_QUEST_NAME, reward, "Wake up after your crash landing.");
    }

    /**
     * Creates the Clearing Your Mess {@link ClearDebrisQuest}.
     * @return - the Clearing Your Mess Quest
     */
    public static ClearDebrisQuest createClearingYourMessQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createSowingYourFirstSeedsQuest());

        String dialogue = """
                "Good. But your {WAIT}"landing"{WAIT} has {SHAKE}completely{ENDSHAKE} destroyed my crops! {WAIT}Take this {COLOR=#76428A}HOE{COLOR=WHITE} and these {COLOR=#76428A}SEEDS{COLOR=WHITE} and start replanting the crops you so viciously destroyed!"
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createHoe(),
                        ItemFactory.createCosmicCobSeed(),
                        ItemFactory.createCosmicCobSeed(),
                        ItemFactory.createCosmicCobSeed(),
                        ItemFactory.createCosmicCobSeed(),
                        ItemFactory.createCosmicCobSeed(),
                        ItemFactory.createCosmicCobSeed()
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new ClearDebrisQuest(CLEARING_YOUR_MESS_QUEST_NAME, reward, 15);
    }

    /**
     * Creates the Sowing Your First Seeds {@link PlantInteractionQuest}
     * @return - the Sowing Your First Seeds Quest
     */
    public static PlantInteractionQuest createSowingYourFirstSeedsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createReapingYourRewardsQuest());

        String dialogue = """
                "Impressive. {WAIT}I see that you can follow basic instructions. {WAIT}I understand that you probably didn't {WAIT}intend{WAIT} to crash here. {WAIT}I will let you stay, as long as you can prove yourself worthy of staying. {WAIT}I have a few more tasks for you to repair my work area, and only then might I consider helping you."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createScythe(), ItemFactory.createWateringcan())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new PlantInteractionQuest(SOWING_YOUR_FIRST_SEEDS_QUEST_NAME, reward, MissionManager.MissionEvent.PLANT_CROP,
                Set.of("Cosmic Cob"), 6);
    }

    /**
     * Creates the Reaping Your Rewards {@link PlantInteractionQuest}.
     * @return - the Reaping Your Rewards Quest
     */
    public static PlantInteractionQuest createReapingYourRewardsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createMakingFriendsQuest());

        String dialogue = """
                "Good job. {WAIT}You have proven capable of tending to crops. {WAIT}But in isolation, that means nothing. {WAIT}Now prove to me that you can use the fruits of your labour for the benefit of this planet. {WAIT}You may have noticed some {COLOR=#76428A}COW LIKE CREATURES{COLOR=WHITE} roaming about - show me you can gain their trust." {WAIT}You are suspicious that this {COLOR=#76428A}ANGRY ALIEN CREATURE{COLOR=WHITE} knows of cows...
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createSprinklerItem()
                        // TODO import sprinkler pump
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new PlantInteractionQuest(REAPING_YOUR_REWARDS_QUEST_NAME, reward, MissionManager.MissionEvent.HARVEST_CROP,
                Set.of("Cosmic Cob"), 6);
    }

    /**
     * Creates the Tractor {@link MissionCompleteQuest}
     * @return - the Tractor Quest
     */
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

    /**
     * Creates the Making Friends {@link TameAnimalsQuest}.
     * @return - the Making Friends Quest
     */
    public static TameAnimalsQuest createMakingFriendsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createFertilisingFiestaQuest());

        String dialogue = """
                "You are beginning to understand... {WAIT}Treat this planet well, and it will treat you well in return." {WAIT}Memories of a shattered Earth and a sky alight cloud your vision. {WAIT}You snap back to now. {WAIT}"You may have noticed the {COLOR=#76428A}KIND OFFERINGS{COLOR=WHITE} our fauna provide. {WAIT}Maybe you could try to use them?"
                """;

        MultiReward reward = new MultiReward(List.of(
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new TameAnimalsQuest(MAKING_FRIENDS_QUEST_NAME, reward, 1);
    }

    /**
     * Creates the Fertilising Fiesta {@link FertiliseCropTilesQuest}
     * @return - the Fertilising Fiesta Quest
     */
    public static FertiliseCropTilesQuest createFertilisingFiestaQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createAliensAttackQuest());

        String dialogue = """
                "It seems like you're getting the hang of this. There's only one more thing you must do, before I know to trust you. Some of our wildlife are {COLOR=RED}not so kind{COLOR=WHITE}. In particular, there are 3 {COLOR=#76428A}HOSTILE CREATURES{COLOR=WHITE} we consider pests. Take this {COLOR=#76428A}WEAPON{COLOR=WHITE}, and defend our work. I will also give you this {COLOR=#76428A}SPACE SNAPPER SEED{COLOR=WHITE}. When grown, the {COLOR=#76428A}SPACE SNAPPER{COLOR=WHITE} will happily munch on any creatures nearby, even non-hostile creatures."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        // TODO - Add weapon to defeat incoming enemies
                        ItemFactory.createSpaceSnapperSeed(),
                        ItemFactory.createFenceItem(),
                        ItemFactory.createGateItem()
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new TriggerHostilesReward(List.of(
                        // TODO - Add extra hostiles
                        NPCFactory.createOxygenEater(ServiceLocator.getGameArea().getPlayer()),
                        NPCFactory.createOxygenEater(ServiceLocator.getGameArea().getPlayer()),
                        NPCFactory.createOxygenEater(ServiceLocator.getGameArea().getPlayer())
                )),
                new DialogueReward(dialogue)
        ));
        return new FertiliseCropTilesQuest(FERTILISING_FIESTA_QUEST_NAME, reward, 1);
    }

    /**
     * Creates the Aliens Attack! {@link ManageHostilesQuest}
     * @return - the Aliens Attack! Quest
     */
    public static ManageHostilesQuest createAliensAttackQuest() {
        String dialogue = """
                "Impressive. {WAIT}I see you've inherited your species' aggressive tendencies. {WAIT}Now that you've shown you can be trusted, there is more I must reveal to you. {WAIT}Come back and speak to me when you are ready. In the mean time, take these seeds - the {COLOR=#76428A}ALOE VERA{COLOR=WHITE} plant produces a nectar which can heal you, and the {COLOR=#76428A}HAMMER PLANT{COLOR=WHITE} heals nearby creatures and plants when fully grown."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createHammerPlantSeed(),
                        ItemFactory.createAloeVeraSeed()
                )),
                new DialogueReward(dialogue)
        ));
        return new ManageHostilesQuest(ALIENS_ATTACK_QUEST_NAME, reward, Set.of(EntityType.OxygenEater), 5);
    }

    /**
     * Creates the Act I {@link MainQuest}
     * @return - the Act I Main Quest
     */
    public static MainQuest createActIMainQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createConnectionQuest());

        String dialogue = """
                For the first time since your landing, the {COLOR=#76428A}ALIEN CREATURE{COLOR=WHITE}'s vicious grimace fades. {WAIT}"I apologise for my initial hostility. {WAIT}My name is Jarrael. {WAIT}I am- {WAIT}was a member of the Karreshiq people. {WAIT}I was sent here for a special purpose, after {COLOR=RED}The Night of the Black Sun{COLOR=WHITE}. {WAIT}Our people had interacted with your civilisation many millennia ago, but we found a home here after we were {COLOR=RED}driven out{COLOR=WHITE}. {WAIT}So imagine my shock when you came crashing down from the sky. {WAIT}What happened?" {WAIT}You begin to explain your predicament to Jarrael...
                """;

        Set<String> requiredQuests = new HashSet<>();
        requiredQuests.add(CLEARING_YOUR_MESS_QUEST_NAME);
        requiredQuests.add(SOWING_YOUR_FIRST_SEEDS_QUEST_NAME);
        requiredQuests.add(REAPING_YOUR_REWARDS_QUEST_NAME);
        requiredQuests.add(MAKING_FRIENDS_QUEST_NAME);
        requiredQuests.add(FERTILISING_FIESTA_QUEST_NAME);
        requiredQuests.add(ALIENS_ATTACK_QUEST_NAME);

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createLightItem(),
                        ItemFactory.createChestItem()
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));

        return new MainQuest(ACT_I_MAIN_QUEST_NAME, reward, 5, requiredQuests, "gain ALIEN NPC's trust");
    }

    /**
     * Creates the Connection {@link AutoQuest}.
     * @return - the Connection Quest
     */
    public static AutoQuest createConnectionQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createActIIMainQuest());
        questsToActivate.add(createHomeSickQuest());

        String dialogue = """
                I will need some time to think of a solution to your issue.
                {WAIT}
                In the mean time, I have a small favour to ask - maybe it will help keep your mind off things.
                {WAIT}
                There is a plant that is- was sacred to our people.
                {WAIT}
                It is known as the Nightshade, although for you, a more apt description might be Deadly Nightshade.
                {WAIT}
                It's berries are poisonous to fleshy life-forms such as yourself.
                {WAIT}
                But to us, it is a delicacy.
                {WAIT}
                If you harvest a batch of them, I'm sure I will have thought of a solution to your problem.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createDeadlyNightshadeSeed())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new AutoQuest(CONNECTION_QUEST_NAME, reward, "Make a connection with ALIEN NPC, figure out how you can be reunited with Humanity.");
    }

    /**
     * Creates the Home Sick {@link PlantInteractionQuest}
     * @return - the Home Sick Quest
     */
    public static PlantInteractionQuest createHomeSickQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createShipRepairsQuest());

        String dialogue = """
                ALIEN NPC takes the berries you offer, and consumes them.
                {WAIT}
                A spark lights up in ALIEN NPC's eye.
                {WAIT}
                Ah... a taste of home. It reminds me of what life was like before- before the {COLOUR=red}Night of the Burning Sky{COLOUR=white}.
                {WAIT}
                *sigh*
                {WAIT}
                Nevermind...
                {WAIT}
                I've though of a solution to your predicament.
                {WAIT}
                Your ship is in need of repair. I don't greatly understand your technology, but I know physics, and how radio communication works.
                {WAIT}
                If you continue to repair your ship, I will be able to work on repairing your radio device.
                {WAIT}
                Here, take a few ship parts I found lying around and talk to me once you've done that.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart()
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new PlantInteractionQuest(HOME_SICK_QUEST_NAME, reward, MissionManager.MissionEvent.HARVEST_CROP,
                Set.of("Deadly Nightshade"), 5);
    }

    /**
     * Creates the Ship Repairs {@link ShipRepairQuest}.
     * @return - the Ship Repairs Quest
     */
    public static ShipRepairQuest createShipRepairsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createBringingItAllTogetherQuest());

        String dialogue = """
                Good job. I believe you will need to make a lot more progress in order to successfully repair the ship.
                {WAIT}
                Continue repairing your ship, and I will work on the radio device.
                {WAIT}
                I might even have some additional tasks for you, for which I could provide some help in finding other parts of your ship.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createShipPart())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new ShipRepairQuest(SHIP_REPAIRS_QUEST_NAME, reward, 3);
    }

    /**
     * Creates the Bringing It All Together {@link ShipRepairQuest}.
     * @return - the Bringing It All Together Quest
     */
    public static ShipRepairQuest createBringingItAllTogetherQuest() {
        String dialogue = """
                Your repairs appear to be successful.
                {WAIT}
                Come and talk to me when you want to try and reach out.
                """;

        DialogueReward reward = new DialogueReward(dialogue);
        return new ShipRepairQuest(BRINGING_IT_ALL_TOGETHER_QUEST_NAME, reward, 17);
    }

    /**
     * Creates the Act II {@link MainQuest}
     * @return - the Act II Main Quest
     */
    public static MainQuest createActIIMainQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createAnImminentThreatQuest());

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
                Taken aback by all this information, you attempt to speak to them again, but just as you go to speak, the sky lights up with the power of the sun as another powerful Solar Surge strikes.
                {WAIT}
                The radio cuts out...
                """;

        Set<String> requiredQuests = new HashSet<>();
        requiredQuests.add(CONNECTION_QUEST_NAME);
        requiredQuests.add(HOME_SICK_QUEST_NAME);
        requiredQuests.add(SHIP_REPAIRS_QUEST_NAME);
        requiredQuests.add(BRINGING_IT_ALL_TOGETHER_QUEST_NAME);

        MultiReward reward = new MultiReward(List.of(
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));

        return new MainQuest(ACT_II_MAIN_QUEST_NAME, reward, 10, requiredQuests, "make connection with the rest of your people");
    }

    /**
     * Creates the Imminent Threat {@link AutoQuest}
     * @return - the Imminent Threat Quest
     */
    public static AutoQuest createAnImminentThreatQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createAirAndAlgaeQuest());
        questsToActivate.add(createStratosphericSentinelQuest());
        questsToActivate.add(createActIIIMainQuest());

        String dialogue = """
                You let out a cry of anger.
                {WAIT}
                ALIEN NPC walks up to.
                {WAIT}
                If this storm, the one they are talking about, is as bad as they say, you could lose the technology allowing you to survive on this planet.
                {WAIT}
                I have an idea.
                {WAIT}
                I only have a few seeds left, but in the remaining seeds, there is one type of plant that I haven't given to you before.
                {WAIT}
                It's known as the Atomic Algae - it is supposed to be a powerful photosynthesiser.
                {WAIT}
                It is so strong it is said a few of these, if cared for correctly, can produce enough oxygen to change the atmosphere itself.
                {WAIT}
                However, I suspect Solar Surges are only going to get stronger in the coming days.
                {WAIT}
                And my guess is that our hostile wildlife won't like such melding with the atmosphere.
                {WAIT}
                Good luck, my friend.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createAloeVeraSeed(),
                        ItemFactory.createAtomicAlgaeSeed(),
                        ItemFactory.createCosmicCobSeed(),
                        ItemFactory.createDeadlyNightshadeSeed(),
                        ItemFactory.createHammerPlantSeed(),
                        ItemFactory.createSpaceSnapperSeed()
                )),
                new TriggerWeatherReward(List.of(
                        new SolarSurgeEvent(0, 2, 100, 1.25f),
                        new SolarSurgeEvent(120, 2, 100, 1.4f),
                        new SolarSurgeEvent(240, 2, 100, 1.5f),
                        new SolarSurgeEvent(312, 2, 100, 1.5f)
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new AutoQuest(AN_IMMINENT_THREAT_QUEST_NAME, reward, "Learn about the imminent threat.");
    }

    /**
     * Creates the Air and Algae {@link PlantInteractionQuest}
     * @return - the Air and Alge Quest
     */
    public static PlantInteractionQuest createAirAndAlgaeQuest() {
        String dialogue = """
                Keep at it.
                {WAIT}
                If your people are to be able to survive on this planet, you need to ensure the oxygen content in the atmosphere is at least 80%.
                {WAIT}
                I've found 4 more seeds of Atomic Algae to spare - the rest you will have to get from harvesting your current crops.
                """;
        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createAtomicAlgaeSeed(),
                        ItemFactory.createAtomicAlgaeSeed(),
                        ItemFactory.createAtomicAlgaeSeed(),
                        ItemFactory.createAtomicAlgaeSeed()
                )),
                new DialogueReward(dialogue)
        ));
        return new PlantInteractionQuest(AIR_AND_ALGAE_QUEST_NAME, reward, MissionManager.MissionEvent.PLANT_CROP,
                Set.of("Atomic Algae"), 10);
    }

    /**
     * Creates the Stratospheric Sentinel {@link OxygenLevelQuest}
     * @return - the Stratospheric Sentinel Quest
     */
    public static OxygenLevelQuest createStratosphericSentinelQuest() {
        String dialogue = """
                Well done!
                {WAIT}
                With the work you've done, your people should be able to live here without life support systems.
                {WAIT}
                «Something sentimental to be added...»
                """;
        DialogueReward reward = new DialogueReward(dialogue);
        return new OxygenLevelQuest(STRATOSPHERIC_SENTINEL_QUEST_NAME, reward, ServiceLocator.getPlanetOxygenService(),
                "the planet's oxygen level", 348, 95);
    }

    /**
     * Creates the Act III {@link MainQuest}
     * @return - the Act III Main Quest
     */
    public static MainQuest createActIIIMainQuest() {
        Set<String> requiredQuests = new HashSet<>();
        requiredQuests.add(AIR_AND_ALGAE_QUEST_NAME);
        requiredQuests.add(STRATOSPHERIC_SENTINEL_QUEST_NAME);

        WinReward reward = new WinReward();
        return new MainQuest(ACT_III_MAIN_QUEST_NAME, reward, 15, requiredQuests, "weather the incoming storm, provide a haven for humanity");
    }

    /**
     * Creates the Haber Hobbyist {@link FertiliseCropTilesQuest}
     * @return - the Haber Hobbyist Quest
     */
    public static FertiliseCropTilesQuest createHaberHobbyist() {
        // To be decided
        ItemReward reward = new ItemReward(new ArrayList<>());
        return new FertiliseCropTilesQuest("Haber Hobbyist", reward, 24, 10);
    }

    /**
     * Creates the Fertiliser Fanatic {@link FertiliseCropTilesQuest}
     * @return - the Fertiliser Fanatic Quest
     */
    public static FertiliseCropTilesQuest createFertiliserFanatic() {
        // To be decided
        ItemReward reward = new ItemReward(new ArrayList<>());
        return new FertiliseCropTilesQuest("Fertiliser Fanatic", reward, 48, 40);
    }

}
