package pl.cyfrogen.skijumping.character;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

public class CurvePath {
    Vector2 points[];

    public CurvePath(Vector2... points) {
        this.points = points;
    }

    /**
     * Generates mesh vertices from Bezier Curve made with points
     *
     * @param numberOfPoints number of points in final curve
     * @return array of MeshVertex
     */
    public MeshVertex[] generateMeshPoints(int numberOfPoints) {
        int curves = (points.length - 1) / 3;
        MeshVertex[] meshVertices = new MeshVertex[numberOfPoints * curves];
        Vector2 tmp = new Vector2();
        Vector2 out = new Vector2();
        for (int j = 0; j < curves; j++) {
            for (int i = 0; i < numberOfPoints; i++) {
                float percentage = i / (float) (numberOfPoints - 1);
                Bezier.cubic(out, percentage, points[j * 3], points[1 + j * 3], points[2 + j * 3], points[3 + j * 3], tmp);
                meshVertices[i + numberOfPoints * j] = new MeshVertex(out.cpy());
            }
        }

        return meshVertices;
    }
}
