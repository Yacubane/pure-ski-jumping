package pl.cyfrogen.skijumping.debug;

import java.util.LinkedHashMap;
import java.util.Map;

public class Profiler {
    Map<String, Measurement> measurementMap = new LinkedHashMap<String, Measurement>();
    private Measurement lastMeasurement;

    public void startMeasurement(String id) {
        lastMeasurement = Measurement.startMeasurement();
        measurementMap.put(id, lastMeasurement);

    }
    public void stopMeasurement(String id) {
        if(measurementMap.get(id) == null) {
            throw new IllegalArgumentException("Measurement with this id doesn't exists");
        }
        measurementMap.get(id).end();
    }

    public void print() {
        System.out.println("[Profiler]");
        for(Map.Entry<String, Measurement> entry : measurementMap.entrySet()) {
            System.out.printf("    %-20s = %12d\n", entry.getKey(), entry.getValue().getDifference());
        }
    }

    public void stopLast() {
        lastMeasurement.end();
    }

    public void reset() {
        lastMeasurement = null;
        measurementMap.clear();
    }
}
