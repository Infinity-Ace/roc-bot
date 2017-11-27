package jn.rocbot.ships;

import jn.rocbot.info.SimpleDescBuilder;
import jn.rocbot.utils.Formatter;

import java.util.HashMap;

public class Weapon implements Formatter{
    public final String name;
    public final float dps;
    public final DAMAGETYPE damageType;
    public final HashMap<String, String> properties;
    public final HashMap<String, String> propertiesFormat;

    public Weapon(String name, float dps, String damageType, HashMap<String, String> properties, HashMap<String, String> propertiesFormat) throws Exception {
        this.name = name;
        this.dps = dps;
        this.damageType = DAMAGETYPE.fromString(damageType);
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
        } return desc.get();
    }

    public enum DAMAGETYPE {
        SB("sb"), AP("ap"), HI("hi");

        private String string;

        DAMAGETYPE(String s) {
            this.string = s;
        }

        public static boolean isDamageType(String s){
            for(DAMAGETYPE dt : DAMAGETYPE.values()){
                if(dt.string.equals(s.toLowerCase())) return true;
            } return false;
        }

        public String toString(){
            switch (this) {
                case SB: return "Shield breaking";
                case AP: return "Armor piercing";
                case HI: return "High impact";
                default: return null; //Doesn't happen
            }
        }

        public static DAMAGETYPE fromString(String s) throws Exception {
            for (DAMAGETYPE dt : DAMAGETYPE.values()){
                if(s.toLowerCase().equals(dt.string)) return  dt;
            } throw new Exception("No damagetype named " + s + "'");
        }
    }
}
