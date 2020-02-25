package pl.cyfrogen.skijumping.hill;

import com.fasterxml.jackson.databind.JsonNode;

import pl.cyfrogen.skijumping.jumper.performance.PerformanceFunction;
import pl.cyfrogen.skijumping.jumper.performance.PerformanceFunctions;

public class PhysicsConfiguration {
    private final float kineticFrictionCoefficient;
    private final float maxWind;
    private final float minWind;
    private final float windMultiplier;
    private final float k1;
    private final float delta0;
    private final float tau;
    private final float k2base;
    private final PerformanceFunction performanceFunction;

    public float getWindMultiplier() {
        return windMultiplier;
    }

    public float getK1() {
        return k1;
    }

    public float getDelta0() {
        return delta0;
    }

    public float getTau() {
        return tau;
    }

    public float getK2base() {
        return k2base;
    }

    public float getK2maxDegradation() {
        return k2maxDegradation;
    }

    public float getTakeoffVelocity() {
        return takeoffVelocity;
    }

    public boolean isTakeoffVelocityPerpendicular() {
        return takeoffVelocityPerpendicular;
    }

    private final float k2maxDegradation;
    private final float takeoffVelocity;
    private final boolean takeoffVelocityPerpendicular;

    public PhysicsConfiguration(float kineticFrictionCoefficient, float maxWind, float minWind, float windMultiplier, float k1, float delta0, float tau, float k2base, float k2maxDegradation, float takeoffVelocity, boolean takeoffVelocityPerpendicular, PerformanceFunction performanceFunction) {
        this.kineticFrictionCoefficient = kineticFrictionCoefficient;
        this.maxWind = maxWind;
        this.minWind = minWind;
        this.windMultiplier = windMultiplier;
        this.k1 = k1;
        this.delta0 = delta0;
        this.tau = tau;
        this.k2base = k2base;
        this.k2maxDegradation = k2maxDegradation;
        this.takeoffVelocity = takeoffVelocity;
        this.takeoffVelocityPerpendicular = takeoffVelocityPerpendicular;
        this.performanceFunction = performanceFunction;
    }

    public static PhysicsConfiguration of(JsonNode node) {
        return new PhysicsConfiguration(
                node.get("kineticFrictionCoefficient").floatValue(),
                node.get("maxWind").floatValue(),
                node.get("minWind").floatValue(),
                node.get("windMultiplier").floatValue(),
                node.get("k1").floatValue(),
                node.get("delta0").floatValue(),
                node.get("tau").floatValue(),
                node.get("k2base").floatValue(),
                node.get("k2maxDegradation").floatValue(),
                node.get("takeoffVelocity").floatValue(),
                node.get("takeoffVelocityPerpendicular").booleanValue(),
                PerformanceFunctions.of(node.get("performanceFunction"))
                );
    }

    public float getKineticFrictionCoefficient() {
        return kineticFrictionCoefficient;
    }

    public float getMaxWind() {
        return maxWind;
    }

    public float getMinWind() {
        return minWind;
    }

    public PerformanceFunction getPerformanceFunction() {
        return performanceFunction;
    }
}
