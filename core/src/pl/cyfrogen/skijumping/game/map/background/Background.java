package pl.cyfrogen.skijumping.game.map.background;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import pl.cyfrogen.skijumping.game.GameWorld;
import pl.cyfrogen.skijumping.hill.HillGameObject;

public class Background {
    private final List<SkyColorLine> skyColorLines;
    private final Vector2 viewpoint;
    private final float width;
    private final float height;
    private Mesh mesh;
    private final Texture whiteTexture;
    private int vertIterator;

    public Background(float width, float height, Vector2 viewpoint, Texture whiteTexture, List<SkyColorLine> skyColorLines) {
        this.viewpoint = viewpoint;
        this.width = width;
        this.height = height;
        //setupEditor(topColor, centerColor, bottomColor);
        this.skyColorLines = skyColorLines;
        this.whiteTexture = whiteTexture;

        refresh();
    }

    private void refresh() {
        float scale = 1.5f;
        float width = this.width * scale;
        float height = this.height * scale;
        float minIn = 0.0f - (scale - 1) / 2f;
        float maxIn = 1.0f + (scale - 1) / 2f;
        float x = viewpoint.x - width / 2f;
        float y = viewpoint.y - height / 2f;

        vertIterator = 0;
        int quads = skyColorLines.size() - 1;
        int triangles = quads * 2;
        int vertsSize = triangles * 3 * 3;

        float[] verts = new float[vertsSize];

        mesh = new Mesh(true, vertsSize, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));

        for (int i = 1; i < skyColorLines.size(); i++) {
            SkyColorLine skyColorLine1 = skyColorLines.get(i - 1);
            SkyColorLine skyColorLine2 = skyColorLines.get(i);

            addVertex(verts, x, viewpoint.y + GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM / 2f * skyColorLine1.getHeightPercentage(), skyColorLine1.getColorFloat(), 0, 0);
            addVertex(verts, x + width, viewpoint.y + GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM / 2f * skyColorLine1.getHeightPercentage(), skyColorLine1.getColorFloat(), 0, 0);
            addVertex(verts, x, viewpoint.y + GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM / 2f * skyColorLine2.getHeightPercentage(), skyColorLine2.getColorFloat(), 0, 0);

            addVertex(verts, x, viewpoint.y + GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM / 2f * skyColorLine2.getHeightPercentage(), skyColorLine2.getColorFloat(), 0, 0);
            addVertex(verts, x + width, viewpoint.y + GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM / 2f * skyColorLine1.getHeightPercentage(), skyColorLine1.getColorFloat(), 0, 0);
            addVertex(verts, x + width, viewpoint.y + GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM / 2f * skyColorLine2.getHeightPercentage(), skyColorLine2.getColorFloat(), 0, 0);
        }

        mesh.setVertices(verts);

    }

//    private void setupEditor(Color topColor, Color centerColor, Color bottomColor) {
//        Main.getInstance().getEditor().addColorChanged("BACKGROUND_TOP", topColor.toString(), new OnColorChanged() {
//
//            @Override
//            public void colorChanged(String hex) {
//                Background.this.topColor = (Color.valueOf(hex).toFloatBits());
//                Gdx.app.postRunnable(new Runnable() {
//                    @Override
//                    public void run() {
//                        refresh();
//                    }
//                });
//            }
//        });
//        Main.getInstance().getEditor().addColorChanged("BACKGROUND_CENTER", centerColor.toString(), new OnColorChanged() {
//
//            @Override
//            public void colorChanged(String hex) {
//                Background.this.centerColor = (Color.valueOf(hex).toFloatBits());
//                Gdx.app.postRunnable(new Runnable() {
//                    @Override
//                    public void run() {
//                        refresh();
//                    }
//                });
//            }
//        });
//        Main.getInstance().getEditor().addColorChanged("BACKGROUND_BOTTOM", bottomColor.toString(), new OnColorChanged() {
//
//            @Override
//            public void colorChanged(String hex) {
//                Background.this.bottomColor = (Color.valueOf(hex).toFloatBits());
//                Gdx.app.postRunnable(new Runnable() {
//                    @Override
//                    public void run() {
//                        refresh();
//                    }
//                });
//            }
//        });
//    }

    public void draw(ShaderProgram shaderProgram) {
        mesh.render(shaderProgram, GL20.GL_TRIANGLES);
    }

    public void addVertex(float[] verts, float x, float y, float color, float u, float v) {
        verts[vertIterator++] = x;
        verts[vertIterator++] = y;
        verts[vertIterator++] = color;
    }

    public void dispose() {
        mesh.dispose();
    }
}
