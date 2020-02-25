package pl.cyfrogen.skijumping.game.map.background;

import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.databind.JsonNode;

public class SkyColorLine {
    private final Color color;
    private final float heightPercentage;

    public SkyColorLine(JsonNode jsonNode) {
        this.color= Color.valueOf(jsonNode.get("color").textValue());
        this.heightPercentage = jsonNode.get("heightPercentage").floatValue();
    }

    public float getColorFloat() {
        return color.toFloatBits();
    }

    public float getHeightPercentage() {
        return heightPercentage;
    }
}
