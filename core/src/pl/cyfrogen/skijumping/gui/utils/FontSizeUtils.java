package pl.cyfrogen.skijumping.gui.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class FontSizeUtils {

    public static float calculateFontScaleByHeight(BitmapFont font, float spaceHeight) {
        GlyphLayout glyphLayout = new GlyphLayout();
        font.getData().setScale(1);
        glyphLayout.setText(font, "jT"); //characters with different bounds
        float scaleXY = spaceHeight / glyphLayout.height;
        if (scaleXY > 0) {
            font.getData().setScale(scaleXY);
            return scaleXY;
        }
        return 1;
    }

    public static float calculateFontScaleByWidth(BitmapFont font, float width, String text) {
        GlyphLayout glyphLayout = new GlyphLayout();
        font.getData().setScale(1);
        glyphLayout.setText(font, text);
        float scaleXY = width / glyphLayout.width;
        if (scaleXY > 0) {
            font.getData().setScale(scaleXY);
            return scaleXY;
        }

        return 1;
    }

    public static float calculateFontWidth(BitmapFont font, String text, float scale) {
        GlyphLayout glyphLayout = new GlyphLayout();
        font.getData().setScale(scale);
        glyphLayout.setText(font, text);
        return glyphLayout.width;
    }

}
