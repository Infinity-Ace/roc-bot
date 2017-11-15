package jn.rocbot.ships;

import java.util.HashMap;

public class Zen {
    public final String name;
    public final String desc;

    public final String ultimateName;
    public final String ultimateDesc;

    public final HashMap<String, String> properties;
    private final HashMap<String, String> formatting;

    public Zen(String name, String desc, String ultimateName, String ultimateDesc, HashMap<String, String> properties, HashMap<String, String> formatting) {
        /* */   this.name = name;
        /* */   this.desc = desc;
        this.ultimateName = ultimateName;
        this.ultimateDesc = ultimateDesc;

        this.properties = properties;

        this.formatting = formatting;
    }
}
