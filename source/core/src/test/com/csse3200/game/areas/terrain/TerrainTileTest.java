package com.csse3200.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
class TerrainTileTest {
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
        basicTestTile = new TerrainTile(null, TerrainTile.TerrainCategory.GRASS);
        pathTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.PATH);
        beachSandTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.BEACHSAND);
        grassTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.GRASS);
        dirtTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.DIRT);
        shallowWaterTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.SHALLOWWATER);
        desertTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.DESERT);
        snowTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.SNOW);
        iceTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.ICE);
        deepwaterTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.DEEPWATER);
        rockTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.ROCK);
        lavaTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.LAVA);
        lavaGroundTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.LAVAGROUND);
        gravelTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.GRAVEL);
        flowingWaterTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.FLOWINGWATER);
    }

    @Test
    void testGetTerrainCategory() {
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
    void testSetTerrainCategory() {
        basicTestTile.setTerrainCategory(TerrainTile.TerrainCategory.BEACHSAND);
        assertEquals(basicTestTile.getTerrainCategory(), TerrainTile.TerrainCategory.BEACHSAND);
    }

    @Test
    void testIsTraversable() {
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
    void testIsUnoccupiedAtStart() {
        assertFalse(basicTestTile.isOccupied());
    }

    @Test
    void testSetOccupied() {
        basicTestTile.setOccupied();
        assertTrue(basicTestTile.isOccupied());
    }

    @Test
    void testSetUnoccupied() {
        basicTestTile.setOccupied();
        basicTestTile.setUnOccupied();
        assertFalse(basicTestTile.isOccupied());
    }

    @Test
    void testIsTillable() {
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
    void testGetSpeedModifier() {
        assertEquals(1.2f, pathTerrainTile.getSpeedModifier());
        assertEquals(0.9f, beachSandTerrainTile.getSpeedModifier());
        assertEquals(1.0f, grassTerrainTile.getSpeedModifier());
        assertEquals(1.0f, dirtTerrainTile.getSpeedModifier());
        assertEquals(0.4f, shallowWaterTerrainTile.getSpeedModifier());
        assertEquals(0.8f, desertTerrainTile.getSpeedModifier());
        assertEquals(0.8f, snowTerrainTile.getSpeedModifier());
        assertEquals(1.5f, iceTerrainTile.getSpeedModifier());
        assertEquals(0.2f, deepwaterTerrainTile.getSpeedModifier());
        assertEquals(0.2f, rockTerrainTile.getSpeedModifier());
        assertEquals(0.2f, lavaTerrainTile.getSpeedModifier());
        assertEquals(0.7f, lavaGroundTerrainTile.getSpeedModifier());
        assertEquals(1.0f, gravelTerrainTile.getSpeedModifier());
        assertEquals(0.4f, flowingWaterTerrainTile.getSpeedModifier());
    }

    @Test
    void testTerrainTileCropEntityNullAfterInitialisation() {
        assertNull(basicTestTile.getOccupant());
    }

    @Test
    void testSetCropTileSetsCropTile() {
        Entity cropTile = new Entity();
        cropTile.addComponent(new CropTileComponent());
        basicTestTile.setOccupant(cropTile);
        assertEquals(cropTile, basicTestTile.getOccupant());
    }

    @Test
    void testSetCropTileSetsTileOccupied() {
        Entity cropTile = new Entity();
        cropTile.addComponent(new CropTileComponent());
        basicTestTile.setOccupant(cropTile);
        assertTrue(basicTestTile.isOccupied());
    }

    @Test
    void testRemoveCropTileSetsTileUnoccupied() {
        Entity cropTile = new Entity();
        cropTile.addComponent(new CropTileComponent());
        basicTestTile.setOccupant(cropTile);
        basicTestTile.removeOccupant();
        assertFalse(basicTestTile.isOccupied());
    }

    @Test
    void testRemoveCropTileSetsCropTileAsNull() {
        Entity cropTile = new Entity();
        cropTile.addComponent(new CropTileComponent());
        basicTestTile.setOccupant(cropTile);
        basicTestTile.removeOccupant();
        assertNull(basicTestTile.getOccupant());
    }

    @Test
    void testPathTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.PATH, pathTerrainTile.getTerrainCategory());
        assertTrue(pathTerrainTile.isTraversable());
        assertTrue(pathTerrainTile.isTillable());
        assertEquals(1.2f, pathTerrainTile.getSpeedModifier());
    }

    @Test
    void testBeachSandTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.BEACHSAND, beachSandTerrainTile.getTerrainCategory());
        assertTrue(beachSandTerrainTile.isTraversable());
        assertFalse(beachSandTerrainTile.isTillable());
        assertEquals(0.9f, beachSandTerrainTile.getSpeedModifier());
    }

    @Test
    void testGrassTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.GRASS, grassTerrainTile.getTerrainCategory());
        assertTrue(grassTerrainTile.isTraversable());
        assertTrue(grassTerrainTile.isTillable());
        assertEquals(1.0f, grassTerrainTile.getSpeedModifier());
    }

    @Test
    void testDirtTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.DIRT, dirtTerrainTile.getTerrainCategory());
        assertTrue(dirtTerrainTile.isTraversable());
        assertTrue(dirtTerrainTile.isTillable());
        assertEquals(1.0f, dirtTerrainTile.getSpeedModifier());
    }

    @Test
    void testShallowWaterTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.SHALLOWWATER, shallowWaterTerrainTile.getTerrainCategory());
        assertTrue(shallowWaterTerrainTile.isTraversable());
        assertFalse(shallowWaterTerrainTile.isTillable());
        assertEquals(0.4f, shallowWaterTerrainTile.getSpeedModifier());
    }

    @Test
    void testDesertTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.DESERT, desertTerrainTile.getTerrainCategory());
        assertTrue(desertTerrainTile.isTraversable());
        assertFalse(desertTerrainTile.isTillable());
        assertEquals(0.8f, desertTerrainTile.getSpeedModifier());
    }

    @Test
    void testSnowTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.SNOW, snowTerrainTile.getTerrainCategory());
        assertTrue(snowTerrainTile.isTraversable());
        assertFalse(snowTerrainTile.isTillable());
        assertEquals(0.8f, snowTerrainTile.getSpeedModifier());
    }

    @Test
    void testIceTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.ICE, iceTerrainTile.getTerrainCategory());
        assertTrue(iceTerrainTile.isTraversable());
        assertFalse(iceTerrainTile.isTillable());
        assertEquals(1.5f, iceTerrainTile.getSpeedModifier());
    }

    @Test
    void testDeepWaterTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.DEEPWATER, deepwaterTerrainTile.getTerrainCategory());
        assertFalse(deepwaterTerrainTile.isTraversable());
        assertFalse(deepwaterTerrainTile.isTillable());
        assertEquals(0.2f, deepwaterTerrainTile.getSpeedModifier());
    }

    @Test
    void testRockTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.ROCK, rockTerrainTile.getTerrainCategory());
        assertFalse(rockTerrainTile.isTraversable());
        assertFalse(rockTerrainTile.isTillable());
        assertEquals(0.2f, rockTerrainTile.getSpeedModifier());
    }

    @Test
    void testLavaTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.LAVA, lavaTerrainTile.getTerrainCategory());
        assertFalse(lavaTerrainTile.isTraversable());
        assertFalse(lavaTerrainTile.isTillable());
        assertEquals(0.2f, lavaTerrainTile.getSpeedModifier());
    }

    @Test
    void testLavaGroundTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.LAVAGROUND, lavaGroundTerrainTile.getTerrainCategory());
        assertTrue(lavaGroundTerrainTile.isTraversable());
        assertFalse(lavaGroundTerrainTile.isTillable());
        assertEquals(0.7f, lavaGroundTerrainTile.getSpeedModifier());
    }

    @Test
    void testGravelTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.GRAVEL, gravelTerrainTile.getTerrainCategory());
        assertTrue(gravelTerrainTile.isTraversable());
        assertFalse(gravelTerrainTile.isTillable());
        assertEquals(1, gravelTerrainTile.getSpeedModifier());
    }

    @Test
    void testFlowingWaterTerrainTileProperties() {
        assertEquals(TerrainTile.TerrainCategory.FLOWINGWATER, flowingWaterTerrainTile.getTerrainCategory());
        assertTrue(flowingWaterTerrainTile.isTraversable());
        assertFalse(flowingWaterTerrainTile.isTillable());
        assertEquals(0.4f, flowingWaterTerrainTile.getSpeedModifier());
    }
}


