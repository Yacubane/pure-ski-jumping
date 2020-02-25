package pl.cyfrogen.skijumping.jumper.performance;

class PowerPerformanceFunction implements PerformanceFunction {
    private final float power;

    public PowerPerformanceFunction(float power) {
        this.power = power;
    }

    @Override
    public float valueAt(float offsetFromBestPosition) {
        return (float) (Math.pow((1 - offsetFromBestPosition), power));
    }
}
