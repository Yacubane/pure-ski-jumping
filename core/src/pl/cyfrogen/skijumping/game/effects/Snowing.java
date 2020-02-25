package pl.cyfrogen.skijumping.game.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class Snowing {
    public float boxWidth = 5;
    public float boxHeight = 5;
    public float topBoxOffsetLeft = 5;
    public float topBoxOffsetRight = 5;

    public float boxX;
    public float boxY;


    public float spriteSizeMax = 0.2f;
    public float spriteSizeMin = 0.1f;
    public float padding = spriteSizeMax;

    final float TIME_TO_CATCH_UP = 5f;


    private float particlesPerSecond = 120;
    private float timeToNextParticle = 1 / particlesPerSecond;
    private float snowflakeMinVelocityY = -10f;
    private float snowflakeMaxVelocityY = -10f;
    private float snowflakeMinVelocityX = -8f;
    private float snowflakeMaxVelocityX = -6f;

    Texture texture;

    private int fromBoxColumn;
    private int toBoxColumn;
    private int fromBoxRow;
    private int toBoxRow;

    private float timeFromLastParticle;

    List<SnowFlake> snowFlakes = new ArrayList<SnowFlake>();
    private float leftAreaX;

    float particlesPerArea = 2f;
    private float leftAreaY;


    public Snowing(float speedX, float speedY, float particlesPerSecond) {
        texture = new Texture(Gdx.files.internal("particles/particle.png"));
        snowflakeMinVelocityX = speedX-1;
        snowflakeMaxVelocityX = speedX+1;
        snowflakeMinVelocityY = speedY-2;
        snowflakeMaxVelocityY = speedY+2;

        this.particlesPerSecond = particlesPerSecond;
        timeToNextParticle = 1 / particlesPerSecond;

    }

    public void sizeChanged() {
        topBoxOffsetLeft = boxHeight;
        topBoxOffsetRight = boxHeight;
        float time = 0;
        int particles = 0;
        while (time < TIME_TO_CATCH_UP) {
            float x =  -topBoxOffsetLeft + (boxWidth + topBoxOffsetLeft + topBoxOffsetRight) * MathUtils.random();
            float y = boxHeight;
            SnowFlake snowFlake = new SnowFlake(texture, x, y, null, getNewSnowflakeVelocityX(), getNewSnowflakeVelocityY());
            snowFlake.update(TIME_TO_CATCH_UP - time);
            if (snowFlake.getY() > 0 && snowFlake.getX() > 0 && snowFlake.getX() < boxWidth){
                particles++;
            }
            time += timeToNextParticle;
        }

        particlesPerArea = particles / (boxWidth * boxHeight);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);


        shapeRenderer.line(boxX, boxY, boxX - topBoxOffsetLeft, boxY + boxHeight);
        shapeRenderer.line(boxX - topBoxOffsetLeft, boxY + boxHeight, boxX + topBoxOffsetRight + boxWidth, boxY + boxHeight);
        shapeRenderer.line(boxX + topBoxOffsetRight + boxWidth, boxY + boxHeight, boxX + boxWidth, boxY);
        shapeRenderer.line(boxX + boxWidth, boxY, boxX, boxY);

        shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.line(boxX, boxY, boxX, boxY + boxHeight);
        shapeRenderer.line(boxX, boxY + boxHeight, boxX + boxWidth, boxY + boxHeight);
        shapeRenderer.line(boxX + boxWidth, boxY + boxHeight, boxX + boxWidth, boxY);
        shapeRenderer.line(boxX + boxWidth, boxY, boxX, boxY);

    }

    public void update(OrthographicCamera camera, float delta) {
        float oldBoxX = boxX;
        float oldBoxY = boxY;

        float oldBoxWidth = boxWidth;
        float oldBoxHeight = boxHeight;
        boxWidth = camera.viewportWidth * camera.zoom * 1.1f;
        boxHeight = camera.viewportHeight * camera.zoom * 1.1f;

        boolean sizeChanged = false;
        if(boxWidth != oldBoxWidth || boxHeight != oldBoxHeight) {
            sizeChanged = true;
            sizeChanged();
        }

        boxX = camera.position.x - boxWidth / 2f;
        boxY = camera.position.y - boxHeight /2f;

        float deltaBoxX = boxX - oldBoxX;
        float deltaBoxY = boxY - oldBoxY;

        float toCreateX;
        float toCreateWidth;
        float toCreateY;
        float toCreateHeight;

        if (deltaBoxX > 0) {
            toCreateX = oldBoxX + boxWidth;
            toCreateWidth = deltaBoxX;
            toCreateY = oldBoxY;
            toCreateHeight = boxHeight;
        } else {
            toCreateX = oldBoxX + deltaBoxX;
            toCreateWidth = -deltaBoxX;
            toCreateY = oldBoxY;
            toCreateHeight = boxHeight;
        }

        if(!sizeChanged)
        create(toCreateX, toCreateY, toCreateWidth, toCreateHeight, true);


        if (deltaBoxY > 0) {
            toCreateX = oldBoxX;
            toCreateWidth = boxWidth;
            toCreateY = oldBoxY + boxHeight;
            toCreateHeight = deltaBoxY;
        } else {
            toCreateX = oldBoxX;
            toCreateWidth = boxWidth;
            toCreateY = oldBoxY + deltaBoxY;
            toCreateHeight = -deltaBoxY;
        }

        if(!sizeChanged)
        create(toCreateX, toCreateY, toCreateWidth, toCreateHeight, false);

        int particlesToCreate = 0;
        timeFromLastParticle += delta;
        while (timeFromLastParticle > timeToNextParticle) {
            particlesToCreate++;
            timeFromLastParticle -= timeToNextParticle;
        }

        for(int i = 0; i < particlesToCreate; i++) {
            float xMin = boxX - topBoxOffsetLeft;
            float xMax = boxX + boxWidth + topBoxOffsetRight;
            float y = boxY + boxHeight;

            float flakeX = xMin + (xMax-xMin) * MathUtils.random();
            float flakeY = y;
            SnowFlake snowFlake = new SnowFlake(texture, flakeX, flakeY, null, getNewSnowflakeVelocityX(), getNewSnowflakeVelocityY());
            snowFlakes.add(snowFlake);

        }


        for (int i = 0; i < snowFlakes.size(); i++) {
            snowFlakes.get(i).update(delta);
            float x = snowFlakes.get(i).getX();
            float y = snowFlakes.get(i).getY();
            float yPerc = (y - boxY) / boxHeight;

            if(x < boxX - topBoxOffsetLeft || x > boxX + boxWidth + topBoxOffsetRight || y < boxY || y > boxY + boxHeight) {
                snowFlakes.remove(i--);
            }

        }

    }

    private void create(float x, float y, float width, float height, boolean xAxis) {
        float area = width * height;
        int particles = getParticlesPerArea(area, xAxis);

        for (int i = 0; i < particles; i++) {
            float flakeX = x + width * MathUtils.random();
            float flakeY = y + height * MathUtils.random();
            SnowFlake snowFlake = new SnowFlake(texture, flakeX, flakeY, null, getNewSnowflakeVelocityX(), getNewSnowflakeVelocityY());
            snowFlakes.add(snowFlake);
        }
    }

    private int getParticlesPerArea(float area, boolean xAxis) {
        int particles = 0;
        if(xAxis) {
            area += leftAreaX;
            particles = (int) (particlesPerArea * area);
            leftAreaX = area - (particles / particlesPerArea);
        } else {
            area += leftAreaY;
            particles = (int) (particlesPerArea * area);
            leftAreaY = area - (particles / particlesPerArea);
        }


        return particles;
    }

    private float getNewSnowflakeVelocityX() {
        return snowflakeMinVelocityX + (snowflakeMaxVelocityX - snowflakeMinVelocityX) * MathUtils.random();
    }

    private float getNewSnowflakeVelocityY() {
        return snowflakeMinVelocityY + (snowflakeMaxVelocityY - snowflakeMinVelocityY) * MathUtils.random();
    }


    public void draw(OrthographicCamera camera, Batch batch) {
        float left = camera.position.x - camera.viewportWidth * 0.5f * camera.zoom;
        float top = camera.position.y + camera.viewportHeight * 0.5f * camera.zoom;
        float right = camera.position.x + camera.viewportWidth * 0.5f * camera.zoom;
        float bottom = camera.position.y - camera.viewportHeight * 0.5f * camera.zoom;

        for (SnowFlake snowFlake : snowFlakes) {
            if (snowFlake.getX() > left && snowFlake.getX() < right
                    && snowFlake.getY() > bottom && snowFlake.getY() < top) {
                snowFlake.draw(batch);
            }
        }
    }


    private class Box {
        private final int column;
        private final int row;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Box box = (Box) o;
            return column == box.column &&
                    row == box.row;
        }

        public Box(int column, int row) {
            this.column = column;
            this.row = row;
        }
    }

    private class SnowFlake {
        private final Sprite sprite;
        private Box box;
        private final float velocityX;
        private final float velocityY;


        public SnowFlake(Texture texture, float x, float y, Box box, float velocityX, float velocityY) {
            sprite = new Sprite(texture);
            float spriteSize = spriteSizeMin + (spriteSizeMax - spriteSizeMin) * MathUtils.random();
            sprite.setSize(spriteSize, spriteSize);
            sprite.setPosition(x - spriteSize / 2f, y - spriteSize / 2f);
            this.box = box;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }

        public void setBox(Box box) {
            this.box = box;
        }

        public Box getBox() {
            return box;
        }

        public void update(float delta) {
            sprite.setX(sprite.getX() + velocityX * delta);
            sprite.setY(sprite.getY() + velocityY * delta);
        }

        public float getX() {
            return sprite.getX() + sprite.getWidth() / 2f;
        }

        public float getY() {
            return sprite.getY() + sprite.getHeight() / 2f;
        }

        public void draw(Batch batch) {
            sprite.draw(batch);
        }
    }
}
