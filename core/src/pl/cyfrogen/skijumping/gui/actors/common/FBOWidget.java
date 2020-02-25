package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import pl.cyfrogen.skijumping.Main;

public class FBOWidget extends Group {
    private FrameBuffer fbo;
    private TextureRegion fboRegion;
    private boolean fallback;
    private int frame;
    private float lastAlpha = -1;

    public FBOWidget() {
        try {
            fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
            fboRegion = new TextureRegion(fbo.getColorBufferTexture());
            fboRegion.flip(false, true);
        } catch (Exception e) {
            Main.getInstance().getPlatformAPI().logCrashString("width", String.valueOf(Gdx.graphics.getWidth()));
            Main.getInstance().getPlatformAPI().logCrashString("height", String.valueOf(Gdx.graphics.getHeight()));
            Main.getInstance().getPlatformAPI().logCrash(e);
            fallback = true;
        }

        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static FBOWidget of(Actor actor) {
        FBOWidget FBOWidget = new FBOWidget();
        FBOWidget.setSize(actor.getWidth(), actor.getHeight());
        FBOWidget.addActor(actor);
        return FBOWidget;
    }


    private void renderFBO(Batch batch, float parentAlpha) {
        batch.end();
        fbo.begin();

        // we need to first clear our FBO with transparent black
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // start our batch
        batch.begin();

        // use -1 to ignore.. somebody should fix this in LibGDX :\
        batch.setBlendFunction(-1, -1);

        // setup our alpha blending to avoid blending twice
        Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA,
                GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);

        // draw sprites
        super.draw(batch, 1f);

        // end (flush) our batch
        batch.end();

        // unbind the FBO
        fbo.end();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();
        // now let's reset blending to the default...
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!fallback) {
            if (Math.abs(parentAlpha - lastAlpha) > 0.001f) {
                renderFBO(batch, parentAlpha);
                batch.setColor(1f, 1f, 1f, parentAlpha * getColor().a);
                Vector2 a = getParent().localToStageCoordinates(new Vector2(0, 0));
                batch.draw(fboRegion, -a.x, -a.y);
                lastAlpha = parentAlpha;
            } else {
                super.draw(batch, parentAlpha);
            }
        } else {
            super.draw(batch, parentAlpha);
        }

    }
}

