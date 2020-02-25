package pl.cyfrogen.skijumping.gui.actors.tutorial.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TutorialAssets {
    private final TextureRegion phoneTexture;
    private final TextureRegion thumbTexture;

    public TutorialAssets(TextureRegion phoneTexture, TextureRegion thumbTexture) {

        this.phoneTexture = phoneTexture;
        this.thumbTexture = thumbTexture;
    }

    public TextureRegion getPhoneTexture() {
        return phoneTexture;
    }

    public TextureRegion getThumbTexture() {
        return thumbTexture;
    }
}
