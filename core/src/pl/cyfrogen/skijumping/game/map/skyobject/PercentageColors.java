package pl.cyfrogen.skijumping.game.map.skyobject;

import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class PercentageColors {
    List<PercentageColor> percentageColorList;
    public PercentageColors() {
        percentageColorList = new ArrayList<PercentageColor>();
    }

    public static PercentageColors fromJson(JsonNode colorsNode) {
        PercentageColors percentageColors = new PercentageColors();
        for(JsonNode colorNode : colorsNode) {
            percentageColors.addColor(new PercentageColor(
                    Color.valueOf(colorNode.get("color").textValue()),
                    colorNode.get("percentage").floatValue()
            ));
        }
        return percentageColors;
    }

    private void addColor(PercentageColor percentageColor) {
        percentageColorList.add(percentageColor);
    }

    public List<PercentageColor> getColors() {
        return percentageColorList;
    }
}
