package jn.rocbot.ships;

import jn.rocbot.info.SimpleDescBuilder;
import jn.rocbot.utils.Formatter;

import java.util.HashMap;

public class Weapon implements Formatter{
    public final String name;
    public final float dps;
    public final String damageType;
    public final HashMap<String, String> properties;
    public final HashMap<String, String> propertiesFormat;

    public Weapon(String name, float dps, String damageType, HashMap<String, String> properties, HashMap<String, String> propertiesFormat) {
        this.name = name;
        this.dps = dps;
        this.damageType = damageType;
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

        desc.addLine("Damage output: " + italic(String.valueOf(dps)));

        for (String key : properties.keySet()) {
            if (propertiesFormat.containsKey(key + "-format"))
                desc.addLine(key + ": " + italic(properties.get(key) + propertiesFormat.get(key + "-format")));
            else
                desc.addLine(key + ": " + italic(properties.get(key)));
        } return desc.get();
    }
}
