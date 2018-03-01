package jn.rocbot.ships;

import jn.rocbot.info.SimpleDescBuilder;
import jn.rocbot.utils.Formatter;

import java.util.HashMap;

public class Weapon  extends Ship.ShipProperty implements Formatter{
    public final float dps;
    public final DamageType damageType;
    public final HashMap<String, String> properties;
    public final HashMap<String, String> propertiesFormat;

    public Weapon(String name, float dps, String damageType, HashMap<String, String> properties, HashMap<String, String> propertiesFormat) throws DamageType.DamageTypeNotFoundException {
        super(name);
        this.dps = dps;
        this.damageType = DamageType.fromString(damageType);
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

        desc.addLine(bold("Damage output: ") + spaced_italic(String.valueOf(dps)));
        desc.addLine(bold("Damage type:  ") + spaced_italic(damageType.toString()));
        for (String key : properties.keySet()) {
            if (propertiesFormat.containsKey(key + "-format"))
                desc.addLine(key + ": " + italic(properties.get(key) + propertiesFormat.get(key + "-format")));
            else
                desc.addLine(key + ": " + italic(properties.get(key)));
        } return desc.toString();
    }

}
