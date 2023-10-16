package com.csse3200.game.missions.quests;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ShipDebrisFactory;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.RandomUtils;

import java.util.List;
import java.util.stream.IntStream;

/**
 * SpaceDebrisQuest is an extension of ClearDebrisQuest that spawns new debris to be cleared
 *
 * Code adopted from spawnShipDebris in SpaceGameArea
 */
public class SpaceDebrisQuest extends ClearDebrisQuest {

    /**
     * Position to spawn space debris
     */
    private static final GridPoint2 SPACE_DEBRIS_SPAWN = new GridPoint2(53, 14);

    /**
     * A Quest where player is required to clear a certain amount of debris.
     *
     * @param name                  - Human-readable name of the Quest.
     * @param reward                - Reward player will receive after completing the Quest.
     * @param numberOfDebrisToClear - Amount of debris the player is required to clear.
     */
    public SpaceDebrisQuest(String name, Reward reward, int numberOfDebrisToClear) {
        super(name, reward, numberOfDebrisToClear);
    }

        /**
         * Registers the Quest with the {@link MissionManager} by listening to the DEBRIS_CLEAR Mission event.
         * Also spawns in new debris at the specified location.
         * @param missionManagerEvents - A reference to the {@link EventHandler} on the
         *                             {@link MissionManager}, with which relevant events should be
         *                             listened to.
         */
        @Override
        public void registerMission (EventHandler missionManagerEvents) {
            super.registerMission(missionManagerEvents);

            GridPoint2 maxPos = new GridPoint2(SPACE_DEBRIS_SPAWN.x + 4, SPACE_DEBRIS_SPAWN.y + 4);
            GridPoint2 minPos = new GridPoint2(SPACE_DEBRIS_SPAWN.x - 4, SPACE_DEBRIS_SPAWN.y - 4);

            List<GridPoint2> clearedTilesAroundShip = List.of(
                    SPACE_DEBRIS_SPAWN,
                    new GridPoint2(SPACE_DEBRIS_SPAWN.x, SPACE_DEBRIS_SPAWN.y - 1),
                    new GridPoint2(SPACE_DEBRIS_SPAWN.x, SPACE_DEBRIS_SPAWN.y + 1),
                    new GridPoint2(SPACE_DEBRIS_SPAWN.x - 1, SPACE_DEBRIS_SPAWN.y - 1),
                    new GridPoint2(SPACE_DEBRIS_SPAWN.x - 1, SPACE_DEBRIS_SPAWN.y),
                    new GridPoint2(SPACE_DEBRIS_SPAWN.x - 1, SPACE_DEBRIS_SPAWN.y + 1),
                    new GridPoint2(SPACE_DEBRIS_SPAWN.x + 1, SPACE_DEBRIS_SPAWN.y - 1),
                    new GridPoint2(SPACE_DEBRIS_SPAWN.x + 1, SPACE_DEBRIS_SPAWN.y),
                    new GridPoint2(SPACE_DEBRIS_SPAWN.x + 1, SPACE_DEBRIS_SPAWN.y + 1)
            );

            IntStream.range(0, 10).forEach(i -> {
                GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
                TerrainTile tile = ServiceLocator.getGameArea().getMap().getTile(randomPos);

                while (!tile.isTraversable() || tile.isOccupied() || clearedTilesAroundShip.contains(randomPos)) {
                    randomPos = RandomUtils.random(minPos, maxPos);
                    tile = ServiceLocator.getGameArea().getMap().getTile(randomPos);
                }

                Entity shipDebris = ShipDebrisFactory.createShipDebris();
                ServiceLocator.getGameArea().spawnEntity(shipDebris);
                shipDebris.setPosition(ServiceLocator.getGameArea().getMap().getTerrainComponent().tileToWorldPosition(randomPos));

                tile.setOccupant(shipDebris);
                tile.setOccupied();
            });
        }
    }
