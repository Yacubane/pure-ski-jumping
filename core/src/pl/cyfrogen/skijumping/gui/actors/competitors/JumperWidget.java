package pl.cyfrogen.skijumping.gui.actors.competitors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.jumper.JumperOutfitTextures;
import pl.cyfrogen.skijumping.jumper.body.JumperBody;
import pl.cyfrogen.skijumping.jumper.body.JumperBodyPartBuilder;

public class JumperWidget extends Group {
    private final JumperData jumperData;
    private final ShaderProgram shaderProgram;
    private JumperBody jumperBody;

    public JumperWidget(JumperData jumperData, float width, float height) {
        setSize(width, height);
        this.jumperData = jumperData;

        refresh();


        shaderProgram = new ShaderProgram(
                Gdx.files.internal("shaders/jumper_menu_vertex.glsl").readString(),
                Gdx.files.internal("shaders/jumper_menu_fragment.glsl").readString()
        );

        addActor(new Actor(){
            @Override
            public void draw (Batch batch, float parentAlpha) {
                ShaderProgram prevShader = batch.getShader();
                batch.setShader(shaderProgram);
                shaderProgram.setUniformf("alpha", JumperWidget.this.getColor().a);
                jumperBody.draw(batch);
                batch.setShader(prevShader);

            }
        });

    }

    public void refresh() {
        JumperOutfitTextures jumperOutfitTextures = new JumperOutfitTextures();
        JumperBodyPartBuilder bodyPartBuilder = new JumperBodyPartBuilder(jumperOutfitTextures);

        float jumperStandardWidth = bodyPartBuilder.maxJumperX - bodyPartBuilder.minJumperX;
        float jumperStandardHeight = bodyPartBuilder.maxJumperY - bodyPartBuilder.minJumperY;

        float scaleX = getWidth() / jumperStandardWidth;
        float scaleY = getHeight() / jumperStandardHeight;

        float scale = Math.min(scaleX, scaleY);

        float newWidth = jumperStandardWidth * scale;
        float newHeight = jumperStandardHeight * scale;

        float newX = -bodyPartBuilder.minJumperX * scale + (getWidth() - newWidth) /2f;
        float newY = -bodyPartBuilder.minJumperY * scale + (getHeight() - newHeight) /2f;


        jumperBody = new JumperBody(jumperData, new JumperOutfitTextures(), scale);
        jumperBody.setPosition(new Vector2(newX, newY));
        jumperBody.updateDontCenter();
    }

    public void dispose() {
        shaderProgram.dispose();
    }
}
