package pl.cyfrogen.skijumping.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Takes 3D array of mesh vertices [BodyPart][Column][Points] and produces Mesh
 */
public class MeshBuilder {
    private final Mesh mesh;
    private final int vertsSize;
    private final MeshVertex[][][] meshVertices;
    private final float[] verts;
    private int vertIterator;
    float color = Color.toFloatBits(255, 255, 255, 255);

    public MeshBuilder(MeshVertex[][]... meshVertices) {
        this.meshVertices = meshVertices;
        int quads = 0;
        for (int k = 0; k < meshVertices.length; k++)
            quads += (meshVertices[k][0].length - 1) * (meshVertices[k].length - 1);
        int triangles = quads * 2;
        vertsSize = triangles * 5 * 3;

        verts = new float[vertsSize];

        mesh = new Mesh(true, vertsSize, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
        rebuild();
    }

    public void rebuild() {
        vertIterator = 0;
        for (int k = 0; k < meshVertices.length; k++) {
            for (int i = 1; i < meshVertices[k].length; i++) {
                for (int j = 1; j < meshVertices[k][i].length; j++) {
                    addQuad(verts, meshVertices[k][i - 1][j - 1],
                            meshVertices[k][i][j - 1],
                            meshVertices[k][i][j],
                            meshVertices[k][i - 1][j]);
                }
            }
        }
        mesh.setVertices(verts);
    }

    public void addVertex(float[] verts, MeshVertex point) {
        verts[vertIterator++] = point.getX();
        verts[vertIterator++] = point.getY();
        verts[vertIterator++] = point.getColor();
        verts[vertIterator++] = point.getU();   //U
        verts[vertIterator++] = point.getV();   //V
    }

    private void addQuad(float[] verts, MeshVertex topLeft, MeshVertex topRight, MeshVertex botRight, MeshVertex botLeft) {
        //Top left
        addVertex(verts, topLeft);

        //Top Right Vertex Triangle 1
        addVertex(verts, topRight);

        //Bottom Left Vertex Triangle 1
        addVertex(verts, botLeft);

        //Top Right Vertex Triangle 2
        addVertex(verts, topRight);

        //Bottom Right Vertex Triangle 2
        addVertex(verts, botRight);

        //Bottom Left Vertex Triangle 2
        addVertex(verts, botLeft);

    }

    public Mesh getMesh() {
        return mesh;
    }

    public void dispose() {
        mesh.dispose();
    }
}
