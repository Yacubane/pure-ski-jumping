package pl.cyfrogen.skijumping.game.map.shading;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.JsonNode;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.editor.OnCheckboxChanged;
import pl.cyfrogen.skijumping.editor.OnColorChanged;
import pl.cyfrogen.skijumping.editor.OnSliderChanged;

public class Shading {
    public static final Shading DEFAULT = new Shading()
            .withAmbientColor(Color.valueOf("#FFFFFFFF"))
            .withFogEnabled(false)
            .withLightEnabled(false);

    public static Shading of(JsonNode json) {
        return new Shading()
                .withAmbientColor(Color.valueOf(json.get("ambientColor").textValue()))
                .withFogEnabled(json.get("fog").get("enabled").booleanValue())
                .withFogCenterColor(Color.valueOf(json.get("fog").get("centerColor").textValue()))
                .withFogCenterStrength(json.get("fog").get("centerStrength").floatValue())
                .withFogOutsideColor(Color.valueOf(json.get("fog").get("outsideColor").textValue()))
                .withFogOutsideStrength(json.get("fog").get("outsideStrength").floatValue());
    }

    public Color getFogCenterColor() {
        return fogCenterColor;
    }

    public Shading withFogCenterColor(Color fogCenterColor) {
        this.fogCenterColor = fogCenterColor;
        return this;
    }

    public Color getFogOutsideColor() {
        return fogOutsideColor;
    }

    public Shading withFogOutsideColor(Color fogOutsideColor) {
        this.fogOutsideColor = fogOutsideColor;
        return this;

    }

    public float getFogCenterStrength() {
        return fogCenterStrength;
    }

    public Shading withFogCenterStrength(float fogCenterStrength) {
        this.fogCenterStrength = fogCenterStrength;
        return this;

    }

    public float getFogOutsideStrength() {
        return fogOutsideStrength;
    }

    public Shading withFogOutsideStrength(float fogOutsideStrength) {
        this.fogOutsideStrength = fogOutsideStrength;
        return this;

    }

    public boolean isFogEnabled() {
        return fogEnabled;
    }

    public Shading withFogEnabled(boolean fogEnabled) {
        this.fogEnabled = fogEnabled;
        return this;

    }

    private Color ambientColor = new Color();
    private Color fogCenterColor = new Color();
    private Color fogOutsideColor = new Color();
    private float fogCenterStrength;
    private float fogOutsideStrength;
    private boolean fogEnabled;
    private boolean lightEnabled;
    private Vector2 lightPosition;
    private float lightStrength;

    public Shading() {

    }

    public boolean isLightEnabled() {
        return lightEnabled;
    }

    public Shading withLightEnabled(boolean lightEnabled) {
        this.lightEnabled = lightEnabled;
        return this;

    }

    public Vector2 getLightPosition() {
        return lightPosition;
    }

    public Shading withLightPosition(Vector2 lightPosition) {
        this.lightPosition = lightPosition;
        return this;

    }

    public float getLightStrength() {
        return lightStrength;

    }

    public Shading withLightStrength(float lightStrength) {
        this.lightStrength = lightStrength;
        return this;

    }

    public Color getAmbientColor() {
        return ambientColor;
    }

    public Shading withAmbientColor(Color ambientColor) {
        this.ambientColor = ambientColor;
        return this;
    }


    public void setupEditor(String key) {
        Main.getInstance().getEditor().addColorChanged(key + " - ambient color", getColorHex(ambientColor), new OnColorChanged() {

            @Override
            public void colorChanged(String hex) {
                ambientColor.set(Color.valueOf(hex));
            }
        });

        Main.getInstance().getEditor().addColorChanged(key + " - fog center color", getColorHex(fogCenterColor), new OnColorChanged() {

            @Override
            public void colorChanged(String hex) {
                fogCenterColor.set(Color.valueOf(hex));
            }
        });
        Main.getInstance().getEditor().addColorChanged(key + " - fog outside color", getColorHex(fogOutsideColor), new OnColorChanged() {

            @Override
            public void colorChanged(String hex) {
                fogOutsideColor.set(Color.valueOf(hex));
            }
        });
        Main.getInstance().getEditor().addSlider(key + " - fogCenterStrength", 0f, 1f, fogCenterStrength, new OnSliderChanged() {

            @Override
            public void sliderChanged(float value) {
                fogCenterStrength = value;
            }
        });
        Main.getInstance().getEditor().addSlider(key + " - fogOutsideStrength", 0f, 1f, fogOutsideStrength, new OnSliderChanged() {

            @Override
            public void sliderChanged(float value) {
                fogOutsideStrength = value;
            }
        });
        Main.getInstance().getEditor().addCheckbox(key + " - fogEnabled",fogEnabled, new OnCheckboxChanged() {

            @Override
            public void checkboxChanged(boolean state) {
                fogEnabled = state;
            }
        });
    }

    private String getColorHex(Color color) {
        if(color == null) {
            return "00000000";
        }
        return color.toString();
    }

    public void apply(ShaderProgram shader) {
        shader.setUniformf("ambient_color",
                getAmbientColor().r,
                getAmbientColor().g,
                getAmbientColor().b,
                getAmbientColor().a);

        if (isFogEnabled()) {
            shader.setUniformi("fog_enabled", 1);
            shader.setUniformf("fog_center_color", getFogCenterColor());
            shader.setUniformf("fog_center_strength", getFogCenterStrength());
            shader.setUniformf("fog_outside_color", getFogOutsideColor());
            shader.setUniformf("fog_outside_strength", getFogOutsideStrength());
        } else {
            shader.setUniformi("fog_enabled", 0);

        }

        if (isLightEnabled()) {
            shader.setUniformi("light_enabled", 1);
            shader.setUniformf("light_strength", getLightStrength());
            shader.setUniformf("light_position", getLightPosition().x,
                    getLightPosition().y);

        } else {
            shader.setUniformi("light_enabled", 0);

        }
    }
}
