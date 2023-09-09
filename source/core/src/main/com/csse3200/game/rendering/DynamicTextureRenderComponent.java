package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.services.ServiceLocator;

/** Render a static texture. */
public class DynamicTextureRenderComponent extends RenderComponent {
  private Texture texture;

  private static int layer;

  /**
   * @param texturePath Internal path of static texture to render.
   *                    Will be scaled to the entity's scale.
   */
  public DynamicTextureRenderComponent(String texturePath) {
    this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
  }
//...
  /** @param texture Static texture to render. Will be scaled to the entity's scale. */
  public DynamicTextureRenderComponent(Texture texture) {
    this.texture = texture;
    this.layer = 1;
  }

  public void setTexture(String texturePath) {
    this.texture = ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);
  }


  /** Scale the entity to a width of 1 and a height matching the texture's ratio */
  public void scaleEntity() {
    entity.setScale(1f, (float) texture.getHeight() / texture.getWidth());
  }

  @Override
  protected void draw(SpriteBatch batch) {
    Vector2 position = entity.getPosition();
    Vector2 scale = entity.getScale();
    float textureWidth = texture.getWidth();
    float textureHeight = texture.getHeight();
    batch.draw(texture, position.x, position.y, scale.x, scale.y);
  }

  public void setLayer(int layer) {
    this.layer = layer;
  }
  @Override
  public int getLayer() {
    return layer;
  }
}
