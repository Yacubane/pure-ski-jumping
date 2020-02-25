package pl.cyfrogen.skijumping.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pl.cyfrogen.skijumping.data.HillSetup;
import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.debug.Logger;
import pl.cyfrogen.skijumping.game.effects.Snowing;
import pl.cyfrogen.skijumping.game.input.Pointer;
import pl.cyfrogen.skijumping.game.interfaces.OnUpdateListener;
import pl.cyfrogen.skijumping.game.map.shading.Shading;
import pl.cyfrogen.skijumping.game.map.shading.ShadingConfigurations;
import pl.cyfrogen.skijumping.game.physics.CollidingListener;
import pl.cyfrogen.skijumping.game.physics.Edge;
import pl.cyfrogen.skijumping.game.physics.EdgeGroup;
import pl.cyfrogen.skijumping.game.physics.Line;
import pl.cyfrogen.skijumping.game.physics.Point;
import pl.cyfrogen.skijumping.game.physics.SkiJumpPhysics;
import pl.cyfrogen.skijumping.game.physics.TickListener;
import pl.cyfrogen.skijumping.game.renderer.GameRenderer;
import pl.cyfrogen.skijumping.game.sound.SlidingSound;
import pl.cyfrogen.skijumping.hill.HillFile;
import pl.cyfrogen.skijumping.hill.HillGameObject;
import pl.cyfrogen.skijumping.hill.HillModel;
import pl.cyfrogen.skijumping.hill.PhysicsConfiguration;
import pl.cyfrogen.skijumping.jumper.JumperController;
import pl.cyfrogen.skijumping.jumper.JumperListener;
import pl.cyfrogen.skijumping.jumper.JumperOutfitTextures;
import pl.cyfrogen.skijumping.jumper.JumperPhysicObject;
import pl.cyfrogen.skijumping.jumper.OnJumperSlidingPosition;
import pl.cyfrogen.skijumping.jumper.body.JumperBody;

public class GameWorld {

    public static final float DEFAULT_VIEWPORT_WIDTH_HEIGHT_RATIO = 16f / 9f;
    public static final float DEFAULT_VIEWPORT_HEIGHT = 300;
    private final SlidingSound slidingSound;
    private final JumperOutfitTextures jumperOutfitTextures;
    private final Shading skiJumperShader;
    private final Shading snowParticlesShader;
    private final PhysicsConfiguration physicsConfiguration;
    private final int gate;
    private float deltaTimeMultiplier = 1f;
    private int noOfGates;
    ShadingConfigurations shadingConfigurations;
    private HillModel hillModel;
    private Box2DDebugRenderer box2drenderer;

    private static final float GRAVITY_G = -9.80f;
    public static final boolean DEBUG = false;

    private final GameRenderer gameRenderer;
    private final float wind;
    private final Sound takeoffSound;
    private Edge collidingEdge;

    private Map<Integer, Pointer> pointers = new HashMap<Integer, Pointer>();
    private boolean pause;
    private OnUpdateListener onUpdateListener;
    private boolean isDisposed;
    private Snowing snowing;
    private int lastMouseMoveY;

    public boolean isLandingEnabled() {
        return isLandingEnabled;
    }

    public void setLandingEnabled(boolean landingEnabled) {
        isLandingEnabled = landingEnabled;
    }

    private boolean isLandingEnabled = true;


    public SkiJumpPhysics getPhysics() {
        return physics;
    }

    private final SkiJumpPhysics physics;
    private final EdgeGroup inrunEdgeGroup;
    private final EdgeGroup landingEdgeGroup;

    private HillGameObject hillGameObject;

    private World world;

    private OrthographicCamera cam;

    private JumperController jumperController;
    private Body landingHillBody;

    public JumperListener getJumperListener() {
        return jumperListener;
    }

    private JumperListener jumperListener;
    boolean cameraStickToJumper = true;

    public GameWorld(HillFile hillFile, HillSetup.Snowing snowingType, HillSetup.Mode mode, final float wind) throws IOException {
        physicsConfiguration = PhysicsConfiguration.of(hillFile.getMetaData().get("physics"));
        gate = hillFile.getMetaData().get("defaultStartGate").intValue();
        noOfGates = hillFile.getMetaData().get("noOfStartGates").intValue();

        jumperOutfitTextures = new JumperOutfitTextures();

        JsonNode modeNode = hillFile.openModeData(mode);
        JsonNode hillModelNode = hillFile.openHillModel(mode);

        hillModel = new HillModel(hillFile.getMetaData(), hillModelNode);

        JsonNode shadingsNode = modeNode.get("shadings");
        shadingConfigurations = new ShadingConfigurations(shadingsNode);
        skiJumperShader = shadingConfigurations.getShaderConfiguration(modeNode.get("skiJumperShading").textValue());
        snowParticlesShader = shadingConfigurations.getShaderConfiguration(modeNode.get("effectShadings").get("snowParticlesShading").textValue());

        setupSnow(snowingType, wind);
        takeoffSound = Gdx.audio.newSound(Gdx.files.internal("sounds/takeoff_sound.ogg"));
        slidingSound = new SlidingSound();
        slidingSound.start();


        this.wind = wind;
        ShaderProgram.pedantic = false;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(DEFAULT_VIEWPORT_HEIGHT * (w / h), DEFAULT_VIEWPORT_HEIGHT);
        cam.zoom = 0.08f;

        gameRenderer = new GameRenderer();

        JsonNode outRunTopBorderVerticesJson = hillModelNode.get("outRunTopBorderVertices");
        ArrayList<Vector2> outRunTopBorderVertices = new ArrayList<Vector2>();

        for (Iterator<JsonNode> it = outRunTopBorderVerticesJson.iterator(); it.hasNext(); ) {
            JsonNode vertex = it.next();
            Iterator<JsonNode> coords = vertex.elements();
            outRunTopBorderVertices.add(new Vector2(
                    coords.next().floatValue(),
                    -coords.next().floatValue()));
        }

        JsonNode outRunBottomBorderVerticesJson = hillModelNode.get("outRunBottomBorderVertices");
        ArrayList<Vector2> outRunBottomBorderVertices = new ArrayList<Vector2>();

        for (Iterator<JsonNode> it = outRunBottomBorderVerticesJson.iterator(); it.hasNext(); ) {
            JsonNode vertex = it.next();
            Iterator<JsonNode> coords = vertex.elements();
            outRunBottomBorderVertices.add(new Vector2(
                    coords.next().floatValue(),
                    -coords.next().floatValue()));
        }

        try {
            hillGameObject = new HillGameObject(hillFile.getHillModelData(mode), hillModel, outRunTopBorderVertices, outRunBottomBorderVertices, modeNode, hillModelNode, shadingConfigurations, mode, gameRenderer, cam);
        } catch (IOException e) {
            e.printStackTrace();
        }

        physics = new SkiJumpPhysics();
        physics.setKineticFrictionCoefficient(physicsConfiguration.getKineticFrictionCoefficient());
        physics.pause();

        world = new World(new Vector2(0, GRAVITY_G), true);
        if (DEBUG)
            box2drenderer = new Box2DDebugRenderer();


        createBodyFromArray(hillModel.getInRunVertices(), "inrun");
        landingHillBody = createBodyFromArray(hillModel.getOutRunVertices(), "landing");

        inrunEdgeGroup = new EdgeGroup();
        {
            ArrayList<Vector2> inrun = hillModel.getInRunVertices();
            for (int i = 0; i < inrun.size() - 1; i++) {
                Point point1 = new Point(inrun.get(i));
                Point point2 = new Point(inrun.get(i + 1));
                inrunEdgeGroup.addEdge(new Edge(new Line(point1, point2)));
            }
        }

        landingEdgeGroup = new EdgeGroup();
        {
            double distance = 0;
            ArrayList<Vector2> landing = hillModel.getOutRunVertices();
            for (int i = 0; i < landing.size() - 1; i++) {
                Point point1 = new Point(landing.get(i));
                Point point2 = new Point(landing.get(i + 1));
                Edge edge = new Edge(new Line(point1, point2));

                edge.setUserData(distance);
                distance += point1.dst(point2);
                landingEdgeGroup.addEdge(edge);
            }
        }

        physics.addEdgeGroup(inrunEdgeGroup);
        physics.addEdgeGroup(landingEdgeGroup);


        cam.position.set(cam.viewportWidth / 2f - 120, 0, 0);
        cam.update();

        physics.setCollidingListener(new CollidingListener() {

            @Override
            public void startColliding(Edge collidingEdge, Point collisionPoint) {
                GameWorld.this.collidingEdge = collidingEdge;
                collision(collidingEdge, collisionPoint);
            }

            @Override
            public void endColliding(Edge collidingEdge, Point collisionPoint) {
                GameWorld.this.collidingEdge = null;

            }

            @Override
            public void nextColliding(Edge collidingEdge, Edge nextCollidingEdge, Point collisionPoint) {
                GameWorld.this.collidingEdge = collidingEdge;
                collision(collidingEdge, collisionPoint);

            }
        });

        physics.setTickListener(new TickListener() {

            @Override
            public void tickStart() {
                if (jumperController.isLaunched() && !jumperController.isLanded()) {
                    final double delta0 = MathUtils.degreesToRadians * physicsConfiguration.getDelta0();
                    final double k1 = physicsConfiguration.getK1();
                    final double maxKdegradation = physicsConfiguration.getK2maxDegradation();
                    double k2 = physicsConfiguration.getK2base()
                            + physicsConfiguration.getWindMultiplier() * wind
                            - maxKdegradation * (1 - jumperController.getGoodFlyingPositionPercent());
                    if (DEBUG) {
                        k2 = physicsConfiguration.getK2base()
                                + physicsConfiguration.getWindMultiplier() * wind
                                - maxKdegradation * (0);
                    }
                    final double tau = physicsConfiguration.getTau();


                    double flyingTime = jumperController.getFlyingTime() + physics.getTickTime();
                    jumperController.setFlyingTime(flyingTime);

                    float delta = (float) (delta0 * (1 - Math.pow(Math.E, -flyingTime / tau))); //orginal formula from paper
                    delta = (float) (delta0 * flyingTime / tau);
                    if (flyingTime > tau) {
                        jumperController.setFlyingTime(tau);
                        delta = (float) delta0;
                    }

                    float ydot = jumperController.getPhysicObject().getLinearVelocity().y;
                    float xdot = jumperController.getPhysicObject().getLinearVelocity().x;
                    float v = jumperController.getPhysicObject().getLinearVelocity().len();

                    float cosDelta0 = MathUtils.cos((float) delta0);
                    float cosDelta = MathUtils.cos(delta);
                    float sinDelta = MathUtils.sin(delta);

                    float k = (float) (k1 + (((k2 - k1) / cosDelta0) * (-ydot / v))); //orginal formula from paper
                    k = (float) (k1 + ((k2 - k1) * (delta / delta0)));
                    if (!jumperController.isFlying()) {
                        k = 0;
                    }

                    float mxdotdot = -k * v * xdot * cosDelta - k * v * ydot * sinDelta;
                    float mydotdot = -k * v * ydot * cosDelta + k * v * xdot * sinDelta;

                    jumperController.getPhysicObject().applyForce(new Vector2(mxdotdot, mydotdot));
                }
            }

            @Override
            public void tickEnd() {

            }
        });
    }

    public void setJumper(JumperData jumperData, int gate) {
        physics.pause();
        physics.reset();
        physics.setKineticFrictionCoefficient(physicsConfiguration.getKineticFrictionCoefficient());
        collidingEdge = null;

        cam.zoom = 0.04f;

        float startPosition = (gate - 1) / (float) (noOfGates - 1);

        if (jumperController != null) {
            jumperController.dispose();
        }
        jumperController = new JumperController(this, physicsConfiguration, hillModel,
                createJumperPhysicObject(startPosition),
                new JumperBody(jumperData, jumperOutfitTextures, 0.01f));
        slidingSound.setJumperController(jumperController);

        Vector2 startBarStart = hillModel.getStartBarAreaStartVertex();
        Vector2 startBarEnd = hillModel.getStartBarAreaEndVertex();

        float angle = startBarStart.cpy().sub(startBarEnd).angleRad() + MathUtils.PI;
        if (angle > MathUtils.PI) {
            angle -= MathUtils.PI2;
        }
        jumperController.setAngle(angle);
    }

    public void collision(Edge collisionEdge, Point collisionPoint) {
        if ((jumperController.isFlying() || jumperController.isPreparingToLand()) && jumperController.isLaunched()) {
            Logger.debug("RNJ: Collision " + jumperController.getState());
            double distance;
            if (collisionEdge.getUserData() == null) {
                distance = 0;
            } else {
                distance = (Double) collisionEdge.getUserData();
                distance += collisionEdge.getLine().getPoint1().dst(collisionPoint);
            }

            jumperController.setLanded((float) distance);

        } else if (jumperController.isSliding() && !jumperController.isLaunched() && collisionEdge.getUserData() != null) {
            Logger.debug("RNJ: Collision " + jumperController.getState());
            jumperController.setLandedWithCrash(0);
        }


    }

    private JumperPhysicObject createJumperPhysicObject(float startPosition) {
        Vector2 startBarStart = hillModel.getStartBarAreaStartVertex();
        Vector2 startBarEnd = hillModel.getStartBarAreaEndVertex();

        Vector2 position = new Vector2(startBarEnd).lerp(startBarStart, startPosition);
        hillGameObject.setGatePosition(position);

        Vector2 angle = new Vector2(1, 0);

        angle.rotateRad(startBarStart.cpy().sub(startBarEnd).angleRad());
        position.add(0, 0.001f);


        return new JumperPhysicObject(physics, position);
    }

    private Body createBodyFromArray(ArrayList<Vector2> points, String text) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;


        float minY = points.get(0).y;

        for (Vector2 vector2 : points) {
            if (vector2.y < minY) {
                minY = vector2.y;
            }
        }

        float thresholdX = 0.02f;
        float thresholdY = 3;

        Body body = world.createBody(bodyDef);
        for (int i = 1; i < points.size(); i++) {
            Vector2 point1 = new Vector2(points.get(i - 1));


            Vector2 point2 = new Vector2(points.get(i));

            Vector2 point1to2 = point1.cpy().lerp(point2, 0.05f);
            Vector2 point2to1 = point1.cpy().lerp(point2, 0.95f);
            point1.x -= thresholdX;
            point2.x += thresholdX;
            point1.y -= thresholdX;
            point2.y -= thresholdX;

            Vector2 point3 = new Vector2(point2);
            point3.y = minY - thresholdY;

            Vector2 point4 = new Vector2(point1);
            point4.y = minY - thresholdY;

            PolygonShape polygonShape = new PolygonShape();
            polygonShape.set(new Vector2[]{point1, point1to2, point2to1, point2, point3, point4});


            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.density = 1f;
            fixtureDef.restitution = 0;
            fixtureDef.friction = 0f;
            body.createFixture(fixtureDef);
            polygonShape.dispose();

        }
        return body;

    }


    public void update(float deltaTime) {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            updateKeys();

        deltaTime *= deltaTimeMultiplier;

        if (!pause) {
            stepWorld(deltaTime);
            if (jumperController != null) {
                if (!jumperController.isLaunched() && collidingEdge != null) {
                    if (collidingEdge.prevLine != null && collidingEdge.nextLine != null) {
                        float prevLineAngle = collidingEdge.getAngle();
                        float nextLineAngle = collidingEdge.nextLine.getAngle();

                        double percentage = collidingEdge.line.getPoint1().dst(physics.getJumperPosition()) /
                                collidingEdge.line.getPoint1().dst(collidingEdge.line.getPoint2());

                        jumperController.setAngle(MathUtils.lerp(prevLineAngle, nextLineAngle, (float) percentage));

                    } else {
                        jumperController.setAngle(collidingEdge.getAngle());
                    }

                }

                jumperController.update(deltaTime);
            }

            if (onUpdateListener != null)
                onUpdateListener.onUpdate(jumperController);


            if (jumperController != null && cameraStickToJumper)
                jumperController.getJumperBody().setCameraValues(cam);
            cam.update();

            if (snowing != null)
                snowing.update(cam, deltaTime);

            hillGameObject.updateParalaxes();
        }

    }

    public void draw() {
        hillGameObject.renderBackground();

        if (jumperController != null) {
            gameRenderer.beginSpriteBatch();
            jumperController.getJumperBody().drawShadow(cam, physics, gameRenderer.getSpriteBatch());
            skiJumperShader.apply(gameRenderer.getSpriteBatch().getShader());
            jumperController.getJumperBody().draw(gameRenderer.getSpriteBatch());
        }

        hillGameObject.renderForeground();
        gameRenderer.beginSpriteBatch();
        snowParticlesShader.apply(gameRenderer.getSpriteBatch().getShader());
        if (snowing != null)
            snowing.draw(cam, gameRenderer.getSpriteBatch());
        gameRenderer.end();

        if (DEBUG) {
            box2drenderer.render(world, cam.combined);
        }
    }

    private void updateKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            cameraStickToJumper = !cameraStickToJumper;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            deltaTimeMultiplier -= 0.1f;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            deltaTimeMultiplier += 0.1f;
        }

        float speed = 0.1f;

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            speed = 0.02f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed = 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.position.x -= speed;
            cam.update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.position.x += speed;
            cam.update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.position.y += speed;
            cam.update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.position.y -= speed;
            cam.update();
        }
    }

    private void setupSnow(HillSetup.Snowing snowingType, float wind) {
        if (snowingType != HillSetup.Snowing.NO) {
            snowing = new Snowing(-wind * 4.5f, -10, snowingType == HillSetup.Snowing.SOFT ? 60 : 120);
        }

    }

    public void stepWorld(float time) {
        world.step(time, 6, 2);
        physics.update(time);

    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public void notifyTouchDown(int screenX, int screenY, int pointerId, int button) {
        Pointer pointer = getOrCreatePointer(pointerId);
        pointer.updateState(Pointer.State.TOUCH_DOWN, screenX, screenY);
    }

    public boolean touchDown(int screenX, int screenY, int pointerId, int button) {
        if (jumperListener == null) return false;
        Pointer pointer = getOrCreatePointer(pointerId);

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (button == 0 && jumperController.isSliding()) {
                jumperController.setLaunch(1);
            }

            if (button == 1) {
                jumperController.setLanding();
                jumperController.updateLanding(true);
            }
        }

        for (Map.Entry<Integer, Pointer> pointerEntry : pointers.entrySet()) {
            Pointer pointer2 = pointerEntry.getValue();
            if (pointerEntry.getKey() != pointerId &&
                    (pointer2.getState() == Pointer.State.TOUCH_DOWN ||
                            pointer2.getState() == Pointer.State.TOUCH_DRAGGED)) {

                long time = System.currentTimeMillis() - pointer2.getLastTouchDownTime();
                float launchStrength = MathUtils.lerp(1f, 0f, (time - 40) / (float) 500);
                launchStrength = MathUtils.clamp(launchStrength, 0, 1);
                if (jumperController.isSliding() && !physics.isStopped()) {
                    jumperController.setLaunch(launchStrength);
                } else if (jumperController.isFlying()) {
                    if (isLandingEnabled) {
                        jumperController.setLanding();
                    }
                }
            }

        }

        pointer.updateState(Pointer.State.TOUCH_DOWN, screenX, screenY);
        return false;
    }

    private Pointer getOrCreatePointer(int pointerId) {
        Pointer pointer = pointers.get(pointerId);
        if (pointer != null) return pointer;

        pointer = new Pointer();
        pointers.put(pointerId, pointer);
        return pointer;
    }

    public void notifyTouchUp(int screenX, int screenY, int pointerId, int button) {
        Pointer pointer = getOrCreatePointer(pointerId);
        pointer.updateState(Pointer.State.TOUCH_UP, screenX, screenY);
    }

    public boolean touchUp(int screenX, int screenY, int pointerId, int button) {
        if (jumperListener == null) return false;
        Pointer pointer = getOrCreatePointer(pointerId);

        boolean isAnyOtherActiveTouch = false;
        for (Map.Entry<Integer, Pointer> pointerEntry : pointers.entrySet()) {
            Pointer pointer2 = pointerEntry.getValue();
            if (pointerEntry.getKey() != pointerId &&
                    (pointer2.getState() == Pointer.State.TOUCH_DOWN ||
                            pointer2.getState() == Pointer.State.TOUCH_DRAGGED)) {
                isAnyOtherActiveTouch = true;
            }

        }

        boolean isTap = System.currentTimeMillis() - pointer.getLastTouchDownTime() < 500;
        if (!isAnyOtherActiveTouch && isTap && jumperListener != null) {
            if (physics.isStopped() && jumperController.isOnStartingPlatform()) {
                startJumper();
            }
        }

        pointer.updateState(Pointer.State.TOUCH_UP, screenX, screenY);
        return false;
    }

    public void startJumper() {
        jumperListener.jumperStartedRide();
        jumperController.startJumper(new OnJumperSlidingPosition() {

            @Override
            public void enablePhysic() {
                physics.resume();
            }

        });
    }

    public boolean touchDragged(int screenX, int screenY, int pointerId) {
        if (jumperListener == null) return false;

        Pointer pointer = getOrCreatePointer(pointerId);

        if (pointer.getState() != Pointer.State.TOUCH_DOWN &&
                pointer.getState() != Pointer.State.TOUCH_DRAGGED)
            return true;

        if (jumperController.isLanding()) {
            float dy = screenY - pointer.getTouchDownPosition().y;
            float perentage = dy / Gdx.graphics.getHeight();
            if (perentage > 0.1f) {
                jumperController.updateLanding(true);
            }
        }

        for (Map.Entry<Integer, Pointer> pointerEntry : pointers.entrySet()) {
            Pointer pointer2 = pointerEntry.getValue();
            if (pointerEntry.getKey() != pointerId &&
                    (pointer2.getState() == Pointer.State.TOUCH_DOWN
                            || pointer2.getState() == Pointer.State.TOUCH_DRAGGED)) {
                float prevDistance = pointer.getPosition().dst(pointer2.getPosition());
                float newDistance = new Vector2(screenX, screenY).dst(pointer2.getPosition());
                float diff = newDistance / prevDistance;

                cam.zoom /= diff;
                cam.zoom = MathUtils.clamp(cam.zoom, 0.015f, 0.1f);
            }
        }

        if (jumperController.isFlying()) {
            float dy = screenY - pointer.getPosition().y;


            if (jumperController.isLaunched()) {
                dy /= Gdx.graphics.getHeight() * 0.08f;
                jumperController.movePosition(dy);
            }
        }

        pointer.updateState(Pointer.State.TOUCH_DRAGGED, screenX, screenY);
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        if (jumperController != null)
            if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                if (jumperController.isLaunched()) {
                    float deltaY = screenY - lastMouseMoveY;
                    deltaY /= Gdx.graphics.getHeight() * 0.15f;
                    jumperController.movePosition(deltaY);
                }

                lastMouseMoveY = screenY;
            }
        return false;
    }

    public boolean scrolled(int amount) {
        cam.zoom += amount / (float) 100;
        return false;
    }

    public void setHillFriction(float hillFriction) {
        for (Fixture fixture : landingHillBody.getFixtureList()) {
            fixture.setFriction(hillFriction);
        }
    }

    public World getWorld() {
        return world;
    }

    public void setJumperListener(JumperListener jumperListener) {
        this.jumperListener = jumperListener;
    }

    public void dispose() {
        if (!isDisposed) {
            if (jumperController != null) {
                jumperController.dispose();
            }
            jumperOutfitTextures.dispose();

            gameRenderer.dispose();
            hillGameObject.dispose();
            world.dispose();
            takeoffSound.dispose();
            slidingSound.dispose();

        }
        slidingSound.setDisposed(true);
        isDisposed = true;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setOnUpdate(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    public boolean isPaused() {
        return pause;
    }

    public JumperController getJumperController() {
        return jumperController;
    }

    public void onTakeoff() {
        takeoffSound.play(0.7f);
    }

    public HillModel getHillModel() {
        return hillModel;
    }
}
