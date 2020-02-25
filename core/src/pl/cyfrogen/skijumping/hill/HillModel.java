package pl.cyfrogen.skijumping.hill;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;

public class HillModel {
    private final float hillSize;
    private final float constructionPoint;
    private final ArrayList<Vector2> inRunVertices;
    private final ArrayList<Vector2> outRunVertices;
    private final float hillSizePathLength;
    private final float constructionPointPathLength;
    private final float inRunPathLength;
    private final float outRunPathLength;
    private final Vector2 startBarAreaStart;
    private final Vector2 startBarAreaEnd;

    public HillModel(JsonNode hillInfoNode, JsonNode hillData) {
        hillSize = hillInfoNode.get("hillSize").floatValue();
        constructionPoint = hillInfoNode.get("constructionPoint").floatValue();

        JsonNode inRunVerticesJson = hillData.get("inRunVertices");
        inRunVertices = new ArrayList<Vector2>();

        for (Iterator<JsonNode> it = inRunVerticesJson.iterator(); it.hasNext(); ) {
            JsonNode vertex = it.next();
            Iterator<JsonNode> coords = vertex.elements();
            inRunVertices.add(new Vector2(
                    coords.next().floatValue(),
                    -coords.next().floatValue()));
        }

        Vector2 vector0 = inRunVertices.get(0).cpy()
                .add(inRunVertices.get(0).cpy().sub(inRunVertices.get(1))
                        .nor().scl(1));
        //physic on edge. inRunVertices[0] is often point of highest starting gate, lets add some physic to the left
        inRunVertices.add(0, vector0);
        //inRunVertices.get(0).set(vector0);

        JsonNode outRunVerticesJson = hillData.get("outRunVertices");
        outRunVertices = new ArrayList<Vector2>();

        for (Iterator<JsonNode> it = outRunVerticesJson.iterator(); it.hasNext(); ) {
            JsonNode vertex = it.next();
            Iterator<JsonNode> coords = vertex.elements();
            outRunVertices.add(new Vector2(
                    coords.next().floatValue(),
                    -coords.next().floatValue()));
        }

        hillSizePathLength = hillData.get("sizes").get("hillSizePathLength").floatValue();
        constructionPointPathLength = hillData.get("sizes").get("constructionPointPathLength").floatValue();
        inRunPathLength = hillData.get("sizes").get("inRunPathLength").floatValue();
        outRunPathLength = hillData.get("sizes").get("outRunPathLength").floatValue();


        JsonNode startGatesAreaJson = hillData.get("startGatesArea");

        Iterator<JsonNode> start = startGatesAreaJson.elements();
        Iterator<JsonNode> coords1 = start.next().elements();
        startBarAreaStart = new Vector2(coords1.next().floatValue(), -coords1.next().floatValue());
        Iterator<JsonNode> coords2 = start.next().elements();
        startBarAreaEnd = new Vector2(coords2.next().floatValue(), -coords2.next().floatValue());
    }

    public ArrayList<Vector2> getInRunVertices() {
        return inRunVertices;
    }

    public ArrayList<Vector2> getOutRunVertices() {
        return outRunVertices;
    }

    public Vector2 getStartBarAreaStartVertex() {
        return startBarAreaStart;
    }

    public Vector2 getStartBarAreaEndVertex() {
        return startBarAreaEnd;
    }

    public float getConstructionPointPathLength() {
        return constructionPointPathLength;
    }

    public float getHillSizePathLength() {
        return hillSizePathLength;
    }
}
