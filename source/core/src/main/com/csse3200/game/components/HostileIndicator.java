package com.csse3200.game.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.*;
import com.csse3200.game.entities.*;
import com.csse3200.game.rendering.RenderService;

import com.badlogic.gdx.utils.Array;

public class HostileIndicator extends Component{

    private Array<Entity> entities = new Array<>();

    @Override
    public void create() {
        super.create();

        entities = ServiceLocator.getEntityService().getEntities();

        updateDisplay();
    }




}