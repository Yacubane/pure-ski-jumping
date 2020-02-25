package pl.cyfrogen.skijumping.data;

import com.badlogic.gdx.graphics.Color;

public class SettableColor {
    Color color = Color.WHITE;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
