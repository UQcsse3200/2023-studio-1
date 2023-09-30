package com.csse3200.game.entities;

import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.extensions.GameExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;



@ExtendWith(GameExtension.class)
public class EntitySpawnerTest {

    List<EntitySpawner> toSpawn;

    EntitySpawner spawner;

    Entity entity;
    Entity player;
    NPCFactory mocknpcFactory;

    @BeforeEach
    private void beforeEach() {
        this.player = new Entity(EntityType.Player);
        entity = new Entity(EntityType.Cow);

        when(NPCFactory.createCow(player)).thenReturn(entity);
        this.toSpawn = new ArrayList<>();

        spawner = new EntitySpawner(10, NPCFactory::createCow, player, 0, 3, 1,
                0, 2);
        this.toSpawn.add(spawner);

      //  EntitiesSpawner spawner = new EntitiesSpawner();
    }
}
