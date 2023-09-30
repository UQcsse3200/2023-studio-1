package com.csse3200.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class TerrainFactoryTest {

    /**
     * Test to see that image filePaths all exist
     */
    @Test
    public void testTileImagesExist(){
        for (String filepath: TerrainFactory.charToTileImageMap.values()) {
            FileHandle file = Gdx.files.internal(filepath);
            assertTrue(file.exists());
        }
    }

    /**
     * Test to see all images are pngs
     * Png file pattern check algorithm modified from:
     *        https://www.w3schools.blog/validate-image-file-extension-regular-expression-regex-java
     */
    @Test
    public void testTileImagesArePNGs(){
        String pngRegex = "([^\\s]+(\\.(?i)(png))$)";
        Pattern pattern = Pattern.compile(pngRegex);
        for (String filepath: TerrainFactory.charToTileImageMap.values()) {
            Matcher matcher = pattern.matcher(filepath);
            assertTrue(matcher.matches());
        }
    }

    /**
     * Test to see that the map filePath exists
     */
    @Test
    public void testMapExist(){
        FileHandle file = Gdx.files.internal(TerrainFactory.mapPath);
        assertTrue(file.exists());
    }

    /**
     * Test to see map is a txt file
     * Txt file pattern check algorithm modified from:
     *      *        https://www.w3schools.blog/validate-image-file-extension-regular-expression-regex-java
     */
    @Test
    public void testMapisTxt(){
        String pngRegex = "([^\\s]+(\\.(?i)(txt))$)";
        Pattern pattern = Pattern.compile(pngRegex);
        Matcher matcher = pattern.matcher(TerrainFactory.mapPath);
        assertTrue(matcher.matches());
    }

    /**
     * Test to see that map sizes specified correctly
     */
    @Test
    public void testMapSyntaxSizeStated(){
        BufferedReader bf = new BufferedReader(new InputStreamReader(Gdx.files.internal(TerrainFactory.mapPath).read()));
        String line1, line2;
        try {
            line1 = bf.readLine();
            line2 = bf.readLine();
            Integer.parseInt(line1);
            Integer.parseInt(line2);
        } catch (IOException e) {
            fail();
        } catch (NumberFormatException e) {
            fail();
        }
    }

    /**
     * Test to see that map fits specified map dimensions (dimensions are 1-indexed when specifying size)
     */
    @Test
    public void testMapSizeCorrect(){
        BufferedReader bf = new BufferedReader(new InputStreamReader(Gdx.files.internal(TerrainFactory.mapPath).read()));
        String line1, line2, line;
        try {
            line1 = bf.readLine();
            line2 = bf.readLine();
            int maxX = Integer.parseInt(line1);
            int maxY = Integer.parseInt(line2);
            int y = 0;
            line = bf.readLine();
            while(line != null){
                y++;
                assertTrue(line.length() == maxX);
                line = bf.readLine();
            }
            assertTrue(y == maxY);
        } catch (IOException e) {
            fail();
        } catch (NumberFormatException e) {
            fail();
        }
    }

    /**
     * Test to see map contents are correct.
     */
    @Test
    public void testMapCharsValid(){
        BufferedReader bf = new BufferedReader(new InputStreamReader(Gdx.files.internal(TerrainFactory.mapPath).read()));
        String line;
        try {
            line = bf.readLine();
            line = bf.readLine();
            line = bf.readLine();
            while(line != null){
                for (int i = 0; i < line.length(); i++) {
                    assertTrue(TerrainFactory.charToTileImageMap.containsKey(line.charAt(i)));
                }
                line = bf.readLine();
            }
        } catch (IOException e) {
            fail();
        } catch (NumberFormatException e) {
            fail();
        }
    }
}
