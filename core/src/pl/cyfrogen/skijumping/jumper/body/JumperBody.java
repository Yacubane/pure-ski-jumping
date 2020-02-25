package pl.cyfrogen.skijumping.jumper.body;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.character.BodyPart;
import pl.cyfrogen.skijumping.character.MeshBuilder;
import pl.cyfrogen.skijumping.character.MeshVertex;
import pl.cyfrogen.skijumping.character.PointManipulation;
import pl.cyfrogen.skijumping.character.UVMapper;
import pl.cyfrogen.skijumping.character.VectorUtils;
import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.game.physics.Point;
import pl.cyfrogen.skijumping.game.physics.RayCastResult;
import pl.cyfrogen.skijumping.game.physics.SkiJumpPhysics;
import pl.cyfrogen.skijumping.jumper.JumperOutfitTextures;
import pl.cyfrogen.skijumping.jumper.animation.AnimationFrame;

public class JumperBody {
    private final SpriteContainer leftBootSprite;
    private final Sprite leftGloveSprite;

    private final MeshBuilder torsoMesh;
    private final MeshBuilder leftLegMesh;
    private final MeshBuilder leftArmMesh;
    private final MeshBuilder leftSkiMesh;
    private final MeshBuilder rightLegMesh;
    private final MeshBuilder rightArmMesh;
    private final MeshBuilder rightSkiMesh;

    private final BodyPart upperTorso;
    private final BodyPart lowerTorso;
    private final BodyPart leftUpperLeg;
    private final BodyPart leftLowerLeg;
    private final BodyPart leftAnkle;
    private final BodyPart leftBootBodyPart;
    private final BodyPart neck;
    private final BodyPart leftShoulder;
    private final BodyPart leftForearm;
    private final BodyPart leftWrist;
    private final BodyPart leftSki;
    private final BodyPart rightUpperLeg;
    private final BodyPart rightLowerLeg;
    private final BodyPart rightAnkle;
    private final BodyPart rightBootBodyPart;
    private final BodyPart rightShoulder;
    private final BodyPart rightForearm;
    private final BodyPart rightWrist;
    private final BodyPart rightSki;
    private final SpriteContainer rightBootSprite;
    private final Sprite rightGloveSprite;
    private final JumperOutfitTextures textures;

    public JumperBody(JumperData jumperData, JumperOutfitTextures textures, float scale) {
        this.textures = textures;

        JumperBodyPartBuilder partBuilder = new JumperBodyPartBuilder(textures);

        upperTorso = partBuilder.getUpperTorso();
        lowerTorso = partBuilder.getLowerTorso();
        neck = partBuilder.getNeck();

        leftUpperLeg = partBuilder.getUpperLeg();
        leftLowerLeg = partBuilder.getLowerLeg();
        leftAnkle = partBuilder.getAnkle();
        leftBootBodyPart = partBuilder.getBoot();
        leftShoulder = partBuilder.getShoulder();
        leftForearm = partBuilder.getForearm();
        leftWrist = partBuilder.getWrist();
        leftSki = partBuilder.getSki();

        rightUpperLeg = partBuilder.getUpperLeg();
        rightLowerLeg = partBuilder.getLowerLeg();
        rightAnkle = partBuilder.getAnkle();
        rightBootBodyPart = partBuilder.getBoot();
        rightShoulder = partBuilder.getShoulder();
        rightForearm = partBuilder.getForearm();
        rightWrist = partBuilder.getWrist();
        rightSki = partBuilder.getSki();


        upperTorso.addBodyPart(lowerTorso);

        lowerTorso.addBodyPart(leftUpperLeg);
        leftUpperLeg.addBodyPart(leftLowerLeg);
        leftLowerLeg.addBodyPart(leftAnkle);
        leftAnkle.addBodyPart(leftBootBodyPart);

        lowerTorso.addBodyPart(rightUpperLeg);
        rightUpperLeg.addBodyPart(rightLowerLeg);
        rightLowerLeg.addBodyPart(rightAnkle);
        rightAnkle.addBodyPart(rightBootBodyPart);


        upperTorso.addBodyPart(neck);

        upperTorso.addBodyPart(leftShoulder);
        leftShoulder.addBodyPart(leftForearm);
        leftForearm.addBodyPart(leftWrist);

        upperTorso.addBodyPart(rightShoulder);
        rightShoulder.addBodyPart(rightForearm);
        rightForearm.addBodyPart(rightWrist);

        upperTorso.scale(scale);
        upperTorso.reset();
        upperTorso.calculate(new Vector2());

        leftSki.scale(scale);
        leftSki.reset();
        leftSki.calculate(new Vector2());

        rightSki.scale(scale);
        rightSki.reset();
        rightSki.calculate(new Vector2());

        leftBootSprite = new SpriteContainer(73.223f, 2.336f, 39.351f, 14.042f, scale);
        leftBootSprite.add(textures.getBootTexture(), jumperData.getColors().getBootColor().getColor(), 73.223f, 2.336f, 39.351f, 14.042f);
        leftBootSprite.add(textures.getBootStripes(), Color.BLACK, 88.760f, 2.354f, 19.823f, 11.679f);

        rightBootSprite = leftBootSprite.clone();

        head = new SpriteContainer(73.123f, 159.242f, 24.1f, 24.7f, scale);
        head.add(textures.getHelmetTexture(), jumperData.getColors().getHelmetColor().getColor(), 73.123f, 161.098f, 21.894f, 22.836f);
        head.add(textures.getFaceTexture(), jumperData.getColors().getSkinColor().getColor(), 75.882f, 159.301f, 21.393f, 9.618f);
        head.add(textures.getGoggleBandTexture(), jumperData.getColors().getGoggleBandColor().getColor(), 73.123f, 167.396f, 23.102f, 8.818f);
        head.add(textures.getGoggleTexture(), jumperData.getColors().getGoggleColor().getColor(), 93.237f, 168.852f, 2.426f, 7.391f);
        head.add(textures.getHelmetBandTexture(), Color.BLACK, 87.363f, 159.242f, 4.367f, 3.955f);

        leftGloveSprite = new Sprite(textures.getGloveTexture());
        leftGloveSprite.setColor(jumperData.getColors().getGloveColor().getColor());
        leftGloveSprite.setSize(15.5f * scale, 21.6f * scale);
        leftGloveSprite.setOrigin(0, 0);

        rightGloveSprite = new Sprite(textures.getGloveTexture());
        rightGloveSprite.setColor(jumperData.getColors().getGloveColor().getColor());
        rightGloveSprite.setSize(15.5f * scale, 21.6f * scale);
        rightGloveSprite.setOrigin(0, 0);

        torsoMesh = new MeshBuilder(
                new UVMapper().map(textures.getWhiteDot(), textures.getShirtColor(),
                        0, 2, 2,
                        upperTorso.getStaticVertices(),
                        lowerTorso.getJointVertices(),
                        lowerTorso.getStaticVertices()
                ),
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getBodyLeftColor().getColor().toFloatBits(),
                        0, 1, 1,
                        neck.getJointVertices()),
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getBodyRightColor().getColor().toFloatBits(),
                        1, 2, 1,
                        neck.getJointVertices()));

        MeshVertex[][][] leftLegMeshVertices = new MeshVertex[][][]{
                leftUpperLeg.getJointVertices(), leftUpperLeg.getStaticVertices(),
                leftLowerLeg.getJointVertices(), leftLowerLeg.getStaticVertices(),
                leftAnkle.getJointVertices(), leftAnkle.getStaticVertices(),
                leftBootBodyPart.getJointVertices(), leftBootBodyPart.getStaticVertices()
        };

        leftLegMesh = new MeshBuilder(
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getBodyLeftColor().getColor().toFloatBits(),
                        0, 1, 1, leftLegMeshVertices),
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getBodyRightColor().getColor().toFloatBits(),
                        1, 2, 1, leftLegMeshVertices));

        MeshVertex[][][] leftArmMeshVertices = new MeshVertex[][][]{
                leftShoulder.getStaticVertices(),
                leftForearm.getJointVertices(), leftForearm.getStaticVertices(),
                leftWrist.getJointVertices()
        };

        leftArmMesh = new MeshBuilder(
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getArmLeftColor().getColor().toFloatBits(),
                        0, 1, 1, leftArmMeshVertices),
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getArmRightColor().getColor().toFloatBits(),
                        1, 2, 1, leftArmMeshVertices)
        );

        leftSkiMesh = new MeshBuilder(
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getSkiColor().getColor().toFloatBits(),
                        0, 1, 1, leftSki.getStaticVertices()));

        MeshVertex[][][] rightLegMeshVertices = new MeshVertex[][][]{
                rightUpperLeg.getJointVertices(), rightUpperLeg.getStaticVertices(),
                rightLowerLeg.getJointVertices(), rightLowerLeg.getStaticVertices(),
                rightAnkle.getJointVertices(), rightAnkle.getStaticVertices(),
                rightBootBodyPart.getJointVertices(), rightBootBodyPart.getStaticVertices(),
        };

        rightLegMesh = new MeshBuilder(
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getBodyLeftColor().getColor().toFloatBits(),
                        0, 1, 1, rightLegMeshVertices),
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getBodyRightColor().getColor().toFloatBits(),
                        1, 2, 1, rightLegMeshVertices));

        MeshVertex[][][] rightArmMeshVertices = new MeshVertex[][][]{
                rightShoulder.getStaticVertices(),
                rightForearm.getJointVertices(), rightForearm.getStaticVertices(),
                rightWrist.getJointVertices()
        };

        rightArmMesh = new MeshBuilder(
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getArmLeftColor().getColor().toFloatBits(),
                        0, 1, 1, rightArmMeshVertices),
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getArmRightColor().getColor().toFloatBits(),
                        1, 2, 1, rightArmMeshVertices)
        );

        rightSkiMesh = new MeshBuilder(
                new UVMapper().map(textures.getWhiteDot(), jumperData.getColors().getSkiColor().getColor().toFloatBits(),
                        0, 1, 1, rightSki.getStaticVertices()));
    }

    public BodyPart getUpperTorso() {
        return upperTorso;
    }

    public BodyPart getLowerTorso() {
        return lowerTorso;
    }

    public BodyPart getLeftUpperLeg() {
        return leftUpperLeg;
    }

    public BodyPart getLeftLowerLeg() {
        return leftLowerLeg;
    }

    public BodyPart getLeftAnkle() {
        return leftAnkle;
    }

    public BodyPart getLeftBootBodyPart() {
        return leftBootBodyPart;
    }

    public BodyPart getNeck() {
        return neck;
    }

    public BodyPart getLeftShoulder() {
        return leftShoulder;
    }

    public BodyPart getLeftForearm() {
        return leftForearm;
    }

    public BodyPart getLeftWrist() {
        return leftWrist;
    }

    public BodyPart getLeftSki() {
        return leftSki;
    }

    public BodyPart getRightUpperLeg() {
        return rightUpperLeg;
    }

    public BodyPart getRightLowerLeg() {
        return rightLowerLeg;
    }

    public BodyPart getRightAnkle() {
        return rightAnkle;
    }

    public BodyPart getRightBootBodyPart() {
        return rightBootBodyPart;
    }

    public BodyPart getRightShoulder() {
        return rightShoulder;
    }

    public BodyPart getRightForearm() {
        return rightForearm;
    }

    private final SpriteContainer head;
    private Vector2 position = new Vector2(0, 0);
    private boolean box2dPhysic;

    public BodyPart getRightWrist() {
        return rightWrist;
    }

    private float time;
    private float damping = 3;
    private float speed = 20f;
    private float maxValue = 0f;
    private boolean disposed;

    public BodyPart getRightSki() {
        return rightSki;
    }

    public void update(float deltaTime) {
        time += deltaTime;
        if (box2dPhysic) {
            position.set(leftBootBodyPart.getBox2dBody().getPosition().cpy());
        }

        rightSki.reset();
        leftSki.reset();
        upperTorso.reset();

        upperTorso.setAngle(upperTorso.getLocalAngle());
        lowerTorso.setAngle(lowerTorso.getLocalAngle());

        leftUpperLeg.setAngle(leftUpperLeg.getLocalAngle());
        leftLowerLeg.setAngle(leftLowerLeg.getLocalAngle());
        leftAnkle.setAngle(leftAnkle.getLocalAngle());
        rightUpperLeg.setAngle(rightUpperLeg.getLocalAngle());
        rightLowerLeg.setAngle(rightLowerLeg.getLocalAngle());
        rightAnkle.setAngle(rightAnkle.getLocalAngle());

        neck.setAngle(neck.getLocalAngle());
        leftShoulder.setAngle(leftShoulder.getLocalAngle());
        leftForearm.setAngle(leftForearm.getLocalAngle());
        leftWrist.setAngle(leftWrist.getLocalAngle());
        leftSki.setAngle(leftSki.getLocalAngle());
        leftBootBodyPart.setAngle(leftBootBodyPart.getLocalAngle());

        rightShoulder.setAngle(rightShoulder.getLocalAngle());
        rightForearm.setAngle(rightForearm.getLocalAngle());
        rightWrist.setAngle(rightWrist.getLocalAngle());
        rightSki.setAngle(rightSki.getLocalAngle());
        rightBootBodyPart.setAngle(rightBootBodyPart.getLocalAngle());


        upperTorso.calculate(position);

        PointManipulation pointManipulation = new PointManipulation() {
            @Override
            public void manipulatePoint(Vector2 point) {
                float force = 0.15f;
                force = (float) (maxValue * MathUtils.cos(time * speed) * Math.exp(-damping * time));
                {
                    float perc = (point.x - 1.50f) / (2.50f - 1.50f);
                    if (perc > 1f) perc = 1f;
                    if (perc < 0f) perc = 0;

                    Vector2 rotationOrigin = new Vector2(1.5f, 0.01f);
                    point.sub(rotationOrigin);
                    VectorUtils.fastRotateRad(point, force * perc).add(rotationOrigin);
                }

                {
                    float perc = 1 - (point.x / 1.20f);
                    if (perc > 1f) perc = 1f;
                    if (perc < 0f) perc = 0;

                    Vector2 rotationOrigin = new Vector2(1.5f, 0.01f);
                    point.sub(rotationOrigin);
                    VectorUtils.fastRotateRad(point, -force * perc).add(rotationOrigin);
                }

            }
        };
        leftSki.calculateAndManipulate(position, pointManipulation);
        rightSki.calculateAndManipulate(position, pointManipulation);

        {
            Vector2 skiPktZaczepienia = leftSki.getOrientationPoints().get(2);
            Vector2 bootPktZaczepienia = leftBootBodyPart.getOrientationPoints().get(1);

            Vector2 move = bootPktZaczepienia.cpy().sub(skiPktZaczepienia);
            leftSki.move(move);
        }
        {
            Vector2 skiPktZaczepienia = rightSki.getOrientationPoints().get(2);
            Vector2 bootPktZaczepienia = rightBootBodyPart.getOrientationPoints().get(1);

            Vector2 move = bootPktZaczepienia.cpy().sub(skiPktZaczepienia);
            rightSki.move(move);
        }
        {
            Vector2 pktZaczepienia = leftBootBodyPart.getOrientationPoints().get(2).cpy()
                    .lerp(rightBootBodyPart.getOrientationPoints().get(2), 0.5f);
            Vector2 pktZaczepienia2 = position;

            Vector2 move = pktZaczepienia2.cpy().sub(pktZaczepienia);
            upperTorso.move(move);
            leftSki.move(move);
            rightSki.move(move);
        }


        leftBootSprite.setRotation(leftBootBodyPart.getAngle() * MathUtils.radDeg);
        leftBootSprite.setPosition(leftBootBodyPart.getOrientationPoints().get(0).x, leftBootBodyPart.getOrientationPoints().get(0).y);

        head.setRotation(neck.getAngle() * MathUtils.radDeg);
        head.setPosition(neck.getOrientationPoints().get(0).x, neck.getOrientationPoints().get(0).y);

        rightBootSprite.setRotation(rightBootBodyPart.getAngle() * MathUtils.radDeg);
        rightBootSprite.setPosition(rightBootBodyPart.getOrientationPoints().get(0).x, rightBootBodyPart.getOrientationPoints().get(0).y);


        leftGloveSprite.setRotation(leftWrist.getAngle() * MathUtils.radDeg);
        leftGloveSprite.setPosition(leftWrist.getOrientationPoints().get(0).x, leftWrist.getOrientationPoints().get(0).y);


        rightGloveSprite.setRotation(rightWrist.getAngle() * MathUtils.radDeg);
        rightGloveSprite.setPosition(rightWrist.getOrientationPoints().get(0).x, rightWrist.getOrientationPoints().get(0).y);

        torsoMesh.rebuild();
        leftArmMesh.rebuild();
        leftSkiMesh.rebuild();
        leftLegMesh.rebuild();
        rightArmMesh.rebuild();
        rightSkiMesh.rebuild();
        rightLegMesh.rebuild();
    }

    public void updateDontCenter() {
        if (box2dPhysic) {
            position.set(leftBootBodyPart.getBox2dBody().getPosition().cpy());
        }

        rightSki.reset();
        leftSki.reset();
        upperTorso.reset();

        upperTorso.setAngle(upperTorso.getLocalAngle());
        lowerTorso.setAngle(lowerTorso.getLocalAngle());

        leftUpperLeg.setAngle(leftUpperLeg.getLocalAngle());
        leftLowerLeg.setAngle(leftLowerLeg.getLocalAngle());
        leftAnkle.setAngle(leftAnkle.getLocalAngle());
        rightUpperLeg.setAngle(rightUpperLeg.getLocalAngle());
        rightLowerLeg.setAngle(rightLowerLeg.getLocalAngle());
        rightAnkle.setAngle(rightAnkle.getLocalAngle());

        neck.setAngle(neck.getLocalAngle());
        leftShoulder.setAngle(leftShoulder.getLocalAngle());
        leftForearm.setAngle(leftForearm.getLocalAngle());
        leftWrist.setAngle(leftWrist.getLocalAngle());
        leftSki.setAngle(leftSki.getLocalAngle());
        leftBootBodyPart.setAngle(leftBootBodyPart.getLocalAngle());

        rightShoulder.setAngle(rightShoulder.getLocalAngle());
        rightForearm.setAngle(rightForearm.getLocalAngle());
        rightWrist.setAngle(rightWrist.getLocalAngle());
        rightSki.setAngle(rightSki.getLocalAngle());
        rightBootBodyPart.setAngle(rightBootBodyPart.getLocalAngle());


        upperTorso.calculate(position);
        leftSki.calculate(position);
        rightSki.calculate(position);

        {
            Vector2 skiPktZaczepienia = leftSki.getOrientationPoints().get(2);
            Vector2 bootPktZaczepienia = leftBootBodyPart.getOrientationPoints().get(1);

            Vector2 move = bootPktZaczepienia.cpy().sub(skiPktZaczepienia);
            leftSki.move(move);
        }
        {
            Vector2 skiPktZaczepienia = rightSki.getOrientationPoints().get(2);
            Vector2 bootPktZaczepienia = rightBootBodyPart.getOrientationPoints().get(1);

            Vector2 move = bootPktZaczepienia.cpy().sub(skiPktZaczepienia);
            rightSki.move(move);
        }


        leftBootSprite.setRotation(leftBootBodyPart.getAngle() * MathUtils.radDeg);
        leftBootSprite.setPosition(leftBootBodyPart.getOrientationPoints().get(0).x, leftBootBodyPart.getOrientationPoints().get(0).y);

        head.setRotation(neck.getAngle() * MathUtils.radDeg);
        head.setPosition(neck.getOrientationPoints().get(0).x, neck.getOrientationPoints().get(0).y);

        rightBootSprite.setRotation(rightBootBodyPart.getAngle() * MathUtils.radDeg);
        rightBootSprite.setPosition(rightBootBodyPart.getOrientationPoints().get(0).x, rightBootBodyPart.getOrientationPoints().get(0).y);


        leftGloveSprite.setRotation(leftWrist.getAngle() * MathUtils.radDeg);
        leftGloveSprite.setPosition(leftWrist.getOrientationPoints().get(0).x, leftWrist.getOrientationPoints().get(0).y);


        rightGloveSprite.setRotation(rightWrist.getAngle() * MathUtils.radDeg);
        rightGloveSprite.setPosition(rightWrist.getOrientationPoints().get(0).x, rightWrist.getOrientationPoints().get(0).y);

        torsoMesh.rebuild();
        leftArmMesh.rebuild();
        leftSkiMesh.rebuild();
        leftLegMesh.rebuild();
        rightArmMesh.rebuild();
        rightSkiMesh.rebuild();
        rightLegMesh.rebuild();
    }

    public void draw(Batch batch) {
        leftGloveSprite.draw(batch);
        leftBootSprite.draw(batch);
        batch.flush();

        //no need to bind texture, because spritebatch already done this to us (it's same texture)

        leftArmMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);
        leftSkiMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);
        leftLegMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);

        head.draw(batch);
        batch.flush();

        torsoMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);

        rightBootSprite.draw(batch);
        batch.flush();

        rightLegMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);
        rightSkiMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);
        rightArmMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);

        rightGloveSprite.draw(batch);
        batch.flush();
    }

    public void setPosition(Vector2 position) {
        if (!box2dPhysic)
            this.position = position;
    }

    public AnimationFrame dumpCurrentFrame() {
        AnimationFrame frame = new AnimationFrame();
        frame.getInfo(this);
        return frame;
    }

    public void setFrame(AnimationFrame frame) {
        frame.apply(this);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setAsBox2d() {
        upperTorso.setAsBox2d();
    }

    public void setBox2dPhysic() {
        this.box2dPhysic = true;
    }

    public void setCameraValues(OrthographicCamera cam) {
        cam.position.set(
                upperTorso.getOrientationPoints().get(0).x + 0.5f,
                upperTorso.getOrientationPoints().get(0).y,
                0);


    }

    public Vector2 getCenterPosition() {
        return upperTorso.getOrientationPoints().get(0).cpy().lerp(position, 0.5f);
    }

    public void drawWithAlpha(SpriteBatch batch) {
        leftGloveSprite.draw(batch);
        leftBootSprite.draw(batch);
        batch.flush();

        leftArmMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);
        leftSkiMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);
        leftLegMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);

        head.draw(batch);
        batch.flush();

        torsoMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);

        rightBootSprite.draw(batch);
        batch.flush();

        rightArmMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);
        rightSkiMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);
        rightLegMesh.getMesh().render(batch.getShader(), GL20.GL_TRIANGLES);

        rightGloveSprite.draw(batch);
        batch.flush();
    }

    public void drawShadow(OrthographicCamera cam, SkiJumpPhysics physics, SpriteBatch batch) {
        float viewportWidth = cam.viewportWidth;
        float viewportHeight = cam.viewportHeight;


        RayCastResult shadowPoint = physics.rayCast(
                new Point(getPosition()),
                new Point(0, -1));


        try {
            batch.getShader().setUniformf("ambient_color", 0, 0, 0, 0.3f);
            batch.getShader().setUniformi("light_enabled", 0);

            Point diff = shadowPoint.getResult().cpy().sub(new Point(getPosition()));

            Vector3 prevPos = cam.position.cpy();

            float shadowScale = 4f;
            float shadowAngle = shadowPoint.getCollidedEdge().getAngle() * MathUtils.radiansToDegrees;
            cam.setToOrtho(false, viewportWidth, viewportHeight * shadowScale);
            float diff2 = (getPosition().y - prevPos.y) * (shadowScale - 1);
            cam.position.set(prevPos);
            cam.rotateAround(new Vector3(getPosition().x, getPosition().y, 0),
                    cam.direction,
                    shadowAngle);
            Vector3 translateVec = new Vector3((float) diff.x, (float) diff.y * (shadowScale) + diff2, 0);
            translateVec.rotate(Vector3.Z, -shadowAngle);

            cam.update();
            batch.setProjectionMatrix(cam.combined);

            batch.setProjectionMatrix(batch.getProjectionMatrix()
                    .translate(translateVec));
            drawWithAlpha(batch);
            batch.flush();


            cam.setToOrtho(false, viewportWidth, viewportHeight);
            cam.position.set(prevPos);
            cam.update();
            batch.setProjectionMatrix(cam.combined);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("PosX", String.valueOf(getPosition().x));
            map.put("PosY", String.valueOf(getPosition().y));
            Main.getInstance().getPlatformAPI().sendLoggingEvent("ERROR_DRAW_SHADOW_#1", map);

            Main.getInstance().getPlatformAPI().logCrashMessage("Draw shadow error");
            Main.getInstance().getPlatformAPI().logCrashString("PosX", String.valueOf(getPosition().x));
            Main.getInstance().getPlatformAPI().logCrashString("PosY", String.valueOf(getPosition().y));
            Main.getInstance().getPlatformAPI().logCrash(e);
        }

        batch.getShader().setUniformi("light_enabled", 1);
        batch.flush();

    }

    public void dispose() {
        if (disposed) {
            Main.getInstance().getPlatformAPI().logCrash(
                    new IllegalStateException("Jumper body already disposed"));
            return;
        }
        torsoMesh.dispose();
        leftArmMesh.dispose();
        leftSkiMesh.dispose();
        leftLegMesh.dispose();
        rightArmMesh.dispose();
        rightSkiMesh.dispose();
        rightLegMesh.dispose();
        disposed = true;
    }

    public void jumperLaunched() {
        time = 0;
        damping = 2.5f;
        speed = 20f;
        maxValue = 0.15f;
    }
}