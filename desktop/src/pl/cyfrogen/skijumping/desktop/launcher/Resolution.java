package pl.cyfrogen.skijumping.desktop.launcher;

public class Resolution {
    public int width;
    public int height;

    public Resolution(int w, int h) {
        this.width = w;
        this.height = h;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
