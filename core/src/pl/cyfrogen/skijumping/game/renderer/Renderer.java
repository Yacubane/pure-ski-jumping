package pl.cyfrogen.skijumping.game.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

public class Renderer {
    private boolean batchBegan;
    private boolean shapeRendererBegan;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Matrix4 projectionMatrix;
    private ShapeRenderer.ShapeType shapeRendererType;

    public Renderer() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        projectionMatrix = batch.getProjectionMatrix();
    }

    private void endShapeRenderer() {
        shapeRenderer.end();
        shapeRendererBegan = false;
    }

    private void endBatch() {
        batch.end();
        batchBegan = false;
    }

    public void beginShapeRenderer(ShapeRenderer.ShapeType type) {
        if (shapeRendererBegan){
            if(shapeRendererType == type) return;
            else shapeRenderer.end();
        }
        if (batchBegan) endBatch();
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        shapeRenderer.begin(type);
        shapeRendererType = type;
        shapeRendererBegan = true;
    }

    public void beginSpriteBatch() {
        if (shapeRendererBegan) endShapeRenderer();
        if (batchBegan) return;
        batch.setProjectionMatrix(projectionMatrix);
        batch.begin();
        batchBegan = true;
    }

    public void end() {
        if (batchBegan) endBatch();
        if (shapeRendererBegan) endShapeRenderer();
    }

    public void dispose() {
        end();
        batch.dispose();
        shapeRenderer.dispose();
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public void setProjectionMatrix(Matrix4 matrix) {
        this.projectionMatrix = matrix;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void reset() {
        batchBegan = false;
        batchBegan = false;
    }
}
