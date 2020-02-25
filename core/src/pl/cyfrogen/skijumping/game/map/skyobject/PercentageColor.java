package pl.cyfrogen.skijumping.game.map.skyobject;

import com.badlogic.gdx.graphics.Color;

class PercentageColor {
    private final Color color;
    private final float percentage;

    public PercentageColor(Color color, float percentage) {

        this.color = color;
        this.percentage = percentage;
    }

    public Color getColor() {
        return color;
    }

    public float getPercentage() {
        return percentage;
    }
}
