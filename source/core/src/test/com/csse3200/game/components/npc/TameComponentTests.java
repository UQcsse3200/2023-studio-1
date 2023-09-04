package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseAnimalConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.mock;


@ExtendWith(GameExtension.class)
public class TameComponentTests {
        private static final NPCConfigs configs =
                FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

        private Entity player;
        private ItemComponent fooditem;
        private Entity animaltotest;
        private TamableComponent tame;
        private BaseAnimalConfig baseAnimal;

        @BeforeEach
        void beforeEach() {
            player = mock(PlayerFactory.createPlayer().getClass());
            fooditem = new ItemComponent("Food for Animals", ItemType.ANIMAL_FOOD,
                    new Texture("images/animal/pngfood.png"));
            baseAnimal = new BaseAnimalConfig();

        }
}
