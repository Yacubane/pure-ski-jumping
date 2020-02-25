package pl.cyfrogen.skijumping.game.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import pl.cyfrogen.skijumping.game.ShaderPrograms;

public class GameRenderer {
    private final SpriteBatch spriteBatch;
    private final ShaderProgram meshShader;

    private ShaderState state = ShaderState.NONE;
    private ProjectionState projectionState = ProjectionState.NORMAL;

    private Matrix4 tempProjectionMatrix = new Matrix4();
    private boolean tempProjectionHasChanged;
    private Matrix4 normalProjectionMatrix = new Matrix4();
    private boolean normalProjectionHasChanged;
    private Vector2 minWorldToScreenPosition;
    private Vector2 maxWorldToScreenPosition;

    public GameRenderer() {
        ShaderPrograms shaderPrograms = new ShaderPrograms();

        spriteBatch = shaderPrograms.getSpriteBatch();
        this.meshShader = shaderPrograms.getMeshShader();

        spriteBatch.begin();
        spriteBatch.getShader().setUniformf("screen_size", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
        meshShader.begin();
        meshShader.setUniformf("screen_size", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        meshShader.end();
    }

    public void beginSpriteBatch() {
        switch (state) {
            case NONE:
                setupAndStartSpriteBatch();
                break;
            case SPRITE_SHADER:
                break;
            case MESH_SHADER:
                meshShader.end();
                setupAndStartSpriteBatch();
                break;
        }
        state = ShaderState.SPRITE_SHADER;
    }

    private void setupAndStartSpriteBatch() {
        spriteBatch.setProjectionMatrix(
                projectionState == ProjectionState.NORMAL ?
                        normalProjectionMatrix : tempProjectionMatrix);
        spriteBatch.begin();
    }

    private void setupAndStartMeshShader() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        meshShader.begin();
        meshShader.setUniformMatrix("u_projTrans", getActualProjectionMatrix());

    }

    private Matrix4 getActualProjectionMatrix() {
        return projectionState == ProjectionState.NORMAL ?
                normalProjectionMatrix : tempProjectionMatrix;
    }

    public void beginMeshShader() {
        switch (state) {
            case NONE:
                setupAndStartMeshShader();
                break;
            case SPRITE_SHADER:
                spriteBatch.end();
                setupAndStartMeshShader();
                break;
            case MESH_SHADER:
                break;
        }
        state = ShaderState.MESH_SHADER;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public ShaderProgram getMeshShader() {
        return meshShader;
    }

    public Matrix4 getNormalProjectionMatrix() {
        return normalProjectionMatrix;
    }

    public void setNormalProjectionMatrix(Matrix4 tempProjectionMatrix) {
        this.normalProjectionMatrix = tempProjectionMatrix;
        normalProjectionHasChanged = true;
    }

    public void setTempProjectionMatrix(Matrix4 tempProjectionMatrix) {
        this.tempProjectionMatrix = tempProjectionMatrix;
        tempProjectionHasChanged = true;
    }

    public Matrix4 getTempProjectionMatrix() {
        return tempProjectionMatrix;
    }

    public void useNormalProjectionMatrix() {
        if (projectionState == ProjectionState.NORMAL) {
            if (normalProjectionHasChanged) {
                normalProjectionHasChanged = false;
                refreshProjectionMatrix(normalProjectionMatrix);
            }
        } else {
            refreshProjectionMatrix(normalProjectionMatrix);
        }

        projectionState = ProjectionState.NORMAL;
    }

    public void useTempProjectionMatrix() {
        if (projectionState == ProjectionState.TEMP) {
            if (tempProjectionHasChanged) {
                tempProjectionHasChanged = false;
                refreshProjectionMatrix(tempProjectionMatrix);
            }
        } else {
            refreshProjectionMatrix(tempProjectionMatrix);
        }

        projectionState = ProjectionState.TEMP;
    }

    private void refreshProjectionMatrix(Matrix4 projectionMatrix) {
        switch (state) {
            case NONE:
                break;
            case SPRITE_SHADER:
                spriteBatch.setProjectionMatrix(projectionMatrix);
                break;
            case MESH_SHADER:
                meshShader.setUniformMatrix("u_projTrans", projectionMatrix);
                break;
        }
    }

    public void end() {
        switch (state) {
            case NONE:
                break;
            case SPRITE_SHADER:
                spriteBatch.end();
                break;
            case MESH_SHADER:
                meshShader.end();
                break;
        }

        state = ShaderState.NONE;
    }

    public void setWorldToScreenBounds(Vector2 minWorldToScreenPosition, Vector2 maxWorldToScreenPosition) {
        this.minWorldToScreenPosition = minWorldToScreenPosition;
        this.maxWorldToScreenPosition = maxWorldToScreenPosition;
    }

    public Vector2 getMinWorldToScreenPosition() {
        return minWorldToScreenPosition;
    }

    public Vector2 getMaxWorldToScreenPosition() {
        return maxWorldToScreenPosition;
    }

    public void dispose() {
        spriteBatch.dispose();
        meshShader.dispose();
    }

    enum ShaderState {
        NONE, SPRITE_SHADER, MESH_SHADER
    }

    enum ProjectionState {
        NORMAL, TEMP
    }
}
