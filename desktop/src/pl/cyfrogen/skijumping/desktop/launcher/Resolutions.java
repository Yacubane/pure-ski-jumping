package pl.cyfrogen.skijumping.desktop.launcher;

import java.util.ArrayList;
import java.util.List;

public class Resolutions {

    public static final List<Resolution> resolutions = new ArrayList<Resolution>();

    static {
        resolutions.add(new Resolution(960, 540));
        resolutions.add(new Resolution(1280, 800));
        resolutions.add(new Resolution(640, 480));
        resolutions.add(new Resolution(640, 360));
        resolutions.add(new Resolution(800, 600));
        resolutions.add(new Resolution(1280, 720));
        resolutions.add(new Resolution(1920, 1080));
        resolutions.add(new Resolution(1600, 900));
    }

}
