package pl.cyfrogen.skijumping.game.map.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class TextureObject implements GameObject {
    private final TextureRegion region;
    private float x;
    private float y;
    private final float width;
    private final float height;

    public TextureObject(TextureRegion region, float x, float y, float width, float height) {
        this.region = region;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(region, x, y, width, height);
    }

    public boolean checkInBounds(Vector2 min, Vector2 max) {
        if (x > max.x) return false;
        if (y > max.y) return false;
        if (x + width < min.x) return false;
        if (y + height < min.y) return false;
        return true;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
