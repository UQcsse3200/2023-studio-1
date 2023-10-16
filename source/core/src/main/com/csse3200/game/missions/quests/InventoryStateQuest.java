package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.ServiceLocator;

import java.util.Map;

public class InventoryStateQuest extends Quest {

    private final Map<String, Integer> targetQuantities;

    private InventoryComponent playerInventoryComponent;

    public InventoryStateQuest(String name, Reward reward, Map<String, Integer> targetQuantities) {
        super(name, reward);
        this.targetQuantities = targetQuantities;
    }

    public InventoryStateQuest(String name, Reward reward, int expiryDuration, Map<String, Integer> targetQuantities) {
        super(name, reward, expiryDuration, false);
        this.targetQuantities = targetQuantities;
    }

    protected boolean checkPlayerInventoryMissing() {
        if (playerInventoryComponent == null) {
            playerInventoryComponent = ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class);
            // If the player entity does not have an inventory, just return false
            return playerInventoryComponent == null;
        }
        return false;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        // To notify the QuestGiver of potential completion of quest, we will try to check on minuteUpdate if this quest
        // has been completed
        ServiceLocator.getTimeService().getEvents().addListener("minuteUpdate", this::notifyUpdate);
    }

    @Override
    public boolean isCompleted() {
        // We need to check this in case the player loses items from the inventory later (this would make the quest
        // giver think that the quest is yet to be completed
        if (isRewardCollected()) {
            return true;
        }

        if (checkPlayerInventoryMissing()) {
            return false;
        }

        for (Map.Entry<String, Integer> targetQuantity : targetQuantities.entrySet()) {
            if (playerInventoryComponent.getItemCount(targetQuantity.getKey()) < targetQuantity.getValue()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getDescription() {
        if (checkPlayerInventoryMissing()) {
            return "Error, player inventory not detected!";
        }

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("Gather items of all shapes and sizes to improve your efficiency!\n");
        descriptionBuilder.append("Gather the following items:\n");
        for (Map.Entry<String, Integer> targetQuantity : targetQuantities.entrySet()) {
            descriptionBuilder.append("    ");
            descriptionBuilder.append(targetQuantity.getValue());
            descriptionBuilder.append(" items of type: ");
            descriptionBuilder.append(targetQuantity.getKey());
            descriptionBuilder.append(" (");
            int amountCollected = playerInventoryComponent.getItemCount(targetQuantity.getKey());
            if (amountCollected < targetQuantity.getValue()) {
                descriptionBuilder.append(amountCollected);
                descriptionBuilder.append(" collected");
            } else {
                descriptionBuilder.append("done");
            }
            descriptionBuilder.append(").\n");
        }
        return descriptionBuilder.toString();
    }

    @Override
    public String getShortDescription() {
        if (checkPlayerInventoryMissing()) {
            return "Error, player inventory not detected!";
        }

        int numItemsToGet = 0;
        int numItemsCollected = 0;
        for (Map.Entry<String, Integer> targetQuantity : targetQuantities.entrySet()) {
            numItemsToGet += targetQuantity.getValue();
            numItemsCollected += Math.max(playerInventoryComponent.getItemCount(targetQuantity.getKey()), targetQuantity.getValue());
        }

        return Math.min(numItemsCollected, numItemsToGet) + " out of " + numItemsToGet + " required items collected";
    }

    @Override
    public void readProgress(JsonValue progress) {
        // All progress is tracked by directly accessing the state of the player's inventory
        // This quest tracks no dynamic progress
    }

    @Override
    public Object getProgress() {
        return 0;
    }

    @Override
    protected void resetState() {
        // All progress is tracked by directly accessing the state of the player's inventory
        // This quest tracks no dynamic progress
    }

}
