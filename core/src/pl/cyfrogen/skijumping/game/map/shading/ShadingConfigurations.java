package pl.cyfrogen.skijumping.game.map.shading;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ShadingConfigurations {
    private Map<String, Shading> shadings = new HashMap<String, Shading>();

    public ShadingConfigurations(JsonNode shadingsNode) {
        shadings.put("default", Shading.DEFAULT);
        for (Iterator<Map.Entry<String, JsonNode>> it = shadingsNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> node = it.next();
            shadings.put(node.getKey(), Shading.of(node.getValue()));
        }
    }

    public Shading getShaderConfiguration(String shading) {
        return shadings.get(shading);
    }

    public void setupEditor() {
        for (Map.Entry<String, Shading> entry : shadings.entrySet()) {
            entry.getValue().setupEditor(entry.getKey());
        }
    }

}
