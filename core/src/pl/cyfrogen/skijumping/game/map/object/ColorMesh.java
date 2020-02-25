package pl.cyfrogen.skijumping.game.map.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.List;

import pl.cyfrogen.skijumping.game.map.util.MeshData;

public class ColorMesh implements GameObject {

    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final float[] verts;
    private final short[] indices;
    private Mesh mesh;

    public ColorMesh(List<MeshData> meshDatas, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;


        ShortArray[] trianglesArray = new ShortArray[meshDatas.size()];
        float[][] meshPointsArray = new float[meshDatas.size()][];
        int meshPointsSize = 0;

        int trianglesCount = 0;
        for (int i = 0; i < meshDatas.size(); i++) {
            MeshData meshData = meshDatas.get(i);
            meshPointsArray[i] = meshData.getPointsInFloatArray();
            meshPointsSize += meshPointsArray[i].length;
            trianglesArray[i] = new EarClippingTriangulator().computeTriangles(meshPointsArray[i]);
            trianglesCount += (trianglesArray[i].size / 3);
        }


        float[] verts = new float[(meshPointsSize / 2) * 3];
        short[] indicies = new short[trianglesCount * 3];

        mesh = new Mesh(true, verts.length, indicies.length,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));


        int k = 0;
        int l = 0;
        int actualPointsCount = 0;
        for (int i = 0; i < meshDatas.size(); i++) {
            MeshData meshData = meshDatas.get(i);
            ShortArray triangles = trianglesArray[i];

            for (int j = 0; j < triangles.size; j++) {
                indicies[l++] = (short) (triangles.get(j) + actualPointsCount);
            }

            for (Vector2 vector2 : meshData.getPoints()) {
                verts[k++] = vector2.x;
                verts[k++] = vector2.y;
                verts[k++] = meshData.getColor();
                actualPointsCount++;
            }
        }

        this.verts = verts;
        this.indices = indicies;

        mesh.setVertices(verts);
        mesh.setIndices(indicies);
    }

    public ColorMesh(float x, float y, float width, float height, float[] verts, short[] indices) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.verts = verts;
        this.indices = indices;

        mesh = new Mesh(true, verts.length, indices.length,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));

        mesh.setVertices(verts);
        mesh.setIndices(indices);
    }

    public static ColorMesh of(JsonNode shape) {
        float boundingBoxX = shape.get("boundingBoxX").floatValue();
        float boundingBoxY = shape.get("boundingBoxY").floatValue();
        float boundingBoxWidth = shape.get("boundingBoxWidth").floatValue();
        float boundingBoxHeight = shape.get("boundingBoxHeight").floatValue();
        boundingBoxY = -boundingBoxY - boundingBoxHeight;


        JsonNode verticesNode = shape.get("vertices");

        int verticesNum = 0;
        for (Iterator<JsonNode> it = verticesNode.iterator(); it.hasNext(); ) {
            it.next();
            verticesNum++;
        }

        float[] vertices = new float[verticesNum * 3];
        int i = 0;
        for (Iterator<JsonNode> it = verticesNode.iterator(); it.hasNext(); ) {
            JsonNode vertice = it.next();
            Iterator<JsonNode> verticeParts = vertice.elements();
            vertices[i++] = verticeParts.next().floatValue();
            vertices[i++] = -verticeParts.next().floatValue();
            vertices[i++] = Color.valueOf(verticeParts.next().asText()).toFloatBits();
        }


        JsonNode indiciesNode = shape.get("indicies");

        int indiciesNum = 0;
        for (Iterator<JsonNode> it = indiciesNode.iterator(); it.hasNext(); ) {
            it.next();
            indiciesNum++;
        }

        short[] indicies = new short[indiciesNum * 3];
        i = 0;
        for (Iterator<JsonNode> it = indiciesNode.iterator(); it.hasNext(); ) {
            JsonNode indice = it.next();
            Iterator<JsonNode> indiceParts = indice.elements();
            indicies[i++] = indiceParts.next().shortValue();
            indicies[i++] = indiceParts.next().shortValue();
            indicies[i++] = indiceParts.next().shortValue();
        }

        return new ColorMesh(boundingBoxX, boundingBoxY, boundingBoxWidth, boundingBoxHeight, vertices, indicies);
    }

    public void draw(ShaderProgram program) {
        if (mesh != null)
            mesh.render(program, GL20.GL_TRIANGLES);
    }

    public boolean checkInBounds(Vector2 min, Vector2 max) {
        if (x > max.x) return false;
        if (y > max.y) return false;
        if (x + width < min.x) return false;
        if (y + height < min.y) return false;
        return true;
    }

    public void dispose() {
        mesh.dispose();
    }
}
