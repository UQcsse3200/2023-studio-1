package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

/**
 * Factory to create a mock player entity for testing.
 * Only includes necessary components for testing.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
@ExtendWith(GameExtension.class)
class TooltipTest {

	@Test
	void testTooltipEnter() {
		InstantTooltipManager tooltipManager = spy(new InstantTooltipManager());
		Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
		TextTooltip tooltip = new TextTooltip("hi", tooltipManager, skin);
		InputEvent ie = new InputEvent();
		ie.setListenerActor(new Window("window",skin));
		tooltip.enter(ie,1,1,-1,new Actor());
		verify(tooltipManager).enter(tooltip);

	}
	@Test
	void testTooltipExit() {
		InstantTooltipManager tooltipManager = spy(new InstantTooltipManager());
		Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
		TextTooltip tooltip = new TextTooltip("hi", tooltipManager, skin);
		InputEvent ie = new InputEvent();
		ie.setListenerActor(new Window("window",skin));
		tooltip.enter(ie,1,1,-1,new Actor());
		tooltip.exit(ie,1,1,-1,new Actor());
		verify(tooltipManager).hide(tooltip);

	}
}
