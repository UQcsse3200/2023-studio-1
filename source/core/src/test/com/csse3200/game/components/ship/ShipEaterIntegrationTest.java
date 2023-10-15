package com.csse3200.game.components.ship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.areas.TestGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.IntStream;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
class ShipEaterIntegrationTest {
	private static final TestGameArea gameArea = new TestGameArea();
	private Entity ship;
	private boolean isDigging;
	private boolean isHiding;
	private boolean isEating;

	@BeforeEach
	void beforeEach() {
		ResourceService resourceService = new ResourceService();
		resourceService.loadTextureAtlases(new String[]{"images/shipeater.atlas", "images/animals/animal_effects.atlas"});

		String[] mapTextures = TerrainFactory.getMapTextures();
		String[] entityTextures = new String[]{
				"images/hostile_indicator.png", "images/invisible_sprite.png"
		};

		String[] textures = new String[mapTextures.length + entityTextures.length];
		System.arraycopy(mapTextures, 0, textures, 0, mapTextures.length);
		System.arraycopy(entityTextures, 0, textures, mapTextures.length, entityTextures.length);

		resourceService.loadTextures(textures);
		resourceService.loadAll();
		ServiceLocator.registerResourceService(resourceService);

		//Loads the test terrain into the GameMap
		TerrainComponent terrainComponent = mock(TerrainComponent.class);
		doReturn(TerrainFactory.WORLD_TILE_SIZE).when(terrainComponent).getTileSize();
		GameMap gameMap = new GameMap(new TerrainFactory(new CameraComponent()));
		gameMap.setTerrainComponent(terrainComponent);
		gameMap.loadTestTerrain("configs/TestMaps/allDirt20x20_map.txt");

		//Sets the GameMap in the TestGameArea
		gameArea.setGameMap(gameMap);

		ServiceLocator.registerCameraComponent(new CameraComponent());

		// Mock rendering, physics, game time
		RenderService renderService = new RenderService();
		renderService.setDebug(mock(DebugRenderer.class));
		ServiceLocator.registerRenderService(renderService);

		Stage stage = mock(Stage.class);
		ServiceLocator.getRenderService().setStage(stage);

		ServiceLocator.registerTimeSource(new GameTime());
		ServiceLocator.registerPhysicsService(new PhysicsService());
		ServiceLocator.registerEntityService(new EntityService());
		ServiceLocator.registerTimeService(new TimeService());
		ServiceLocator.registerGameArea(gameArea);

		ship = new Entity(EntityType.SHIP)
				.addComponent(new PhysicsComponent())
				.addComponent(new PhysicsMovementComponent())
				.addComponent(new ColliderComponent())
				.addComponent(new HitboxComponent())
				.addComponent(new ShipProgressComponent());
		ship.setCenterPosition(new Vector2(10f, 10f));

		ServiceLocator.getEntityService().register(ship);

		isDigging = false;
		isHiding = false;
		isEating = false;

	}

	private void setDigging(boolean digging) {
		isDigging = digging;
	}
	private void setEating(boolean eating) {
		isEating = eating;
	}
	private void setHiding(boolean hiding) {
		isHiding = hiding;
	}

	@Test
	void shouldMoveTowardsShip() {
		Entity entity = NPCFactory.createShipEater();

		entity.setPosition(new Vector2(10f, 1f));
		ServiceLocator.getGameArea().spawnEntity(entity);

		float initialDistanceToShip = entity.getPosition().dst(ship.getPosition());

		// Run the game for a few cycles
		runGame(5);

		float updatedDistanceToShip = entity.getPosition().dst(ship.getPosition());

		assert (initialDistanceToShip > updatedDistanceToShip);
	}

	@Test
	@Timeout(value = 10, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
	void shouldDigUnderObstacles() {
		/*
		 * If you've failed this test, something has messed up the ShipEater's physics components.
		 */

		// setup obstacles
		IntStream.range(7,13).forEach(x -> {
			Entity invisibleObstacle = ObstacleFactory.createInvisibleObstacle();
			invisibleObstacle.setPosition(new Vector2(x, 5f));
			ServiceLocator.getGameArea().spawnEntity(invisibleObstacle);
			ServiceLocator.getGameArea().getMap().getTile(new Vector2(x, 5f)).setOccupant(invisibleObstacle);
		});

		Entity entity = NPCFactory.createShipEater();

		entity.setPosition(new Vector2(10f, 1f));
		ServiceLocator.getGameArea().spawnEntity(entity);

		entity.getEvents().addListener("diggingUpdated", this::setDigging);

		// wait for the ship eater to start digging after hitting the obstacles
		// if this times out, something has gone wrong with the physics
		while (!isDigging) {
			updateGame();
		}

		// digging was triggered successfully!
		// get the current distance to the ship

		float distanceToShipAfterStartDigging = entity.getPosition().dst(ship.getPosition());

		// update a bit more to see if movement continues while digging

		runGame(10);

		float updatedDistanceToShip = entity.getPosition().dst(ship.getPosition());

		assert (distanceToShipAfterStartDigging > updatedDistanceToShip);
	}


	@Test
	@Timeout(value = 10, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
	void shouldStopDiggingAtShip() {
		/*
		 * If you've failed this test, something has messed up the ShipEater's physics components.
		 */

		// setup obstacles
		IntStream.range(7,13).forEach(x -> {
			Entity invisibleObstacle = ObstacleFactory.createInvisibleObstacle();
			invisibleObstacle.setPosition(new Vector2(x, 9f));
			ServiceLocator.getGameArea().spawnEntity(invisibleObstacle);
			ServiceLocator.getGameArea().getMap().getTile(new Vector2(x, 9f)).setOccupant(invisibleObstacle);
		});

		Entity entity = NPCFactory.createShipEater();

		entity.setPosition(new Vector2(10f, 7f));
		ServiceLocator.getGameArea().spawnEntity(entity);

		entity.getEvents().addListener("diggingUpdated", this::setDigging);
		entity.getEvents().addListener("eatingUpdated", this::setEating);

		// wait for the ship eater to start digging after hitting the obstacles
		// if this times out, something has gone wrong with the physics
		while (!isDigging) {
			updateGame();
		}

		// digging was triggered successfully!
		// wait for digging to stop

		while (isDigging) {
			updateGame();
		}

		// digging stopped, run the game a couple more times to make sure the
		// ship eater starts eating

		runGame(5);

		assert(isEating);
	}

	@Test
	void shouldHideWhenPlayerIsNear() {
		Entity entity = NPCFactory.createShipEater();

		entity.setPosition(new Vector2(10f, 9f));
		ServiceLocator.getGameArea().spawnEntity(entity);

		Entity player = new Entity(EntityType.PLAYER)
				.addComponent(new PhysicsComponent())
				.addComponent(new ColliderComponent())
				.addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));

		entity.getEvents().addListener("eatingUpdated", this::setEating);
		entity.getEvents().addListener("hidingUpdated", this::setHiding);

		runGame(20);

		assert(isEating);

		player.setPosition(11f, 11f);
		ServiceLocator.getGameArea().spawnEntity(player);

		runGame(1);

		assert(isHiding);
	}

	@Test
	void attacksAfterPlayerMovesAway() {
		Entity entity = NPCFactory.createShipEater();

		entity.setPosition(new Vector2(10f, 9.6f));
		ServiceLocator.getGameArea().spawnEntity(entity);

		entity.getEvents().addListener("eatingUpdated", this::setEating);
		entity.getEvents().addListener("hidingUpdated", this::setHiding);

		runGame(1);

		assert(isEating);

		Entity player = new Entity(EntityType.PLAYER)
				.addComponent(new PhysicsComponent())
				.addComponent(new ColliderComponent())
				.addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
		player.setPosition(11f, 11f);
		ServiceLocator.getGameArea().spawnEntity(player);

		runGame(1);

		assert(isHiding);

		player.setPosition(1f, 1f);

		runGame(1);

		assert(!isHiding);
		assert(isEating);

	}

	@Test
	void doesntMoveWhenNoShip() {
		// bye bye ship
		ServiceLocator.getEntityService().unregister(ship);

		Entity entity = NPCFactory.createShipEater();

		entity.setPosition(new Vector2(10f, 1f));
		ServiceLocator.getGameArea().spawnEntity(entity);
		float initialDistanceToShip = entity.getPosition().dst(ship.getPosition());

		// run the game a couple of times
		runGame(10);

		float updatedDistanceToShip = entity.getPosition().dst(ship.getPosition());

		assert(initialDistanceToShip == updatedDistanceToShip);
	}

	@Test
	void digForeverrrrrrrr() {
		// setup obstacles
		IntStream.range(2,6).forEach(y -> {
			Entity invisibleObstacle = ObstacleFactory.createInvisibleObstacle();
			invisibleObstacle.setPosition(new Vector2(10f, y));
			ServiceLocator.getGameArea().spawnEntity(invisibleObstacle);
			ServiceLocator.getGameArea().getMap().getTile(new Vector2(10f, y)).setOccupant(invisibleObstacle);
		});

		Entity entity = NPCFactory.createShipEater();

		entity.setPosition(new Vector2(10f, 1f));
		ServiceLocator.getGameArea().spawnEntity(entity);

		entity.getEvents().addListener("diggingUpdated", this::setDigging);
		entity.getEvents().addListener("eatingUpdated", this::setEating);

		// wait for the ship eater to start digging after hitting the obstacles
		// if this times out, something has gone wrong with the physics
		while (!isDigging) {
			updateGame();
		}

		// digging was triggered successfully!
		// wait for digging to stop

		// dig real slow to test checking whether tile conditions are good to stop digging
		entity.getComponent(PhysicsMovementComponent.class).setMaxSpeed(new Vector2(0.01f, 0.01f));

		while (isDigging) {
			updateGame();
		}

		// digging stopped, run the game a couple more times to make sure the
		// ship eater gets to the ship and starts eating

		// increase buddy's speed as well
		entity.getComponent(PhysicsMovementComponent.class).setMaxSpeed(new Vector2(10f, 10f));

		runGame(50);

		assert(isEating);
	}

	private void runGame(int numCycles) {
		IntStream.range(0, numCycles + 1).forEach(i -> {
			updateGame();
		});
	}

	private void updateGame() {
		ServiceLocator.getEntityService().update();
		ServiceLocator.getPhysicsService().getPhysics().update();

	}
}
