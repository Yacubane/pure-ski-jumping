package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.data.HillSetup;
import pl.cyfrogen.skijumping.gui.actors.common.FBOWidget;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.actors.competitors.EditWidget;
import pl.cyfrogen.skijumping.gui.actors.hill.HillIconWidget;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;
import pl.cyfrogen.skijumping.gui.utils.OnDisposeListener;
import pl.cyfrogen.skijumping.hill.HillFile;

public class HillSetupStage extends MenuStage {
    private final float minWind;
    private final float maxWind;
    private final HillSetup hillSetup;
    private FilledLabel windLabel;
    float tileHeight = Gdx.graphics.getHeight() * 0.6f;
    float tileWidth = tileHeight * 50 / 70f;
    List<HillSetup.Mode> supportedModes = new ArrayList<HillSetup.Mode>();

    public HillSetupStage(final HillFile hillFile, final HillSetup hillSetup, final MainMenuController mainMenuController, final OnHillSetuped onHillSetuped) {
        super(mainMenuController);
        this.hillSetup = hillSetup;

        for (Iterator<Map.Entry<String, JsonNode>> it = hillFile.getMetaData().get("modes").fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            supportedModes.add(HillSetup.Mode.valueOf(entry.getKey()));
        }

        final int startGates = hillFile.getMetaData().get("noOfStartGates").intValue();
        minWind = hillFile.getMetaData().get("physics").get("defaultMinWind").floatValue();
        maxWind = hillFile.getMetaData().get("physics").get("defaultMaxWind").floatValue();

        addTitlebar("CUSTOMIZE HILL");
        if (onHillSetuped != null)
            addBackButton();
        animateShowing(Direction.LEFT);


        FileHandle icon = hillFile.getFile(hillFile.getMetaData().get("icon").textValue());
        final Texture texture = new Texture(icon);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        HillIconWidget menuButton = new HillIconWidget(new TextureRegion(texture),
                hillFile.getMetaData().get("name").textValue().toUpperCase(), tileWidth, tileHeight);

        float screenPadding = Gdx.graphics.getWidth() * 0.1f;
        menuButton.setPosition(screenPadding, Gdx.graphics.getHeight() * 0.8f / 2f - menuButton.getHeight() / 2f);

        addMenuActor(menuButton, 0.06f);


        float editWidgetWidth = Gdx.graphics.getWidth() * .45f;
        float editWidgetHeight = Gdx.graphics.getHeight() * .12f;
        EditWidget editWidget = new EditWidget("Mode", editWidgetWidth, editWidgetHeight, mainMenuController.getMenuAssets());
        EditWidget editWidget2 = new EditWidget("Snow", editWidgetWidth, editWidgetHeight, mainMenuController.getMenuAssets());
        EditWidget editWidget3 = new EditWidget("Wind", editWidgetWidth, editWidgetHeight, mainMenuController.getMenuAssets());
        EditWidget editWidget4 = new EditWidget("Gate", editWidgetWidth, editWidgetHeight, mainMenuController.getMenuAssets());


        Group group = group(editWidgetWidth, editWidgetHeight, editWidget, editWidget2, editWidget3, editWidget4);

        Container<Actor> container = MainMenuUtils.createScrollPanelContainer(
                group, Gdx.graphics.getHeight() * 0.75f);

        ScrollPane scrollPane = new ScrollPane(FBOWidget.of(container));
        scrollPane.setSize(editWidgetWidth, Gdx.graphics.getHeight() * 0.75f);
        scrollPane.setPosition(Gdx.graphics.getWidth() * 0.45f, 0f);

        addMenuActor(scrollPane, 0.12f);

        TextureRegion acceptTextureRegion = getMenuAssets().get(MenuAssets.Asset.ACCEPT_ICON_TEXTURE,
                TextureRegion.class);

        MenuButton acceptButton = new MenuButton(acceptTextureRegion);
        float backButtonSize = Gdx.graphics.getHeight() * 0.15f;
        acceptButton.setSize(backButtonSize, backButtonSize);
        acceptButton.setPosition(Gdx.graphics.getWidth() - backButtonSize * 1.5f, backButtonSize * 0.25f);
        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onHillSetuped != null) {
                    onHillSetuped.hillSetuped(hillSetup);
                    hide();
                } else {
                    backPressed();
                }
            }
        });
        addMenuActor(acceptButton, 0.3f);


        Group editTypePlaceholder = editWidget.getPlaceholder();

        final MenuButton weatherIcon = new MenuButton(
                mainMenuController.getMenuAssets().get(MenuAssets.Asset.WEATHER_DAY_ICON, TextureRegion.class));

        switch (hillSetup.getMode()) {
            case DAY:
                weatherIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.WEATHER_DAY_ICON, TextureRegion.class));
                break;
            case MORNING:
                weatherIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.WEATHER_MORNING_ICON, TextureRegion.class));
                break;
            case NIGHT:
                weatherIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.WEATHER_NIGHT_ICON, TextureRegion.class));
                break;
        }


        weatherIcon.setSize(editWidgetHeight, editWidgetHeight);
        weatherIcon.setPosition(editTypePlaceholder.getWidth() / 2f - weatherIcon.getWidth() / 2f, 0);
        editTypePlaceholder.addActor(weatherIcon);

        weatherIcon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int currentModeIndex = supportedModes.indexOf(hillSetup.getMode());
                int nextIndex = currentModeIndex == supportedModes.size() - 1 ? 0 : currentModeIndex + 1;

                HillSetup.Mode nextMode = supportedModes.get(nextIndex);
                hillSetup.setMode(nextMode);

                switch (nextMode) {
                    case DAY:
                        weatherIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.WEATHER_DAY_ICON, TextureRegion.class));
                        break;
                    case MORNING:
                        weatherIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.WEATHER_MORNING_ICON, TextureRegion.class));
                        break;
                    case NIGHT:
                        weatherIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.WEATHER_NIGHT_ICON, TextureRegion.class));
                        break;
                }
            }
        });


        Group editSnowingPlaceholder = editWidget2.getPlaceholder();

        final MenuButton snowingIcon = new MenuButton(
                mainMenuController.getMenuAssets().get(MenuAssets.Asset.WEATHER_DAY_ICON, TextureRegion.class));

        switch (hillSetup.getSnowing()) {
            case NO:
                snowingIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.SNOWING_NO_ICON, TextureRegion.class));
                break;
            case SOFT:
                snowingIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.SNOWING_SOFT_ICON, TextureRegion.class));
                break;
            case HARD:
                snowingIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.SNOWING_HARD_ICON, TextureRegion.class));
                break;
        }


        snowingIcon.setSize(editWidgetHeight, editWidgetHeight);
        snowingIcon.setPosition(editSnowingPlaceholder.getWidth() / 2f - snowingIcon.getWidth() / 2f, 0);
        editSnowingPlaceholder.addActor(snowingIcon);

        snowingIcon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (hillSetup.getSnowing()) {
                    case NO:
                        hillSetup.setSnowing(HillSetup.Snowing.SOFT);
                        snowingIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.SNOWING_SOFT_ICON, TextureRegion.class));
                        break;
                    case SOFT:
                        hillSetup.setSnowing(HillSetup.Snowing.HARD);
                        snowingIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.SNOWING_HARD_ICON, TextureRegion.class));
                        break;
                    case HARD:
                        hillSetup.setSnowing(HillSetup.Snowing.NO);
                        snowingIcon.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.SNOWING_NO_ICON, TextureRegion.class));
                        break;
                }
            }
        });


        Group editGatePlaceholder = editWidget4.getPlaceholder();


        final FilledLabel gateNumberLabel = new FilledLabel(editGatePlaceholder.getWidth(), editWidgetHeight, 0.9f, 0.3f,
                String.valueOf(hillSetup.getStartGate()),
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        editGatePlaceholder.addActor(gateNumberLabel);


        final MenuButton arrowDown = new MenuButton(
                mainMenuController.getMenuAssets().get(MenuAssets.Asset.ARROW_DOWN_TEXTURE, TextureRegion.class));

        arrowDown.setSize(editWidgetHeight, editWidgetHeight);
        arrowDown.setPosition(editTypePlaceholder.getWidth() * 1 / 4f - weatherIcon.getWidth() / 2f, 0);
        editGatePlaceholder.addActor(arrowDown);

        arrowDown.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (hillSetup.getStartGate() > 1) {
                    hillSetup.setStartGate(hillSetup.getStartGate() - 1);
                    gateNumberLabel.setText(String.valueOf(hillSetup.getStartGate()));
                }
            }
        });

        final MenuButton arrowUp = new MenuButton(
                mainMenuController.getMenuAssets().get(MenuAssets.Asset.ARROW_UP_TEXTURE, TextureRegion.class));

        arrowUp.setSize(editWidgetHeight, editWidgetHeight);
        arrowUp.setPosition(editTypePlaceholder.getWidth() * 3 / 4f - weatherIcon.getWidth() / 2f, 0);
        editGatePlaceholder.addActor(arrowUp);

        arrowUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (hillSetup.getStartGate() < startGates) {
                    hillSetup.setStartGate(hillSetup.getStartGate() + 1);
                    gateNumberLabel.setText(String.valueOf(hillSetup.getStartGate()));
                }
            }
        });


        Group editWindPlaceholder = editWidget3.getPlaceholder();

        final MenuButton editWindButton = new MenuButton(
                mainMenuController.getMenuAssets().get(MenuAssets.Asset.EDIT_ICON_TEXTURE, TextureRegion.class));

        editWindButton.setSize(editWindPlaceholder.getHeight(), editWindPlaceholder.getHeight());
        editWindButton.setPosition(editWindPlaceholder.getWidth() - editWindButton.getWidth(), 0);
        editWindPlaceholder.addActor(editWindButton);


        windLabel = new FilledLabel(
                editWindPlaceholder.getWidth() - editWindButton.getWidth(),
                editWindPlaceholder.getHeight(),
                1f, 0.3f,
                "",
                Main.getInstance().getAssets().getLabelStyle(Color.WHITE));
        windLabel.setPosition(0, 0);


        editWindButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMinWind();
            }
        });
        editWindPlaceholder.addActor(windLabel);
        setWindLabelText();


        animateShowing(Direction.LEFT);

        setOnClose(new OnDisposeListener(){

            @Override
            public void disposed() {
                texture.dispose();
            }
        });

    }

    private void setWindLabelText() {
        windLabel.setText((hillSetup.getMinWind()) + " to " + (hillSetup.getMaxWind()) + " m/s");
    }

    private void setMinWind() {
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                try {
                    float value = Float.valueOf(text);
                    if (value < minWind || value > maxWind) {
                        throw new NumberFormatException("Value out of range");
                    }
                    hillSetup.setMinWind(value);
                    setMaxWind(value);

                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    setMinWind();
                }
            }

            @Override
            public void canceled() {

            }
        }, "Change minimum wind from range: " + minWind + " to " + maxWind, String.valueOf(minWind), "");
    }

    private void setMaxWind(final float minWind) {
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                try {
                    float value = Float.valueOf(text);
                    if (value < minWind || value > maxWind) {
                        throw new NumberFormatException("Value out of range");
                    }
                    hillSetup.setMinWind(minWind);
                    hillSetup.setMaxWind(value);
                    setWindLabelText();

                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    setMaxWind(minWind);
                }
            }

            @Override
            public void canceled() {

            }
        }, "Change maximum wind from range: " + minWind + " to " + maxWind, String.valueOf(maxWind), "");
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

    @Override
    void backPressed() {
        super.backPressed();
    }

    public interface OnHillSetuped {
        void hillSetuped(HillSetup hillSetup);
    }
}
