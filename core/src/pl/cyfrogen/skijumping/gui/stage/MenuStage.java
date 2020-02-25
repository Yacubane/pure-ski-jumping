package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.gui.actors.common.MenuActor;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.actors.common.TilesGrid;
import pl.cyfrogen.skijumping.gui.actors.common.TitleBar;
import pl.cyfrogen.skijumping.gui.interfaces.OnReturnListener;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;
import pl.cyfrogen.skijumping.gui.utils.MenuAnimations;
import pl.cyfrogen.skijumping.gui.utils.OnDisposeListener;
import pl.cyfrogen.skijumping.gui.utils.OnStageShown;

public class MenuStage extends Stage {

    private final Direction direction = Direction.LEFT;
    private final MainMenuController menuController;
    private OnReturnListener onReturnListener;
    private OnDisposeListener onDisposeListener;
    private boolean disposable;
    private boolean disposed;

    public MenuAssets getMenuAssets() {
        return menuAssets;
    }

    private final MenuAssets menuAssets;
    private TitleBar titleBar;
    private MenuButton backButton;
    private TilesGrid tilesGrid;
    private ArrayList<MenuActor> menuActors = new ArrayList<MenuActor>();

    public Container createMainContainer(Actor actor) {
        Container<Actor> container = new Container<Actor>();
        container.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.2f);
        container.setActor(actor);
        return container;
    }

    public void addMenuActor(Actor actor, float delay) {
        addActor(actor);
        menuActors.add(new MenuActor(actor, delay));
    }

    public void addTitlebar(String text) {
        titleBar = new TitleBar(text);
        titleBar.setPosition((Gdx.graphics.getWidth() - titleBar.getWidth()) / 2f, Gdx.graphics.getHeight() - titleBar.getHeight() * 1.25f);
        addMenuActor(titleBar, 0.06f);
    }

    @Override
    public boolean keyDown(final int keycode) {
        if (keycode == Input.Keys.BACK && backButton != null) {
            backPressed();
        }
        return super.keyDown(keycode);
    }

    public void addBackButton() {

        TextureRegion backTextureRegion = getMenuAssets().get(MenuAssets.Asset.BACK_BUTTON_TEXTURE,
                TextureRegion.class);

        backButton = new MenuButton(backTextureRegion);
        float backButtonSize = Gdx.graphics.getHeight() * 0.15f;
        backButton.setSize(backButtonSize, backButtonSize);
        backButton.setPosition(backButtonSize * 0.5f, Gdx.graphics.getHeight() - backButtonSize * 1.25f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backPressed();
            }
        });
        addMenuActor(backButton, 0f);
    }

    public ScrollPane addTiles(Actor... tiles) {
        float screenPadding = Gdx.graphics.getWidth() * 0.1f;
        int tilesToFit = 4;
        float paddingPercentage = 0.2f;

        float restWidth = Gdx.graphics.getWidth() - screenPadding * 2;

        float tileSize = restWidth / (tilesToFit + paddingPercentage * tilesToFit - paddingPercentage);
        tilesGrid = new TilesGrid(1, tiles.length, tileSize, tileSize, tileSize * paddingPercentage);

        for (int i = 0; i < tiles.length; i++) {
            tiles[i].setSize(tileSize, tileSize);
            tilesGrid.add(tiles[i], 0, i);
        }


        Container<TilesGrid> container = new Container<TilesGrid>();
        container.setActor(tilesGrid);
        container.setSize(Math.max(tilesGrid.getWidth() + screenPadding * 2f, Gdx.graphics.getWidth()), Gdx.graphics.getHeight() * 0.4f);


        ScrollPane scrollPane = new ScrollPane(groupOf(container));
        scrollPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 0.6f);

        addActor(scrollPane);
        return scrollPane;
    }

    public void animateShowing(Direction direction) {
        if (tilesGrid != null)
            tilesGrid.show(direction);
        for (MenuActor menuActor : menuActors) {
            menuActor.getActor().setColor(new Color(1f, 1f, 1f, 0f));
            MenuAnimations.apply(MenuAnimations.showingAnimation()
                            .withDirection(direction)
                            .withStartIdleTime(menuActor.getDelay()),
                    menuActor.getActor());
        }

    }


    public void show(final Direction direction) {
        MainMenuUtils.showStage(getMenuController(), this, new OnStageShown() {

            @Override
            public void stageShown() {
                animateShowing(direction);
            }
        });
    }


    void backPressed() {
        close();
        if (onReturnListener != null) onReturnListener.returned(this);
    }

    public void hide() {
        Direction direction = Direction.LEFT;


        for (MenuActor menuActor : menuActors) {
            menuActor.getActor().addAction(Actions.sequence(
                    MenuAnimations.hidingAnimation()
                            .withDirection(direction)
                            .withStartIdleTime(menuActor.getDelay())
                            .build()));
        }

        if (tilesGrid != null) {
            tilesGrid.hide(direction);
        }

    }

    public void close() {
        Direction direction = Direction.RIGHT;
            disposable = true;


        for (MenuActor menuActor : menuActors) {
            menuActor.getActor().addAction(Actions.sequence(
                    MenuAnimations.hidingAnimation()
                            .withDirection(direction)
                            .withStartIdleTime(menuActor.getDelay())
                            .build()));
        }

        if (tilesGrid != null) {
            tilesGrid.hide(direction);
        }

    }

    public MenuStage(MainMenuController mainMenuController) {
        super();
        this.menuAssets = mainMenuController.getMenuAssets();
        this.menuController = mainMenuController;

    }

    Group groupOf(Actor container) {
        Group group = new Group();
        group.addActor(container);
        group.setSize(container.getWidth(), container.getHeight());
        return group;
    }

    @Override
    public void dispose() {
        if (disposable && !disposed) {
            super.dispose();
            disposed = true;
            if (onDisposeListener != null) {
                onDisposeListener.disposed();
            }
        }
    }

    public MenuStage withOnReturnListener(OnReturnListener onReturnListener) {
        this.onReturnListener = onReturnListener;
        return this;
    }

    void setTilesGrid(TilesGrid tilesGrid) {
        this.tilesGrid = tilesGrid;
    }

    public MainMenuController getMenuController() {
        return menuController;
    }

    public void setOnClose(OnDisposeListener onDisposeListener) {
        this.onDisposeListener = onDisposeListener;
    }


}
