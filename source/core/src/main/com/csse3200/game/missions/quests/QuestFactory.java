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
import java.util.function.Supplier;

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
    public static final String PART_FINDER_I_QUEST_NAME = "Part Finder I";
    public static final String SPACE_DEBRIS_QUEST_NAME = "Space Debris";
    public static final String BRINGING_IT_ALL_TOGETHER_QUEST_NAME = "Bringing It All Together";
    public static final String ACT_II_MAIN_QUEST_NAME = "Making Contact";
    public static final String AN_IMMINENT_THREAT_QUEST_NAME = "An Imminent Threat";
    public static final String AIR_AND_ALGAE_QUEST_NAME = "Air and Algae";
    public static final String STRATOSPHERIC_SENTINEL_QUEST_NAME = "Stratospheric Sentinel";
    public static final String ACT_III_MAIN_QUEST_NAME = "Weather the Storm";
    public static final String TRACTOR_GO_BRRRRRR = "A Special Gift";
    public static final String FISHING_QUEST = "Pro Fisherman";
    public static final String HABER_HOBBYIST_QUEST_NAME = "Haber Hobbyist";
    public static final String FERTILISER_FANATIC_QUEST_NAME = "Fertiliser Fanatic";
    public static final String ANIMAL_REPEAT_QUEST = "Animal Lover";
    public static final String PLANT_REPEAT_QUEST = "Green Thumb";
    public static final String WATER_REPEAT_QUEST = "Wet roots";
    public static final String SHIP_PART_REPEAT_QUEST = "No Parts?";

    private QuestFactory() {
        // This class should not be instantiated - if it is, do nothing
    }

    /**
     * Creates the First Contact {@link AutoQuest}
     * @return - the First Contact Quest
     */
    public static AutoQuest createFirstContactQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createClearingYourMessQuest);
        questsToActivate.add(QuestFactory::createActIMainQuest);

        String dialogue = """
                As you come to consciousness, you notice a towering {COLOR=#76428A}ALIEN CREATURE{COLOR=BLACK} with what you can only describe as a vicious scowl standing over you.
                {SHAKE}"WHAT HAVE YOU DONE!!! {WAIT}CLEAR THIS MESS UP AT ONCE, OR {COLOR=RED}THERE WILL BE CONSEQUENCES{COLOR=BLACK}!!!"{ENDSHAKE}
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createShovel())),
                new QuestReward(new ArrayList<>(), questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new AutoQuest(FIRST_CONTACT_QUEST_NAME, reward, "Wake up after your crash landing.");
    }

    /**
     * Creates the Clearing Your Mess {@link ClearDebrisQuest}.
     * @return - the Clearing Your Mess Quest
     */
    public static ClearDebrisQuest createClearingYourMessQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createSowingYourFirstSeedsQuest);

        String dialogue = """
                "Good. But your {WAIT}"landing"{WAIT} has {SHAKE}completely{ENDSHAKE} destroyed my crops!
                {WAIT}Take this {COLOR=#76428A}HOE{COLOR=BLACK} and these {COLOR=#76428A}SEEDS{COLOR=BLACK} and start replanting the crops you so viciously destroyed!"
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
                new QuestReward(new ArrayList<>(), questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new ClearDebrisQuest(CLEARING_YOUR_MESS_QUEST_NAME, reward, 15);
    }

    /**
     * Creates the Sowing Your First Seeds {@link PlantInteractionQuest}
     * @return - the Sowing Your First Seeds Quest
     */
    public static PlantInteractionQuest createSowingYourFirstSeedsQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createReapingYourRewardsQuest);

        String dialogue = """
                "Impressive. {WAIT}I see that you can follow basic instructions.
                {WAIT}I understand that you probably didn't {WAIT}intend{WAIT} to crash here.
                {WAIT}I will let you stay, as long as you can prove yourself worthy of staying.
                {WAIT}I have a few more tasks for you to repair my work area, and only then might I consider helping you."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createScythe(), ItemFactory.createWateringcan())),
                new QuestReward(new ArrayList<>(), questsToActivate),
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
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createMakingFriendsQuest);

        String dialogue = """
                "Good job. {WAIT}You have proven capable of tending to crops.
                {WAIT}But in isolation, that means nothing. {WAIT}Now prove to me that you can use the fruits of your labour for the benefit of this planet.
                {WAIT}You may have noticed some {COLOR=#76428A}COW LIKE CREATURES{COLOR=BLACK} roaming about - show me you can gain their trust."
                {WAIT}You are suspicious that this {COLOR=#76428A}ANGRY ALIEN CREATURE{COLOR=BLACK} knows of cows...
                """;

        MultiReward reward = new MultiReward(List.of(
                new QuestReward(new ArrayList<>(), questsToActivate),
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
                Here's something I found out back, might make it easier to till the ground and harvest your crops!!!
                """;
        MultiReward reward = new MultiReward(List.of(
                new EntityReward(List.of(TractorFactory.createTractor())),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new MissionCompleteQuest(TRACTOR_GO_BRRRRRR, reward, 7);
    }

    public static FishingQuest createFishingQuest() {
        String dialogue = """
                I am hungers, gimme fods or you = fods!!! >:(
                """;
        MultiReward reward = new MultiReward(List.of(
                new QuestReward(List.of(QuestFactory::createFishingQuest), new ArrayList<>()),
                new ItemReward(List.of(ItemFactory.createLightItem())),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new FishingQuest(FISHING_QUEST, reward, 5);
    }

    public static TameAnimalsQuest createRecursiveAnimalQuest() {
        String dialogue = """
                You look lonely, maybe get some more animals?!
                """;
        List<Entity> items = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            items.add(ItemFactory.createFenceItem());
        }
        items.add(ItemFactory.createGateItem());
        items.add(ItemFactory.createGateItem());
        MultiReward reward = new MultiReward(List.of(
                new QuestReward(List.of(QuestFactory::createRecursiveAnimalQuest), new ArrayList<>()),
                new ItemReward(items),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new TameAnimalsQuest(ANIMAL_REPEAT_QUEST, reward, 2);
    }

    public static PlantInteractionQuest createRecursivePlantQuest() {
        String dialogue = """
                You look lonely, maybe get some more animals?!
                """;
        List<Entity> items = new ArrayList<>();
        items.add(ItemFactory.createFertiliser());
        items.add(ItemFactory.createFertiliser());
        items.add(ItemFactory.createFertiliser());
        items.add(ItemFactory.createFertiliser());
        items.add(ItemFactory.createFertiliser());
        MultiReward reward = new MultiReward(List.of(
                new QuestReward(List.of(QuestFactory::createRecursivePlantQuest), new ArrayList<>()),
                new ItemReward(items),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new PlantInteractionQuest(PLANT_REPEAT_QUEST, reward, MissionManager.MissionEvent.PLANT_CROP,
                Set.of("Cosmic Cob", "Aloe Vera", "Hammer Plant", "Space Snapper", "Deadly Nightshade", "Atomic Algae"), 15);
    }

    public static PlantInteractionQuest createRecursiveWaterQuest() {
        String dialogue = """
                You look lonely, maybe get some more animals?!
                """;
        List<Entity> items = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            items.add(ItemFactory.createSprinklerItem());
        }
        items.add(ItemFactory.createPumpItem());
        items.add(ItemFactory.createPumpItem());
        MultiReward reward = new MultiReward(List.of(
                new QuestReward(List.of(QuestFactory::createRecursiveWaterQuest), new ArrayList<>()),
                new ItemReward(items),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new PlantInteractionQuest(WATER_REPEAT_QUEST, reward, MissionManager.MissionEvent.WATER_CROP,
                Set.of("Cosmic Cob", "Aloe Vera", "Hammer Plant", "Space Snapper", "Deadly Nightshade", "Atomic Algae"), 15);
    }

    /**
     * Creates the recursive Ship Part {@link InventoryStateQuest}
     * @return - the recursive Ship Part Quest
     */
    public static InventoryStateQuest createRecursivePartQuest() {
        String dialogue = """
                No parts? :(
                """;

        MultiReward reward = new MultiReward(List.of(
                new ConsumePlayerItemsReward(Map.of("Hammer Flower", 3)),
                new QuestReward(List.of(QuestFactory::createRecursivePartQuest), new ArrayList<>()),
                new ItemReward(List.of(ItemFactory.createShipPart())),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));

        return new InventoryStateQuest(SHIP_PART_REPEAT_QUEST, reward, Map.of("Hammer Flower", 3));
    }

    /**
     * Creates the Making Friends {@link TameAnimalsQuest}.
     * @return - the Making Friends Quest
     */
    public static TameAnimalsQuest createMakingFriendsQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createFertilisingFiestaQuest);
        String dialogue = """
                "You are beginning to understand... {WAIT}Treat this planet well, and it will treat you well in return."
                {WAIT}Memories of a shattered Earth and a sky alight cloud your vision. {WAIT}You snap back to now.
                {WAIT}"You may have noticed the {COLOR=#76428A}KIND OFFERINGS{COLOR=BLACK} our fauna provide. {WAIT}Maybe you could try to use them?"
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createFishingRod())),
                new QuestReward(new ArrayList<>(), questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new TameAnimalsQuest(MAKING_FRIENDS_QUEST_NAME, reward, 1);
    }

    /**
     * Creates the Fertilising Fiesta {@link FertiliseCropTilesQuest}
     * @return - the Fertilising Fiesta Quest
     */
    public static FertiliseCropTilesQuest createFertilisingFiestaQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createAliensAttackQuest);

        String dialogue = """
                "It seems like you're getting the hang of this. {WAIT}There's only one more thing you must do, before I know to trust you.
                {WAIT}Some of our wildlife are {COLOR=RED}not so kind{COLOR=BLACK}. {WAIT}In particular, there are 3 {COLOR=#76428A}HOSTILE CREATURES{COLOR=BLACK} we consider pests.
                {WAIT}Take this {COLOR=#76428A}WEAPON{COLOR=BLACK}, and defend our work.
                {WAIT}I will also give you this {COLOR=#76428A}SPACE SNAPPER SEED{COLOR=BLACK}.
                {WAIT}When grown, the {COLOR=#76428A}SPACE SNAPPER{COLOR=BLACK} will happily munch on any creatures nearby, even non-hostile creatures."
                """;

        List<Entity> itemRewards = new ArrayList<>();
        itemRewards.add(ItemFactory.createSword());
        for (int i = 0; i < 5; i++) {
            itemRewards.add(ItemFactory.createSpaceSnapperSeed());
        }

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(itemRewards),
                new QuestReward(new ArrayList<>(), questsToActivate),
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
                "Impressive. {WAIT}I see you've inherited your species' aggressive tendencies.
                {WAIT}Now that you've shown you can be trusted, there is more I must reveal to you.
                {WAIT}Come back and speak to me when you are ready.
                {WAIT}In the mean time, take these 3 boons.
                {WAIT}The {COLOR=#76428A}ALOE VERA{COLOR=BLACK} plant produces a nectar which can heal you.
                {WAIT}The {COLOR=#76428A}HAMMER PLANT{COLOR=BLACK} heals nearby creatures and plants when fully grown.
                {WAIT}This {COLOR=#76428A}GUN{COLOR=BLACK} might make dealing with hostile creatures easier."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createHammerPlantSeed(),
                        ItemFactory.createAloeVeraSeed(),
                        ItemFactory.createGun()
                )),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));

        return new ManageHostilesQuest(ALIENS_ATTACK_QUEST_NAME, reward, Set.of(EntityType.BAT, EntityType.DRAGONFLY,
                EntityType.OXYGEN_EATER), 5);
    }

    /**
     * Creates the Act I {@link MainQuest}
     * @return - the Act I Main Quest
     */
    public static MainQuest createActIMainQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createConnectionQuest);
        questsToActivate.add(QuestFactory::createTractorQuest);
        List<Supplier<Quest>> questsToBeSelectable = new ArrayList<>();
        questsToBeSelectable.add(QuestFactory::createFishingQuest);
        questsToBeSelectable.add(QuestFactory::createRecursiveAnimalQuest);
        questsToBeSelectable.add(QuestFactory::createRecursivePlantQuest);
        questsToBeSelectable.add(QuestFactory::createRecursiveWaterQuest);

        String dialogue = """
                For the first time since your landing, the {COLOR=#76428A}ALIEN CREATURE{COLOR=BLACK}'s vicious scowl fades.
                {WAIT}"I apologise for my initial hostility.
                {WAIT}My name is Jarrael. {WAIT}I am- {WAIT}was a member of the Karreshiq people.
                {WAIT}I was sent here for a special purpose, after {COLOR=RED}The Night of the Black Sun{COLOR=BLACK}.
                {WAIT}Our people had interacted with your civilisation many millennia ago, but we found a home here after we were {COLOR=RED}driven out{COLOR=BLACK}.
                {WAIT}So imagine my shock when you came crashing down from the sky.
                {WAIT}What happened?"
                {WAIT}You begin to explain your predicament to Jarrael...
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
                new QuestReward(questsToBeSelectable, questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));

        return new MainQuest(ACT_I_MAIN_QUEST_NAME, reward, 3, requiredQuests, "gain ALIEN NPC's trust");
    }

    /**
     * Creates the Connection {@link AutoQuest}.
     * @return - the Connection Quest
     */
    public static AutoQuest createConnectionQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createHomeSickQuest);
        questsToActivate.add(QuestFactory::createActIIMainQuest);

        String dialogue = """
                As you finish explaining your crash landing, you warn Jarrael that if you aren't able to make contact with your people within {COLOR=#3ABE88}6 DAYS{COLOR=BLACK}, they will leave this planet's orbit, making reaching them again nearly impossible.
                {WAIT}"Hmm... {WAIT}This is indeed a grave issue. {WAIT}I will need some time to think of a possible solution.
                {WAIT}In the mean time, I have a favour to ask.
                {WAIT}The {COLOR=#76428A}NIGHTSHADE{COLOR=BLACK} produces {COLOR=#76428A}BERRIES{COLOR=BLACK} which are damaging to fleshy creatures such as you, but they are a delicacy to our people, and those {COLOR=#76428A}ASTROLOTLS{COLOR=BLACK} floating around the place.
                {WAIT}Here are some {COLOR=#76428A}SEEDS{COLOR=BLACK}. {WAIT}Come back to me when you have harvested them, and I should have thought of a solution by then."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createDeadlyNightshadeSeed(),
                        ItemFactory.createDeadlyNightshadeSeed(),
                        ItemFactory.createDeadlyNightshadeSeed()
                )),
                new QuestReward(new ArrayList<>(), questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new AutoQuest(CONNECTION_QUEST_NAME, reward, "Make a connection with ALIEN NPC, figure out how you can be reunited with Humanity.");
    }

    /**
     * Creates the Home Sick {@link InventoryStateQuest}
     * @return - the Home Sick Quest
     */
    public static InventoryStateQuest createHomeSickQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createShipRepairsQuest);

        String dialogue = """
                Jarrael takes the {COLOR=#76428A}BERRIES{COLOR=BLACK} you harvested, and consumes them.
                {WAIT}"Ah... It reminds me of our life here before {COLOR=RED}The Night of the Black Sun{COLOR=BLACK}".
                {WAIT}Jarrael's eyes seem to trail off for a moment, before focussing again.
                {WAIT}"I've thought of a solution to your issue.
                {WAIT}Your {COLOR=#76428A}SHIP{COLOR=BLACK} is in need of repair, that much is clear.
                {WAIT}I don't think we will be able to get it space-worthy in time, but maybe I could work on repairing your {COLOR=#76428A}SHIP{COLOR=BLACK}'s radio communications device.
                {WAIT}Take a few {COLOR=#76428A}SHIP PARTS{COLOR=BLACK} that you cleaned up when we first met, and see if you can make a start."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ConsumePlayerItemsReward(Map.of("Nightshade Berry", 9)),
                new ItemReward(List.of(
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart()
                )),
                new QuestReward(new ArrayList<>(), questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new InventoryStateQuest(HOME_SICK_QUEST_NAME, reward, Map.of("Nightshade Berry", 9));
    }

    /**
     * Creates the Ship Repairs {@link ShipRepairQuest}.
     * @return - the Ship Repairs Quest
     */
    public static ShipRepairQuest createShipRepairsQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createPartFinderIQuest);
        questsToActivate.add(QuestFactory::createBringingItAllTogetherQuest);

        String dialogue = """
                "Well done. {WAIT}Now I can get started on repairing your radio. 
                {WAIT}Keep repairing the {COLOR=#76428A}SHIP{COLOR=BLACK}, and come to me when you have added enough {COLOR=#76428A}SHIP PARTS{COLOR=BLACK}."
                {WAIT}By the way, I've found some more stray pieces of debris." 
                Jarrael takes out a piece of paper with a rough sketch of the nearby area. 
                {WAIT}"I've marked out the general area, go there and clean up anything you find. You might find more spare parts to repair your ship."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ClueReward(ItemFactory.createClueItem()),
                new QuestReward(new ArrayList<>(), questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new ShipRepairQuest(SHIP_REPAIRS_QUEST_NAME, reward, 3);
    }

    /**
     * Creates the Clue {@link ShipRepairQuest}
     * @return - the Clue Finder Quest
     */
    public static ShipRepairQuest createPartFinderIQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createSpaceDebrisQuest);

        String dialogue = """
                As you return to Jarrael, a bright flash in the sky catches your attention. 
                {WAIT}"Well, it seems like some stray space debris has just broken through the atmosphere..." 
                {WAIT}Both of you watch as the burning trail of debris slowly descends to the planet's surface in the south. 
                {WAIT}"What a perfect opportunity! You should go over and check it out. 
                Bring me back whatever wreckage you can find and I'll try my best to make them usable for repairs."
                """;

        MultiReward reward = new MultiReward(List.of(
                //new ClueReward(ItemFactory.createClueItem()),
                new ItemReward(List.of(ItemFactory.createShipPart())),
                new QuestReward(new ArrayList<>(), questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));
        return new ShipRepairQuest(PART_FINDER_I_QUEST_NAME, reward, 1);
    }

    /**
     * Creates the Space Debris {@link ClearDebrisQuest}
     * @return - the Space Debris Quest
     */
    // TODO: set up debris spawning/tracking. Change to ClearDebrisQuest
    // PLACEHOLDER ATM JUST SO I DON'T HAVE TO DEAL WITH SO MANY CONFLICTS LATER PLEASE
    public static ShipRepairQuest createSpaceDebrisQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createBringingItAllTogetherQuest);

        List<Supplier<Quest>> questsToBeSelectable = new ArrayList<>();
        questsToBeSelectable.add(QuestFactory::createRecursivePartQuest);

        String dialogue = """
                "Lucky for you there's quite a few salvageable pieces from this wreckage. 
                Here, I've cleaned them up for you. This should help with repairs."
                {WAIT}The alien hands you a hefty number of ship parts before continuing. 
                {WAIT}"You might have also noticed some strange creatures appearing from the wreckage. They are SHIP EATERS.
                They'll start eating away at your ship if you let the get too close. 
                {WAIT}If they eat too much of your ship, just bring me some HAMMER FLOWERS and I can get you more SHIP PARTS."
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart(),
                        ItemFactory.createShipPart()
                )),
                new QuestReward(questsToBeSelectable, questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));

        return new ShipRepairQuest(SPACE_DEBRIS_QUEST_NAME, reward, 1);
    }

    /**
     * Creates the Bringing It All Together {@link ShipRepairQuest}.
     * @return - the Bringing It All Together Quest
     */
    public static ShipRepairQuest createBringingItAllTogetherQuest() {
        String dialogue = """
                "Nice work. {WAIT}The {COLOR=#76428A}SHIP{COLOR=BLACK} is fully repaired - the radio should be working now.
                {WAIT}Come speak to me when you are ready to attempt to make contact with your people."
                """;

        DialogueReward reward = new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN);
        return new ShipRepairQuest(BRINGING_IT_ALL_TOGETHER_QUEST_NAME, reward, 7);
    }

    /**
     * Creates the Act II {@link MainQuest}
     * @return - the Act II Main Quest
     */
    public static MainQuest createActIIMainQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createAnImminentThreatQuest);

        String dialogue = """
                Jarrael ushers you into your {COLOR=#76428A}SHIP{COLOR=BLACK}, where your radio device currently sits.
                {WAIT}A faint crackle emanates from the radio as it turns on.
                {WAIT}You tune it to the {COLOR=#3ABE88}MOTHERSHIP{COLOR=BLACK}'s frequency.
                {WAIT}A distant voice is transmitted from the radio,
                {WAIT}"Hello? {WAIT}Hello? {WAIT}Is this {SHAKE}--STATIC--{ENDSHAKE}?
                The {COLOR=#3ABE88}SOLAR SURGE{COLOR=BLACK} which caused your crash wiped out some of our life support systems.
                {WAIT}We predict an increase in {COLOR=#3ABE88}SOLAR SURGE{COLOR=BLACK} activity over the next {COLOR=#3ABE88}9 DAYS{COLOR=BLACK}.
                {WAIT}We need to be able to land on your planet and survive without life support, or {COLOR=RED}humanity may be lost{COLOR=BLACK}!"
                {SHAKE}--STATIC--{ENDSHAKE} begins to take over the radio, as a powerful {COLOR=#3ABE88}SOLAR SURGE{COLOR=BLACK} hits you...
                """;

        Set<String> requiredQuests = new HashSet<>();
        requiredQuests.add(HOME_SICK_QUEST_NAME);
        requiredQuests.add(SHIP_REPAIRS_QUEST_NAME);
        requiredQuests.add(BRINGING_IT_ALL_TOGETHER_QUEST_NAME);

        MultiReward reward = new MultiReward(List.of(
                new QuestReward(new ArrayList<>(), questsToActivate),
                new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN)
        ));

        return new MainQuest(ACT_II_MAIN_QUEST_NAME, reward, 6, requiredQuests, "make connection with the rest of your people");
    }

    /**
     * Creates the Imminent Threat {@link AutoQuest}
     * @return - the Imminent Threat Quest
     */
    public static AutoQuest createAnImminentThreatQuest() {
        List<Supplier<Quest>> questsToActivate = new ArrayList<>();
        questsToActivate.add(QuestFactory::createAirAndAlgaeQuest);
        questsToActivate.add(QuestFactory::createStratosphericSentinelQuest);
        questsToActivate.add(QuestFactory::createActIIIMainQuest);

        String dialogue = """
                You let out a cry of desperation, as the fate of humanity now rests in your hands.
                {WAIT}Jarrael puts a hand on your shoulder.
                {WAIT}"I have an idea. {WAIT}I was sent here to cultivate a very special type of plant, the {COLOR=#76428A}ATOMIC ALGAE{COLOR=BLACK}.
                {WAIT}It is a very strong photosynthesiser, and we were going to use it to reverse the negative affects of {COLOR=RED}The Night of the Black Sun{COLOR=BLACK}.
                {WAIT}If you plant and cultivate enough of them, you should be able to manifestly increase the oxygen content of our atmosphere, and make the planet survivable for your kind.
                {WAIT}I believe I only have a few {COLOR=#76428A}SEEDS{COLOR=BLACK} to spare, so make sure you treat them well."
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
                new QuestReward(new ArrayList<>(), questsToActivate),
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
                "Keep at it.
                {WAIT}If your people are to survive the coming {COLOR=#3ABE88}SOLAR SURGES{COLOR=BLACK}, you will need to ensure the relative oxygen content of atmosphere is above 95% on the day they land.
                {WAIT}I have found a few more seeds to spare.
                {WAIT}Come to me on the day of your people's arrival when the oxygen is high enough, and I can help you signal them for landing.
                {WAIT}Good luck!"
                """;
        MultiReward reward = new MultiReward(List.of(
                new QuestReward(List.of(QuestFactory::createHaberHobbyist), new ArrayList<>()),
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
                "Well done! {WAIT}You have your people one more chance at survival.
                {WAIT}Come with me, let us use your ship to signal the others for landing.
                {WAIT}I hope our people can help each other rebuild what we each have lost."
                """;
        DialogueReward reward = new DialogueReward(dialogue, Cutscene.CutsceneType.ALIEN);
        return new OxygenLevelQuest(STRATOSPHERIC_SENTINEL_QUEST_NAME, reward, ServiceLocator.getPlanetOxygenService(),
                "the planet's oxygen level", 192, 95);
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
        return new MainQuest(ACT_III_MAIN_QUEST_NAME, reward, 9, requiredQuests, "weather the incoming storm, provide a haven for humanity");
    }

    /**
     * Creates the Haber Hobbyist {@link FertiliseCropTilesQuest}
     * @return - the Haber Hobbyist Quest
     */
    public static FertiliseCropTilesQuest createHaberHobbyist() {
        ItemReward itemReward = new ItemReward(List.of(
                ItemFactory.createFertiliser(),
                ItemFactory.createFertiliser(),
                ItemFactory.createFertiliser(),
                ItemFactory.createFertiliser(),
                ItemFactory.createFertiliser()
        ));

        MultiReward reward = new MultiReward(List.of(
                itemReward,
                new QuestReward(List.of(QuestFactory::createFertiliserFanatic), new ArrayList<>())
        ));
        return new FertiliseCropTilesQuest(HABER_HOBBYIST_QUEST_NAME, reward, 24, 5);
    }

    /**
     * Creates the Fertiliser Fanatic {@link FertiliseCropTilesQuest}
     * @return - the Fertiliser Fanatic Quest
     */
    public static FertiliseCropTilesQuest createFertiliserFanatic() {
        ItemReward itemReward = new ItemReward(List.of(
                ItemFactory.createFertiliser(),
                ItemFactory.createFertiliser(),
                ItemFactory.createFertiliser(),
                ItemFactory.createAtomicAlgaeSeed(),
                ItemFactory.createAtomicAlgaeSeed()
        ));

        MultiReward reward = new MultiReward(List.of(
                itemReward,
                new QuestReward(List.of(QuestFactory::createHaberHobbyist), new ArrayList<>())
        ));
        return new FertiliseCropTilesQuest(FERTILISER_FANATIC_QUEST_NAME, reward, 48, 15);
    }

}
