package jn.rocbot.ships;

import jn.rocbot.info.SimpleDescBuilder;
import jn.rocbot.utils.Formatter;

import java.util.HashMap;

public class Weapon implements Formatter{
    public final String name;
    public final float dps;
    public final HashMap<String, String> properties;
    public final HashMap<String, String> propertiesFormat;

    public Weapon(String name, float dps, HashMap<String, String> properties, HashMap<String, String> propertiesFormat) {
        this.name = name;
        this.dps = dps;
        this.properties = properties;
        this.propertiesFormat = propertiesFormat;
    }

    public String simpleDesc(boolean withTitle) {
        SimpleDescBuilder desc;
        if (withTitle)
            desc = new SimpleDescBuilder(bold(name));
        else {
            desc = new SimpleDescBuilder();
        }

        desc.add("Damage output: " + italic(String.valueOf(dps)));

        for (String key : properties.keySet()) {
            if(key != "name") {
                if (propertiesFormat.containsKey(key + "-format"))
                    desc.add(key + ": " + italic(properties.get(key) + propertiesFormat.get(key + "-format")));
                else
                    desc.add(key + ": " + italic(properties.get(key)));
            }
        }

        return desc.get();
    }
}
