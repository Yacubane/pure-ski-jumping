package pl.cyfrogen.skijumping.common.timeout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class TimeoutHandler {
    private Map<String, Timeout> timeouts;
    private ArrayList<TimeoutEvent> events;

    public TimeoutHandler() {
        timeouts = new HashMap<String, Timeout>();
        events = new ArrayList<TimeoutEvent>();

    }

    public String add(Timeout e) {
        String uuid = UUID.randomUUID().toString();
        timeouts.put(uuid, e);
        return uuid;
    }

    public void add(String id, Timeout e) {
        if (timeouts.get(id) == null) {
            timeouts.put(id, e);
        } else {
            throw new IllegalArgumentException("Duplicated timeout id");
        }
    }

    public void update() {
        Iterator<Timeout> iterator = timeouts.values().iterator();
        while (iterator.hasNext()) {
            Timeout timeout = iterator.next();
            if (timeout.update()) {
                events.add(timeout.event);
                iterator.remove();
            }
        }

        for (int i = 0; i < events.size(); i++) {
            events.get(i).fire();
        }

        events.clear();
    }

    public void removeAll() {
        timeouts.clear();
    }

}
