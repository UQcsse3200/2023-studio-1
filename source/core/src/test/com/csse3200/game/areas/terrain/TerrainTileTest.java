package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class TerrainTileTest {
    private static TextureRegion textureRegion;
    private TerrainTile basicTestTile;
    private TerrainTile pathTerrainTile;
    private TerrainTile beachSandTerrainTile;
    private TerrainTile grassTerrainTile;
    private TerrainTile dirtTerrainTile;
    private TerrainTile shallowWaterTerrainTile;
    private TerrainTile desertTerrainTile;
    private TerrainTile snowTerrainTile;
    private TerrainTile iceTerrainTile;
    private TerrainTile deepwaterTerrainTile;
    private TerrainTile rockTerrainTile;
    private TerrainTile lavaTerrainTile;
    private TerrainTile lavaGroundTerrainTile;
    private TerrainTile gravelTerrainTile;
    private TerrainTile flowingWaterTerrainTile;

    @BeforeEach
    void beforeEach() {
        //textureRegion = new TextureRegion();
        textureRegion = null;
        basicTestTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.GRASS);
        pathTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.PATH);
        beachSandTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.BEACHSAND);
        grassTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.GRASS);
        dirtTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.DIRT);
        shallowWaterTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.SHALLOWWATER);
        desertTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.DESERT);
        snowTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.SNOW);
        iceTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.ICE);
        deepwaterTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.DEEPWATER);
        rockTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.ROCK);
        lavaTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.LAVA);
        lavaGroundTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.LAVAGROUND);
        gravelTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.GRAVEL);
        flowingWaterTerrainTile = new TerrainTile(textureRegion, TerrainTile.TerrainCategory.FLOWINGWATER);
    }

    @Test
    public void getTerrainCategory() {
        assertEquals(pathTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.PATH);
        assertEquals(beachSandTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.BEACHSAND);
        assertEquals(grassTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.GRASS);
        assertEquals(dirtTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.DIRT);
        assertEquals(shallowWaterTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.SHALLOWWATER);
        assertEquals(desertTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.DESERT);
        assertEquals(snowTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.SNOW);
        assertEquals(iceTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.ICE);
        assertEquals(deepwaterTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.DEEPWATER);
        assertEquals(rockTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.ROCK);
        assertEquals(lavaTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.LAVA);
        assertEquals(lavaGroundTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.LAVAGROUND);
        assertEquals(gravelTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.GRAVEL);
        assertEquals(flowingWaterTerrainTile.getTerrainCategory(), TerrainTile.TerrainCategory.FLOWINGWATER);
    }

    @Test
    public void setTerrainCategory() {
        basicTestTile.setTerrainCategory(TerrainTile.TerrainCategory.BEACHSAND);
        assertEquals(basicTestTile.getTerrainCategory(), TerrainTile.TerrainCategory.BEACHSAND);
    }

    @Test
    public void isTraversable() {
        assertTrue(pathTerrainTile.isTraversable());
        assertTrue(beachSandTerrainTile.isTraversable());
        assertTrue(grassTerrainTile.isTraversable());
        assertTrue(dirtTerrainTile.isTraversable());
        assertTrue(shallowWaterTerrainTile.isTraversable());
        assertTrue(desertTerrainTile.isTraversable());
        assertTrue(snowTerrainTile.isTraversable());
        assertTrue(iceTerrainTile.isTraversable());
        assertFalse(deepwaterTerrainTile.isTraversable());
        assertFalse(rockTerrainTile.isTraversable());
        assertFalse(lavaTerrainTile.isTraversable());
        assertTrue(lavaGroundTerrainTile.isTraversable());
        assertTrue(gravelTerrainTile.isTraversable());
        assertTrue(flowingWaterTerrainTile.isTraversable());
    }

    @Test
    public void isUnoccupiedAtStart() {
        assertFalse(basicTestTile.isOccupied());
    }

    @Test
    public void setOccupied() {
        basicTestTile.setOccupied();
        assertTrue(basicTestTile.isOccupied());
    }

    @Test
    public void setUnoccupied() {
        basicTestTile.setOccupied();
        basicTestTile.setUnOccupied();
        assertFalse(basicTestTile.isOccupied());
    }

    @Test
    public void isTillable() {
        assertTrue(pathTerrainTile.isTillable());
        assertFalse(beachSandTerrainTile.isTillable());
        assertTrue(grassTerrainTile.isTillable());
        assertTrue(dirtTerrainTile.isTillable());
        assertFalse(shallowWaterTerrainTile.isTillable());
        assertFalse(desertTerrainTile.isTillable());
        assertFalse(snowTerrainTile.isTillable());
        assertFalse(iceTerrainTile.isTillable());
        assertFalse(deepwaterTerrainTile.isTillable());
        assertFalse(rockTerrainTile.isTillable());
        assertFalse(lavaTerrainTile.isTillable());
        assertFalse(lavaGroundTerrainTile.isTillable());
        assertFalse(gravelTerrainTile.isTillable());
        assertFalse(flowingWaterTerrainTile.isTillable());
    }

    @Test
    public void cropTile() {
        Entity cropTile = new Entity();
        assertNull(basicTestTile.getCropTile());
        basicTestTile.setCropTile(cropTile);
        assertTrue(basicTestTile.isOccupied());
        assertEquals(cropTile, basicTestTile.getCropTile());
        basicTestTile.removeCropTile();
        assertFalse(basicTestTile.isOccupied());
        assertNull(basicTestTile.getCropTile());
    }

}


