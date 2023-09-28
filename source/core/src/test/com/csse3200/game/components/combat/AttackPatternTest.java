package com.csse3200.game.components.combat;

import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.combat.attackpatterns.AttackPatternComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
public class AttackPatternTest {

    private AttackPatternComponent attackPatternComponent;
    private Entity entity;
    private InteractionDetector interactionDetector;


    @BeforeEach
    public void setUp() {
        interactionDetector = new InteractionDetector(1f);
        entity = new Entity()
                .addComponent(mock(InteractionDetector.class));
//                .addComponent()
    }
}
