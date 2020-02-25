package pl.cyfrogen.skijumping.hill;

import pl.cyfrogen.skijumping.data.HillLocation;

public class Hills {
    public static final HillLocation ZAKOPANE_HS140v1 = new HillLocation("zakopane_hs140v1", true);
    public static final HillLocation PLANICA_HS240v1 = new HillLocation("planica_hs240v1", true);

    public static HillLocation[] hillLocations = {
            ZAKOPANE_HS140v1,
            PLANICA_HS240v1
    };
}
