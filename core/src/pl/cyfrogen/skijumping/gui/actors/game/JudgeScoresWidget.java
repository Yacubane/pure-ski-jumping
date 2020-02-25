package pl.cyfrogen.skijumping.gui.actors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;

import pl.cyfrogen.skijumping.gui.actors.common.Divider;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;
import pl.cyfrogen.skijumping.jumper.judge.JudgeScores;


public class JudgeScoresWidget extends Group {
    private final ArrayList<Actor> actorList;
    private float dividerHeight = Math.min(Gdx.graphics.getHeight() * 0.003f, 1);

    public JudgeScoresWidget(JudgeScores scores) {
        setTouchable(Touchable.disabled);
        float oneScoreHeight = Gdx.graphics.getHeight() * 0.05f;
        float width = Gdx.graphics.getWidth() * 0.1f;

        float height = oneScoreHeight * scores.size() + dividerHeight * (scores.size() - 1);

        setSize(width, height);

        actorList = new ArrayList<Actor>();
        for (int i = 0; i < scores.size(); i++) {
            int score = scores.getJudgeScore(i).getPoints();
            Group group = new Group();
            group.setSize(width, oneScoreHeight);

            JudgeScoreWidget judgeScoreWidget = new JudgeScoreWidget(width, oneScoreHeight, score);

            if (i != scores.size() - 1) {
                group.setHeight(group.getHeight() + dividerHeight);
                judgeScoreWidget.setY(dividerHeight);
                Divider divider = new Divider(width, dividerHeight, Color.WHITE);
                group.addActor(divider);
            }

            group.addActor(judgeScoreWidget);

            if (!scores.getJudgeScore(i).isActive()) {
                Divider divider2 = new Divider(width * 0.8f, group.getHeight() * 0.05f, Color.WHITE);
                divider2.setPosition(group.getWidth() / 2f - divider2.getWidth() / 2f, group.getHeight() / 2f - divider2.getHeight() / 2f);
                group.addActor(divider2);
            }

            actorList.add(group);


        }

        addActor(MainMenuUtils.verticalGroupOf(actorList));
    }

    public void showWithAnimation() {
        for (int i = 0; i < actorList.size(); i++) {
            Actor actor = actorList.get(i);
            actor.setColor(Color.CLEAR);
            actor.addAction(Actions.sequence(
                    Actions.alpha(1f, 0.2f * (actorList.size() - i - 1), Interpolation.smooth)
            ));
        }
    }
}
