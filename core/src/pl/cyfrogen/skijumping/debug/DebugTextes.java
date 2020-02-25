package pl.cyfrogen.skijumping.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class DebugTextes {
    private final BitmapFont font;
    private final float fontSize;
    private final float padding;

    ArrayList<DebugText> textes;
    public DebugTextes(BitmapFont font){
        this.font = font;
        textes = new ArrayList<DebugText>();
        fontSize = Gdx.graphics.getHeight()*0.025f;
        padding = Gdx.graphics.getHeight()*0.008f;
        calculateFontScaleByHeight(font, fontSize);
    }
    public static float calculateFontScaleByHeight(BitmapFont font, float spaceHeight) {
        font.getData().setScale(1);
        GlyphLayout glyphLayout = new GlyphLayout(font, "TEXT");
        float scaleXY = spaceHeight / glyphLayout.height;
        if (scaleXY > 0) {
            font.getData().setScale(scaleXY);
            return scaleXY;
        }
        return 1;
    }
    public void setText(String id, Object value, Color color){
        for(DebugText text: textes){
            if(text.getId().contentEquals(id)){
                text.setValue(String.valueOf(value));
                return;
            }
        }

        textes.add(new DebugText(id,String.valueOf(value), color));
    }
    public void draw(SpriteBatch batch){
        for(int i = 0; i < textes.size(); i++){
            float y = (fontSize+padding)*(i+1);
            float x = 0;
            font.setColor(textes.get(i).getColor());
            font.draw(batch, textes.get(i).getId() + ":"+textes.get(i).getValue(),x,y);
        }
    }

    public void clear() {
        textes.clear();
    }
}
