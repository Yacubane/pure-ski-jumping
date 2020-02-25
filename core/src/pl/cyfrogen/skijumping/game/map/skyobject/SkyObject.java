package pl.cyfrogen.skijumping.game.map.skyobject;

import com.badlogic.gdx.graphics.Color;

public abstract class SkyObject {
    private final BlendingType blendingType;
    private final Color ambientColor;

    public SkyObject(BlendingType blendingType, Color ambientColor) {
        this.blendingType = blendingType;
        this.ambientColor = ambientColor;
    }

    public BlendingType getBlendingType() {
        return blendingType;
    }

    public Color getAmbientColor() {
        return ambientColor;
    }

    public abstract void dispose();
}
