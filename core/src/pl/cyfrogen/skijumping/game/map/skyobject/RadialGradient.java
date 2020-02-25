package pl.cyfrogen.skijumping.game.map.skyobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

public class RadialGradient extends SkyObject implements MeshObject {
    private final Mesh mesh;

    private int vertIterator;

    public RadialGradient(int trianglesNum, Vector2 center, float radius, PercentageColors percentageColors, BlendingType blendingType, Color ambientColor) {
        super(blendingType, ambientColor);

        int vertsSize = trianglesNum * 3 * 3;

        float[] verts = new float[vertsSize];


        mesh = new Mesh(true, vertsSize, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));


        for (int i = 0; i < trianglesNum; i++) {

            int nextI = i + 1;
            if (nextI == trianglesNum) nextI = 0;

            float percentage1 = i / (float) trianglesNum;
            float percentage2 = nextI / (float) trianglesNum;


            double angle1 = 2 * Math.PI * percentage1;
            double angle2 = 2 * Math.PI * percentage2;

            float x1 = (float) (center.x + Math.cos(angle1) * radius);
            float y1 = (float) (center.y + Math.sin(angle1) * radius);

            float x2 = (float) (center.x + Math.cos(angle2) * radius);
            float y2 = (float) (center.y + Math.sin(angle2) * radius);

            addVertex(verts, center.x, center.y, percentageColors.getColors().get(0).getColor().toFloatBits(), 0, 0);
            addVertex(verts, x1, y1, percentageColors.getColors().get(1).getColor().toFloatBits(), 0, 0);
            addVertex(verts, x2, y2, percentageColors.getColors().get(1).getColor().toFloatBits(), 0, 0);




        }

        mesh.setVertices(verts);

    }


    public void draw(ShaderProgram shaderProgram) {
        mesh.render(shaderProgram, GL20.GL_TRIANGLES);
    }

    public void addVertex(float[] verts, float x, float y, float color, float u, float v) {
        verts[vertIterator++] = x;
        verts[vertIterator++] = y;
        verts[vertIterator++] = color;
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }
}
