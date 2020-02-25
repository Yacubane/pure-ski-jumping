package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.cyfrogen.skijumping.data.HillLocation;
import pl.cyfrogen.skijumping.data.HillSetup;
import pl.cyfrogen.skijumping.gui.actors.common.TilesGrid;
import pl.cyfrogen.skijumping.gui.actors.hill.HillIconWidget;
import pl.cyfrogen.skijumping.gui.interfaces.OnReturnListener;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;
import pl.cyfrogen.skijumping.gui.utils.OnDisposeListener;
import pl.cyfrogen.skijumping.hill.HillFile;
import pl.cyfrogen.skijumping.hill.Hills;

class ChooseHillStage extends MenuStage {
    float tileHeight = Gdx.graphics.getHeight() * 0.6f;
    float tileWidth = tileHeight * 50 / 70f;
    List<Texture> textures = new ArrayList<Texture>();

    public ChooseHillStage(MainMenuController mainMenuController, final onHillSelected onHillSelected) {
        super(mainMenuController);

        List<Actor> menuButtons = new ArrayList<Actor>();

        for (final HillLocation hillLocation : Hills.hillLocations) {
            try {
                FileHandle zipFile = hillLocation.getFileHandle();
                ObjectMapper objectMapper = new ObjectMapper();
                FileHandle manifest = zipFile.child("manifest.json");
                JsonNode manifestNode = objectMapper.readTree
                        (manifest.readString());
                JsonNode hillMeta = objectMapper.readTree(
                        zipFile.child(manifestNode.get("data").textValue()).readString());
                String hillName = hillMeta.get("name").textValue().toUpperCase();

                FileHandle icon = zipFile.child("icon.png");
                Texture texture = new Texture(icon);
                texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                textures.add(texture);

                HillIconWidget menuButton = new HillIconWidget(new TextureRegion(texture), hillName, tileWidth, tileHeight);
                menuButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        try {
                            HillFile hillFile = new HillFile(hillLocation);
                            int gate = hillFile.getMetaData().get("defaultStartGate").intValue();
                            float defaultMinWind = (hillFile.getMetaData().get("physics").get("defaultMinWind").floatValue());
                            float defaultMaxWind = (hillFile.getMetaData().get("physics").get("defaultMaxWind").floatValue());
                            HillSetup.Mode mode = HillSetup.createWeather(
                                    hillFile.getMetaData().get("defaultCompetitiveMode").textValue());
                            HillSetup.Snowing snowing = HillSetup.createSnowing(
                                    hillFile.getMetaData().get("defaultCompetitiveSnowing").textValue());

                            MainMenuUtils.showStage(
                                    getMenuController(),
                                    new HillSetupStage(hillFile, new HillSetup(hillLocation, mode, snowing, gate, defaultMinWind, defaultMaxWind),
                                            getMenuController(),
                                            new HillSetupStage.OnHillSetuped() {
                                                @Override
                                                public void hillSetuped(HillSetup hillSetup) {
                                                    onHillSelected.hillSelected(hillSetup);
                                                    backPressed();
                                                }
                                            })
                                            .withOnReturnListener(new OnReturnListener() {
                                                @Override
                                                public void returned(MenuStage menuStage) {
                                                    show(Direction.RIGHT);
                                                }
                                            }));
                            hide();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                });
                menuButtons.add(menuButton);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addTiles(menuButtons.toArray(new Actor[0]));


        addBackButton();
        addTitlebar("ADD HILL");
        animateShowing(Direction.LEFT);

        setOnClose(new OnDisposeListener(){

            @Override
            public void disposed() {
                for(Texture texture : textures) {
                    texture.dispose();
                }
            }
        });
    }


    public ScrollPane addTiles(Actor... tiles) {
        float screenPadding = Gdx.graphics.getWidth() * 0.1f;
        float paddingPercentage = 0.2f;

        TilesGrid tilesGrid = new TilesGrid(1, tiles.length, tileWidth, tileHeight, tileWidth * paddingPercentage);
        setTilesGrid(tilesGrid);

        for (int i = 0; i < tiles.length; i++) {
            tiles[i].setSize(tileWidth, tileHeight);
            tilesGrid.add(tiles[i], 0, i);
        }


        Container<TilesGrid> container = new Container<TilesGrid>();
        container.setActor(tilesGrid);
        container.setSize(Math.max(tilesGrid.getWidth() + screenPadding * 2f, Gdx.graphics.getWidth()), Gdx.graphics.getHeight() * 0.6f);


        ScrollPane scrollPane = new ScrollPane(groupOf(container));
        scrollPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 0.6f);
        scrollPane.setY(Gdx.graphics.getHeight() * 0.05f);

        addActor(scrollPane);
        return scrollPane;
    }


    public interface onHillSelected {
        void hillSelected(HillSetup hillSetup);
    }
}
