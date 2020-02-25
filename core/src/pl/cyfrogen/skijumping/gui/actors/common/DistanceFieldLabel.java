package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.cyfrogen.skijumping.Main;

public class DistanceFieldLabel extends Label {
    private static final float DISTANCE_FIELD_FONT_SPREAD = 4f;

    private float fontScale;
    private ShaderProgram customShader;

    public DistanceFieldLabel(CharSequence text, LabelStyle style) {
        super(text, style);
        init();
    }

    void init() {
        customShader = Main.getInstance().getAssets().getDistanceFieldShader();
        this.fontScale = getStyle().font.getData().scaleY;

    }

    @Override
    public void layout() {
        if (fontScale != 0)
            getStyle().font.getData().setScale(fontScale);
        super.layout();
    }

    public static float getDefaultSmoothingFactor(float fontScale) {
        return DISTANCE_FIELD_FONT_SPREAD * fontScale;
    }

    @Override
    public float getPrefWidth() {
        if (fontScale != 0)
            getStyle().font.getData().setScale(fontScale);
        return super.getPrefWidth();
    }

    @Override
    public float getPrefHeight() {
        if (fontScale != 0)
            getStyle().font.getData().setScale(fontScale);
        return super.getPrefHeight();

    }

    @Override
    public void draw(Batch batch, float alpha) {
        getStyle().font.getData().setScale(fontScale);
        if (customShader != null) {
            ShaderProgram previousShader = batch.getShader();
            batch.setShader(customShader);
            batch.getShader().setUniformf("u_smoothing", getDefaultSmoothingFactor(fontScale));
            super.draw(batch, alpha);
            batch.setShader(previousShader);
        } else {
            super.draw(batch, alpha);
        }
    }

    @Override
    public void setText(CharSequence text) {
        getStyle().font.getData().setScale(fontScale);
        super.setText(text);
    }

    public void fontScaleUpdated() {
        this.fontScale = getStyle().font.getData().scaleY;
        validate();
    }

    @Override
    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
    }

    public void setFontColor(Color fontColor) {
        setStyle(new LabelStyle(getStyle()));
        getStyle().fontColor = fontColor;
    }
}
