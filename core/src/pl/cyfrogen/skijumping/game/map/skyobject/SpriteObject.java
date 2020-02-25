package pl.cyfrogen.skijumping.game.map.skyobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteObject extends SkyObject implements SpriteBatchObject {
    private final TextureRegion region;
    private float x;
    private float y;
    private final float width;
    private final float height;

    public SpriteObject(TextureRegion region, float x, float y, float width, float height, BlendingType blendingType, Color ambientColor) {
        super(blendingType, ambientColor);
        this.region = region;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(region, x, y, width, height);
    }

    @Override
    public void dispose() {

    }
}
