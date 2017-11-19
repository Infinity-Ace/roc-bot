package jn.rocbot.ships;

import java.util.HashMap;

public class Weapon {
    public final String name;
    public final float dps;
    public final HashMap<String, String> properties;

    public Weapon(String name, float dps, HashMap<String, String> properties) {
        this.name = name;
        this.dps = dps;
        this.properties = properties;
    }
}
