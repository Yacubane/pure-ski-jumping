package pl.cyfrogen.skijumping.data;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorldCupSetup {
    private List<HillSetup> hillSetupList = new ArrayList<HillSetup>();
    public WorldCupSetup() {

    }

    public static WorldCupSetup fromJson(JsonNode jsonNode) {
        WorldCupSetup worldCupSetup = new WorldCupSetup();
        for (Iterator<JsonNode> it = jsonNode.get("hills").iterator(); it.hasNext(); ) {
            JsonNode hillNode = it.next();

            worldCupSetup.hillSetupList.add(HillSetup.fromJson(hillNode));
        }
        return worldCupSetup;
    }

    public List<HillSetup> getHillSetups() {
        return hillSetupList;
    }

    public JsonNode toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        ArrayNode hillsNode = objectMapper.createArrayNode();

        for(HillSetup hillSetup : hillSetupList) {
            hillsNode.add(hillSetup.toJson());
        }

        rootNode.set("hills", hillsNode);
        return rootNode;
    }
}
