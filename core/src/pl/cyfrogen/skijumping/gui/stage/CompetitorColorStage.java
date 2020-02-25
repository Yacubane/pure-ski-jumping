package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.data.SettableColor;
import pl.cyfrogen.skijumping.gui.actors.common.FBOWidget;
import pl.cyfrogen.skijumping.gui.actors.competitors.JumperWidget;
import pl.cyfrogen.skijumping.gui.actors.competitors.colors.ColorPalette;
import pl.cyfrogen.skijumping.gui.actors.competitors.colors.OnColorSelected;
import pl.cyfrogen.skijumping.gui.actors.competitors.colors.TextButton;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;
import pl.cyfrogen.skijumping.gui.utils.MenuAnimations;

public class CompetitorColorStage extends MenuStage {
    private ColorPalette palette;

    private class ColorEntry {
        private final String displayText;
        private final SettableColor colorToSelect;
        private final Color[] colorsToChoose;

        public ColorEntry(String displayText, SettableColor colorToSelect, Color[] colorsToChoose) {
            this.displayText = displayText;
            this.colorToSelect = colorToSelect;
            this.colorsToChoose = colorsToChoose;
        }
    }


    public CompetitorColorStage(final JumperWidget jumperWidget, final JumperData jumperData, MainMenuController mainMenuController) {
        super(mainMenuController);


        final float competitorWidth = Gdx.graphics.getWidth() * .22f;
        final float competitorHeight = Gdx.graphics.getHeight() * .12f;

        Color[] colors = new Color[]{
                Color.valueOf("#1abc9c"),
                Color.valueOf("#2ecc71"),
                Color.valueOf("#3498db"),
                Color.valueOf("#9b59b6"),
                Color.valueOf("#34495e"),
                Color.valueOf("#222831"),
                Color.valueOf("#f1c40f"),
                Color.valueOf("#e67e22"),
                Color.valueOf("#c0392b"),
                Color.valueOf("#ffda79"),
                Color.valueOf("#ff5252"),
                Color.valueOf("#7efff5"),
                Color.valueOf("#ffcccc"),
                Color.valueOf("#18dcff"),
                Color.valueOf("#ecf0f1"),
                Color.valueOf("#bdc3c7"),
                Color.valueOf("#7f8c8d"),
                Color.valueOf("#00adb5"),
                Color.valueOf("#b83b5e"),
                Color.valueOf("#6a2c70"),
                Color.valueOf("#eaffd0"),
                Color.valueOf("#95e1d3"),
                Color.valueOf("#f38181"),
                Color.valueOf("#08d9d6"),
        };

        Color[] skinColors = new Color[]{
                Color.valueOf("#fddcbc"),
                Color.valueOf("#f5c09b"),
                Color.valueOf("#f69777"),
                Color.valueOf("#f4a462"),
                Color.valueOf("#b94b25"),
                Color.valueOf("#86201b"),
        };

        ArrayList<ColorEntry> colorEntries = new ArrayList<ColorEntry>();
        colorEntries.add(new ColorEntry(
                "Helmet",
                jumperData.getColors().getHelmetColor(),
                colors));
        colorEntries.add(new ColorEntry(
                "Goggles band",
                jumperData.getColors().getGoggleBandColor(),
                colors));
        colorEntries.add(new ColorEntry(
                "Goggles",
                jumperData.getColors().getGoggleColor(),
                colors));
        colorEntries.add(new ColorEntry(
                "Skin",
                jumperData.getColors().getSkinColor(),
                skinColors));
        colorEntries.add(new ColorEntry(
                "Body left",
                jumperData.getColors().getBodyLeftColor(),
                colors));
        colorEntries.add(new ColorEntry(
                "Body right",
                jumperData.getColors().getBodyRightColor(),
                colors));
        colorEntries.add(new ColorEntry(
                "Arm left",
                jumperData.getColors().getArmLeftColor(),
                colors));
        colorEntries.add(new ColorEntry(
                "Arm right",
                jumperData.getColors().getArmRightColor(),
                colors));
        colorEntries.add(new ColorEntry(
                "Glove",
                jumperData.getColors().getGloveColor(),
                colors));
        colorEntries.add(new ColorEntry(
                "Boot",
                jumperData.getColors().getBootColor(),
                colors));
        colorEntries.add(new ColorEntry(
                "Ski",
                jumperData.getColors().getSkiColor(),
                colors));


        final ArrayList<TextButton> textButtons = new ArrayList<TextButton>();
        for (final ColorEntry colorEntry : colorEntries) {
            final TextButton textButton = new TextButton(
                    colorEntry.displayText,
                    competitorWidth,
                    competitorHeight);
            textButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    for (TextButton textButton : textButtons) {
                        textButton.setBackgroundColor(Color.BLACK);
                        textButton.setFontColor(Color.WHITE);
                    }
                    textButton.setBackgroundColor(Color.WHITE);
                    textButton.setFontColor(Color.BLACK);

                    if (palette != null) {
                        palette.addAction(Actions.sequence(
                                Actions.alpha(0, 0.25f),
                                Actions.removeActor()
                        ));
                    }

                    palette = new ColorPalette(null, colorEntry.colorsToChoose,
                            competitorWidth, Gdx.graphics.getHeight() * 0.75f, 3,
                            new OnColorSelected() {

                                @Override
                                public void colorSelected(Color color) {
                                    colorEntry.colorToSelect.setColor(color);
                                    jumperWidget.refresh();
                                }
                            });

                    palette.setPosition(Gdx.graphics.getWidth() * 0.05f + competitorWidth, 0f);
                    addActor(palette);
                    palette.addAction(Actions.sequence(
                            Actions.alpha(0),
                            Actions.alpha(1, 0.25f)
                    ));
                }
            });
            textButtons.add(textButton);
        }

        Group group = group(competitorWidth, competitorHeight, textButtons.toArray(new TextButton[0]));

        Container<Actor> container = MainMenuUtils.createScrollPanelContainer(
                group, Gdx.graphics.getHeight() * 0.75f);

        ScrollPane scrollPane = new ScrollPane(FBOWidget.of(container));
        scrollPane.setSize(competitorWidth, Gdx.graphics.getHeight() * 0.75f);
        scrollPane.setPosition(Gdx.graphics.getWidth() * 0.05f, 0f);

        addMenuActor(scrollPane, 0.12f);

        addTitlebar("COLORS");
        addBackButton();
        animateShowing(Direction.LEFT);
    }

    @Override
    public void backPressed() {
        super.backPressed();
        if (palette != null)
            MenuAnimations.apply(MenuAnimations.hidingAnimation()
                            .withDirection(Direction.RIGHT)
                            .withStartIdleTime(0.1f),
                    palette);
    }

    public Group group(float width, float oneWidgetHeight, Actor... actors) {
        Group group = new Group();
        group.setSize(width, oneWidgetHeight * actors.length);
        float y = group.getHeight();
        for (Actor actor : actors) {
            group.addActor(actor);
            y -= actor.getHeight();
            actor.setPosition(0, y);
            group.addActor(actor);
        }
        return group;


    }


}
