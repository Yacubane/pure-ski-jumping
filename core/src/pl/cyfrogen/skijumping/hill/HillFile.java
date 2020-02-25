package pl.cyfrogen.skijumping.hill;

import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import pl.cyfrogen.skijumping.data.HillLocation;
import pl.cyfrogen.skijumping.data.HillSetup;

public class HillFile {
    private final JsonNode hillMeta;
    private final FileHandle root;
    private final ObjectMapper objectMapper;

    public HillFile(HillLocation hillLocation) throws IOException {
        objectMapper = new ObjectMapper();

        root = hillLocation.getFileHandle();
        FileHandle manifest = root.child("manifest.json");
        JsonNode manifestNode = objectMapper.readTree
                (manifest.readString());


        hillMeta = objectMapper.readTree(
                root.child(manifestNode.get("data").textValue()).readString());
    }

    public JsonNode getMetaData() {
        return hillMeta;
    }

    public JsonNode openModeData(HillSetup.Mode mode) throws IOException {
        String realMode = mode.toString().toUpperCase();

        FileHandle modeFile = root.child(
                hillMeta.get("modes").get(realMode).get("modeData").textValue());

        JsonNode modeNode = objectMapper.readTree(modeFile.readString());
        return modeNode;
    }


    public JsonNode openHillModel(HillSetup.Mode mode) throws IOException {
        JsonNode rootNode = objectMapper.readTree(getHillModelData(mode).readString());
        return rootNode;
    }

    public FileHandle getHillModelData(HillSetup.Mode mode) {
        String realMode = mode.toString().toUpperCase();

        return root.child(hillMeta.get("modes").get(realMode).get("hillModelData").textValue());
    }

    public FileHandle getFile(String path) {
        return root.child(path);
    }
}
