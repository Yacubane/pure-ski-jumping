package pl.cyfrogen.skijumping.jumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class JumperOutfitTextures {
    private final TextureAtlas.AtlasRegion boot;
    private final TextureAtlas.AtlasRegion glove;
    private final TextureAtlas.AtlasRegion white;
    private final TextureAtlas.AtlasRegion helmet;
    private final TextureAtlas.AtlasRegion face;
    private final TextureAtlas.AtlasRegion goggle;
    private final TextureAtlas.AtlasRegion goggleBand;
    private final TextureAtlas.AtlasRegion helmetBand;
    private final TextureAtlas.AtlasRegion bootStripes;

    TextureAtlas atlas;

    public JumperOutfitTextures() {
        atlas = new TextureAtlas(Gdx.files.internal("textures/jumper/textures.atlas"));
        boot = atlas.findRegion("boot");
        glove = atlas.findRegion("glove");
        white = atlas.findRegion("white_dot");
        helmet = atlas.findRegion("helmet");
        face = atlas.findRegion("face");
        goggle = atlas.findRegion("goggle");
        goggleBand = atlas.findRegion("google_band");
        helmetBand = atlas.findRegion("helmet_band");
        bootStripes = atlas.findRegion("boot_stripes");


        float newU = MathUtils.lerp(white.getU(), white.getU2(), 0.25f);
        float newU2 = MathUtils.lerp(white.getU(), white.getU2(), 0.75f);
        float newV = MathUtils.lerp(white.getV(), white.getV2(), 0.25f);
        float newV2 = MathUtils.lerp(white.getV(), white.getV2(), 0.75f);
        white.setRegion(newU, newV, newU2, newV2);


    }

    public TextureAtlas.AtlasRegion getBootTexture() {
        return boot;
    }

    public TextureAtlas.AtlasRegion getGloveTexture() {
        return glove;
    }

    public TextureRegion getWhiteDot() {
        return white;
    }

    public float getShirtColor() {
        return Color.valueOf("#f5f6fa").toFloatBits();
    }

    public TextureAtlas.AtlasRegion getHelmetTexture() {
        return helmet;
    }

    public TextureAtlas.AtlasRegion getFaceTexture() {
        return face;
    }

    public TextureAtlas.AtlasRegion getGoggleTexture() {
        return goggle;
    }

    public TextureAtlas.AtlasRegion getGoggleBandTexture() {
        return goggleBand;
    }

    public TextureAtlas.AtlasRegion getHelmetBandTexture() {
        return helmetBand;
    }

    public TextureAtlas.AtlasRegion getBootStripes() {
        return bootStripes;
    }

    public void dispose() {
        atlas.dispose();
    }
}
