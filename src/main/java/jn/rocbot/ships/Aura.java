package jn.rocbot.ships;

import jn.rocbot.info.SimpleDescBuilder;
import jn.rocbot.utils.Formatter;

import java.util.HashMap;
import java.util.StringJoiner;

public class Aura implements Formatter{
    public final String name;
    public final String desc;
    public final String ultimateName;
    public final HashMap<String, String> properties;
    public final HashMap<String, String> ultimateProperties;
    public final HashMap<String, String> formatting;

    public Aura(String name, String desc, String ultimateName, HashMap<String, String> properties, HashMap<String, String> ultimateProperties, HashMap<String, String> formatting) {
        this.name = name;
        this.desc = desc;
        this.ultimateName = ultimateName;
        this.properties = properties;
        this.ultimateProperties = ultimateProperties;
        this.formatting = formatting;
    }

    public String simpleDesc(){
        SimpleDescBuilder desc = new SimpleDescBuilder(bold(name));
        desc.addLine(this.desc);
        for(String key : properties.keySet()){
            if(formatting.containsKey(key + "-format")){
                desc.addLine("\t" + key + ": " + spaced_italic(properties.get(key) + formatting.get(key + "-format")));
            } else {
                desc.addLine("\t" + key + ": " + spaced_italic(properties.get(key)));
            }
        }

        desc.addLine("\nUltimate: " + italic(ultimateName));
        desc.addLine("\tDescription: " + ultimateProperties.get("desc"));
        return desc.get();
    }

    public boolean isInteger(String str) {
        int size = str.length();

        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        } return size > 0;
    }
}
