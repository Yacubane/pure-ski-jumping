package pl.cyfrogen.skijumping.jumper.performance;

import com.fasterxml.jackson.databind.JsonNode;

public class PerformanceFunctions {

    public static PerformanceFunction of(JsonNode performanceFunctionNode) {
        String type = performanceFunctionNode.get("type").textValue();
        if(!type.equals("power")) {
            throw new IllegalArgumentException("Currently only power function is supported");
        }

        return new PowerPerformanceFunction(performanceFunctionNode.get("power").floatValue());
    }
}
