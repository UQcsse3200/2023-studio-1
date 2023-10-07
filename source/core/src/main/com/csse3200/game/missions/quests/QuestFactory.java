package com.csse3200.game.missions.quests;

import com.csse3200.game.areas.weather.SolarSurgeEvent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.TractorFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.cutscenes.Cutscene;
import com.csse3200.game.missions.rewards.*;
import com.csse3200.game.services.ServiceLocator;

import java.util.*;

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
    public static final String FISHING_QUEST = "BLOP";

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
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
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
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
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
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
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
                        ItemFactory.createSprinklerItem(),
                        ItemFactory.createSprinklerItem(),
                        ItemFactory.createPumpItem()
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
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
                new EntityReward(List.of(TractorFactory.createTractor())),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new MissionCompleteQuest(TRACTOR_GO_BRRRRRR, reward, 1);
    }

    public static FishingQuest createFishingQuest() {
        String dialogue = """
                Fosh!
                """;
        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createFertiliser())),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new FishingQuest(FISHING_QUEST, reward, 1);
    }

    /**
     * Creates the Making Friends {@link TameAnimalsQuest}.
     * @return - the Making Friends Quest
     */
    public static TameAnimalsQuest createMakingFriendsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createFertilisingFiestaQuest());
        questsToActivate.add(createFishingQuest());

        String dialogue = """
                "You are beginning to understand... {WAIT}Treat this planet well, and it will treat you well in return." {WAIT}Memories of a shattered Earth and a sky alight cloud your vision. {WAIT}You snap back to now. {WAIT}"You may have noticed the {COLOR=#76428A}KIND OFFERINGS{COLOR=WHITE} our fauna provide. {WAIT}Maybe you could try to use them?"
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createFishingRod())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
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

        List<Entity> itemRewards = new ArrayList<>();
        // TODO - Add weapon to defeat incoming enemies
        itemRewards.add(ItemFactory.createSpaceSnapperSeed());
        itemRewards.add(ItemFactory.createSpaceSnapperSeed());
        for (int i = 0; i < 20; i++) {
            itemRewards.add(ItemFactory.createFenceItem());
        }
        for (int i = 0; i < 3; i++) {
            itemRewards.add(ItemFactory.createGateItem());
        }

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(itemRewards),
                new QuestReward(questsToAdd, questsToActivate),
                new TriggerHostilesReward(List.of(
                        NPCFactory.createOxygenEater(),
                        NPCFactory.createOxygenEater(),
                        NPCFactory.createDragonfly(),
                        NPCFactory.createDragonfly(),
                        NPCFactory.createBat()
                )),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
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
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));

        return new ManageHostilesQuest(ALIENS_ATTACK_QUEST_NAME, reward, Set.of(EntityType.OXYGEN_EATER), 5);
    }

    /**
     * Creates the Act I {@link MainQuest}
     * @return - the Act I Main Quest
     */
    public static MainQuest createActIMainQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createConnectionQuest());
        questsToActivate.add(createTractorQuest());

        String dialogue = """
                For the first time since your landing, the {COLOR=#76428A}ALIEN CREATURE{COLOR=WHITE}'s vicious scowl fades. {WAIT}"I apologise for my initial hostility. {WAIT}My name is Jarrael. {WAIT}I am- {WAIT}was a member of the Karreshiq people. {WAIT}I was sent here for a special purpose, after {COLOR=RED}The Night of the Black Sun{COLOR=WHITE}. {WAIT}Our people had interacted with your civilisation many millennia ago, but we found a home here after we were {COLOR=RED}driven out{COLOR=WHITE}. {WAIT}So imagine my shock when you came crashing down from the sky. {WAIT}What happened?" {WAIT}You begin to explain your predicament to Jarrael...
                """;

        Set<String> requiredQuests = new HashSet<>();
        requiredQuests.add(CLEARING_YOUR_MESS_QUEST_NAME);
        requiredQuests.add(SOWING_YOUR_FIRST_SEEDS_QUEST_NAME);
        requiredQuests.add(REAPING_YOUR_REWARDS_QUEST_NAME);
        requiredQuests.add(MAKING_FRIENDS_QUEST_NAME);
        requiredQuests.add(FERTILISING_FIESTA_QUEST_NAME);
        requiredQuests.add(ALIENS_ATTACK_QUEST_NAME);

        List<Entity> itemRewards = new ArrayList<>();
        itemRewards.add(ItemFactory.createChestItem());
        for (int i = 0; i < 8; i++) {
            itemRewards.add(ItemFactory.createLightItem());
        }

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(itemRewards),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
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
        questsToActivate.add(createHomeSickQuest());
        questsToActivate.add(createActIIMainQuest());

        String dialogue = """
                As you finish explaining your crash landing, you warn Jarrael that if you aren't able to make contact with your people within {COLOR=#3ABE88}5 DAYS{COLOR=WHITE}, they will leave this planet's orbit, making reaching them again nearly impossible. {WAIT}"Hmm... {WAIT}This is indeed a grave issue. {WAIT}I will need some time to think of a possible solution. {WAIT}In the mean time, I have a favour to ask. {WAIT}The {COLOR=#76428A}NIGHTSHADE{COLOR=WHITE} produces {COLOR=#76428A}BERRIES{COLOR=WHITE} which are damaging to fleshy creatures such as you, but they are a delicacy to our people. {WAIT}A more appropriate name for you might be "{COLOR=#76428A}DEADLY NIGHTSHADE{COLOR=WHITE}". Here are some {COLOR=#76428A}SEEDS{COLOR=WHITE}. {WAIT}Come back to me when you have harvested them, and I should have thought of a solution by then."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createDeadlyNightshadeSeed(),
                        ItemFactory.createDeadlyNightshadeSeed(),
                        ItemFactory.createDeadlyNightshadeSeed()
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
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
                Jarrael takes the {COLOR=#76428A}BERRIES{COLOR=WHITE} you harvested, and consumes them. {WAIT}"Ah... It reminds me of our life here before {COLOR=RED}The Night of the Black Sun{COLOR=WHITE}". {WAIT}Jarrael's eyes seem to trail off for a moment, before focussing again. {WAIT}"I've thought of a solution to your issue. {WAIT}Your {COLOR=#76428A}SHIP{COLOR=WHITE} is in need of repair, that much is clear. {WAIT}I don't think we will be able to get it space-worthy in time, but maybe I could work on repairing your {COLOR=#76428A}SHIP{COLOR=WHITE}'s radio communications device. {WAIT}Take a few {COLOR=#76428A}SHIP PARTS{COLOR=WHITE} that you cleaned up when we first met, and see if you can make a start."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart()
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new PlantInteractionQuest(HOME_SICK_QUEST_NAME, reward, MissionManager.MissionEvent.HARVEST_CROP,
                Set.of("Deadly Nightshade"), 3);
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
                "Well done. {WAIT}Now I can get started on repairing your radio. {WAIT}Keep repairing the {COLOR=#76428A}SHIP{COLOR=WHITE}, and come to me when you have added enough {COLOR=#76428A}SHIP PARTS{COLOR=WHITE}."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createShipPart()
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new ShipRepairQuest(SHIP_REPAIRS_QUEST_NAME, reward, 3);
    }

    /**
     * Creates the Bringing It All Together {@link ShipRepairQuest}.
     * @return - the Bringing It All Together Quest
     */
    public static ShipRepairQuest createBringingItAllTogetherQuest() {
        String dialogue = """
                "Nice work. {WAIT}The {COLOR=#76428A}SHIP{COLOR=WHITE} is fully repaired - the radio should be working now. {WAIT}Come speak to me when you are ready to attempt to make contact with your people."
                """;

        DialogueReward reward = new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN);
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
                Jarrael ushers you into your {COLOR=#76428A}SHIP{COLOR=WHITE}, where your radio device currently sits. {WAIT}A faint crackle emanates from the radio as it turns on. {WAIT}You tune it to the {COLOR=#3ABE88}MOTHERSHIP{COLOR=WHITE}'s frequency. {WAIT}A distant voice is transmitted from the radio, {WAIT}"Hello? {WAIT}Hello? {WAIT}Is this {SHAKE}STATIC{ENDSHAKE}? The {COLOR=#3ABE88}SOLAR SURGE{COLOR=WHITE} which caused your crash wiped out some of our life support systems. {WAIT}We predict an increase in {COLOR=#3ABE88}SOLAR SURGE{COLOR=WHITE} activity over the next 15 days. {WAIT}We need to be able to land on your planet and survive without life support, or {COLOR=RED}humanity may be lost{COLOR=WHITE}!" {SHAKE}STATIC{ENDSHAKE} begins to take over the radio, as a powerful {COLOR=#3ABE88}SOLAR SURGE{COLOR=WHITE} hits you...
                """;

        Set<String> requiredQuests = new HashSet<>();
        requiredQuests.add(HOME_SICK_QUEST_NAME);
        requiredQuests.add(SHIP_REPAIRS_QUEST_NAME);
        requiredQuests.add(BRINGING_IT_ALL_TOGETHER_QUEST_NAME);

        MultiReward reward = new MultiReward(List.of(
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
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
                You let out a cry of desperation, as the fate of humanity now rests in your hands. {WAIT}Jarrael puts a hand on your shoulder. {WAIT}"I have an idea. {WAIT}I was sent here to cultivate a very special type of plant, the {COLOR=#76428A}ATOMIC ALGAE{COLOR=WHITE}. {WAIT}It is a very strong photosynthesiser, and we were going to use it to reverse the negative affects of {COLOR=RED}The Night of the Black Sun{COLOR=WHITE}. {WAIT}If you plant and cultivate enough of them, you should be able to manifestly increase the oxygen content of our atmosphere, and make the planet survivable for your kind. {WAIT}I believe I only have a few {COLOR=#76428A}SEEDS{COLOR=WHITE} to spare, so make sure you treat them well."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createAtomicAlgaeSeed(),
                        ItemFactory.createAtomicAlgaeSeed(),
                        ItemFactory.createAtomicAlgaeSeed()
                )),
                new TriggerWeatherReward(List.of(
                        new SolarSurgeEvent(0, 2, 100, 1.25f),
                        new SolarSurgeEvent(120, 2, 100, 1.4f),
                        new SolarSurgeEvent(240, 2, 100, 1.5f),
                        new SolarSurgeEvent(312, 2, 100, 1.5f)
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new AutoQuest(AN_IMMINENT_THREAT_QUEST_NAME, reward, "Learn about the imminent threat.");
    }

    /**
     * Creates the Air and Algae {@link PlantInteractionQuest}
     * @return - the Air and Alge Quest
     */
    public static PlantInteractionQuest createAirAndAlgaeQuest() {
        String dialogue = """
                "Keep at it. {WAIT}If your people are to survive the coming {COLOR=#3ABE88}SOLAR SURGES{COLOR=WHITE}, you will need to ensure the relative oxygen content of atmosphere is above 95% on the day they land. {WAIT}I have found a few more seeds to spare. {WAIT}Come to me on the day of your people's arrival when the oxygen is high enough, and I can help you signal them for landing. {WAIT}Good luck!"
                """;
        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createAtomicAlgaeSeed(),
                        ItemFactory.createAtomicAlgaeSeed()
                )),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new PlantInteractionQuest(AIR_AND_ALGAE_QUEST_NAME, reward, MissionManager.MissionEvent.PLANT_CROP,
                Set.of("Atomic Algae"), 3);
    }

    /**
     * Creates the Stratospheric Sentinel {@link OxygenLevelQuest}
     * @return - the Stratospheric Sentinel Quest
     */
    public static OxygenLevelQuest createStratosphericSentinelQuest() {
        String dialogue = """
                "Well done! {WAIT}You have your people one more chance at survival. {WAIT}Come with me, let us use your ship to signal the others for landing. {WAIT}I hope our people can help each other rebuild what we each have lost."
                """;
        DialogueReward reward = new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN);
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
