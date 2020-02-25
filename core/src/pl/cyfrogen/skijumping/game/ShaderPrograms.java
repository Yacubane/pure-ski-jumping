package pl.cyfrogen.skijumping.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ShaderPrograms {
    private final SpriteBatch spriteBatch;

    public ShaderPrograms() {
        spriteBatchShader = new ShaderProgram(
                Gdx.files.internal("shaders/game_sprite_vertex.glsl").readString(),
                Gdx.files.internal("shaders/game_sprite_fragment.glsl").readString()
        );
        if (!spriteBatchShader.isCompiled())
            throw new GdxRuntimeException("Couldn't generateMeshPoints shader: " + spriteBatchShader.getLog());

        spriteBatch = new SpriteBatch(1000, spriteBatchShader);

        meshShader = new ShaderProgram(
                Gdx.files.internal("shaders/game_mesh_vertex.glsl").readString(),
                Gdx.files.internal("shaders/game_mesh_fragment.glsl").readString()
        );
        if (!meshShader.isCompiled())
            throw new GdxRuntimeException("Couldn't generateMeshPoints shader: " + meshShader.getLog());
    }

    public ShaderProgram getMeshShader() {
        return meshShader;
    }

    private final ShaderProgram spriteBatchShader;
    private final ShaderProgram meshShader;

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
