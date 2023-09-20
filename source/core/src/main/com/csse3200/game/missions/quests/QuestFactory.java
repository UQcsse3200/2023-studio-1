package com.csse3200.game.missions.quests;

import com.csse3200.game.missions.rewards.ItemReward;

import java.util.ArrayList;

public class QuestFactory {

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
