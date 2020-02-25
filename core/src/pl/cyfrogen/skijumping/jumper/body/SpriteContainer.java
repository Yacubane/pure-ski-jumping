package pl.cyfrogen.skijumping.jumper.body;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class SpriteContainer {
    private final float editorX;
    private final float editorY;
    private final float editorWidth;
    private final float editorHeight;
    private final float scale;
    private List<Sprite> sprites = new ArrayList<Sprite>();

    Vector2 position = new Vector2();

    public SpriteContainer(float x, float y, float width, float height, float scale) {
        this.editorX = x;
        this.editorY = y;
        this.editorWidth = width;
        this.editorHeight = height;
        this.scale = scale;
    }


    public SpriteContainer add(TextureRegion textureRegion, Color color,
                               float x, float y, float width, float height) {
        Sprite sprite = new Sprite(textureRegion);
        sprite.setColor(color);
        sprite.setBounds(
                (x - editorX) * scale,
                (y - editorY) * scale,
                width * scale,
                height * scale);

        sprite.setOrigin(-sprite.getX(), -sprite.getY());
        this.sprites.add(sprite);
        return this;
    }

    public void setPosition(float x, float y) {
        Vector2 delta = new Vector2(x, y).sub(position);
        position.add(delta);
        for (Sprite sprite : sprites) {
            sprite.setPosition(
                    sprite.getX() + delta.x,
                    sprite.getY() + delta.y
            );
        }
    }

    public void draw(Batch batch) {
        for (Sprite sprite : sprites) {
            sprite.draw(batch);
        }
    }

    public void setRotation(float rotation) {
        for (Sprite sprite : sprites) {
            sprite.setRotation(rotation);
        }
    }

    @Override
    public SpriteContainer clone() {
        SpriteContainer spriteContainer =
                new SpriteContainer(editorX, editorY, editorWidth, editorHeight, scale);

        for (Sprite sprite : sprites) {
            spriteContainer.sprites.add(cloneSprite(sprite));
        }

        return spriteContainer;
    }

    private Sprite cloneSprite(Sprite sprite) {
        Sprite clonedSprite = new Sprite(sprite);
        return clonedSprite;
    }

}
