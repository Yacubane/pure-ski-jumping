package pl.cyfrogen.skijumping.data;

import com.badlogic.gdx.graphics.Color;

public class JumperData {
    private JumperColors jumperColors = new JumperColors();
    private String name;
    private boolean activeInWorldCup;
    private boolean isCpuPlayer;

    public JumperData() {
    }

    public JumperData(String name) {
        this.name = name;

        getColors().getHelmetColor().setColor(Color.valueOf("bdc3c7"));
        getColors().getGoggleBandColor().setColor(Color.valueOf("ecf0f1"));
        getColors().getGoggleColor().setColor(Color.valueOf("ffda79"));
        getColors().getArmLeftColor().setColor(Color.valueOf("34495e"));
        getColors().getBodyLeftColor().setColor(Color.valueOf("34495e"));
        getColors().getArmRightColor().setColor(Color.valueOf("3498db"));
        getColors().getBodyRightColor().setColor(Color.valueOf("3498db"));
        getColors().getBootColor().setColor(Color.valueOf("bdc3c7"));
        getColors().getGloveColor().setColor(Color.valueOf("bdc3c6"));
        getColors().getSkinColor().setColor(Color.valueOf("fddcbc"));
        getColors().getSkiColor().setColor(Color.valueOf("2ecc71"));


    }

    public String getName() {
        return name;
    }

    public boolean isActiveInWorldCup() {
        return activeInWorldCup;
    }

    public void setActiveInWorldCup(boolean b) {
        this.activeInWorldCup = b;
    }

    public void setName(String text) {
        this.name = text;
    }

    public void setCpuPlayer(boolean b) {
        this.isCpuPlayer = b;
    }

    public boolean isCpuPlayer() {
        return isCpuPlayer;
    }

    public JumperColors getColors() {
        return jumperColors;
    }
}
