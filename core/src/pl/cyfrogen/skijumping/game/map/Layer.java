package pl.cyfrogen.skijumping.game.map;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import pl.cyfrogen.skijumping.game.map.object.ColorMesh;
import pl.cyfrogen.skijumping.game.map.object.GameObject;
import pl.cyfrogen.skijumping.game.map.object.TextureObject;
import pl.cyfrogen.skijumping.game.map.shading.Shading;
import pl.cyfrogen.skijumping.game.renderer.GameRenderer;

public class Layer {

    private final String key;
    private Vector2 centerOfParalax;

    public float getParam() {
        return param;
    }

    public void setParam(float param) {
        this.param = param;
    }

    private float param;
    private Vector2 prevDiff;
    private Vector2 paralaxDiff = new Vector2();
    private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();

    Vector2 tmp = new Vector2();
    Vector2 tmp2 = new Vector2();
    private Shading shading = new Shading();

    public Layer(String key, Vector2 centerOfParalax, float param) {
        this.key = key;
        this.centerOfParalax = centerOfParalax;
        this.prevDiff = new Vector2();
        this.param = param;
    }

    public void setShaderConfig(Shading shading) {
        this.shading = shading;
    }


    public void addTextureObjects(TextureObject textureObject) {
        gameObjects.add(textureObject);
    }

    public void updateParalax(Vector3 newCamPosition) {
        Vector2 camPosVec2 = new Vector2(newCamPosition.x, newCamPosition.y);
        paralaxDiff = camPosVec2.sub(centerOfParalax).scl(param);
    }

    public void draw(GameRenderer renderer) {
        tmp.set(renderer.getMinWorldToScreenPosition()).sub(paralaxDiff);
        tmp2.set(renderer.getMaxWorldToScreenPosition()).sub(paralaxDiff);


        if (param != 0f) {
            renderer.setTempProjectionMatrix(
                    renderer.getTempProjectionMatrix()
                            .set(renderer.getNormalProjectionMatrix())
                            .translate(paralaxDiff.x, paralaxDiff.y, 0));
            renderer.useTempProjectionMatrix();
        } else {
            renderer.useNormalProjectionMatrix();
        }

        int renderingMode = -1;
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof ColorMesh) {
                ColorMesh colorMesh = (ColorMesh) gameObject;
                if (colorMesh.checkInBounds(tmp, tmp2)) {
                    if (renderingMode != 0) {
                        renderer.beginMeshShader();
                        setShaderUniforms(renderer.getMeshShader());
                        renderingMode = 0;
                    }
                    colorMesh.draw(renderer.getMeshShader());
                }

            } else if (gameObject instanceof TextureObject) {
                TextureObject textureObject = (TextureObject) gameObject;
                if (textureObject.checkInBounds(tmp, tmp2)) {
                    if (renderingMode != 1) {
                        renderer.beginSpriteBatch();
                        setShaderUniforms(renderer.getSpriteBatch().getShader());
                        renderingMode = 1;
                    }

                    textureObject.draw(renderer.getSpriteBatch());
                }
            }
        }

        if (renderingMode == 1) {
            renderer.getSpriteBatch().flush();
        }

    }

    public void setShaderUniforms(ShaderProgram shader) {
        shading.apply(shader);

    }


    public String getKey() {
        return key;
    }


    public void addColorMesh(List<ColorMesh> mesh) {
        for (ColorMesh renderableMesh : mesh) {
            gameObjects.add(renderableMesh);
        }
    }

    public void dispose() {
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof ColorMesh) {
                ((ColorMesh) gameObject).dispose();
            }
        }
    }
}
