package pl.cyfrogen.skijumping.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.cyfrogen.skijumping.hill.HillFile;
import pl.cyfrogen.skijumping.hill.Hills;

public class SaveData {
    private ArrayList<JumperData> jumpers;
    private WorldCupSetup worldCupSetup;

    public SaveData() {
        worldCupSetup = new WorldCupSetup();
        try {
            FileHandle fileHandle = Gdx.files.local("worldcup_jumpers.json");
            jumpers = new Json().fromJson(ArrayList.class, fileHandle.readString());
        } catch (Exception e) {
            e.printStackTrace();
            jumpers = new ArrayList<JumperData>();
            jumpers.add(new JumperData("PLAYER"));
            jumpers.get(0).setActiveInWorldCup(true);
            saveJumpers();
        }

        FileHandle hillsJsonFile = Gdx.files.local("worldcup_hills.json");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(hillsJsonFile.readString());
            worldCupSetup = WorldCupSetup.fromJson(rootNode);

        } catch (Exception e) {
            e.printStackTrace();
            worldCupSetup = new WorldCupSetup();
            try {
                HillFile hillFile = new HillFile(Hills.ZAKOPANE_HS140v1);
                int gate = hillFile.getMetaData().get("defaultStartGate").intValue();
                float defaultMinWind = (hillFile.getMetaData().get("physics").get("defaultMinWind").floatValue());
                float defaultMaxWind = (hillFile.getMetaData().get("physics").get("defaultMaxWind").floatValue());

                worldCupSetup.getHillSetups().add(new HillSetup(Hills.ZAKOPANE_HS140v1, HillSetup.Mode.MORNING, HillSetup.Snowing.SOFT, gate, defaultMinWind, defaultMaxWind));
                saveWorldCupSetup();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }


    }

    public void saveJumpers() {
        FileHandle fileHandle = Gdx.files.local("worldcup_jumpers.json");
        fileHandle.writeString(new Json().toJson(getJumpers()), false);

    }

    public List<JumperData> getJumpers() {
        return jumpers;
    }

    public WorldCupSetup getWorldCupSetup() {
        return worldCupSetup;
    }

    public void saveWorldCupSetup() {
        FileHandle hillsJsonFile = Gdx.files.local("worldcup_hills.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            hillsJsonFile.writeString(
                    objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(worldCupSetup.toJson()),
                    false);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
