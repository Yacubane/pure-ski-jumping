package pl.cyfrogen.skijumping.gui.actors.game.scoreboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

import java.util.ArrayList;
import java.util.List;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.data.WorldCupJumperData;
import pl.cyfrogen.skijumping.gui.actors.common.Divider;
import pl.cyfrogen.skijumping.gui.actors.common.FBOWidget;

public class StartingList extends FBOWidget {
    float width = Gdx.graphics.getWidth() * 0.8f;
    float height = Gdx.graphics.getHeight() * 0.6f;
    float cellHeight = height / 8f;
    float dividerHeight = Math.min(Gdx.graphics.getHeight() * 0.003f, 1);

    public StartingList(int hillNum, String headerTitle, ArrayList<WorldCupJumperData> jumperDataArrayList) {
        setSize(width, height);
        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("161616"));
        background.setSize(width, height);
        addActor(background);

        StartingListHeader header = new StartingListHeader(width, cellHeight);
        header.setTitle(headerTitle);

        ArrayList<Actor> scoreCells = new ArrayList<Actor>();

        for (int i = 0; i < jumperDataArrayList.size(); i++) {
            scoreCells.add(new StartingListCell(hillNum, jumperDataArrayList.get(i), width, cellHeight));
        }

        Group group = createGroup(header, scoreCells);

        Container<Group> container = new Container<Group>();
        container.setSize(width,
                Math.max(height, group.getHeight()));
        container.setActor(group);
        container.top();

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setSize(width, height);

        addActor(scrollPane);
    }

    public Group createGroup(Actor header, List<Actor> actors) {
        ArrayList<Actor> widgets = new ArrayList<Actor>();
        widgets.add(header);
        widgets.add(new Divider(width, dividerHeight, Color.GRAY));
        for (Actor actor : actors) {
            widgets.add(actor);
            widgets.add(new Divider(width, dividerHeight, Color.GRAY));
        }

        float height = 0;
        for (Actor actor : widgets) {
            height += actor.getHeight();
        }

        Group group = new Group();
        group.setSize(width, height);

        for (Actor actor : widgets) {
            height -= actor.getHeight();
            actor.setY(height);
            group.addActor(actor);
        }

        return group;
    }
}
