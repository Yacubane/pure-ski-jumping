package pl.cyfrogen.skijumping.gui.actors.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.DistanceFieldLabel;
import pl.cyfrogen.skijumping.gui.utils.FontSizeUtils;

public class SlideText extends Group {
    private static float SHOWING_TIME = 1f;
    private static float AFTER_SHOWING_TIME = 1f;
    private static float AFTER_MOVING_TIME = 1f;
    private static float HIDING_TIME = 1f;
    private static float AFTER_HIDING_TIME = 0.3f;
    private float TEXT_PADDING = Gdx.graphics.getWidth()*0.02f;


    private enum State {
        SHOWING, AFTER_SHOWING, MOVING, AFTER_MOVING, HIDING, AFTER_HIDING
    }

    private final DistanceFieldLabel label;
    private final float fontHeight;

    private float textSize;
    private float textX;

    private float textSpeed = Gdx.graphics.getWidth() * 0.1f;

    private State state = State.SHOWING;
    private float time;

    public SlideText(String text, float width, float height) {
        setSize(width, height);


        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        final Image background = new Image(whiteDot);
        background.setColor(new Color(0,0,0,0.5f));
        background.setSize(getWidth(), getHeight());
        addActor(background);

        BitmapFont font = Main.getInstance().getAssets().getFont();

        fontHeight = FontSizeUtils.calculateFontScaleByHeight(font, getHeight()) * 0.5f;
        font.getData().setScale(fontHeight);
        label = new DistanceFieldLabel(text, new Label.LabelStyle(font, Color.WHITE));
        label.setY(getHeight() * 0.25f);
        addActor(label);

        setText(text);
    }

    public void setText(String text) {
        textSize = FontSizeUtils.calculateFontWidth(
                Main.getInstance().getAssets().getFont(), text, fontHeight);
        label.setText(text);
        textX = 0;
        setState(State.SHOWING);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        float delta = Gdx.graphics.getDeltaTime();

        float percent;
        if (textSize+2*TEXT_PADDING < getWidth()) {
            setColor(1, 1, 1, 1);
            label.setX(TEXT_PADDING);
        } else {
            switch (state) {
                case SHOWING:
                    time += delta;
                    percent = time / SHOWING_TIME;
                    if (percent > 1) setState(State.AFTER_SHOWING);
                    label.setColor(1, 1, 1, percent);
                    textX = TEXT_PADDING;
                    label.setX(textX);
                    break;
                case AFTER_SHOWING:
                    time += delta;
                    percent = time / AFTER_SHOWING_TIME;
                    if (percent > 1) setState(State.MOVING);
                    label.setColor(1, 1, 1, 1);
                    break;
                case MOVING:
                    textX -= delta * textSpeed;
                    if(textX + textSize + TEXT_PADDING < getWidth()) {
                        textX = getWidth() - textSize - TEXT_PADDING;
                        setState(State.AFTER_MOVING);
                    }
                    label.setX(textX);
                    break;
                case AFTER_MOVING:
                    time += delta;
                    percent = time / AFTER_MOVING_TIME;
                    if (percent > 1) setState(State.HIDING);
                    label.setColor(1, 1, 1, 1);
                    break;
                case HIDING:
                    time += delta;
                    percent = time / HIDING_TIME;
                    if (percent > 1) setState(State.AFTER_HIDING);
                    label.setColor(1, 1, 1, 1 - percent);
                    break;
                case AFTER_HIDING:
                    time += delta;
                    percent = time / AFTER_HIDING_TIME;
                    if (percent > 1) setState(State.SHOWING);
                    label.setColor(1, 1, 1, 0);
                    break;
            }
        }

        super.draw(batch, alpha);
    }

    private void setState(State state) {
        this.state = state;
        this.time = 0;
    }


}
