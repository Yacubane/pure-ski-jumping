package pl.cyfrogen.skijumping.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class HillSetup {
    private Snowing snowing = Snowing.NO;
    private int gate;
    private float defaultMinWind;
    private float defaultMaxWind;

    public Snowing getSnowing() {
        return snowing;
    }

    public int getStartGate() {
        return gate;
    }

    public void setStartGate(int i) {
        this.gate = i;
    }

    public float getMinWind() {
        return defaultMinWind;
    }

    public float getMaxWind() {
        return defaultMaxWind;
    }

    public void setMinWind(float i) {
        this.defaultMinWind = i;
    }

    public void setMaxWind(float i) {
        this.defaultMaxWind = i;
    }

    public enum Snowing {
        NO, SOFT, HARD;
    }

    public enum Mode {
        DAY, MORNING, NIGHT;

    }

    public static Mode createWeather(String string) {
        return Mode.valueOf(string);
    }

    public static Snowing createSnowing(String string) {
        return Snowing.valueOf(string);
    }

    private final HillLocation hillLocation;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    private Mode mode = Mode.MORNING;

    public HillSetup(HillLocation hillLocation, Mode mode, Snowing snowing, int gate, float defaultMinWind, float defaultMaxWind) {
        this.hillLocation = hillLocation;
        this.mode = mode;
        this.snowing = snowing;
        this.gate = gate;
        this.defaultMinWind = defaultMinWind;
        this.defaultMaxWind = defaultMaxWind;
    }


    public HillLocation getHillLocation() {
        return hillLocation;
    }

    public static HillSetup fromJson(JsonNode hillNode) {
        Mode mode = createWeather(hillNode.get("mode").asText());
        Snowing snowing = createSnowing(hillNode.get("snowing").asText());

        HillSetup hillSetup = new HillSetup(HillLocation.fromJson(hillNode.get("hillLocation")),
                mode, snowing,
                hillNode.get("gate").intValue(),
                hillNode.get("minWind").floatValue(),
                hillNode.get("maxWind").floatValue());
        return hillSetup;
    }

    public void setSnowing(Snowing snowing) {
        this.snowing = snowing;
    }

    public JsonNode toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.set("hillLocation", hillLocation.toJson());
        objectNode.put("mode", mode.toString());
        objectNode.put("snowing", snowing.toString());
        objectNode.put("gate", gate);
        objectNode.put("minWind", getMinWind());
        objectNode.put("maxWind", getMaxWind());

        return objectNode;
    }
}
