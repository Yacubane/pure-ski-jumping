package pl.cyfrogen.skijumping.game.physics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class EdgeGroup {
    List<Edge> edges;
    private Edge lastEdge;

    public EdgeGroup() {
        this.edges = new ArrayList<Edge>();
    }

    public void addEdge(Edge edge) {
        edge.setEdgeGroup(this);
        edges.add(edge);
        if (lastEdge != null) {
            lastEdge.withNextEdge(edge);
            edge.withPrevEdge(lastEdge);
        }
        lastEdge = edge;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void draw(ShapeRenderer sr) {
        for (Edge edge : edges) {
            sr.line(edge.getLine().getPoint1().toVec2(), edge.getLine().getPoint2().toVec2());
        }
    }
}
