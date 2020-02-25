package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Scaling;


public class FilledTextButton extends Group {
    private final Texture buttonTexture;
    private final Image img;
    private final Actor clickArea;
    private final FilledLabel label;


    public FilledTextButton(float width, float height, float percentageX, float percentageY, CharSequence text, Label.LabelStyle style, Texture buttonTexture) {
        super();
        img = new Image(buttonTexture);
        img.setSize(width, height);
        img.setScaling(Scaling.fit);
        addActor(img);

        Vector2 size = Scaling.fit.apply(buttonTexture.getWidth(), buttonTexture.getHeight(), width, height);

        label = new FilledLabel(size.x, size.y, percentageX, percentageY, text, style);
        label.setPosition((width-size.x)/2f, (height-size.y)/2f);
        addActor(label);

        clickArea = new Actor();
        clickArea.setBounds((width-size.x)/2f, (height-size.y)/2f, size.x, size.y);
        addActor(clickArea);

        this.buttonTexture = buttonTexture;
        setSize((int) width, (int) height);


        init();
    }
    @Override
    public boolean addListener (EventListener listener) {
        return clickArea.addListener(listener);

    }

    private void init() {

    }


    @Override
    public void draw(Batch batch, float alpha) {
        super.draw(batch, alpha);
    }

    @Override
    public void setColor(Color color){
        img.setColor(color);
        label.setFontColor(color);
    }
}
