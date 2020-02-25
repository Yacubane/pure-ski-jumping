package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import pl.cyfrogen.skijumping.gui.utils.FontSizeUtils;


public class FilledLabel extends Group {
    private CharSequence text;
    private DistanceFieldLabel label;
    BitmapFont font;
    private float percentageX = 1f;
    private float percentageY = 1f;
    private Container container;

    public FilledLabel(float width, float height, float percentageX, float percentageY, CharSequence text, Label.LabelStyle style) {
        super();
        style.font.getData().setScale(1f);
        this.text = text;
        setSize((int) width, (int) height);

        this.percentageX = percentageX;
        this.percentageY = percentageY;
        this.font = style.font;
        float scaleY = getFontScaleY();
        float scaleX = getFontScaleX();
        this.font.getData().setScale(Math.min(scaleX, scaleY));
        label = new DistanceFieldLabel(text, style);

        init();

    }

    public FilledLabel(Actor actor, float percentageX, float percentageY, CharSequence text, Label.LabelStyle style) {
        super();
        style.font.getData().setScale(1f);
        this.text = text;
        setSize((int) actor.getWidth(), (int) actor.getHeight());
        setPosition(actor.getX(), actor.getY());

        this.percentageX = percentageX;
        this.percentageY = percentageY;
        this.font = style.font;
        float scaleY = getFontScaleY();
        float scaleX = getFontScaleX();
        this.font.getData().setScale(Math.min(scaleX, scaleY));
        label = new DistanceFieldLabel(text, style);

        init();

    }

    private void init() {
        container = new Container();
        container.setSize(getWidth(), getHeight());
        addActor(container);
        container.setActor(label);
    }

    public float getFontScaleX() {
        if (text.equals("")) return 1;
        return FontSizeUtils.calculateFontScaleByWidth(font,
                getWidth() * percentageX, (String) text);
    }

    public float getFontScaleY() {
        if (text.equals("")) return 1;
        return FontSizeUtils.calculateFontScaleByHeight(font,
                getHeight() * percentageY);
    }

    @Override
    protected void drawDebugBounds(ShapeRenderer shapes) {
        if (!getDebug()) return;
        shapes.set(ShapeRenderer.ShapeType.Line);
        shapes.setColor(getStage().getDebugColor());
        shapes.rect(getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        shapes.setColor(Color.MAGENTA);
        shapes.rect(getX() + (getWidth() * (1 - percentageX) / 2), getY() + (getHeight() * (1 - percentageY) / 2), getOriginX(), getOriginY(), getWidth() * percentageX, getHeight() * percentageY, getScaleX(), getScaleY(), getRotation());

    }

    public static void scaleToMinimum(FilledLabel... labels) {
        if (labels.length == 0) return;
        float minScale = Math.min(labels[0].getFontScaleX(), labels[0].getFontScaleY());
        for (int i = 1; i < labels.length; i++) {
            minScale = Math.min(minScale, labels[i].getFontScaleX());
            minScale = Math.min(minScale, labels[i].getFontScaleY());
        }
        for (int i = 0; i < labels.length; i++) {
            labels[i].setFontScale(minScale);
        }
    }

    public void setFontScale(float fontScale) {
        font.getData().setScale(fontScale);
        label.fontScaleUpdated();
    }

    public static void leftAlign(FilledLabel... labels) {
        for (FilledLabel label : labels) {
            label.left();
        }
    }

    public void setAlignment(int alignment) {
        if (alignment == Align.center) center();
        else if (alignment == Align.left) left();
        else if (alignment == Align.right) right();
    }

    private void right() {
        container.right();
    }

    private void left() {
        container.left();
    }

    private void center() {
        container.center();
    }

    public void setFontColor(Color fontColor) {
        label.setFontColor(fontColor);
    }

    public void setText(String text) {
        this.text = text;
        float scaleY = getFontScaleY();
        float scaleX = getFontScaleX();
        this.font.getData().setScale(Math.min(scaleX, scaleY));
        label.setFontScale(font.getData().scaleX);
        label.setText(text);
        label.fontScaleUpdated();
    }

    public void setTextIgnoreScaling(String text) {
        this.text = text;
        label.setText(text);
    }
}
