package pl.cyfrogen.skijumping.character;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;


public class UVMapper {

    /**
     * @param textureToMap texture that will be mapped
     * @param color        color that will be assigned to mesh
     * @param startColumn  start column of 3D mesh vertices
     * @param endColumn    end column of 3D mesh vertices
     * @param step         step between points (i.e. if set to 2, points 0, 2, 4... are taken)
     * @param meshPoints   3D array of mesh vertices [Part][Column][Points]
     * @return 2D array of mesh vertices [Column][Points] UV mapped
     */
    public MeshVertex[][] map(TextureRegion textureToMap, float color,
                              int startColumn, int endColumn, int step, MeshVertex[][]... meshPoints) {
        int size = 0;
        for (int i = 0; i < meshPoints.length; i++) {
            size += meshPoints[i][0].length;
        }

        float minU = 1f;
        float maxU = 0f;
        float minV = 1f;
        float maxV = 0f;

        for (int i = 0; i < meshPoints.length; i++) {
            for (int j = startColumn; j <= endColumn; j += step) {
                for (int k = 0; k < meshPoints[i][j].length; k++) {
                    MeshVertex meshPoint = meshPoints[i][j][k];
                    minU = Math.min(minU, meshPoint.getU());
                    minV = Math.min(minV, meshPoint.getV());
                    maxU = Math.max(maxU, meshPoint.getU());
                    maxV = Math.max(maxV, meshPoint.getV());
                }
            }
        }

        MeshVertex[][] result = new MeshVertex[(endColumn - startColumn) / step + 1][size];

        int column = 0;
        for (int j = startColumn; j <= endColumn; j += step) {
            int iterator = 0;
            for (int i = 0; i < meshPoints.length; i++) {
                for (int k = 0; k < meshPoints[i][j].length; k++) {
                    MeshVertex meshPoint = meshPoints[i][j][k];
                    result[column][iterator] = new MeshVertex(meshPoints[i][j][k]);
                    float percentageX = (meshPoint.getU() - minU) / (maxU - minU);
                    float percentageY = (meshPoint.getV() - minV) / (maxV - minV);

                    result[column][iterator].setU(MathUtils.lerp(textureToMap.getU(), textureToMap.getU2(), percentageX));
                    result[column][iterator].setV(MathUtils.lerp(textureToMap.getV(), textureToMap.getV2(), percentageY));
                    result[column][iterator].setColor(color);

                    iterator++;

                }
            }
            column++;
        }

        return result;

    }
}
