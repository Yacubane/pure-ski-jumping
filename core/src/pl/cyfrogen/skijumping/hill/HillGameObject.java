package pl.cyfrogen.skijumping.hill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.data.HillSetup;
import pl.cyfrogen.skijumping.game.GameWorld;
import pl.cyfrogen.skijumping.game.map.Layer;
import pl.cyfrogen.skijumping.game.map.background.Background;
import pl.cyfrogen.skijumping.game.map.background.SkyColorLine;
import pl.cyfrogen.skijumping.game.map.object.ColorMesh;
import pl.cyfrogen.skijumping.game.map.object.TextureObject;
import pl.cyfrogen.skijumping.game.map.shading.ShadingConfigurations;
import pl.cyfrogen.skijumping.game.map.skyobject.BlendingType;
import pl.cyfrogen.skijumping.game.map.skyobject.MeshObject;
import pl.cyfrogen.skijumping.game.map.skyobject.PercentageColors;
import pl.cyfrogen.skijumping.game.map.skyobject.RadialGradient;
import pl.cyfrogen.skijumping.game.map.skyobject.SkyObject;
import pl.cyfrogen.skijumping.game.map.skyobject.SpriteBatchObject;
import pl.cyfrogen.skijumping.game.map.skyobject.SpriteObject;
import pl.cyfrogen.skijumping.game.renderer.GameRenderer;

public class HillGameObject {
    private final ArrayList<TextureAtlas> textureAtlases;
    private final HillSetup.Mode mode;
    private final ShadingConfigurations shadingConfigurations;
    private final Vector2 paralaxCenter;
    private final List<SkyObject> skyBackgroundObjects;
    private final List<SkyObject> skyForegroundObjects;
    private TextureObject backgroundGateTextureObject;
    private Vector2 backgroundStartGateTextureOffset;
    private Vector2 foregroundStartGateTextureOffset;
    private TextureObject foregroundGateTextureObject;


    private final Texture whiteDot;
    private final Background background;
    private final GameRenderer gameRenderer;
    private final OrthographicCamera cam;
    private Layer layer0;
    private ArrayList<Layer> backgroundLayers = new ArrayList<Layer>();
    private ArrayList<Layer> hillBodyLayers = new ArrayList<Layer>();
    private ArrayList<Layer> hillBackgroundLayers = new ArrayList<Layer>();
    private ArrayList<Layer> hillForegroundLayers = new ArrayList<Layer>();
    private ArrayList<Layer> foregroundLayers = new ArrayList<Layer>();
    private Map<String, Layer> allLoadedLayers = new HashMap<String, Layer>();

    private Layer hillOverlay = new Layer("hillOverlay", new Vector2(), 0);
    private Layer foregroundGateLayer = new Layer("foregroundGateLayer", new Vector2(), 0);
    private Layer backgroundGateLayer = new Layer("backgroundGateLayer", new Vector2(), 0);


    public static final float BASE_ZOOM = 0.04f;


    public HillGameObject(FileHandle hillDataFileHandle, HillModel hillModel, List<Vector2> outrunTopBorderVertices,
                          List<Vector2> outrunBottomBorderVertices, JsonNode modeNode,
                          JsonNode hillDataNode, ShadingConfigurations shadingConfigurations, HillSetup.Mode mode, GameRenderer gameRenderer,
                          OrthographicCamera camera) throws IOException {
        this.shadingConfigurations = shadingConfigurations;
        this.mode = mode;
        ObjectMapper objectMapper = new ObjectMapper();


        this.cam = camera;
        this.gameRenderer = gameRenderer;
        whiteDot = Main.getInstance().getAssets().getWhiteDot();

        JsonNode skyNode = modeNode.get("background").get("sky");
        List<SkyColorLine> skyColorLines = new ArrayList<SkyColorLine>();
        for (JsonNode jsonNode : skyNode) {
            skyColorLines.add(new SkyColorLine(jsonNode));
        }


        textureAtlases = new ArrayList<TextureAtlas>();
        JsonNode atlasPathsNode = hillDataNode.get("texturePaths");
        for (Iterator<JsonNode> it = atlasPathsNode.iterator(); it.hasNext(); ) {
            JsonNode node = it.next();
            String atlasPath = node.textValue();
            textureAtlases.add(new TextureAtlas(hillDataFileHandle.parent().child(atlasPath)));
        }


        JsonNode viewpointVertexNode = hillDataNode.get("viewpointVertex");
        Iterator<JsonNode> viewpointVertexIterator = viewpointVertexNode.elements();

        paralaxCenter = new Vector2(viewpointVertexIterator.next().floatValue(),
                -viewpointVertexIterator.next().floatValue());

        background = new Background(GameWorld.DEFAULT_VIEWPORT_HEIGHT * GameWorld.DEFAULT_VIEWPORT_WIDTH_HEIGHT_RATIO * HillGameObject.BASE_ZOOM,
                GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM,
                paralaxCenter, whiteDot, skyColorLines);


        skyBackgroundObjects = loadSkyObjects(modeNode.get("background").get("objects"));
        skyForegroundObjects = loadSkyObjects(modeNode.get("foreground").get("objects"));


        for (JsonNode node : hillDataNode.get("layers").get("background")) {
            Layer layer = new Layer(node.get("name").textValue(), new Vector2(paralaxCenter), node.get("paralax_offset").floatValue());
            layer.setShaderConfig(shadingConfigurations.getShaderConfiguration("default"));
            addObjectsToLayer(node.get("objects"), layer);
            allLoadedLayers.put(node.get("name").textValue(), layer);
            backgroundLayers.add(layer);
        }

        for (JsonNode node : hillDataNode.get("layers").get("hillBody")) {
            Layer layer = new Layer(node.get("name").textValue(), new Vector2(paralaxCenter), node.get("paralax_offset").floatValue());
            layer.setShaderConfig(shadingConfigurations.getShaderConfiguration("default"));
            addObjectsToLayer(node.get("objects"), layer);
            allLoadedLayers.put(node.get("name").textValue(), layer);
            hillBodyLayers.add(layer);
        }

        for (JsonNode node : hillDataNode.get("layers").get("hillBackground")) {
            Layer layer = new Layer(node.get("name").textValue(), new Vector2(paralaxCenter), node.get("paralax_offset").floatValue());
            layer.setShaderConfig(shadingConfigurations.getShaderConfiguration("default"));
            addObjectsToLayer(node.get("objects"), layer);
            allLoadedLayers.put(node.get("name").textValue(), layer);
            hillBackgroundLayers.add(layer);
        }

        for (JsonNode node : hillDataNode.get("layers").get("hillForeground")) {
            Layer layer = new Layer(node.get("name").textValue(), new Vector2(paralaxCenter), node.get("paralax_offset").floatValue());
            layer.setShaderConfig(shadingConfigurations.getShaderConfiguration("default"));
            addObjectsToLayer(node.get("objects"), layer);
            allLoadedLayers.put(node.get("name").textValue(), layer);
            hillForegroundLayers.add(layer);
        }

        for (JsonNode node : hillDataNode.get("layers").get("foreground")) {
            Layer layer = new Layer(node.get("name").textValue(), new Vector2(paralaxCenter), node.get("paralax_offset").floatValue());
            layer.setShaderConfig(shadingConfigurations.getShaderConfiguration("default"));
            addObjectsToLayer(node.get("objects"), layer);
            allLoadedLayers.put(node.get("name").textValue(), layer);
            foregroundLayers.add(layer);
        }


        for (Iterator<Map.Entry<String, JsonNode>> it = modeNode.get("layerShadings").fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String layerName = entry.getKey();
            String shading = entry.getValue().textValue();

            allLoadedLayers.get(layerName).setShaderConfig(shadingConfigurations.getShaderConfiguration(shading));
        }


        try {
            JsonNode startGateTextureOffsetNode = hillDataNode.get("startGateForegroundTextureOffset");
            Iterator<JsonNode> startGateTextureOffsetIterator = startGateTextureOffsetNode.elements();
            foregroundStartGateTextureOffset = new Vector2(startGateTextureOffsetIterator.next().floatValue(),
                    -startGateTextureOffsetIterator.next().floatValue());

            JsonNode backgroundStartGateTextureOffsetNode = hillDataNode.get("startGateBackgroundTextureOffset");
            Iterator<JsonNode> backgroundStartGateTextureOffsetIterator = backgroundStartGateTextureOffsetNode.elements();
            backgroundStartGateTextureOffset = new Vector2(backgroundStartGateTextureOffsetIterator.next().floatValue(),
                    -backgroundStartGateTextureOffsetIterator.next().floatValue());

            foregroundGateTextureObject = loadGate(hillDataNode, "Foreground");
            foregroundGateLayer.addTextureObjects(foregroundGateTextureObject);
            backgroundGateTextureObject = loadGate(hillDataNode, "Background");
            backgroundGateLayer.addTextureObjects(backgroundGateTextureObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


//        Vector2[] kLineVector = HillLines.generate(
//                hillModel.getOutRunVertices(), outrunTopBorderVertices,
//                outrunBottomBorderVertices, hillModel.getConstructionPointPathLength());
//        Vector2[] minMax = HillLines.getBounds(kLineVector);
//
//        MeshData meshData = new MeshData(kLineVector, whiteDot, Color.RED.cpy().lerp(Color.BLACK, 0.2f).toFloatBits());
//        hillOverlay.addColorMesh(Collections.singletonList(
//                new ColorMesh(Collections.singletonList(meshData), minMax[0].x, minMax[0].y, minMax[1].x, minMax[1].y)));
//
//        Vector2[] hsLineVector = HillLines.generate(
//                hillModel.getOutRunVertices(), outrunTopBorderVertices,
//                outrunBottomBorderVertices,  hillModel.getHillSizePathLength());
//        minMax = HillLines.getBounds(hsLineVector);
//
//        meshData = new MeshData(hsLineVector, whiteDot, Color.RED.cpy().lerp(Color.BLACK, 0.2f).toFloatBits());
//        hillOverlay.addColorMesh(Collections.singletonList(
//                new ColorMesh(Collections.singletonList(meshData), minMax[0].x, minMax[0].y, minMax[1].x, minMax[1].y)));

        hillOverlay.setShaderConfig(shadingConfigurations.getShaderConfiguration(
                modeNode.get("hillBodyOverlayShading").textValue()));
        foregroundGateLayer.setShaderConfig(shadingConfigurations.getShaderConfiguration(
                modeNode.get("skiJumperShading").textValue()));
        backgroundGateLayer.setShaderConfig(shadingConfigurations.getShaderConfiguration(
                modeNode.get("skiJumperShading").textValue()));

        setupEditor();
    }

    private List<SkyObject> loadSkyObjects(JsonNode jsonNode) {
        List<SkyObject> skyObjects = new ArrayList<SkyObject>();
        for (JsonNode objectNode : jsonNode) {
            String type = objectNode.get("type").textValue();
            if (type.equals("radial-gradient")) {
                float x = objectNode.get("x").floatValue();
                float y = objectNode.get("y").floatValue();
                float radius = objectNode.get("radius").floatValue();
                BlendingType blendingType = BlendingType.valueOf(objectNode.get("blending").textValue());
                Color ambientColor = Color.valueOf(objectNode.get("ambientColor").textValue());
                JsonNode colorsNode = objectNode.get("colors");

                skyObjects.add(new RadialGradient(30, new Vector2(
                        paralaxCenter.x + GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM * (x / 2f),
                        paralaxCenter.y + GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM * (y / 2f)
                ), radius * GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM, PercentageColors.fromJson(colorsNode), blendingType, ambientColor));

            } else if (type.equals("sprite")) {
                float x = objectNode.get("x").floatValue();
                float y = objectNode.get("y").floatValue();
                float width = objectNode.get("width").floatValue();
                float height = objectNode.get("height").floatValue();
                String textureId = objectNode.get("textureId").textValue();
                BlendingType blendingType = BlendingType.valueOf(objectNode.get("blending").textValue());
                Color ambientColor = Color.valueOf(objectNode.get("ambientColor").textValue());
                TextureRegion textureRegion = null;
                for (TextureAtlas atlas : textureAtlases) {
                    if ((textureRegion = atlas.findRegion(textureId)) != null) {
                        break;
                    }
                }
                if (textureRegion == null) {
                    throw new IllegalStateException(
                            "Cannot find texture with textureId: " + textureId);
                }


                skyObjects.add(new SpriteObject(textureRegion,
                        paralaxCenter.x + GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM * (x / 2f),
                        paralaxCenter.y + GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM * (y / 2f),
                        GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM * width / 2f,
                        GameWorld.DEFAULT_VIEWPORT_HEIGHT * HillGameObject.BASE_ZOOM * height / 2f
                        , blendingType, ambientColor));
            }
        }
        return skyObjects;
    }

    private TextureObject loadGate(JsonNode hillDataNode, String type) {
        String startGateTextureIdNode = hillDataNode.get("startGate" + type + "TextureId").textValue();

        JsonNode startGateTextureSizeNode = hillDataNode.get("startGate" + type + "TextureSize");
        Iterator<JsonNode> startGateTextureSizeIterator = startGateTextureSizeNode.elements();
        Vector2 startGateTextureSize = new Vector2(startGateTextureSizeIterator.next().floatValue(),
                startGateTextureSizeIterator.next().floatValue());


        TextureRegion textureRegion = null;
        for (TextureAtlas atlas : textureAtlases) {
            if ((textureRegion = atlas.findRegion(startGateTextureIdNode)) != null) {
                break;
            }
        }
        if (textureRegion == null) {
            throw new IllegalStateException(
                    "Cannot find texture with textureId: " + startGateTextureIdNode);
        }
        return new TextureObject(textureRegion, 0, 0, startGateTextureSize.x, startGateTextureSize.y);

    }

    private void addObjectsToLayer(JsonNode jsonNode, Layer layer) {
        for (Iterator<JsonNode> it = jsonNode.iterator(); it.hasNext(); ) {
            JsonNode node = it.next();
            String type = node.get("type").textValue();
            if (type.equals("shape")) {
                JsonNode subShapes = node.get("subShapes");
                for (Iterator<JsonNode> it2 = subShapes.iterator(); it2.hasNext(); ) {
                    JsonNode shape = it2.next();
                    layer.addColorMesh(Collections.singletonList(ColorMesh.of(shape)));
                }
            } else if (type.equals("sprite")) {
                float x = node.get("x").floatValue();
                float y = node.get("y").floatValue();
                float width = node.get("width").floatValue();
                float height = node.get("height").floatValue();
                y = -y - height;
                String textureId = node.get("textureId").textValue();
                JsonNode visibilityIfModes = node.get("visibilityIfModes");

                boolean visible = false;
                for (JsonNode visibilityIfModeNode : visibilityIfModes) {
                    String mode = visibilityIfModeNode.textValue();
                    if (this.mode.toString().equals(mode) || mode.equals("ANY")) {
                        visible = true;
                    }
                }

                if (visible) {
                    TextureRegion textureRegion = null;
                    for (TextureAtlas atlas : textureAtlases) {
                        if ((textureRegion = atlas.findRegion(textureId)) != null) {
                            break;
                        }
                    }
                    if (textureRegion == null) {
                        throw new IllegalStateException(
                                "Cannot find texture with textureId: " + textureId);
                    }

                    layer.addTextureObjects(new TextureObject(textureRegion, x, y, width, height));
                }
            }
        }
    }

    private void setupEditor() {

        shadingConfigurations.setupEditor();

    }

    public void drawBackground() {
        cam.zoom = BASE_ZOOM;
        cam.update();
        Vector2 camPosVec2 = new Vector2(cam.position.x, cam.position.y);
        Vector2 paralaxDiff = camPosVec2.sub(paralaxCenter);

        gameRenderer.setTempProjectionMatrix(
                gameRenderer.getTempProjectionMatrix()
                        .set(gameRenderer.getNormalProjectionMatrix())
                        .translate(paralaxDiff.x, paralaxDiff.y, 0));
        gameRenderer.useTempProjectionMatrix();
        gameRenderer.beginMeshShader();
        gameRenderer.getMeshShader().setUniformf("ambient_color", 1f, 1f, 1f, 1f);

        background.draw(gameRenderer.getMeshShader());

        drawSkyObjects(skyBackgroundObjects);
    }

    private void drawSkyObjects(List<SkyObject> skyObjects) {
        for (SkyObject skyObject : skyObjects) {

            if (skyObject instanceof MeshObject) {
                gameRenderer.beginMeshShader();
                if (skyObject.getBlendingType() == BlendingType.ADDITIVE) {
                    Gdx.gl.glEnable(GL20.GL_BLEND);
                    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
                }
                gameRenderer.getMeshShader().setUniformi("fog_enabled", 0);
                gameRenderer.getMeshShader().setUniformf("ambient_color",
                        skyObject.getAmbientColor().r,
                        skyObject.getAmbientColor().g,
                        skyObject.getAmbientColor().b,
                        skyObject.getAmbientColor().a);
                ((MeshObject) skyObject).draw(gameRenderer.getMeshShader());
                if (skyObject.getBlendingType() == BlendingType.ADDITIVE) {
                    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                }

            } else if (skyObject instanceof SpriteBatchObject) {
                if (skyObject.getBlendingType() == BlendingType.ADDITIVE) {
                    Gdx.gl.glEnable(GL20.GL_BLEND);
                    gameRenderer.getSpriteBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
                }
                gameRenderer.beginSpriteBatch();
                gameRenderer.getSpriteBatch().getShader().setUniformi("fog_enabled", 0);
                gameRenderer.getSpriteBatch().getShader().setUniformf("ambient_color",
                        skyObject.getAmbientColor().r,
                        skyObject.getAmbientColor().g,
                        skyObject.getAmbientColor().b,
                        skyObject.getAmbientColor().a);
                ((SpriteBatchObject) skyObject).draw(gameRenderer.getSpriteBatch());
                if (skyObject.getBlendingType() == BlendingType.ADDITIVE) {
                    gameRenderer.getSpriteBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                }
            }
        }
        gameRenderer.end();
    }

    public void drawForeground() {
        cam.zoom = BASE_ZOOM;
        cam.update();
        Vector2 camPosVec2 = new Vector2(cam.position.x, cam.position.y);
        Vector2 paralaxDiff = camPosVec2.sub(paralaxCenter);

        gameRenderer.setTempProjectionMatrix(
                gameRenderer.getTempProjectionMatrix()
                        .set(gameRenderer.getNormalProjectionMatrix())
                        .translate(paralaxDiff.x, paralaxDiff.y, 0));
        gameRenderer.useTempProjectionMatrix();

        drawSkyObjects(skyForegroundObjects);

    }

    public void updateParalaxes() {
        for (Layer renderable : backgroundLayers) {
            renderable.updateParalax(cam.position.cpy());
        }
    }

    public void renderLayer(Layer layer, float prevZoom) {
        float zoomParam = BASE_ZOOM / prevZoom;
        float newZoom = MathUtils.lerp(prevZoom, BASE_ZOOM, layer.getParam());
        cam.zoom = newZoom;
        cam.update();

        Vector3 minBound = cam.unproject(new Vector3(0, Gdx.graphics.getHeight(), 0));
        Vector3 maxBound = cam.unproject(
                new Vector3(Gdx.graphics.getWidth(),
                        0, 0));

//        Vector3 minBound = cam.unproject(new Vector3(Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()*3/4f, 0));
//        Vector3 maxBound = cam.unproject(
//                new Vector3(Gdx.graphics.getWidth()*3/4f,
//                        Gdx.graphics.getHeight()/4f, 0));

        gameRenderer.setWorldToScreenBounds(
                new Vector2(minBound.x, minBound.y),
                new Vector2(maxBound.x, maxBound.y));

        layer.updateParalax(cam.position.cpy());
        gameRenderer.setNormalProjectionMatrix(cam.combined);

        layer.draw(gameRenderer);
    }


    public void dispose() {
        background.dispose();

        for (TextureAtlas textureAtlas : textureAtlases) {
            textureAtlas.dispose();
        }

        for (SkyObject skyObject : skyBackgroundObjects) {
            skyObject.dispose();
        }

        for (Layer layer : allLoadedLayers.values()) {
            layer.dispose();
        }

        hillOverlay.dispose();
        backgroundGateLayer.dispose();
        foregroundGateLayer.dispose();
    }

    public void renderBackground() {
        float prevZoom = cam.zoom;

        drawBackground();

        for (Layer renderable : backgroundLayers) {
            renderLayer(renderable, prevZoom);
        }
        for (Layer renderable : hillBodyLayers) {
            renderLayer(renderable, prevZoom);
        }

        renderLayer(hillOverlay, prevZoom);

        for (Layer renderable : hillBackgroundLayers) {
            renderLayer(renderable, prevZoom);
        }

        renderLayer(backgroundGateLayer, prevZoom);


        cam.zoom = prevZoom;
        cam.update();

        gameRenderer.beginSpriteBatch();
        gameRenderer.getSpriteBatch().getShader().setUniformi("fog_enabled", 0);
        gameRenderer.beginMeshShader();
        gameRenderer.getMeshShader().setUniformi("fog_enabled", 0);
        gameRenderer.end();
        gameRenderer.useNormalProjectionMatrix();
    }


    public void renderForeground() {
        float prevZoom = cam.zoom;

        renderLayer(foregroundGateLayer, prevZoom);

        for (Layer renderable : hillForegroundLayers) {
            renderLayer(renderable, prevZoom);
        }
        for (Layer renderable : foregroundLayers) {
            renderLayer(renderable, prevZoom);
        }

        drawForeground();

        cam.zoom = prevZoom;
        cam.update();

        gameRenderer.beginSpriteBatch();
        gameRenderer.getSpriteBatch().getShader().setUniformi("fog_enabled", 0);
        gameRenderer.beginMeshShader();
        gameRenderer.getMeshShader().setUniformi("fog_enabled", 0);
        gameRenderer.end();
        gameRenderer.useNormalProjectionMatrix();
    }

    public void setGatePosition(Vector2 position) {
        if (foregroundGateTextureObject != null) {
            foregroundGateTextureObject.setPosition(position.x + foregroundStartGateTextureOffset.x, position.y + foregroundStartGateTextureOffset.y);
        }
        if (backgroundGateTextureObject != null) {
            backgroundGateTextureObject.setPosition(position.x + backgroundStartGateTextureOffset.x, position.y + backgroundStartGateTextureOffset.y);
        }
    }

}
