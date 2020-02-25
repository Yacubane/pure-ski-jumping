package pl.cyfrogen.skijumping.debug;

import com.badlogic.gdx.graphics.Color;

public class DebugText {
    private final Color color;
    private String value;
    private final String id;

    public DebugText(String id, String value, Color color) {
        this.id=id;
        this.value=value;
        this.color = color;
    }

    public String getId() {
        return id;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }
}
