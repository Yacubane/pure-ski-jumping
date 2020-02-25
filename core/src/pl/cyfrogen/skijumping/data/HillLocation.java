package pl.cyfrogen.skijumping.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.zip.ZipFile;

import pl.cyfrogen.skijumping.zipfile.ZipFileHandle;

public class HillLocation {
    private String path;
    private boolean internal;

    public HillLocation(String path, boolean internal) {
        this.path = path;
        this.internal = internal;
    }

    public String getPath() {
        return path;
    }

    public boolean isInternal() {
        return internal;
    }

    public static HillLocation fromJson(JsonNode node) {
        return new HillLocation(
                node.get("path").textValue(),
                node.get("internal").booleanValue());
    }

    public JsonNode toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("path", path);
        objectNode.put("internal", internal);
        return objectNode;
    }

    public FileHandle getFileHandle() throws IOException {
        if(isInternal()) {
            return Gdx.files.internal("hills/" + path);
        } else {
            return new ZipFileHandle(new ZipFile(Gdx.files.internal("hills/" + path).file()), "");
        }
    }
}
