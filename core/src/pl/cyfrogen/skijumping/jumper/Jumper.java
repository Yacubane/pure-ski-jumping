package pl.cyfrogen.skijumping.jumper;

import com.badlogic.gdx.graphics.Color;

public class Jumper {
    private final Color color;
    private final JumperPhysicObject body;
    public int id;
    public float time;
    private JumperController jumperController;

    public Jumper(int id, JumperPhysicObject body, Color color) {
        this.id=id;
        this.body=body;
        this.color = color;

    }

    public Color getColor() {
        return color;
    }

    public JumperPhysicObject getPhysicObject() {
        return body;
    }

    public void setController(JumperController jumperController) {
        this.jumperController = jumperController;
    }

    public JumperController getJumperController() {
        return jumperController;
    }

    public boolean launched() {
        return jumperController.isLaunched();
    }
}
